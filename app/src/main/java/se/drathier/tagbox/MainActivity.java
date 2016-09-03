package se.drathier.tagbox;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import se.drathier.tagbox.tagbox.Model;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Model model = new Model();
        model.CountryCode = "se";
        model.SSN = "910612-1099";
        model.bt_ab = Model.BloodTypeAB.A;
        model.bt_plus = Model.BloodTypePlusMinus.Plus;
        model.is_organ_donor = false;
        model.is_male = true;

        Model.Snomed_id snomed_id = new Model.Snomed_id();
        snomed_id.id = 91934008;
        snomed_id.from = new Date(1991, 6, 12);
        snomed_id.to = new Date();
        snomed_id.severity = Model.Severity.High;

        model.snomed_ids = new ArrayList<>();
        model.snomed_ids.add(snomed_id);

        //HttpRequest.get("http://google.se").body()


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
            Log.d("tag_tech", tagFromIntent.toString());

            try {
                a.connect();
                Log.d("raw_json", gson.toJson(a.readPages(0)));
                Log.d("raw_json", gson.toJson(a.readPages(1)));
                Log.d("raw_json", gson.toJson(a.readPages(2)));
                Log.d("raw_json", gson.toJson(a.readPages(4)));
                Log.d("raw_json", gson.toJson(a.readPages(64)));
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

    public byte[] read_all(MifareUltralight mul) throws IOException {
        /*
        ArrayList<Byte> out = new ArrayList<Byte>();
        int max = 0xDE;
        for (int i = 0; i < max; i+=4) {
            out.addAll(mul.readPages(i));
        }
        //mul.readPages();
        */
        return null;
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
