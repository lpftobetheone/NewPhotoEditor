package com.zentertain.common.util;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;

import net.zenjoy.photoeditor.MainActivity;
import com.supersonic.mediationsdk.sdk.Supersonic;

public class AdManager {
    public static String GOOGLE_ADMOB_INTERSTITIAL_A = "ca-app-pub-9414288950296780/2029603959";
    public static String GOOGLE_ADMOB_INTERSTITIAL_B = "ca-app-pub-9414288950296780/3506337153";
    public static String GOOGLE_ADMOB_INTERSTITIAL_C = "ca-app-pub-9414288950296780/4983070358";
    public static String GOOGLE_ADMOB_INTERSTITIAL_D = "ca-app-pub-9414288950296780/6459803553";
    public static String GOOGLE_ADMOB_INTERSTITIAL_E = "ca-app-pub-9414288950296780/7936536759";

    public static boolean isAdEnabled = true;
    public static boolean isAdmobCancelled = false;    // loading spends more than 5 seconds, abandon this request.
    public static boolean isInShowingChain = false;    // display chain is working.
    public static boolean isInLoadingChain = false;    // loading chain is working.
    public static boolean isShouldShowAdmob = false;
    public static boolean isShouldShowVideoAdmob = false;
    public static boolean isInFeatherActivity = false;	// in aviary sdk, show ad also.
    public static boolean isShowingAdMob = false;      	// ad opened
    public static boolean isShowAnyway = false;      	// show ad anyway
    public static boolean internalActivityFlag = false;	// opened other activity. will be check onresume.
    public static boolean isActive = false;            	// is app in front end.

    public static int adDelay = 1500;

    private static com.google.android.gms.ads.InterstitialAd interstitialAdmobInterA;
    private static com.google.android.gms.ads.InterstitialAd interstitialAdmobInterB;
    private static com.google.android.gms.ads.InterstitialAd interstitialAdmobInterC;
    private static com.google.android.gms.ads.InterstitialAd interstitialAdmobInterD;
    private static com.google.android.gms.ads.InterstitialAd interstitialAdmobInterE;

    private static com.google.android.gms.ads.InterstitialAd loadAdmobInterA;
    private static com.google.android.gms.ads.InterstitialAd loadAdmobInterB;
    private static com.google.android.gms.ads.InterstitialAd loadAdmobInterC;
    private static com.google.android.gms.ads.InterstitialAd loadAdmobInterD;
    private static com.google.android.gms.ads.InterstitialAd loadAdmobInterE;

    private static com.google.android.gms.ads.InterstitialAd cachedInterstitialAdmob;
    
    public static Supersonic videoAdmob;


    private static void adOpened() {
        isShowingAdMob = true;
        isShouldShowAdmob = false;
        isInShowingChain = false;
//        hideLoadView();
    }

    public static void hideLoadView() {
        MainActivity.instance.fullScreenLoadView.setVisibility(View.GONE);
        MainActivity.instance.fullScreenLoadAdView.setVisibility(View.GONE);
        MainActivity.instance.adProgressWheel.stopSpinning();
    }

 // start display chain
    public static void displayInterstitialAdmob() {
        if (!isAdEnabled)
            return;

        isShouldShowAdmob = true;        
        if ((isShowAnyway == true || isActive == true) && isShowingAdMob == false) {
            if (cachedInterstitialAdmob != null && cachedInterstitialAdmob.isLoaded()) {            	                
            	MainActivity.instance.fullScreenLoadAdView.setVisibility(View.VISIBLE);
                MainActivity.instance.adProgressWheel.spin();
				new Handler().postDelayed(new Runnable() {  
					public void run() {						
                        showAd(cachedInterstitialAdmob);
						cachedInterstitialAdmob = null;
						isShowAnyway = false;
					}
				}, adDelay);
            } else {
                isInShowingChain = true;
                interstitialAdmobInterA.loadAd(new AdRequest.Builder().build());
            }
        }
    }

    // start load chain
    public static void loadAndCacheInterstitialAdmob() {
        if (cachedInterstitialAdmob == null && isInLoadingChain == false) {
            isInLoadingChain = true;
            loadAdmobInterA.loadAd(new AdRequest.Builder().build());
        }
    }


