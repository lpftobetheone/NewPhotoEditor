package net.zenjoy.photoeditor;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.flurry.android.FlurryAgent;
import com.zentertain.common.NGCommonConfiguration;

/**
 * Created by liupengfei on 17/1/3.
 */

public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initAdjust();
//        initFabric();
//        initImageLoader();

        initFlurry();

    }

    private void initAdjust() {

    }

    private void initFlurry() {
        new FlurryAgent.Builder()
                .withLogEnabled(false)
                .build(this, NGCommonConfiguration.FLURRY_KEY);
    }
}
