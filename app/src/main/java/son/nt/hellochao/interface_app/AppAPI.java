package son.nt.hellochao.interface_app;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.dto.HelloChaoSubmitDto;
import son.nt.hellochao.dto.RankDto;
import son.nt.hellochao.dto.TopDto;
import son.nt.hellochao.dto.UpdateUserInfoDto;
import son.nt.hellochao.dto.UserDto;
import son.nt.hellochao.loader.HTTPParseUtils;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 11/9/15.
 */
public class AppAPI implements IHelloChao, IUserParse {
    public static final String TAG = "AppAPI";
    private Context context;

    IHelloChao.HelloChaoCallback helloChaoCallback = null;
    IUserParse.Callback parseUserCallback = null;
    ArrayList<TopDto> list = new ArrayList<>();
    static AppAPI INSTANCE = null;

    public static void createInstance(Context context) {
        INSTANCE = new AppAPI(context);

    }

    public AppAPI(Context context) {
        this.context = context;

    }

    public static AppAPI getInstance() {
        return INSTANCE;
    }

    @Override
    public void helloChaoGetDailyQuestions() {
        int[] arr = DatetimeUtils.getCurrentTime();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(DailySpeakDto.class.getSimpleName());
        query.whereEqualTo("day", arr[0]);
        query.whereEqualTo("month", arr[1]);
        query.whereEqualTo("year", arr[2]);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null || list == null || list.size() != 10) {
                    HTTPParseUtils.getInstance().withHelloChaoDailyTest();
                    return;
                }

