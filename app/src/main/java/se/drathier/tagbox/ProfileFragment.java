package se.drathier.tagbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by anders on 03/09/16.
 */
public class ProfileFragment extends Fragment {

    RadioButton zero, a, b, ab, plus, minus, female, male, yes, no;
    EditText name, ssn;

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


        return view;
    }
}
