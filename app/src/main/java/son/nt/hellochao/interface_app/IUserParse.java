package son.nt.hellochao.interface_app;

import com.parse.ParseException;

import son.nt.hellochao.dto.UserDto;

/**
 * Created by Sonnt on 11/14/15.
 */
public interface IUserParse {

    void createAccount (UserDto userDto);
    void isUserExist(String email);

    void setIUserParseCallback(Callback callback);

    interface Callback {
        void onCheckingUserExit(String email, boolean isExist);

        void onUserCreate(UserDto userDto, ParseException error);
    }

}
