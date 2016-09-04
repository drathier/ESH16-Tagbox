package se.drathier.tagbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Activityang extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityang);

        View view = findViewById(R.id.tap);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
