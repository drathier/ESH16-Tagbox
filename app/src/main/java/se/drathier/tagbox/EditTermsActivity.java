package se.drathier.tagbox;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import se.drathier.tagbox.adapters.SearchAdapter;
import se.drathier.tagbox.common.HttpRequest;
import se.drathier.tagbox.common.LocalProfile;
import se.drathier.tagbox.tagbox.Model;

public class EditTermsActivity extends AppCompatActivity {

    public View search;
    public EditText query;
    public ListView list;
    public SearchAdapter searchAdapter;

    public View empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_terms);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }


        search = findViewById(R.id.button_search);
        query = (EditText) findViewById(R.id.search_query);
        list = (ListView) findViewById(R.id.list);

        searchAdapter = new SearchAdapter(this);

        empty = findViewById(R.id.empty);
        empty.setVisibility(searchAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);


        list.setAdapter(searchAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Model.Snomed_id snomedId = searchAdapter.ids.get(i);

                snomedId.severity = Model.Severity.High;
                snomedId.from = Calendar.getInstance();
                snomedId.to = Calendar.getInstance();

                if(LocalProfile.model.snomed_ids == null)
                    LocalProfile.model.snomed_ids = new ArrayList<Model.Snomed_id>();


                LocalProfile.model.snomed_ids.add(snomedId);
                finish();

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchOn(query.getText().toString());


            }
        });
    }

    private void searchOn(final String q) {

        new AsyncTask<Void, Void, ArrayList<Model.Snomed_id>>() {

            @Override
            protected ArrayList<Model.Snomed_id> doInBackground(Void... voids) {
                ArrayList<Model.Snomed_id> data = new ArrayList<>();

                String body = HttpRequest.get("http://browser.ihtsdotools.org/api/snomed/en-edition/v20160731/descriptions?query=" + q + "&limit=50&searchMode=partialMatching&lang=english&statusFilter=activeOnly&skipTo=0&returnLimit=100&normalize=true").body();

                try {
                    JSONObject obj = new JSONObject(body);

                    JSONArray matches = obj.getJSONArray("matches");

                    if(matches != null) {
                        for (int i = 0; i < matches.length(); ++i) {
                            JSONObject rec = matches.getJSONObject(i);

                            Model.Snomed_id add = new Model.Snomed_id();
                            add.response = rec.getString("term");

                            try {
                                add.id = Integer.parseInt(rec.getString("conceptId"));
                            } catch (Exception e) {

                            }

                            data.add(add);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return data;
            }

            @Override
            protected void onPostExecute(ArrayList<Model.Snomed_id> snomed_ids) {
                super.onPostExecute(snomed_ids);

                Log.d("soderstrom", new GsonBuilder().setPrettyPrinting().create().toJson(snomed_ids));

                searchAdapter.ids = snomed_ids;
                searchAdapter.notifyDataSetChanged();
                empty.setVisibility(searchAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);

                //list.setAdapter(new SearchAdapter(EditTermsActivity.this, snomed_ids));

            }



        }.execute();

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
