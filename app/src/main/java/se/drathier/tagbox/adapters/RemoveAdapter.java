package se.drathier.tagbox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import se.drathier.tagbox.R;
import se.drathier.tagbox.common.LocalProfile;
import se.drathier.tagbox.tagbox.Model;

/**
 * Created by anders on 04/09/16.
 */
public class RemoveAdapter extends BaseAdapter {

    public Context context;

    public RemoveAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return LocalProfile.model.snomed_ids == null ? 0 : LocalProfile.model.snomed_ids.size();
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

        View v = LayoutInflater.from(context).inflate(R.layout.list_remove, null);

        TextView data = (TextView) v.findViewById(R.id.snomed_data);
        TextView id = (TextView) v.findViewById(R.id.snomed_id);

        Model.Snomed_id snomed_id = LocalProfile.model.snomed_ids.get(i);

        data.setText(snomed_id.response);
        id.setText("" + snomed_id.id);


        return v;
    }
}
