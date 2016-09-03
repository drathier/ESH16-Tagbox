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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import se.drathier.tagbox.tagbox.Model;
import se.drathier.tagbox.tagbox.mifare.deserializer;
import se.drathier.tagbox.tagbox.mifare.mifare_ultralight;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import se.drathier.tagbox.tagbox.Model;
import se.drathier.tagbox.tagbox.mifare.serializer;
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
        snomed_id.from = Calendar.getInstance();
        snomed_id.from.set(1911,11,24);
        snomed_id.to = Calendar.getInstance();
        snomed_id.to.set(1994,7,29);
        snomed_id.severity = Model.Severity.High;

        model.snomed_ids = new ArrayList<>();
        model.snomed_ids.add(snomed_id);

        buttonProfile = findViewById(R.id.button_profile);
        buttonScan = findViewById(R.id.button_scan);
        // test serializer
        /*Gson gson = new GsonBuilder().setPrettyPrinting().create();
        byte[] data = serializer.serialize(model);
        Log.d("ser_data", gson.toJson(bytesToHex(data)));
        Model des = (new deserializer()).deserialize(data);
        Log.d("ser_test", gson.toJson(this.m));
        Log.d("des_test", gson.toJson(des));
        */
        // end test

        //HttpRequest.get("http://google.se").body()

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

            try {
                mul.mul.connect();
                mul.writeSerialized(this.m);
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
                ArrayList<Byte> all = mul.read_all();
                //Log.d("raw_json", gson.toJson(all));
                Model des = (new deserializer()).deserialize(all);
                Log.d("pre_serializ_model", gson.toJson(this.m));
                Log.d("deserialized_model", gson.toJson(des));

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    a.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
