package son.nt.hellochao.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import butterknife.Bind;
import son.nt.hellochao.R;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.parse.DailyTopDto;
import son.nt.hellochao.dto.parse.UserDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.interface_app.IUserParse;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.KeyBoardUtils;
import son.nt.hellochao.utils.PreferenceUtil;
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
    public static final int WHAT_SEARCH = 10;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String email;
    private String yourName;
    private String password;
    private String passwordConfirm;
    private String gender;

    AppAPI appAPI;

    private OnFragmentInteractionListener mListener;

    enum SIGN_UP {
        ENTER_EMAIL,
        YOUR_NAME,
        GENDER,
        PASSWORD,
        RE_PASSWORD,
        CREATING,
        FINISH,
    }

    SIGN_UP status = SIGN_UP.ENTER_EMAIL;


    @Bind(R.id.sign_up_email_next)
    TextView txtNext;

    @Bind(R.id.sign_up_email)
    AppCompatEditText txtEmail;

    @Bind(R.id.sign_up_your_name)
    EditText txtYourName;

    @Bind(R.id.sign_up_password)
    AppCompatEditText txtPassword1St;

    @Bind(R.id.sign_up_password_confirm)
    AppCompatEditText txtPasswordConfirm;

    @Bind(R.id.sign_up_email_til)
    TextInputLayout textInputLayout;

    @Bind(R.id.sign_up_password1st_til)
    TextInputLayout textPassword1St;


    @Bind(R.id.sign_up_password1st_til_confirm)
    TextInputLayout textPasswordConfirm;

    @Bind(R.id.sign_up_txt_enter_password)TextView txtHiEnterPassword;

    @Bind(R.id.sign_up_email_Clp)
    ContentLoadingProgressBar contentLoadingProgressBar;

    @Bind(R.id.sign_up_ll_email)
    View viewEmail;
    @Bind(R.id.sign_up_ll_name)
    View viewYourName;
    @Bind(R.id.sign_up_ll_password) View viewPassword;
    @Bind(R.id.sign_up_ll_password_confirm) View viewPasswordConfirm;
    @Bind(R.id.sign_up_ll_gender) View viewGender;
    @Bind(R.id.sign_up_view_male) View viewMale;
    @Bind(R.id.sign_up_view_female) View vieFemale;

    @Bind(R.id.sign_up_check_male)
    RadioButton radioButtonMale;

    @Bind(R.id.sign_up_check_female)
    RadioButton radioButtonFemale;


    HandlerCheckEmailValid handle;

    @Override
    protected void initLayout(View view) {
//        getAActivity().getSupportActionBar().setTitle("Sign up");
        viewEmail.setVisibility(View.VISIBLE);
        viewYourName.setVisibility(View.GONE);
        viewGender.setVisibility(View.GONE);
        viewPassword.setVisibility(View.GONE);
        viewPasswordConfirm.setVisibility(View.GONE);
    }

    @Override
    protected void initListener(View view) {
        txtNext.setOnClickListener(this);
        viewMale.setOnClickListener(this);
        vieFemale.setOnClickListener(this);
        txtEmail.addTextChangedListener(textWatcher);
        txtYourName.addTextChangedListener(textWatcherYourName);
        txtPassword1St.addTextChangedListener(textWatcherPassword1St);
        txtPasswordConfirm.addTextChangedListener(textWatcherPasswordConfirm);




    }

    private void doSignUp() {

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
        handle = new HandlerCheckEmailValid(this);
        appAPI = new AppAPI(getContext());
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
        AppAPI.getInstance().setIUserParseCallback(iUserParseCallback);
    }


    @Override
    protected void updateLayout() {
        txtNext.setEnabled(false);
        if (!TextUtils.isEmpty(email)) {
            txtEmail.setText(email);
            doCheckEmail();
//            checkEmailValid(email);
        }

    }

    private void checkEmailValid(String email) {
        if (!StringUtils.isValidEmail(email)) {
            contentLoadingProgressBar.setVisibility(View.GONE);
            textInputLayout.setEnabled(true);
            textInputLayout.setError("Email invalid");
            txtNext.setEnabled(false);
            return;
        }

        AppAPI.getInstance().isUserExist(email);


    }


    @Override
    public void onClick(View view) {
        KeyBoardUtils.close(getAActivity());
        switch (view.getId()) {
            case R.id.sign_up_email_next:
                doNext();
                break;

            case R.id.sign_up_view_male:
                radioButtonMale.setChecked(true);
                radioButtonFemale.setChecked(false);
                gender = "male";
                txtNext.setEnabled(true);
                break;
            case R.id.sign_up_view_female:
                radioButtonMale.setChecked(false);
                radioButtonFemale.setChecked(true);
                gender = "female";
                txtNext.setEnabled(true);
                break;
        }
    }

    private void doNext() {
        txtNext.setEnabled(false);
        switch (status) {
            case ENTER_EMAIL:
                viewEmail.setVisibility(View.GONE);
                viewYourName.setVisibility(View.VISIBLE);
                txtYourName.requestFocus();
                status = SIGN_UP.YOUR_NAME;
                break;
            case YOUR_NAME:
                viewYourName.setVisibility(View.GONE);
                viewGender.setVisibility(View.VISIBLE);
                status= SIGN_UP.GENDER;

                break;
            case GENDER:

                viewGender.setVisibility(View.GONE);
                viewPassword.setVisibility(View.VISIBLE);
                status= SIGN_UP.PASSWORD;
                txtPassword1St.requestFocus();
                txtHiEnterPassword.setText(String.format(getString(R.string.explain_password), yourName));
                break;
            case PASSWORD:
                txtPasswordConfirm.requestFocus();
                viewPassword.setVisibility(View.GONE);
                viewPasswordConfirm.setVisibility(View.VISIBLE);
                status = SIGN_UP.RE_PASSWORD;
                break;
            case RE_PASSWORD:
                txtPasswordConfirm.clearFocus();
                showProgressDialog(getString(R.string.creating_account), false);
                AppAPI.getInstance().createAccount(new UserDto(email, password, yourName, gender));
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
            txtNext.setEnabled(false);
            doCheckEmail();

        }
    };

    TextWatcher textWatcherYourName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            yourName = editable.toString();
            txtNext.setEnabled(false);
            doCheckYourName();

        }
    };TextWatcher textWatcherPassword1St = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            password = editable.toString();
            txtNext.setEnabled(false);
            doCheckPassword1St();

        }
    };

    TextWatcher textWatcherPasswordConfirm = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            passwordConfirm = editable.toString();
            txtNext.setEnabled(false);
            doCheckPasswordConfirm();

        }
    };

    private void doCheckYourName() {
        txtNext.setEnabled(TextUtils.isEmpty(yourName.trim()) ? false : true);
    }private void doCheckPassword1St() {

        if (TextUtils.isEmpty(password.trim()) || password.length() < 6 ) {
            txtNext.setEnabled(false);
            textPassword1St.setEnabled(true);
            textPassword1St.setError(getString(R.string.error_less_than_6_letters));
        } else {
            txtNext.setEnabled(true);
            textPassword1St.setEnabled(false);
            textPassword1St.setError(null);
        }
    }

    private void doCheckPasswordConfirm() {

        if (password.equals(passwordConfirm)) {
            txtNext.setEnabled(true);
            textPasswordConfirm.setEnabled(false);
            textPasswordConfirm.setError(null);
            txtNext.setText(getString(R.string.create_account));
        } else {
            txtNext.setEnabled(false);
            textPasswordConfirm.setEnabled(true);
            textPasswordConfirm.setError(getString(R.string.password_not_match));
        }
    }

    private void doCheckEmail() {
        if (handle == null) {
            handle = new HandlerCheckEmailValid(SignUpFragment.this);
        }
        handle.removeMessages(WHAT_SEARCH);
        handle.sendEmptyMessageDelayed(WHAT_SEARCH, 1000);
    }

    private class HandlerCheckEmailValid extends Handler {
        private WeakReference<SignUpFragment> weakReference = new WeakReference<SignUpFragment>(null);

        public HandlerCheckEmailValid(SignUpFragment signUpFragment) {
            weakReference = new WeakReference<SignUpFragment>(signUpFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SEARCH:
                    if (weakReference.get() == null) {
                        return;
                    }
                    weakReference.get().contentLoadingProgressBar.setVisibility(View.VISIBLE);

                    weakReference.get().checkEmailValid(weakReference.get().email);
                    break;
            }
//            super.handleMessage(msg);
        }
    }

    IUserParse.Callback iUserParseCallback = new IUserParse.Callback() {
        @Override
        public void onCheckingUserExit(String email, boolean isExist) {
            contentLoadingProgressBar.setVisibility(View.GONE);
            if (isExist) {
                textInputLayout.setEnabled(true);
                textInputLayout.setError("Sorry! " + email + " is registered!");
                txtNext.setEnabled(false);
            } else {
                textInputLayout.setEnabled(false);
                textInputLayout.setError("");
                txtNext.setEnabled(true);
                KeyBoardUtils.close(getAActivity());
            }
        }

        @Override
        public void onUserCreated(UserDto userDto, ParseException error) {
            hideProgressDialog();
            if (error == null) {
                Toast.makeText(getActivity(), "Create Account Successful", Toast.LENGTH_SHORT).show();
                int score = PreferenceUtil.getPreference(getContext(), "score", 0);
                if (score != 0 && ParseUser.getCurrentUser() != null) {
                    int totalTimes = PreferenceUtil.getPreference(getContext(), "totalTimes", 0);

                    DailyTopDto dto = new DailyTopDto();
                    dto.setCorrectSentence(score);
                    dto.setTotalSeconds(totalTimes);
                    dto.setParseUser(ParseUser.getCurrentUser());
                    dto.setSubmitTime(Calendar.getInstance().getTime());
                    dto.setRelativeTime(DatetimeUtils.relativeTime());

                    PreferenceUtil.setPreference(getContext(), "score", 0);
                    appAPI.hcSubmitTestResult(dto);
                }

            } else {
                Toast.makeText(getActivity(), getString(R.string.error_create_account) +">>>"+ error.toString(), Toast.LENGTH_SHORT).show();
                txtNext.setVisibility(View.VISIBLE);
            }
        }
    };



}
