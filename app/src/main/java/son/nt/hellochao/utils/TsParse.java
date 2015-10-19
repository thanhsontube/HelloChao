package son.nt.hellochao.utils;

import android.text.TextUtils;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.dto.HomeEntity;

/**
 * Created by Sonnt on 8/18/15.
 */
public  class TsParse implements IParse{


    public static final String TAG ="TsParse";
     TsParse.Callback mCallback = null;

    public static void upData (final HomeEntity d) {
        //check available
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(HomeEntity.class.getSimpleName());
        query.whereEqualTo("homeHref", d.getHomeHref());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Logger.error(TAG, ">>>" + "Error checking:" + d.getHomeTitle() + ">>>>" + e.toString());
                    return;
                }
                if (list == null || list.size() == 0) {
                    ParseObject p = new ParseObject(HomeEntity.class.getSimpleName());
                    p.put("homeGroup", d.getHomeGroup());
                    p.put("homeHref", d.getHomeHref());
                    p.put("homeQuizLink", d.getHomeQuizLink());
                    p.put("homeTitle", d.getHomeTitle());
                    p.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e!= null) {
                                Logger.error(TAG, ">>>" + "Error upload:" + d.getHomeTitle() +">>>" + e.toString());
                            } else {

                                Logger.debug(TAG, ">>>" + "Update complete:" + d.getHomeTitle());
                            }
                        }
                    });
                } else {
                    Logger.debug(TAG, ">>>" + "EXIST:" + d.getHomeTitle());
                }
            }
        });

    }

    public void getHomeEntities () {
        final List<HomeEntity> listData = new ArrayList<>();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(HomeEntity.class.getSimpleName());
        query.addAscendingOrder("homeTitle");
        query.setLimit(200);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Logger.error(TAG, ">>>" + "Error getHomeEntities:" + ">>>" + e.toString());
                } else {
                    HomeEntity d;
                    for (ParseObject p : list) {
                        String homeGroup = p.getString("homeGroup");
                        String homeHref = p.getString("homeHref");
                        String homeQuizLink = p.getString("homeQuizLink");
                        String homeTitle = p.getString("homeTitle");
                        String mp3 = p.getString("homeMp3");
                        d = new HomeEntity();
                        d.setHomeGroup(homeGroup);
                        d.setHomeHref(homeHref);
                        d.setHomeQuizLink(homeQuizLink);
                        d.setHomeTitle(homeTitle);
                        d.setHomeMp3(mp3);
                        d.setHomeImage(p.getString("homeImage"));
                        d.setHomeDescription(p.getString("homeDescription"));
                        d.setHomeFullText(p.getString("homeFullText"));
                        listData.add(d);
                    }
                    Logger.debug(TAG, ">>>" + "listData:" + listData.size());
                    if (mCallback != null) {
                        mCallback.onDoneGetHomeEntities(listData);
                    }

                }
            }
        });

    }



    public void updateMp3 (final HomeEntity d) {
        //check available
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(HomeEntity.class.getSimpleName());
        query.whereEqualTo("homeHref", d.getHomeHref());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null) {
                    Logger.error(TAG, ">>>" + "Error checking:" + d.getHomeTitle() + ">>>>" + e.toString());
                    return;
                }
                String objectID = parseObject.getObjectId();
                query.getInBackground(objectID, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e != null) {
                            Logger.error(TAG, ">>>" + "Error objectID:" + d.getHomeTitle() + ">>>>" + e.toString());
                            return;
                        }
                        String linkMp3 = parseObject.getString("homeMp3");
                        if (TextUtils.isEmpty(linkMp3)) {
                            parseObject.put("homeMp3", d.getHomeMp3());
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Logger.debug(TAG, ">>>" + "update Mp3 success for :" + d.getHomeTitle());
                                }
                            });
                        } else {
                            Logger.debug(TAG, ">>>" + "EXIST mp3:" + d.getHomeTitle());
                        }


                    }
                });

            }
        });

    }

    public void updateImageDescription (final HomeEntity d) {
        //check available
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(HomeEntity.class.getSimpleName());
        query.whereEqualTo("homeHref", d.getHomeHref());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null) {
                    Logger.error(TAG, ">>>" + "Error checking:" + d.getHomeTitle() + ">>>>" + e.toString());
                    return;
                }
                String objectID = parseObject.getObjectId();
                query.getInBackground(objectID, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e != null) {
                            Logger.error(TAG, ">>>" + "Error objectID:" + d.getHomeTitle() + ">>>>" + e.toString());
                            return;
                        }
                        String homeImage = parseObject.getString("homeImage");
                        if (TextUtils.isEmpty(homeImage)) {
                            parseObject.put("homeImage", d.getHomeImage());
                            parseObject.put("homeDescription", d.getHomeDescription());
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Logger.debug(TAG, ">>>" + "update homeImage success for :" + d.getHomeTitle());
                                }
                            });
                        } else {
                            Logger.debug(TAG, ">>>" + "EXIST homeImage:" + d.getHomeTitle());
                        }


                    }
                });

            }
        });

    }

    public void upFullText (final HomeEntity d) {
        Logger.debug(TAG, ">>>" + "upFullText:" + d.getHomeHref());
        //check available
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(HomeEntity.class.getSimpleName());
        query.whereEqualTo("homeHref", d.getHomeHref());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null) {
                    Logger.error(TAG, ">>>" + "Error checking:" + d.getHomeTitle() + ">>>>" + e.toString());
                    return;
                }
                String objectID = parseObject.getObjectId();
                query.getInBackground(objectID, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e != null) {
                            Logger.error(TAG, ">>>" + "Error objectID:" + d.getHomeTitle() + ">>>>" + e.toString());
                            return;
                        }
                        String homeFullText = parseObject.getString("homeFullText");
                        if (TextUtils.isEmpty(homeFullText)) {
                            parseObject.put("homeFullText", d.getHomeFullText());
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Logger.debug(TAG, ">>>" + "update homeFullText success for :" + d.getHomeTitle());
                                }
                            });
                        } else {
                            Logger.debug(TAG, ">>>" + "EXIST homeFullText:" + d.getHomeTitle());
                        }


                    }
                });

            }
        });
    }





