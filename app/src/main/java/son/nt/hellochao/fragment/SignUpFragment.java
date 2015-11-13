package son.nt.hellochao.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import son.nt.hellochao.R;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.utils.KeyBoardUtils;
import son.nt.hellochao.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends AFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String email;
    private String password;

    private OnFragmentInteractionListener mListener;


    @Bind(R.id.sign_up_email_next)
    TextView txtNext;

    @Bind(R.id.sign_up_email)
    AppCompatEditText txtEmail;

    @Bind(R.id.sign_up_email_til)
    TextInputLayout textInputLayout;

    @Override
    protected void initLayout(View view) {
//        getAActivity().getSupportActionBar().setTitle("Sign up");


    }

    @Override
    protected void initListener(View view) {
        txtNext.setOnClickListener(this);
        txtEmail.addTextChangedListener(textWatcher);
        view.findViewById(R.id.sign_up_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser parseUser = new ParseUser();
//                parseUser.setUsername(txtUsername.getText().toString().trim());
//                parseUser.setPassword(txtPassword.getText().toString().trim());
                parseUser.setEmail(txtEmail.getText().toString().trim());

                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(getActivity(), "Error Register:" + e.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Register successful!", Toast.LENGTH_SHORT).show();
                            View view = getActivity().getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            mListener.onRegister();
                        }
                    }
                });
            }
        });


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
            password = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

        void onRegister();
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void updateLayout() {
        if (!TextUtils.isEmpty(email)) {
            txtEmail.setText(email);
            checkEmailValid(email);
        }

    }

    private void checkEmailValid (String email) {
        if (!StringUtils.isValidEmail(email)) {
            textInputLayout.setEnabled(true);
            textInputLayout.setError("Email invalid");
            txtNext.setEnabled(false);
            return;
        }
        textInputLayout.setEnabled(false);
        textInputLayout.setError("");
        txtNext.setEnabled(true);

    }

    @Override
    public void onClick(View view) {
        KeyBoardUtils.close(getAActivity());
        switch (view.getId()) {
            case R.id.sign_up_email_next:
                break;
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            email = editable.toString();
            checkEmailValid(email);

        }
    };


}