    public static void initCacheAdChain(Activity context) {
        if (!isAdEnabled)
            return;

        /** Admob Interstitial */
        loadAdmobInterA = new com.google.android.gms.ads.InterstitialAd(context);
        loadAdmobInterA.setAdUnitId(GOOGLE_ADMOB_INTERSTITIAL_A);
        loadAdmobInterB = new com.google.android.gms.ads.InterstitialAd(context);
        loadAdmobInterB.setAdUnitId(GOOGLE_ADMOB_INTERSTITIAL_B);
        loadAdmobInterC = new com.google.android.gms.ads.InterstitialAd(context);
        loadAdmobInterC.setAdUnitId(GOOGLE_ADMOB_INTERSTITIAL_C);
        loadAdmobInterD = new com.google.android.gms.ads.InterstitialAd(context);
        loadAdmobInterD.setAdUnitId(GOOGLE_ADMOB_INTERSTITIAL_D);
        loadAdmobInterE = new com.google.android.gms.ads.InterstitialAd(context);
        loadAdmobInterE.setAdUnitId(GOOGLE_ADMOB_INTERSTITIAL_E);

        loadAdmobInterA.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                cachedInterstitialAdmob = loadAdmobInterA;
                isInLoadingChain = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                loadAdmobInterB.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                isInLoadingChain = false;
                adOpened();
            }

