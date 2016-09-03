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

import se.drathier.tagbox.common.LocalProfile;
import se.drathier.tagbox.tagbox.Model;

/**
 * Created by anders on 03/09/16.
 */
public class ProfileFragment extends Fragment {

    RadioButton zero, a, b, ab, plus, minus, female, male, yes, no;
    EditText name, ssn;

    public View buttonEdit;

    public ProfileFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, null);

        name = (EditText) view.findViewById(R.id.name);
        ssn = (EditText) view.findViewById(R.id.ssn);

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

        if(LocalProfile.model.is_male)
            male.setChecked(true);
        else
            female.setChecked(true);

        if(LocalProfile.model.is_organ_donor)
            yes.setChecked(true);
        else
            no.setChecked(true);


        buttonEdit = view.findViewById(R.id.button_edit_terms);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditTermsActivity.class));
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
}
