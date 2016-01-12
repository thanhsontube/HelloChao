package son.nt.hellochao.fragment.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;

import butterknife.Bind;
import son.nt.hellochao.R;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.parse.UserDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.interface_app.IUserParse;
import son.nt.hellochao.utils.StringUtils;

/**
 * Created by Sonnt on 1/12/16.
 */
public class ForGetPwFragment extends AFragment {

    @Bind(R.id.forget_email)
    EditText edtEmail;

    @Bind(R.id.forget_next)
    View viewNext;
    public static final String ARG_EMAIL = "ARG_EMAIL";
    String email;
    
    AppAPI appAPI;


    public static ForGetPwFragment newInstance(String email) {
        ForGetPwFragment fragment = new ForGetPwFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
        }
        
        appAPI = new AppAPI(getContext());
        appAPI.setIUserParseCallback(new IUserParse.Callback() {
            @Override
            public void onCheckingUserExist(String email, boolean isExist) {
                
            }

            @Override
            public void onUserCreated(UserDto userDto, ParseException error) {

            }

            @Override
            public void onFinishResetPw(ParseException error) {
                viewNext.setEnabled(true);
                if (error != null) {
                    Toast.makeText(getActivity(), getString(R.string.error_reset_pw), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.success_reset_pw), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_pw, container, false);
        return view;
    }

    @Override
    protected void initData() {
        
    }

    @Override
    protected void initLayout(View view) {
        setupToolbarIfNeeded(view, getString(R.string.forgot_password));
    }

    @Override
    protected void initListener(View view) {
        viewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtils.isValidEmail(edtEmail.getText().toString().trim())) {
                    Toast.makeText(getActivity(), R.string.invalid_email_address, Toast.LENGTH_SHORT).show();
                    return;
                }
                viewNext.setEnabled(false);
                appAPI.resetPassWord(edtEmail.getText().toString().trim());


            }
        });

    }

    @Override
    protected void updateLayout() {
        if (!TextUtils.isEmpty(email)) {
            edtEmail.setText(email);
        }

    }
}
