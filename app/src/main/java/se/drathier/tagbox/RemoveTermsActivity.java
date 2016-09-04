package se.drathier.tagbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import se.drathier.tagbox.adapters.RemoveAdapter;
import se.drathier.tagbox.common.LocalProfile;

public class RemoveTermsActivity extends AppCompatActivity {


    public ListView list;
    public RemoveAdapter removeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_terms);

        list = (ListView) findViewById(R.id.list);

        removeAdapter = new RemoveAdapter(this);
        list.setAdapter(removeAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LocalProfile.model.snomed_ids.remove(i);
                removeAdapter.notifyDataSetChanged();
            }
        });


    }


}
