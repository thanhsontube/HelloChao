package son.nt.hellochao.fragment.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apptimize.Apptimize;
import com.apptimize.ApptimizeTest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.Profile;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.Bind;
import son.nt.hellochao.R;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.UpdateUserInfoDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.utils.Logger;

public class ProfileFragment extends AFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ProfileFragment";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.profile_image)
    ImageView imgAvatar;
    @Bind(R.id.profile_name)
    TextView txtName;

    @Bind(R.id.profile_link_fb)
    View linkFB;
    @Bind(R.id.link_with_facebook)
    TextView txtLinkWithFb;

    @Bind(R.id.logout_ll) View logoutView;
    @Bind(R.id.profile_follows) TextView txtFollows;



    Collection<String> pers = new ArrayList<>();

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pers.add("email");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLayout(View view) {
        Apptimize.runTest("180 Follows", new ApptimizeTest() {
            @Override
            public void baseline() {
// Variant: original
                txtFollows.setText("you have 150 people following");
            }

            @SuppressWarnings("unused")
            public void variation1() {
// Variant: 180 f
                txtFollows.setText("you have 180 people following");
            }
        });

    }

    @Override
    protected void initListener(View view) {
        txtLinkWithFb.setOnClickListener(this);
        logoutView.setOnClickListener(this);

    }

    @Override
    protected void updateLayout() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        Logger.debug(TAG, ">>>" + "updateLayout:" + parseUser);
        if (parseUser == null) {
            logoutView.setVisibility(View.GONE);
            return;
        }
        if (ParseFacebookUtils.isLinked(parseUser)) {
            String avatar = parseUser.getString("avatar");
            if (!TextUtils.isEmpty(avatar)) {

                Glide.with(this).load(avatar).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(imgAvatar);
            }
        } else {
            Glide.with(this).load(R.drawable.ic_no_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(imgAvatar);
        }
        logoutView.setVisibility(View.VISIBLE);

//        linkFB.setVisibility(ParseFacebookUtils.isLinked(parseUser) ? View.GONE : View.VISIBLE);
        txtLinkWithFb.setText(ParseFacebookUtils.isLinked(parseUser) ? "UnLink with Facebook" : "link with Facebook");

        String name = parseUser.getString("name");
        Logger.debug(TAG, ">>>" + "name:" + name);


        if (!TextUtils.isEmpty(name)) {

            txtName.setText(name);
        }


    }

    private void linkWithFb() {
        final ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            return;
        }
        Logger.debug(TAG, ">>>" + "linkWithFb user:" + user.getUsername());
        if (!ParseFacebookUtils.isLinked(user)) {

            ParseFacebookUtils.linkWithReadPermissionsInBackground(user, this, pers, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Logger.debug(TAG, ">>>" + "linkWithFb done:" + e);
                    if (ParseFacebookUtils.isLinked(user)) {
                        Logger.debug(TAG, ">>>" + "Woohoo, user logged in with Facebook!");
                        getUserInfo();
                        updateLayout();
                    }
                }
            });

        } else {
            Logger.debug(TAG, ">>>" + "Un Link");
            ParseFacebookUtils.unlinkInBackground(user, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Logger.debug(TAG, ">>>" + "Unlink Done:" + e);
                }
            });
        }
    }

    private void getUserInfo () {
        Logger.debug(TAG, ">>>" + "getUserInfo");
        Profile profile = Profile.getCurrentProfile();
        if (profile == null) {
            return;
        }
        String name = profile.getName();
        String fbId = profile.getId();
        Uri image = profile.getProfilePictureUri(120,120);
        Logger.debug(TAG, ">>>" + "image:" + image.toString() + " ;name:" + name +";fbId:" + profile.getId() + ";info:" + profile.describeContents());
        Logger.debug(TAG, ">>>" + "parse user:" + ParseUser.getCurrentUser().getUsername() + ";email:" + ParseUser.getCurrentUser().getEmail());

        AppAPI.getInstance().updateUserInfo(new UpdateUserInfoDto(name, fbId, image.toString(), profile.getLinkUri().toString()));


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.link_with_facebook:
                linkWithFb();
                break;
            case R.id.logout_ll:
                Apptimize.track("Profile logout");
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        getActivity().finish();
                    }
                });
                break;
        }
    }


}
