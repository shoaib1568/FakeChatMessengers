package com.codecue.fakechat.activities

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.sdk.AppLovinSdk
import com.codecue.fakechat.BuildConfig
import com.codecue.fakechat.R
import com.codecue.fakechat.ads.AdClass
import com.codecue.fakechat.ads.Admob
import com.codecue.fakechat.ads.Admob2
import com.codecue.fakechat.databinding.ActivityHomeBinding
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import fearless0.ads.AdsHandler
import fearless0.ads.GetSmartAdmob
import fearless0.ads.GoogleAds
import java.util.concurrent.TimeUnit

class HomeActivity : AdClass() {
    private var mInterstitialAd: InterstitialAd? = null
    var progressDialog: ProgressDialog? = null
    private  lateinit var binding:ActivityHomeBinding
    private var maxAdView: MaxAdView? = null
    private var retryAttempt = 0
    private var maxInterstitialAd: MaxInterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AudienceNetworkAds.initialize(this)
        val adsCommand = arrayOf<String>(
            getString(R.string.bnr_admob) // 1st Banner Ads Id
            , getString(R.string.native_admob) // 2st Native Ads Id
            , getString(R.string.int_admob) // 3st interstitial Ads Id
            , getString(R.string.app_open_admob) // 4st App-Open Ads Id
            , getString(R.string.video_admob) // 5st Rewarded Ads Id
        )

        GetSmartAdmob(this, adsCommand) { success: Boolean -> }.execute()

        AdsHandler.setAdsOn(true)


        GoogleAds.getInstance().showCounterInterstitialAd(
            this@HomeActivity
        ) { }

        //  mAdView = findViewById(R.id.adView);
        val adRequest = AdRequest.Builder().build()
        //  mAdView.loadAd(adRequest);
        //applovin adds integration

        //  mAdView.loadAd(adRequest);
        //applovin adds integration
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.initializeSdk(this) { }
        //applovin banner add loader

        //applovin banner add loader
        maxAdView = findViewById<MaxAdView>(R.id.maxbanner)
        maxAdView!!.loadAd()
        maxAdView!!.startAutoRefresh()
        applovinintizialadd()
        setOnClickListners()

        

    }

    private fun applovinintizialadd() {

        maxInterstitialAd =
            MaxInterstitialAd(resources.getString(R.string.applovinintersitial), this@HomeActivity)
        val adlistner: MaxAdListener = object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd) {}
            override fun onAdDisplayed(ad: MaxAd) {}
            override fun onAdHidden(ad: MaxAd) {}
            override fun onAdClicked(ad: MaxAd) {}
            override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                retryAttempt++
                val delayMillis = TimeUnit.SECONDS.toMillis(
                    Math.pow(2.0, Math.min(6, retryAttempt).toDouble()).toLong()
                )
                Handler().postDelayed({ maxInterstitialAd!!.loadAd() }, delayMillis)
            }

            override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {}
        }
        maxInterstitialAd!!.setListener(adlistner)
        maxInterstitialAd!!.loadAd()


    }

    private fun setOnClickListners() {
        binding.FakeChat.setOnClickListener{
            val i=Intent(this, ChatListActivity::class.java)
            startActivity(i)
            if (maxInterstitialAd!!.isReady) {
                maxInterstitialAd!!.showAd()
            } else {
                if (Admob.mInterstitialAd != null) {
                    Admob.mInterstitialAd.show(this)
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                if (Admob2.mInterstitialAd != null) {
                                    Admob2.mInterstitialAd.show(this@HomeActivity)
                                } else {


                                }


                            }
                        }


                } else {

                }
            }

        }
        binding.contactUs.setOnClickListener{

            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://codecue.solutions/")
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {

                // youtube is not installed.Will be opened in other available apps
                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://codecue.solutions/")
                )
                startActivity(i)
            }
        }
        binding.PrivacyPolicy.setOnClickListener{
            if (maxInterstitialAd!!.isReady) {
                maxInterstitialAd!!.showAd()
            } else {
                if (Admob.mInterstitialAd != null) {
                    Admob.mInterstitialAd.show(this)
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                if (Admob2.mInterstitialAd != null) {
                                    Admob2.mInterstitialAd.show(this@HomeActivity)
                                } else {


                                }


                            }
                        }


                } else {

                }
            }
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://pages.flycricket.io/prank-chat/privacy.html")
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {

                // youtube is not installed.Will be opened in other available apps
                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://pages.flycricket.io/prank-chat/privacy.html")
                )
                startActivity(i)
            }
        }
        binding.Share.setOnClickListener{
            if (Admob.mInterstitialAd != null) {
                Admob.mInterstitialAd.show(this)
                mInterstitialAd?.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            if (Admob2.mInterstitialAd != null) {
                                Admob2.mInterstitialAd.show(this@HomeActivity)
                            } else {


                            }


                        }
                    }


            } else {

            }
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                
                
                """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }
        }
        binding.getPremiumAccessButton.setOnClickListener{
            if (maxInterstitialAd!!.isReady) {
                maxInterstitialAd!!.showAd()
            } else {
                if (Admob.mInterstitialAd != null) {
                    Admob.mInterstitialAd.show(this)
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                if (Admob2.mInterstitialAd != null) {
                                    Admob2.mInterstitialAd.show(this@HomeActivity)
                                } else {


                                }


                            }
                        }


                } else {

                }
            }
            Toast.makeText(this, "Check Premium Version", Toast.LENGTH_LONG).show()
        }
        binding.contactUsInstagram.setOnClickListener {

            val uri = Uri.parse("https://www.instagram.com/codecue_/")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)
            likeIng.setPackage("com.instagram.android")
            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/codecue_/")
                    )
                )
            }
        }
        binding.contactUsFacebook.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/codecue.solutions/")
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {

                // youtube is not installed.Will be opened in other available apps
                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/codecue.solutions/")
                )
                startActivity(i)
            }
        }
        binding.contactUsLinkedIn.setOnClickListener {

            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/in/khanzada-saleem-khan-2501b8123")
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {

                // youtube is not installed.Will be opened in other available apps
                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/in/khanzada-saleem-khan-2501b8123")
                )
                startActivity(i)
            }
        }
    }
}