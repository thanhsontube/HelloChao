package son.nt.hellochao.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import butterknife.Bind;
import son.nt.hellochao.R;
import son.nt.hellochao.activity.ProfileActivity;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.UpdateUserInfoDto;
import son.nt.hellochao.dto.parse.DailyTopDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.FacebookUtils;
import son.nt.hellochao.utils.KeyBoardUtils;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.PreferenceUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends AFragment implements View.OnClickListener {
    public static final String TAG = "LoginFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.login_forgot_password)
    TextView txtForgotPassword;

    @Bind(R.id.login_txt_sign_up)
    TextView txtSignUp;
    @Bind(R.id.login_by_facebook)
    TextView loginWithFacebook;
    @Bind(R.id.login_by_google)
    TextView loginWithGoogle;

    @Bind(R.id.login_username)
    AppCompatEditText txtEmail;

    @Bind(R.id.login_password)
    AppCompatEditText txtPassword;
    View viewReset;

    AppAPI appAPI;

    private Collection<String> permissions = new ArrayList<>();

    @Override
    protected void initLayout(View view) {

        setupToolbarIfNeeded(view, getString(R.string.login));
        FacebookUtils.getHashKey(getAActivity());
        viewReset = view.findViewById(R.id.login_reset_ll);
        viewReset.setVisibility(View.GONE);

        txtForgotPassword.setPaintFlags(txtForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null) {
            parseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    Logger.debug(TAG, ">>>" + "ParseUser logout !!!");
                }
            });
        }

    }

    @Override
    protected void initListener(View view) {
        txtForgotPassword.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        loginWithFacebook.setOnClickListener(this);
        loginWithGoogle.setOnClickListener(this);


        view.findViewById(R.id.login_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(txtEmail.getText().toString(), txtPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e != null) {
                            Toast.makeText(getActivity(), "Login Error!", Toast.LENGTH_SHORT).show();
                        } else {
                            View view = getActivity().getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            Toast.makeText(getActivity(), "Hello:" + parseUser.getUsername(), Toast.LENGTH_SHORT).show();
                            mListener.onLoginSuccess();
                        }

                    }
                });
            }
        });

        view.findViewById(R.id.login_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewReset.getVisibility() == View.GONE) {

                    viewReset.setVisibility(View.VISIBLE);
                } else {
                    viewReset.setVisibility(View.GONE);
                }

            }
        });

        view.findViewById(R.id.login_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.requestPasswordResetInBackground(txtEmail.getText().toString(), new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            View view = getActivity().getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            Toast.makeText(getActivity(), "An email was successfully sent with reset instructions.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        appAPI = new AppAPI(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
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
        public void onFragmentInteraction(Uri uri);

        void onLoginSuccess();

        void onSignUp(String email, String password);

        void onResetPw (String email);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void updateLayout() {


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void loginByFacebook() {
        permissions.clear();
        permissions.add("email");
        permissions.add("user_friends");
        Logger.debug(TAG, ">>>" + "loginByFacebook");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException e) {
                Logger.debug(TAG, ">>>" + "loginByFacebook:" + e);
                if (e != null) {
                    Toast.makeText(getActivity(), "Login Error:" + e.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (user == null) {
                    Logger.debug("MyApp", ">>>Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Logger.debug("MyApp", ">>>User signed up and logged in through Facebook!");
                } else {
                    Logger.debug("MyApp", ">>>User logged in through Facebook!");
                    Logger.debug(TAG, ">>>" + "Email:" + user.getEmail() + ";name:" + user.getUsername());
                }

                if (user == null) {
                    return;
                }

                if (ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
                    appAPI.setLinkWithFacebook();
                    Logger.debug(TAG, ">>>" + "user link fb already");
                    getUserInfo();
                    return;
                }

                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginFragment.this, permissions, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {


                    }
                });

//                if (user != null) {
//                    if (!ParseFacebookUtils.isLinked(user)) {
//                        ParseFacebookUtils.linkInBackground(user, AccessToken.getCurrentAccessToken(), new SaveCallback() {
//                            @Override
//                            public void done(ParseException e) {
//                                Logger.debug(TAG, ">>>" + "linkInBackground:" + e);
//                                if (e != null) {
//                                    return;
//                                }
//
//                                if (ParseFacebookUtils.isLinked(user)) {
//                                    Logger.debug("MyApp", ">>> Woohoo, user logged in with Facebook!");
//                                    Logger.debug(TAG, ">>>" + "Email:" + user.getEmail() + ";name:" + user.getUsername());
//                                    getUserInfo();
//                                }
//                            }
//                        });
//                    } else {
//                        Logger.debug(TAG, ">>>" + "LINKED");
//                        getUserInfo();
//                    }
//                }

            }
        });
    }

    private void getUserInfo() {
        Logger.debug(TAG, ">>>" + "getUserInfo");
        Profile profile = Profile.getCurrentProfile();
        if (profile == null) {
            return;
        }
        String name = profile.getName();
        String fbId = profile.getId();
        Uri image = profile.getProfilePictureUri(480, 480);
        Logger.debug(TAG, ">>>" + "image:" + image.toString() + " ;name:" + name + ";fbId:" + profile.getId() + ";info:" + profile.describeContents());
        Logger.debug(TAG, ">>>" + "parse user:" + ParseUser.getCurrentUser().getUsername() + ";email:" + ParseUser.getCurrentUser().getEmail());

        appAPI.updateUserInfo(new UpdateUserInfoDto(name, fbId, image.toString(), profile.getLinkUri().toString()));

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

        getAActivity().startActivity(new Intent(getContext(), ProfileActivity.class));
        getActivity().finish();

    }

    @Override
    public void onClick(View view) {
        KeyBoardUtils.close(getAActivity());
        switch (view.getId()) {
            case R.id.login_forgot_password:
                mListener.onResetPw(txtEmail.getText().toString().trim());
                break;
            case R.id.login_txt_sign_up:
                if (mListener != null) {
                    mListener.onSignUp(txtEmail.getText().toString(), txtPassword.getText().toString());
                }
                break;
            case R.id.login_by_facebook:
                loginByFacebook();

                break;
        }
    }

}