//    public static HeroEntry parse(ParseObject p) {
//        HeroEntry dto = new HeroEntry();
//        int no = p.getInt("no");
//        String heroId = p.getString("heroId");
//        String name = p.getString("name");
//        String fullName = p.getString("fullName");
//        String group = p.getString("group");
//        String href = p.getString("href");
//        String avatar = p.getString("avatar");
//        String lore = p.getString("lore");
//        String bgLink = p.getString("bgLink");
//
//        dto.setBaseInfo(heroId, href, avatar, group);
//        dto.no = no;
//        dto.name = name;
//        dto.fullName = fullName;
//        dto.lore = lore;
//        dto.bgLink = bgLink;
//        return dto;
//    }
//
//    public static void getGif () {
//        try
//        {
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Dota2BgDto");
//            query.whereEqualTo("group","gif");
//            query.setLimit(200);
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> list, ParseException e) {
//                    if (e != null || list.size() == 0) {
//                        Logger.error(TAG, ">>>" + "Error getData nodata" + e.toString());
//                        return;
//                    }
//                    Logger.debug(TAG, ">>>" + "getData size:" + list.size());
//                    GalleryDto galleryDto;
//                    for (ParseObject p : list) {
//
//                        String heroID = p.getString("heroID");
//                        String linkGif = p.getString("link");
//                        galleryDto = new GalleryDto();
//                        galleryDto.group = "gif";
//                        galleryDto.link = linkGif;
//                        galleryDto.heroID = heroID;
//                        HeroManager.getInstance().getHero(heroID).gallery.add(galleryDto);
//                    }
//                    OttoBus.post(new GalleryDto());
//                }
//            });
//
//        } catch (Exception e) {
//
//        }
//    }
//
//    public static void updateBgToHeroEntry(final HeroEntry dto) {
//        Logger.debug(TAG, ">>>" + "updateBgToHeroEntry:" + dto.heroId);
//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(HeroEntry.class.getSimpleName());
//        query.whereEqualTo("heroId", dto.heroId);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> list, ParseException e) {
//                if (e != null || list.size() > 0) {
//
//                    ParseObject p = list.get(0);
//                    String objectId = p.getObjectId();
//                    Logger.debug(TAG, ">>>" + "objectId:" + objectId);
//
//                    ParseQuery<ParseObject> mQuery = ParseQuery.getQuery(HeroEntry.class.getSimpleName());
//                    mQuery.getInBackground(objectId, new GetCallback<ParseObject>() {
//                        @Override
//                        public void done(ParseObject p, ParseException e) {
//                            p.put("bgLink", dto.bgLink);
//                            p.saveInBackground();
//                            Logger.debug(TAG, ">>>" + "updateBgToHeroEntry OK with:" + dto.fullName);
//                        }
//                    });
//
//                }
//            }
//        });
//    }
//    public static void getkensBurns () {
//        final List<String> listKens = new ArrayList<>();
//        try
//        {
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Dota2BgDto");
//            query.whereNotEqualTo("group", "gif");
//            query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> list, ParseException e) {
//                    if (e != null || list.size() == 0) {
//                        Logger.error(TAG, ">>>" + "Error getData nodata" + e.toString());
//                        return;
//                    }
//                    Logger.debug(TAG, ">>>" + "getData size:" + list.size());
//                    for (ParseObject p : list) {
//
//                        String linkGif = p.getString("link");
//                        listKens.add(linkGif);
//                    }
//                    ResourceManager.getInstance().listKenburns.clear();
//                    ResourceManager.getInstance().listKenburns.addAll(listKens);
//
//                }
//            });
//
//        } catch (Exception e) {
//
//        }
//    }
//
//    public static void push(CommentDto d) {
//        TsGaTools.trackPages(MsConst.TRACK_PUSH_COMMENT);
//        ParsePush p = new ParsePush();
//        p.setChannel(MsConst.CHANNEL_COMMON);
//        StringBuilder s = new StringBuilder();
//        s.append(d.getFromName());
//        s.append(">>>");
//        s.append(d.getSpeakDto().heroId);
//        s.append("(" + d.getSpeakDto().text + "):");
//        s.append(d.getMessage());
//
//        p.setMessage(s.toString());
//        p.sendInBackground();
//    }
//
//    public static  void getHeroesRoles () {
//        Logger.debug(TAG, ">>>" + "=====getHeroesRoles");
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(HeroRoleDto.class.getSimpleName());
//        query.setLimit(400);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> list, ParseException e) {
//                if (e != null) {
//                    return;
//                }
//                for (ParseObject p : list) {
//                    String heroID = p.getString("heroID");
//                    String role = p.getString("roleName");
//                    HeroManager.getInstance().getHero(heroID).roles.add(role);
//
//                }
//                Logger.error(TAG, ">>>" + "---finsh getHeroesRoles");
//                try {
//                    FileUtil.saveObject(ResourceManager.getInstance().getContext(), new HeroSavedDto(HeroManager.getInstance().listHeroes),
//                            HeroSavedDto.class.getSimpleName());
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//    }


    @Override
    public void settsParseCallback(Callback callback) {
        this.mCallback = callback;
    }
}
