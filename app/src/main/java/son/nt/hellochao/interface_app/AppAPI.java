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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.dto.DailyTopDto;
import son.nt.hellochao.dto.RankDto;
import son.nt.hellochao.dto.TopDto;
import son.nt.hellochao.dto.UpdateUserInfoDto;
import son.nt.hellochao.dto.UserDto;
import son.nt.hellochao.loader.HTTPParseUtils;
import son.nt.hellochao.parse_object.HelloChaoDaily;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 11/9/15.
 */
public class AppAPI implements IHelloChao, IUserParse {
    public static final String TAG = "AppAPI";
    private Context context;

    HcCallback hcCallback = null;
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
//                    HTTPParseUtils.getInstance().withHcDaily();
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
//                if (hcCallback != null) {
//                    hcCallback.throwDailySentences(dailyList);
//                }

            }
        });

    }

    @Override
    public void getHcUserDailyTop() {
        Logger.debug(TAG, ">>>" + "getHcUserDailyTop");
        ParseQuery<DailyTopDto> query = DailyTopDto.getQuery();
        query.addDescendingOrder("correctSentence");
        query.addDescendingOrder("totalSeconds");
        query.include("user");

        query.whereEqualTo("relativeTime", DatetimeUtils.relativeTime());
        query.findInBackground(new FindCallback<DailyTopDto>() {
            @Override
            public void done(List<DailyTopDto> list, ParseException e) {
                if (hcCallback != null) {
                    hcCallback.throwUserTop(new ArrayList<DailyTopDto>(list));
                }
            }
        });

//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> l, ParseException e) {
//                if (e != null) {
//                    return;
//                }
//                ArrayList<TopDto> list = new ArrayList<TopDto>();
//                TopDto dto;
//                int i = 1;
//                for (ParseObject p : l) {
//                    dto = new TopDto();
//                    dto.setNo(i);
//                    dto.setName(p.getString("userName"));
//                    dto.setScore(p.getInt("correctSentence"));
//                    dto.setTotalTime(p.getInt("totalSeconds"));
//                    dto.setSubmitTime(p.getDate("submitTime"));
//                    i++;
//                    list.add(dto);
//                }
//
//                if (hcCallback != null) {
//                    hcCallback.throwUserTop(list);
//                }
//            }
//        });

    }

    @Override
    public void getHcDaily() {
        Logger.debug(TAG, ">>>" + "getHcDaily");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(HelloChaoDaily.class.getSimpleName());
        query.whereContainedIn("dates", Arrays.asList(getDates()));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null || list == null || list.size() == 0 || list.size() != 10) {
                    Logger.error(TAG, ">>>" + "getHcDaily ERROR:" + e);
                    HTTPParseUtils.getInstance().withHcDaily();
                    return;
                }
                Logger.debug(TAG, ">>>" + "getHcDaily done:" + e + ";list:" + list.size());
                ArrayList<HelloChaoDaily> dailyList = new ArrayList<HelloChaoDaily>();
                for (ParseObject p : list) {
                    Date date = p.getDate("");
                    String audio = p.getString("audio");
                    String text = p.getString("text");
                    String translate = p.getString("translate");
                    int level = p.getInt("level");
                    int value = p.getInt("value");
                    HelloChaoDaily h = new HelloChaoDaily(audio, text, translate, level, value);
                    dailyList.add(h);
                }
//                OttoBus.post(new GoDaiLyTest(dailyList));
                if (hcCallback != null) {
                    Logger.debug(TAG, ">>>" + "AAAAAAA hcCallback.throwDailySentences(dailyList)");
                    hcCallback.throwDailySentences(dailyList);
                }

            }
        });



    }

    private String getDates () {
        int[] arr = DatetimeUtils.getCurrentTime();
        int day = arr[0];
        int month = arr[1];
        int year = arr[2];
        StringBuilder dates = new StringBuilder();
        dates.append(day < 10 ? "0" + day : String.valueOf(day));
        dates.append("_");
        dates.append(month < 10 ? "0" + month : String.valueOf(month));
        dates.append("_");
        dates.append(String.valueOf(year));

        return dates.toString();
    }

    @Override
    public void hcSubmitTestResult(final DailyTopDto dto) {
        Logger.debug(TAG, ">>>" + "hcSubmitTestResult");
        //check first

        ParseQuery<DailyTopDto> query = DailyTopDto.getQuery();
        query.whereEqualTo("relativeTime", dto.getRelativeTime());
        query.whereEqualTo("user", dto.getParseUser());
        query.getFirstInBackground(new GetCallback<DailyTopDto>() {
            @Override
            public void done(DailyTopDto parseObject, ParseException e) {
                if (parseObject == null) {
                    //push
                    dto.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Logger.debug(TAG, ">>>" + "ERROR update score");
                                if (hcCallback != null) {
                                    hcCallback.throwSubmitDaily(false, e.toString());
                                }
                                return;
                            }
                            Logger.debug(TAG, ">>>" + "Your result in committed successful!");
                            if (hcCallback != null) {
                                hcCallback.throwSubmitDaily(false, null);
                            }
                        }
                    });
                } else {
                    int beforeScore = parseObject.getCorrectSentence();
                    int total = parseObject.getTotalSeconds();
                    if (beforeScore < dto.getCorrectSentence() || (beforeScore == dto.getCorrectSentence() && dto.getTotalSeconds() < total)) {

                        parseObject.put("correctSentence", dto.getCorrectSentence());
                        parseObject.put("totalSeconds", dto.getTotalSeconds());
                        parseObject.put("submitTime", dto.getSubmitTime());
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Logger.debug(TAG, ">>>" + "ERROR update score");
                                    if (hcCallback != null) {
                                        hcCallback.throwSubmitDaily(true, e.toString());
                                    }
                                    return;
                                }
                                Logger.debug(TAG, ">>>" + "Update score successful!");
                                if (hcCallback != null) {
                                    hcCallback.throwSubmitDaily(true, null);
                                }

                            }
                        });
                    } else {
                        if (hcCallback != null) {
                            hcCallback.throwSubmitDaily(true, "Your score this time is NOT better than before ! Cancel Update Score.");
                        }
                    }
                }
            }
        });

