package se.drathier.tagbox;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import se.drathier.tagbox.common.LocalProfile;
import se.drathier.tagbox.tagbox.mifare.mifare_ultralight;

public class WriteTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tag);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {

            Tag tagFromIntent = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);


            MifareUltralight a = MifareUltralight.get(tagFromIntent);


            mifare_ultralight mul = new mifare_ultralight(a);


            Log.d("tag_tech", tagFromIntent.toString());

            try {
                mul.mul.connect();
                mul.writeSerialized(LocalProfile.model);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    mul.mul.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {

            Tag tagFromIntent = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);


            MifareUltralight a = MifareUltralight.get(tagFromIntent);


            mifare_ultralight mul = new mifare_ultralight(a);


            Log.d("tag_tech", tagFromIntent.toString());

            try {
                mul.mul.connect();
                mul.writeSerialized(LocalProfile.model);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    mul.mul.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
