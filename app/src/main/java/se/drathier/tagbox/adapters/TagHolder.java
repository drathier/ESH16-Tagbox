package se.drathier.tagbox.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.drathier.tagbox.R;
import se.drathier.tagbox.common.SnomedDB;
import se.drathier.tagbox.tagbox.Model;

/**
 * Created by anders on 03/09/16.
 */
public class TagHolder extends RecyclerView.ViewHolder {

    TextView ssn;
    TextView givenName;
    TextView bloodTypeField;
    TextView organDonor;
    TextView terms;

    ImageView genderSymbol;

    public TagHolder(View itemView) {
        super(itemView);

        ssn = (TextView) itemView.findViewById(R.id.ssn_data);
        givenName = (TextView) itemView.findViewById(R.id.given_name_data);
        bloodTypeField = (TextView) itemView.findViewById(R.id.blood_type_data);
        organDonor = (TextView) itemView.findViewById(R.id.organ_donor_data);
        terms = (TextView) itemView.findViewById(R.id.terms_data);

        genderSymbol = (ImageView) itemView.findViewById(R.id.gender_image);

    }

    public void bindData(Model model) {

        String noData = itemView.getContext().getString(R.string.no_data);

        ssn.setText(model.SSN != null ? model.SSN : noData);
        givenName.setText(model.given_name != null ? model.given_name : noData);

        if(model.bt_ab != null && model.bt_plus != null) {

            String bloodType = "";

            switch (model.bt_ab) {
                case A:
                    bloodType = "A";
                    break;
                case B:
                    bloodType = "B";
                    break;
                case AB:
                    bloodType = "AB";
                    break;
                case Zero:
                    bloodType = "0";
                    break;
            }

            switch (model.bt_plus) {
                case Plus:
                    bloodType += "+";
                    break;
                case Minus:
                    bloodType += "-";
                    break;
            }

            bloodTypeField.setText(bloodType);

        }

        organDonor.setText(model.is_organ_donor ? "YES" : "NO");

        genderSymbol.setImageResource(model.is_male ? R.drawable.male : R.drawable.female);

        List<Model.Snomed_id> ids = model.snomed_ids;

        String t = "";

        for (Model.Snomed_id id : ids) {
            t += (t.equals("") ? "\u2022  " + SnomedDB.get(id.id) : "\n\n\u2022  " + SnomedDB.get(id.id));
        }

        terms.setText(t);

    }
}
