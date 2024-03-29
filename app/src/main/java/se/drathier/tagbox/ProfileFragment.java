package se.drathier.tagbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import se.drathier.tagbox.common.LocalProfile;
import se.drathier.tagbox.tagbox.Model;

/**
 * Created by anders on 03/09/16.
 */
public class ProfileFragment extends Fragment {

    RadioButton zero, a, b, ab, plus, minus, female, male, yes, no;
    EditText name, ssn;
    TextView terms;

    public View buttonEdit;
    public View buttonRemove;
    public View buttonSave;

    public ProfileFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, null);

        name = (EditText) view.findViewById(R.id.name);
        ssn = (EditText) view.findViewById(R.id.ssn);
        terms = (TextView) view.findViewById(R.id.terms_data);

        zero = (RadioButton) view.findViewById(R.id.bt_zero);
        a = (RadioButton) view.findViewById(R.id.bt_a);
        b = (RadioButton) view.findViewById(R.id.bt_b);
        ab = (RadioButton) view.findViewById(R.id.bt_ab);
        plus = (RadioButton) view.findViewById(R.id.bt_plus);
        minus = (RadioButton) view.findViewById(R.id.bt_minus);
        female = (RadioButton) view.findViewById(R.id.bt_female);
        male = (RadioButton) view.findViewById(R.id.bt_male);
        yes = (RadioButton) view.findViewById(R.id.bt_yes_organ);
        no = (RadioButton) view.findViewById(R.id.bt_no_organ);


        name.setText(LocalProfile.model.given_name);
        ssn.setText(LocalProfile.model.SSN);

        if(LocalProfile.model.bt_ab != null) {
            switch (LocalProfile.model.bt_ab) {
                case A:
                    a.setChecked(true);
                    break;
                case B:
                    b.setChecked(true);
                    break;
                case AB:
                    ab.setChecked(true);
                    break;
                case Zero:
                    zero.setChecked(true);
                    break;
            }
        }

        if(LocalProfile.model.bt_plus != null) {
            switch (LocalProfile.model.bt_plus) {
                case Minus:
                    minus.setChecked(true);
                    break;
                case Plus:
                    plus.setChecked(true);
                    break;
            }
        }

        if(LocalProfile.model.is_male != null) {

            if (LocalProfile.model.is_male) {
                male.setChecked(true);
                female.setChecked(false);
            } else
                female.setChecked(true);
        }

        if(LocalProfile.model.is_organ_donor != null) {
            if (LocalProfile.model.is_organ_donor)
                yes.setChecked(true);
            else
                no.setChecked(true);
        }


        if(LocalProfile.model.snomed_ids != null) {

            String t = "";
            for (Model.Snomed_id snomed_id : LocalProfile.model.snomed_ids) {

                if(t.equals(""))
                    t += "•  " + snomed_id.response;
                else
                    t += "\n\n•  " + snomed_id.response;
            }

            if(t.equals(""))
                t = "No data";

            terms.setText(t);
        }

        buttonEdit = view.findViewById(R.id.button_add);
        buttonRemove = view.findViewById(R.id.button_remove);


        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), RemoveTermsActivity.class), 5);
            }
        });



        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    LocalProfile.model.is_male = false;
            }
        });

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    LocalProfile.model.is_male = true;
            }
        });

        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    LocalProfile.model.is_organ_donor = true;
            }
        });

        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    LocalProfile.model.is_organ_donor = false;
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), EditTermsActivity.class), 5);
            }
        });

        zero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    LocalProfile.model.bt_ab = Model.BloodTypeAB.Zero;
            }
        });

        a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    LocalProfile.model.bt_ab = Model.BloodTypeAB.A;
            }
        });

        b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    LocalProfile.model.bt_ab = Model.BloodTypeAB.B;
            }
        });

        ab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    LocalProfile.model.bt_ab = Model.BloodTypeAB.AB;
            }
        });

        plus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    LocalProfile.model.bt_plus = Model.BloodTypePlusMinus.Plus;
                }
            }
        });

        minus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    LocalProfile.model.bt_plus = Model.BloodTypePlusMinus.Minus;
                }
            }
        });


        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                LocalProfile.model.given_name = editable.toString();
            }
        });


        ssn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                LocalProfile.model.SSN = editable.toString();
            }
        });






        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(LocalProfile.model.snomed_ids != null) {

            String t = "";
            for (Model.Snomed_id snomed_id : LocalProfile.model.snomed_ids) {

                if(t.equals(""))
                    t += "•  " + snomed_id.response;
                else
                    t += "\n\n•  " + snomed_id.response;
            }

            if(t.equals(""))
                t = "No data";

            terms.setText(t);
        }

    }
}