//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("DailyTopDto");
//
//        query.whereEqualTo("relativeTime", dto.getRelativeTime());
//        query.whereEqualTo("user", dto.getParseUser());
//        query.getFirstInBackground(new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject parseObject, ParseException e) {
//                if (parseObject != null) {
//                    int beforeScore = parseObject.getInt("correctSentence");
//                    int total = parseObject.getInt("totalSeconds");
//                    if (beforeScore < dto.getCorrectSentence() || (beforeScore == dto.getCorrectSentence() && dto.getTotalSeconds() < total)) {
//
//                        parseObject.put("correctSentence", dto.getCorrectSentence());
//                        parseObject.put("totalSeconds", dto.getTotalSeconds());
//                        parseObject.put("submitTime", dto.getSubmitTime());
//                        parseObject.saveInBackground(new SaveCallback() {
//                            @Override
//                            public void done(ParseException e) {
//                                if (e != null) {
//                                    Logger.debug(TAG, ">>>" + "ERROR update score");
//                                    if (hcCallback != null) {
//                                        hcCallback.throwSubmitDaily(true, e.toString());
//                                    }
//                                    return;
//                                }
//                                Logger.debug(TAG, ">>>" + "Update score successful!");
//                                if (hcCallback != null) {
//                                    hcCallback.throwSubmitDaily(true, null);
//                                }
//
//                            }
//                        });
//                    } else {
//                        if (hcCallback != null) {
//                            hcCallback.throwSubmitDaily(true, "Your score this time is NOT better than before ! Cancel Update Score.");
//                        }
//                    }
//
//
//                } else {
//                    dto.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e != null) {
//                                Logger.debug(TAG, ">>>" + "ERROR update score");
//                                if (hcCallback != null) {
//                                    hcCallback.throwSubmitDaily(false, e.toString());
//                                }
//                                return;
//                            }
//                            Logger.debug(TAG, ">>>" + "Update score successful!");
//                            if (hcCallback != null) {
//                                hcCallback.throwSubmitDaily(false, null);
//                            }
//                        }
//                    });
////                    ParseObject p = new ParseObject("DailyTopDto");
////                    p.put("correctSentence", dto.correctSentence);
////                    p.put("totalSeconds", dto.totalSeconds);
////                    p.put("submitTime", dto.submitTime);
////                    p.put("userName", dto.username);
////                    p.put("relativeTime", dto.relativeTime);
////
////                    p.saveInBackground(new SaveCallback() {
////                        @Override
////                        public void done(ParseException e) {
////                            if (e != null) {
////                                Logger.debug(TAG, ">>>" + "ERROR update score");
////                                if (hcCallback != null) {
////                                    hcCallback.throwSubmitDaily(false, e.toString());
////                                }
////                                return;
////                            }
////                            Logger.debug(TAG, ">>>" + "Update score successful!");
////                            if (hcCallback != null) {
////                                hcCallback.throwSubmitDaily(false, null);
////                            }
////                        }
////                    });
//                }
//            }
//        });

    }

    @Override
    public void pushDailyQuestionsFromWebToParse(List<HelloChaoDaily> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        for (HelloChaoDaily d : list) {
            push(d);
        }


    }

    public void push(final HelloChaoDaily d) {
        Logger.debug(TAG, ">>>" + "---push :" + d.getText());
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(HelloChaoDaily.class.getSimpleName());

        //check the sentence is available
        query.whereEqualTo("text", d.getText());

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    //push
                    Logger.debug(TAG, ">>>" + "begin push");
                    ParseObject p = new ParseObject(HelloChaoDaily.class.getSimpleName());
                    p.put("text", d.getText());
                    p.put("audio", d.getAudio());
                    p.put("translate", d.getTranslate());
                    p.put("level", d.getLevel());
                    p.put("value", d.getValue());
                    p.put("tags", d.getTags());
                    p.put("dates", d.getDates());
                    p.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Logger.debug(TAG, ">>>" + "push Done with error:" + e);
                        }
                    });
                } else {
                    List<String> listDates = parseObject.getList("dates");
                    if (listDates.contains(d.getDates().get(0))) {
                        Logger.debug(TAG, ">>>" + "EXIST DATES:" + d.getDates());
                        return;
                    }
                    Logger.debug(TAG, ">>>" + "update new Dates:");

                    listDates.addAll(d.getDates());
                    parseObject.put("dates", listDates);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Logger.debug(TAG, ">>>" + "Update Dates DONE e:" + e);
                        }
                    });

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
    public void setHcCallback(HcCallback callback) {
        this.hcCallback = null;
        this.hcCallback = callback;
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
