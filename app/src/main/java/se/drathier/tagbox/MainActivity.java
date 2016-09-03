package se.drathier.tagbox;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import se.drathier.tagbox.tagbox.Model;
import se.drathier.tagbox.tagbox.mifare.mifare_ultralight;

public class MainActivity extends AppCompatActivity {

    public Model m;

    public View buttonScan;
    public View buttonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment, new MainActivityFragment()).commit();


        Model model = new Model();
        model.CountryCode = "se";
        model.SSN = "910612-1099";
        model.bt_ab = Model.BloodTypeAB.A;
        model.bt_plus = Model.BloodTypePlusMinus.Plus;
        model.is_organ_donor = false;
        model.is_male = true;
        this.m = model;

        Model.Snomed_id snomed_id = new Model.Snomed_id();
        snomed_id.id = 91934008;
        snomed_id.from = new Date(1991, 6, 12);
        snomed_id.to = new Date();
        snomed_id.severity = Model.Severity.High;

        model.snomed_ids = new ArrayList<>();
        model.snomed_ids.add(snomed_id);

        buttonProfile = findViewById(R.id.button_profile);
        buttonScan = findViewById(R.id.button_scan);

        buttonScan.setAlpha(1.0f);
        buttonProfile.setAlpha(0.3f);


        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment, new ProfileFragment()).commit();
                buttonScan.setAlpha(0.3f);
                buttonProfile.setAlpha(1.0f);

            }
        });

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment, new MainActivityFragment()).commit();
                buttonScan.setAlpha(1.0f);
                buttonProfile.setAlpha(0.3f);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        NdefMessage[] msgs;

        Intent intent = getIntent();
        Log.d("MainAct", intent.toString());

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    Log.d("rawMsg", rawMsgs[i].toString());
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
        }
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            MifareUltralight a = MifareUltralight.get(tagFromIntent);
            mifare_ultralight mul = new mifare_ultralight(a);
            Log.d("tag_tech", tagFromIntent.toString());

            Parcel p = Parcel.obtain();
            this.m.writeToParcel(p, 0);
            try {
                mul.mul.connect();
                mul.writeParcel(p);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    mul.mul.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                a.connect();
                Log.d("raw_json", gson.toJson(mul.read_all()));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    a.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            p.recycle();
        }
        //process the msgs array

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
