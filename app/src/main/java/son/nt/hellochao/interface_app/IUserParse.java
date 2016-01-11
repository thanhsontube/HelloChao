package son.nt.hellochao.interface_app;

import com.parse.ParseException;

import son.nt.hellochao.dto.UpdateUserInfoDto;
import son.nt.hellochao.dto.parse.UserDto;

/**
 * Created by Sonnt on 11/14/15.
 */
public interface IUserParse {

    void createAccount (UserDto userDto);
    void isUserExist(String email);

    void setIUserParseCallback(Callback callback);

    void updateUserInfo (UpdateUserInfoDto updateUserInfoDto);

    interface Callback {
        void onCheckingUserExit(String email, boolean isExist);

        void onUserCreated(UserDto userDto, ParseException error);
    }

}
