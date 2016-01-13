package son.nt.hellochao.fragment.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Bind(R.id.profile_follows)
    TextView txtFollows;

    @Bind(R.id.profile_rank_icon)
    ImageView profileRank;

    AppAPI appAPI;


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
//        pers.add("email");
        appAPI = new AppAPI(getContext());
        setHasOptionsMenu(true);
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);

        void onRankSystem();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLayout(View view) {
        setupToolbarIfNeeded(view, getString(R.string.profile));
    }

    @Override
    protected void initListener(View view) {
        txtLinkWithFb.setOnClickListener(this);
        profileRank.setOnClickListener(this);

    }

    @Override
    protected void updateLayout() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        Logger.debug(TAG, ">>>" + "updateLayout:" + parseUser);
        if (parseUser == null) {
            return;
        }

        String name = parseUser.getString("name");
        String avatar = "";
        if (ParseFacebookUtils.isLinked(parseUser)) {
            linkFB.setVisibility(View.GONE);
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {

                avatar = Profile.getCurrentProfile().getProfilePictureUri(600, 600).toString();
                name = Profile.getCurrentProfile().getName();
            }
            if (TextUtils.isEmpty(avatar)) {
                avatar = parseUser.getString("avatar");
            }

            Glide.with(this).load(avatar).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(imgAvatar);
        } else {
            linkFB.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.ic_no_avatar).fitCenter().into(imgAvatar);
        }

        txtName.setText(name);
    }

    private void linkWithFb() {
        final ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            return;
        }
        if (!ParseFacebookUtils.isLinked(user)) {

            ParseFacebookUtils.linkWithReadPermissionsInBackground(user, this, pers, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), "Sorry ! There was something wrong. ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
                        getUserInfo();
                        updateLayout();
                    }
                }
            });

        }
    }

    private void getUserInfo() {
        Profile profile = Profile.getCurrentProfile();
        if (profile == null) {
            return;
        }
        String name = profile.getName();
        String fbId = profile.getId();
        Uri image = profile.getProfilePictureUri(120, 120);
        appAPI.updateUserInfo(new UpdateUserInfoDto(name, fbId, image.toString(), profile.getLinkUri().toString()));


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

            case R.id.profile_rank_icon:
                if (mListener != null) {
                    mListener.onRankSystem();;
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                processLogout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void processLogout() {
        ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                getActivity().finish();
            }
        });
    }
}
