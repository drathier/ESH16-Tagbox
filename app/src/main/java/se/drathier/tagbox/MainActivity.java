package se.drathier.tagbox;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

import se.drathier.tagbox.common.LocalProfile;
import se.drathier.tagbox.tagbox.Model;

public class MainActivity extends AppCompatActivity {


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

        LocalProfile.load(getApplicationContext());

        overridePendingTransition(0, 0);

        Model model = new Model();
        model.CountryCode = "se";
        model.SSN = "910612-1099";
        model.bt_ab = Model.BloodTypeAB.A;
        model.bt_plus = Model.BloodTypePlusMinus.Plus;
        model.is_organ_donor = false;
        model.is_male = true;

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
    protected void onPause() {
        super.onPause();

        LocalProfile.save(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {





        return false;
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
