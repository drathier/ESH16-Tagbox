package se.drathier.tagbox.common;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import se.drathier.tagbox.tagbox.Model;

/**
 * Created by anders on 03/09/16.
 */
public class SnomedDB {

    public interface SnomedResponse {
        public void dataFound(int id, String data);
    }

    public interface SnomedModelResponse {
        public void dataAdded();
    }


    public static HashMap<Integer, String> db;

    public static String get(Integer id) {
        return db.get(id);
    }


    public static void fetch(final String lang, final Model model, final SnomedModelResponse response) {


        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {

                if(db == null)
                    db = new HashMap<>();


                List<Model.Snomed_id> ids = model.snomed_ids;

                for (int i = 0; i < ids.size(); i++) {

                    if(!db.containsKey(ids.get(i).id)) {

                        String url = getLangUrl(lang, ids.get(i).id);


                        if (url != null) {

                            String body = HttpRequest.get(url).body();

                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(body);
                                db.put(ids.get(i).id, jsonObject.getString("defaultTerm"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {


                response.dataAdded();

            }
        }.execute();



    }


    private static String getLangUrl(String ln, Integer id) {

        switch (ln) {

            case "se":
                return "https://browser-aws-1.ihtsdotools.org/api/snomed/se-edition/v20160531/concepts/" + id;

            case "en":
                return "http://browser.ihtsdotools.org/api/snomed/en-edition/v20160731/concepts/" + id;

            case "uk":
                return "http://browser-aws-1.ihtsdotools.org/api/snomed/uk-edition/v20160401/concepts/" + id;

            case "dk":
                return "https://browser-aws-1.ihtsdotools.org/api/snomed/dk-edition/v20160215/concepts/" + id;

            default:
                return null;
        }

    }
}
