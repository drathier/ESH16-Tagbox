package se.drathier.tagbox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import se.drathier.tagbox.R;
import se.drathier.tagbox.tagbox.Model;

/**
 * Created by anders on 04/09/16.
 */
public class SearchAdapter extends BaseAdapter {

    public ArrayList<Model.Snomed_id> ids;
    public Context context;

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public SearchAdapter(Context applicationContext, ArrayList<Model.Snomed_id> snomed_ids) {
        this.context = context;
        this.ids = snomed_ids;
    }

    @Override
    public int getCount() {
        return ids == null ? 0 : ids.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_search, null);

        TextView data = (TextView) v.findViewById(R.id.snomed_data);
        TextView id = (TextView) v.findViewById(R.id.snomed_id);

        Model.Snomed_id snomed_id = ids.get(i);

        data.setText(snomed_id.response);
        id.setText("" + snomed_id.id);


        return v;
    }
}
