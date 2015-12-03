package com.me.silencedut.nbaplus.app;


import com.google.gson.Gson;
import com.me.silencedut.greendao.DBHelper;
import com.me.silencedut.nbaplus.RxMethod.RxNews;
import com.me.silencedut.nbaplus.data.Cache;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SilenceDut on 2015/11/28.
 */
public class NbaplusService {
    private static final NbaplusService NBAPLUS_SERVICE=new NbaplusService();
    private static EventBus sBus ;
    private static DBHelper sDBHelper;
    private static NbaplusAPI sNbaplus;
    private static final Gson gson = new Gson();
    private Map<Integer,CompositeSubscription> mCompositeSubMap;
    private CompositeSubscription mCompositeSubscription ;
    private Cache mCache;



    public void initService() {
        sBus = new EventBus();
        mCompositeSubMap=new HashMap<Integer,CompositeSubscription>();
        backGroundInit();
    }

    private void backGroundInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sNbaplus=NbaplusFactory.getNbaplus();
                sDBHelper=DBHelper.getInstance(App.getContext());
                mCache= Cache.getInstance();
                mCache.initSnappyDB();
//                mNewsList=(List<NewsEntity>)mCache.get(Cache.CACHEKEY.NEWSFEED.name(),List.class);
//                if (mNewsList==null) {
//                    mNewsList=new ArrayList<NewsEntity>();
//                }
            }
        }).start();

    }

    public void addCompositeSub(int taskId) {
        mCompositeSubscription= new CompositeSubscription();

        mCompositeSubMap.put(taskId, mCompositeSubscription);
    }

    public void removeCompositeSub(int taskId) {
        mCompositeSubscription= mCompositeSubMap.get(taskId);
        mCompositeSubscription.unsubscribe();
        mCompositeSubMap.remove(taskId);
    }

    public void updateNews() {

        mCompositeSubscription.add(RxNews.updateNews());
    }

    private NbaplusService(){}

    public static NbaplusService getInstance() {
        return NBAPLUS_SERVICE;
    }

    public static EventBus getBus() {
        return sBus;
    }

    public static NbaplusAPI getNbaPlus() {
        return sNbaplus;
    }

    public static DBHelper getDBHelper() {
        return sDBHelper;
    }

    public static Gson getGson() {
        return gson;
    }

}