            @Override
            public void onAdClosed() {
                isInLoadingChain = false;
                isShowingAdMob = false;
                isInShowingChain = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }
        });

        loadAdmobInterB.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                cachedInterstitialAdmob = loadAdmobInterB;
                isInLoadingChain = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                loadAdmobInterC.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                isInLoadingChain = false;
                adOpened();
            }

            @Override
            public void onAdClosed() {
                isInLoadingChain = false;
                isShowingAdMob = false;
                isInShowingChain = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }
        });

        loadAdmobInterC.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                cachedInterstitialAdmob = loadAdmobInterC;
                isInLoadingChain = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                loadAdmobInterD.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                isInLoadingChain = false;
                adOpened();
            }

            @Override
            public void onAdClosed() {
                isInLoadingChain = false;
                isShowingAdMob = false;
                isInShowingChain = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }
        });

        loadAdmobInterD.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                cachedInterstitialAdmob = loadAdmobInterD;
                isInLoadingChain = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                loadAdmobInterE.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                isInLoadingChain = false;
                adOpened();
            }

            @Override
            public void onAdClosed() {
                isInLoadingChain = false;
                isShowingAdMob = false;
                isInShowingChain = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }
        });

        loadAdmobInterE.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                cachedInterstitialAdmob = loadAdmobInterE;
                isInLoadingChain = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                isInLoadingChain = false;
                isShowingAdMob = false;
                isInShowingChain = false;
            }

            @Override
            public void onAdOpened() {
                isInShowingChain = false;
                adOpened();
            }

            @Override
            public void onAdClosed() {
                isInLoadingChain = false;
                isShowingAdMob = false;
                isInShowingChain = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }
        });
    }

    public static void initDisplayAdChain(Activity context) {
        if (!isAdEnabled)
            return;

        interstitialAdmobInterA = new com.google.android.gms.ads.InterstitialAd(context);
        interstitialAdmobInterA.setAdUnitId(GOOGLE_ADMOB_INTERSTITIAL_A);
        interstitialAdmobInterB = new com.google.android.gms.ads.InterstitialAd(context);
        interstitialAdmobInterB.setAdUnitId(GOOGLE_ADMOB_INTERSTITIAL_B);
        interstitialAdmobInterC = new com.google.android.gms.ads.InterstitialAd(context);
        interstitialAdmobInterC.setAdUnitId(GOOGLE_ADMOB_INTERSTITIAL_C);
        interstitialAdmobInterD = new com.google.android.gms.ads.InterstitialAd(context);
        interstitialAdmobInterD.setAdUnitId(GOOGLE_ADMOB_INTERSTITIAL_D);
        interstitialAdmobInterE = new com.google.android.gms.ads.InterstitialAd(context);
        interstitialAdmobInterE.setAdUnitId(GOOGLE_ADMOB_INTERSTITIAL_E);

        interstitialAdmobInterA.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                if (isShouldShowAdmob == true) {
                	if (isAdmobCancelled) {
                        isAdmobCancelled = false;
                	} else {
	                    MainActivity.instance.fullScreenLoadAdView.setVisibility(View.VISIBLE);
	                    MainActivity.instance.adProgressWheel.spin();
	                    isShouldShowAdmob = false;
	                    if(isShowAnyway || (isActive == true && isShowingAdMob == false) || (isActive == false && isInFeatherActivity == true)) {
	                        new Handler().postDelayed(new Runnable() {
	                            public void run() {
                                    showAd(interstitialAdmobInterA);
	                            }
	                        }, adDelay);
	                    } else {
	                        hideLoadView();
	                        cachedInterstitialAdmob = interstitialAdmobInterA;
	                    }
                	}
                }
                isInShowingChain = false;
                isShowAnyway = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                interstitialAdmobInterB.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                adOpened();
            }

            @Override
            public void onAdClosed() {
                isShowingAdMob = false;
                isInShowingChain = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }
        });

        interstitialAdmobInterB.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                if (isShouldShowAdmob == true) {
                	if (isAdmobCancelled) {
                        isAdmobCancelled = false;
                	} else {
	                    MainActivity.instance.fullScreenLoadAdView.setVisibility(View.VISIBLE);
	                    MainActivity.instance.adProgressWheel.spin();
	                    isShouldShowAdmob = false;
	                    if((isActive == true && isShowingAdMob == false) || (isActive == false && isInFeatherActivity == true)) {
	                        new Handler().postDelayed(new Runnable() {
	                            public void run() {
                                    showAd(interstitialAdmobInterB);
	                            }
	                        }, adDelay);
	                    } else {
	                        hideLoadView();
	                        cachedInterstitialAdmob = interstitialAdmobInterB;
	                    }
                	}
                }
                isInShowingChain = false;
                isShowAnyway = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                interstitialAdmobInterC.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                adOpened();
            }

            @Override
            public void onAdClosed() {
                isShowingAdMob = false;
                isInShowingChain = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }
        });

        interstitialAdmobInterC.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                if (isShouldShowAdmob == true) {
                	if (isAdmobCancelled) {
                        isAdmobCancelled = false;
                	} else {
	                    MainActivity.instance.fullScreenLoadAdView.setVisibility(View.VISIBLE);
	                    MainActivity.instance.adProgressWheel.spin();
	                    isShouldShowAdmob = false;
	                    if((isActive == true && isShowingAdMob == false) || (isActive == false && isInFeatherActivity == true)) {	                        
	                        new Handler().postDelayed(new Runnable() {
	                            public void run() {
                                    showAd(interstitialAdmobInterC);
	                            }
	                        }, adDelay);
	                    } else {
	                        hideLoadView();
	                        cachedInterstitialAdmob = interstitialAdmobInterC;
	                    }
                	}
                }
                isInShowingChain = false;
                isShowAnyway = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                interstitialAdmobInterD.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                adOpened();
            }

            @Override
            public void onAdClosed() {
                isShowingAdMob = false;
                isInShowingChain = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }
        });

        interstitialAdmobInterD.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                if (isShouldShowAdmob == true) {
                	if (isAdmobCancelled) {
                        isAdmobCancelled = false;
                	} else {
	                    MainActivity.instance.fullScreenLoadAdView.setVisibility(View.VISIBLE);
	                    MainActivity.instance.adProgressWheel.spin();
	                    isShouldShowAdmob = false;
	                    if((isActive == true && isShowingAdMob == false) || (isActive == false && isInFeatherActivity == true)) {
	                        new Handler().postDelayed(new Runnable() {
	                            public void run() {
                                    showAd(interstitialAdmobInterD);
	                            }
	                        }, adDelay);
	                    } else {
	                        hideLoadView();
	                        cachedInterstitialAdmob = interstitialAdmobInterD;
	                    }
                	}
                }
                isInShowingChain = false;
                isShowAnyway = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                interstitialAdmobInterE.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                adOpened();
            }

            @Override
            public void onAdClosed() {
                isShowingAdMob = false;
                isInShowingChain = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }
        });

        interstitialAdmobInterE.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                if (isShouldShowAdmob == true) {
                	if (isAdmobCancelled) {
                        isAdmobCancelled = false;
                	} else {
	                    MainActivity.instance.fullScreenLoadAdView.setVisibility(View.VISIBLE);
	                    MainActivity.instance.adProgressWheel.spin();
	                    isShouldShowAdmob = false;
	                    if((isActive == true && isShowingAdMob == false) || (isActive == false && isInFeatherActivity == true)) {
	                        
		                        new Handler().postDelayed(new Runnable() {
		                            public void run() {
                                        showAd(interstitialAdmobInterE);
		                            }
		                        }, adDelay);
	                    } else {
	                        hideLoadView();
	                        cachedInterstitialAdmob = interstitialAdmobInterE;
	                    }
                	}
                }
                isInShowingChain = false;
                isShowAnyway = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                isShouldShowAdmob = false;
                isInShowingChain = false;
                isAdmobCancelled = false;
                isShowAnyway = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }

            @Override
            public void onAdOpened() {
                adOpened();
            }

            @Override
            public void onAdClosed() {
                isShowingAdMob = false;
                isInShowingChain = false;
                hideLoadView();
                loadAndCacheInterstitialAdmob();
            }
        });
    }
    
    
    public static void displayVideoAdmob() {
		isInShowingChain = false;
		if(videoAdmob != null && videoAdmob.isRewardedVideoAvailable()) {
			videoAdmob.showRewardedVideo();
		} else {
			isShouldShowVideoAdmob = true;
		}
	}
	
	public static void initVideoAdmob(final Activity context) {
        if (!isAdEnabled)
            return;

//        SharedPreferences prefs = context.getSharedPreferences(NGCommonConfiguration.AD_HISTORY_KEY, 0);
//
//		String supersonicUserId = prefs.getString(NGCommonConfiguration.SUPERSONIC_USER_ID_KEY, null);
//		if(supersonicUserId == null) {
//			supersonicUserId = UUID.randomUUID().toString();
//			prefs.edit().putString(NGCommonConfiguration.SUPERSONIC_USER_ID_KEY, supersonicUserId).commit();
//		}
//		videoAdmob = SupersonicFactory.getInstance();
//		// register the rewarded video listener and initalize the rewarded video
//		videoAdmob.setRewardedVideoListener(new RewardedVideoListener() {
//			@Override
//			public void onVideoStart() {}
//
//			@Override
//			public void onVideoEnd() {}
//
//			@Override
//			public void onVideoAvailabilityChanged(boolean arg0) {
//				if (videoAdmob.isRewardedVideoAvailable() && isShouldShowVideoAdmob == true) {
//                	isShouldShowVideoAdmob = false;
//		 			if((isActive == true) || (isActive == false && isInFeatherActivity == true)) {
//		 				videoAdmob.showRewardedVideo();
//		 			} else {
//		 				hideLoadView();
//		 			}
//                }
//			}
//
//			@Override
//			public void onRewardedVideoShowFail(SupersonicError arg0) { }
//
//			@Override
//			public void onRewardedVideoInitSuccess() { }
//
//			@Override
//			public void onRewardedVideoInitFail(SupersonicError arg0) {
//				Log.e("load Supersonic failed", "" + arg0);
//			}
//
//			@Override
//			public void onRewardedVideoAdRewarded(Placement arg0) {}
//
//			@Override
//			public void onRewardedVideoAdOpened() {
//				adOpened();
//			}
//
//			@Override
//			public void onRewardedVideoAdClosed() {
//				isInLoadingChain = false;
//		    	isShowingAdMob = false;
//		    	isInShowingChain = false;
//		    	hideLoadView();
//			}
//		});
//        //Please refer to assets/supersonic.config for initial configurations
//        videoAdmob.initRewardedVideo(context, NGCommonConfiguration.SUPERSONIC_APP_KEY, supersonicUserId);
	}

    public static void showAd(com.google.android.gms.ads.InterstitialAd ad) {
        try {
            ad.show();
        } catch (Exception e) {
            Crashlytics.getInstance().core.logException(e);
        }
    }
}
