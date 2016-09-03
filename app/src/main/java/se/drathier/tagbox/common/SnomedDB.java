package se.drathier.tagbox.common;

import android.os.AsyncTask;

import java.util.HashMap;

/**
 * Created by anders on 03/09/16.
 */
public class SnomedDB {

    public interface SnomedResponse {
        public void dataFound(int id, String data);
    }

    public static HashMap<Integer, String> db;

    public static void fetch(int id, SnomedResponse response) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                String body = HttpRequest.get("").body();

                

                return null;
            }

            
        }.execute();
    }

}
