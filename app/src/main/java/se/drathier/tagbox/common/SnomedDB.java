package se.drathier.tagbox.common;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by anders on 03/09/16.
 */
public class SnomedDB {

    public interface SnomedResponse {
        public void dataFound(int id, String data);
    }

    public static HashMap<Integer, String> db;

    public static void fetch(final String lang, final Integer id, final SnomedResponse response) {

        if(db == null)
            db = new HashMap<>();

        if(db.containsKey(id))
            response.dataFound(id, db.get(id));


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                String url = getLangUrl(lang, id);


                if(url != null) {

                    String body = HttpRequest.get(url).body();


                    //curl 'https://browser-aws-1.ihtsdotools.org/api/snomed/dk-edition/v20160215/concepts/91934008' -H 'Origin: http://browser.ihtsdotools.org' -H 'Accept-Encoding: gzip, deflate, sdch, br' -H 'Accept-Language: en-US,en;q=0.8,es;q=0.6,sv;q=0.4,da;q=0.2,nb;q=0.2' -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36' -H 'Accept: application/json, text/javascript, */*; q=0.01' -H 'Referer: http://browser.ihtsdotools.org/?perspective=full&conceptId1=91934008&edition=dk-edition&release=v20160215&server=https://browser-aws-1.ihtsdotools.org/api/snomed&langRefset=554461000005103' -H 'If-None-Match: "-617840793"' -H 'Connection: keep-alive' --compressed

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        response.dataFound(id, jsonObject.getString("defaultTerm"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                // nöt 91934008

                // nöt sve
                // http://browser-aws-1.ihtsdotools.org/api/snomed/se-edition/v20160531/concepts/91934008

                // nut en
                // http://browser.ihtsdotools.org/api/snomed/en-edition/v20160731/concepts/91934008

                // nut uk
                // http://browser-aws-1.ihtsdotools.org/api/snomed/uk-edition/v20160401/concepts/91934008

                // nut dk
                // https://browser-aws-1.ihtsdotools.org/api/snomed/dk-edition/v20160215/concepts/91934008

                return null;
            }

            
        }.execute();
    }


    private static String getLangUrl(String ln, Integer id) {

        switch (ln) {

            case "se":
                return "http://browser-aws-1.ihtsdotools.org/api/snomed/se-edition/v20160531/concepts/" + id;

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
