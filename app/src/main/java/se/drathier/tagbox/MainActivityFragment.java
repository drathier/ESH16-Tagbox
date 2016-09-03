package se.drathier.tagbox;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import se.drathier.tagbox.adapters.TagAdapter;
import se.drathier.tagbox.common.SnomedDB;
import se.drathier.tagbox.tagbox.Model;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public RecyclerView recyclerView;
    public TagAdapter tagAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.tag_list);

        final Model model = new Model();
        model.CountryCode = "se";
        model.SSN = "910612-1099";
        model.given_name = "Anders";
        model.bt_ab = Model.BloodTypeAB.A;
        model.bt_plus = Model.BloodTypePlusMinus.Plus;
        model.is_organ_donor = false;
        model.is_male = true;

        Model.Snomed_id snomed_id = new Model.Snomed_id();
        snomed_id.id = 91934008;
        snomed_id.from = Calendar.getInstance();
        snomed_id.from.set(1991, 6, 12);
        snomed_id.to = Calendar.getInstance();
        snomed_id.severity = Model.Severity.High;

        Model.Snomed_id snomed_id2 = new Model.Snomed_id();
        snomed_id2.id = 34563004;
        snomed_id2.from = Calendar.getInstance();
        snomed_id2.from.set(1991, 6, 12);
        snomed_id2.to = Calendar.getInstance();
        snomed_id2.severity = Model.Severity.High;

        model.snomed_ids = new ArrayList<>();
        model.snomed_ids.add(snomed_id);
        model.snomed_ids.add(snomed_id2);


        tagAdapter = new TagAdapter();


        SnomedDB.fetch("en", model, new SnomedDB.SnomedModelResponse() {
            @Override
            public void dataAdded() {
                tagAdapter.list = new ArrayList<>();
                tagAdapter.list.add(model);
                tagAdapter.list.add(model);
                tagAdapter.list.add(model);
                tagAdapter.notifyDataSetChanged();
            }
        });




        DisplayMetrics metrics = getResources().getDisplayMetrics();

        int spanCount = (int) (metrics.widthPixels / (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, metrics));

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                int space = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getContext().getResources().getDisplayMetrics());

                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                outRect.top = space;
            }
        });



        recyclerView.setAdapter(tagAdapter);




        return view;
    }
}