                Logger.debug(TAG, ">>>" + "size:" + list.size());
                ArrayList<DailySpeakDto> dailyList = new ArrayList<DailySpeakDto>();
                DailySpeakDto d;
                for (ParseObject p : list) {
                    d = new DailySpeakDto();
                    d.setMonth(p.getInt("month"));
                    d.setYear(p.getInt("year"));
                    d.setDay(p.getInt("day"));
                    d.setLinkMp3(p.getString("linkMp3"));
                    d.setSentenceEng(p.getString("sentenceEng"));
                    d.setSentenceVi(p.getString("sentenceVi"));
                    dailyList.add(d);
                }
//                ResourceManager.getInstance().setDailySpeakDtoList(dailyList);
                if (helloChaoCallback != null) {
                    helloChaoCallback.helloChaoGetDailyQuestions(dailyList);
                }

            }
        });

    }

    @Override
    public void helloChaoGetUserTop() {
        Logger.debug(TAG, ">>>" + "helloChaoGetUserTop");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TopScore");
        query.addDescendingOrder("score");
        query.addDescendingOrder("total");

        int[] arr = DatetimeUtils.getCurrentTime();
        //check first
        query.whereEqualTo("day", arr[0]);
        query.whereEqualTo("month", arr[1]);
        query.whereEqualTo("year", arr[2]);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> l, ParseException e) {
                if (e != null) {
                    return;
                }
                list.clear();
                TopDto dto;
                int i = 1;
                for (ParseObject p : l) {
                    dto = new TopDto();
                    dto.setNo(i);
                    dto.setName(p.getString("username"));
                    dto.setScore(p.getInt("score"));
                    dto.setTotalTime(p.getInt("total"));
                    dto.setSubmitTime(p.getLong("submitTime"));
                    i++;
                    list.add(dto);
                }

                if (helloChaoCallback != null) {
                    helloChaoCallback.helloChaoGetListUserTop(list);
                }
            }
        });

    }

    @Override
    public void helloChaoSubmitTest(HelloChaoSubmitDto helloChaoSubmitDto) {

    }

    @Override
    public void pushDailyQuestionsFromWebToParse(List<DailySpeakDto> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        for (DailySpeakDto d : list) {
            push(d);
        }


    }

    public void push(final DailySpeakDto d) {
        Logger.debug(TAG, ">>>" + "---push :" + d.getSentenceEng());
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(DailySpeakDto.class.getSimpleName());
        query.whereEqualTo("sentenceEng", d.getSentenceEng());
        query.whereEqualTo("day", d.getDay());
        query.whereEqualTo("month", d.getMonth());
        query.whereEqualTo("year", d.getYear());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null || parseObject == null) {
                    Logger.debug(TAG, ">>>" + "begin push");
                    ParseObject p = new ParseObject(DailySpeakDto.class.getSimpleName());
                    p.put("day", d.getDay());
                    p.put("month", d.getMonth());
                    p.put("year", d.getYear());
                    p.put("sentenceEng", d.getSentenceEng());
                    p.put("sentenceVi", d.getSentenceVi());
                    p.put("linkMp3", d.getLinkMp3());
                    p.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Logger.debug(TAG, ">>>" + "push Done with error:" + e);
                        }
                    });
                } else {
                    Logger.debug(TAG, ">>>" + "EXIST:" + d.getSentenceEng());
                }
            }
        });

    }

    //push rank
    public void pushRank(final RankDto d) {
        Logger.debug(TAG, ">>>" + "---push :" + d.getRankName());
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(RankDto.class.getSimpleName());
        query.whereEqualTo("rankNo", d.getRankNo());
        query.whereEqualTo("rankName", d.getRankName());
        query.whereEqualTo("rankIcon", d.getRankIcon());
        query.whereEqualTo("rankScoreStandard", d.getRankScoreStandard());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null || parseObject == null) {
                    Logger.debug(TAG, ">>>" + "begin push");
                    ParseObject p = new ParseObject(RankDto.class.getSimpleName());
                    p.put("rankNo", d.getRankNo());
                    p.put("rankName", d.getRankName());
                    p.put("rankIcon", d.getRankIcon());
                    p.put("rankScoreStandard", d.getRankScoreStandard());
                    p.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Logger.debug(TAG, ">>>" + "push Done with error:" + e);
                        }
                    });
                } else {
                    Logger.debug(TAG, ">>>" + "EXIST:" + d.getRankName());
                }
            }
        });

    }

    @Override
    public void setHelloCHaoCallback(HelloChaoCallback callback) {
        this.helloChaoCallback = callback;
    }


    @Override
    public void isUserExist(final String email) {
        Logger.debug(TAG, ">>>" + "isUserExist:" + email);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", email);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                Logger.debug(TAG, ">>>" + "parseUser:" + parseUser + ";e:" + e);
                if (parseUser != null) {
                    if (parseUserCallback != null) {
                        parseUserCallback.onCheckingUserExit(email, true);
                        return;
                    }
                }
                if (parseUserCallback != null) {
                    parseUserCallback.onCheckingUserExit(email, false);
                    return;
                }

            }
        });

    }

    @Override
    public void setIUserParseCallback(Callback callback) {
        this.parseUserCallback = callback;

    }

    @Override
    public void updateUserInfo(UpdateUserInfoDto d) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser == null) {
            Toast.makeText(context, "ERROR : updateUserInfo parseUser is NULL" , Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(d.fbID)) {
            parseUser.put("fbId", d.fbID);
        }

        if (!TextUtils.isEmpty(d.name)) {
            parseUser.put("name", d.name);
        }
        if (!TextUtils.isEmpty(d.avatar)) {
            parseUser.put("avatar", d.avatar);
        }

        if (!TextUtils.isEmpty(d.fbLink)) {
            parseUser.put("fbLink", d.fbLink);
        }

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Logger.debug(TAG, ">>>" + "updateUserInfo DONE with e:" + e);
            }
        });
    }

    @Override
    public void createAccount(final UserDto userDto) {
        Logger.debug(TAG, ">>>" + "createAccount:" + userDto.getEmail());
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(userDto.getEmail());
        parseUser.setEmail(userDto.getEmail());
        parseUser.setPassword(userDto.getPassword());

        parseUser.put("gender", userDto.getGender());
        parseUser.put("name", userDto.getName());
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                Logger.debug(TAG, ">>>" + "Done e:" + e);
                if (parseUserCallback != null) {
                    parseUserCallback.onUserCreated(userDto, e);
                }
            }
        });
    }
}
