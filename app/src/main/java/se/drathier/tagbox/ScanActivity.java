package se.drathier.tagbox;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

import se.drathier.tagbox.adapters.TagAdapter;
import se.drathier.tagbox.common.SnomedDB;
import se.drathier.tagbox.tagbox.Model;

public class ScanActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public TagAdapter tagAdapter;

    public View empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        overridePendingTransition(0, 0);


        View scan = findViewById(R.id.scan);
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(scan,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();

        recyclerView = (RecyclerView) findViewById(R.id.tag_list);
        empty = findViewById(R.id.empty);
        empty.setVisibility(View.VISIBLE);

        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empty.setVisibility(View.GONE);
            }
        });

        final Model model = new Model();
        model.CountryCode = "se";
        model.SSN = "920612-1099";
        model.given_name = "Filip";
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


        final Model model2 = new Model();
        model2.CountryCode = "se";
        model2.SSN = "891008-2909";
        model2.given_name = "Vahid";
        model2.bt_ab = Model.BloodTypeAB.AB;
        model2.bt_plus = Model.BloodTypePlusMinus.Minus;
        model2.is_organ_donor = true;
        model2.is_male = true;

        Model.Snomed_id snomed_id3 = new Model.Snomed_id();
        snomed_id3.id = 162059005;
        snomed_id3.from = Calendar.getInstance();
        snomed_id3.from.set(1991, 6, 12);
        snomed_id3.to = Calendar.getInstance();
        snomed_id3.severity = Model.Severity.High;


        model2.snomed_ids = new ArrayList<>();
        model2.snomed_ids.add(snomed_id3);

        tagAdapter = new TagAdapter();
        tagAdapter.list = new ArrayList<>();
        recyclerView.setAdapter(tagAdapter);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        int spanCount = (int) (metrics.widthPixels / (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, metrics));

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                int space = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                outRect.top = space;
            }
        });



        SnomedDB.fetch("se", model, new SnomedDB.SnomedModelResponse() {
            @Override
            public void dataAdded() {
                tagAdapter.list.add(model);
                tagAdapter.notifyDataSetChanged();


            }
        });

        SnomedDB.fetch("se", model2, new SnomedDB.SnomedModelResponse() {
            @Override
            public void dataAdded() {
                tagAdapter.list.add(model2);
                tagAdapter.notifyDataSetChanged();



            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                empty.setVisibility(View.GONE);
            }
        }, 4000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
