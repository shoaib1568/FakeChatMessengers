package com.codecue.fakechat.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.codecue.fakechat.DatabaseHelper
import com.codecue.fakechat.ChatListAdapter
import com.codecue.fakechat.StoriesAdapter
import com.codecue.fakechat.ads.Admob
import com.codecue.fakechat.ads.Admob2
import com.codecue.fakechat.databinding.ActivityChatListBinding
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd


class ChatListActivity : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    private var maxAdView: MaxAdView? = null
    private var retryAttempt = 0
    private var maxInterstitialAd: MaxInterstitialAd? = null

    private lateinit var binding: ActivityChatListBinding
    private lateinit var chatsRV: RecyclerView
    private lateinit var storiesRV: RecyclerView
    private lateinit var chatsAdapter: ChatListAdapter
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initRecyclerView()
        getAllChats()
        setOnClickListeners()
    }

    /** Get Chats And Update When New Chat is Added **/
    private fun getAllChats() {
        val chatList = databaseHelper.getAllChats()
        chatsAdapter.addItems(chatList)

        val storiesList = databaseHelper.getAllChats()
        storiesAdapter.addItems(chatList)
    }

    private fun initRecyclerView() {

        databaseHelper = DatabaseHelper(this)

        /** Chats RV **/

        chatsRV = binding.chatRecyclerView
        chatsAdapter = ChatListAdapter(this)
        chatsRV.adapter = chatsAdapter
        chatsRV.layoutManager = LinearLayoutManager(this)

        /** Stories RV **/

        storiesRV = binding.storiesRecyclerView
        storiesAdapter = StoriesAdapter(this)
        storiesRV.adapter = storiesAdapter
        storiesRV.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    private fun setOnClickListeners() {

        binding.FAB.setOnClickListener {
            if (Admob2.mInterstitialAd != null) {
                Admob2.mInterstitialAd.show(this@ChatListActivity)
            } else {


            }
            startActivity(Intent(this@ChatListActivity, AddNewUser::class.java))
        }
        binding.editButton.setOnClickListener {
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
                                    Admob2.mInterstitialAd.show(this@ChatListActivity)
                                } else {


                                }


                            }
                        }


                } else {

                }
            }
            startActivity(Intent(this@ChatListActivity, CurrentUserImage::class.java))
        }
        binding.layout.setOnClickListener {
            if (Admob2.mInterstitialAd != null) {
                Admob2.mInterstitialAd.show(this@ChatListActivity)
            } else {


            }
            startActivity(Intent(this@ChatListActivity, CurrentUserImage::class.java))
        }


    }

}