package se.drathier.tagbox.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import se.drathier.tagbox.R;
import se.drathier.tagbox.tagbox.Model;

/**
 * Created by anders on 03/09/16.
 */
public class TagAdapter extends RecyclerView.Adapter<TagHolder> {

    public ArrayList<Model> list;

    @Override
    public TagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tag_info, parent, false);
        return new TagHolder(view);
    }

    @Override
    public void onBindViewHolder(TagHolder holder, int position) {
        Model model = list.get(position);
        holder.bindData(model);
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }
}
