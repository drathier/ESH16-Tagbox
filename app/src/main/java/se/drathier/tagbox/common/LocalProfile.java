package se.drathier.tagbox.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import se.drathier.tagbox.tagbox.Model;

/**
 * Created by anders on 03/09/16.
 */
public class LocalProfile {

    public static Model model;

    public static void load(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String m = sharedPreferences.getString("model", "");

        if(!m.equals("")) {
            model = new Gson().fromJson(m, Model.class);

        } else
            model = new Model();
    }

    public static void save(Context context) {

        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit();

        sharedPreferences.putString("model", new Gson().toJson(model));
        sharedPreferences.apply();
    }
}
