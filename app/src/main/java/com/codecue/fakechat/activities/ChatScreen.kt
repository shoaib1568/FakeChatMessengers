package com.codecue.fakechat.activities

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codecue.fakechat.*
import com.codecue.fakechat.ads.Admob2
import com.codecue.fakechat.databinding.ActivityChatScreenBinding
import com.google.android.gms.ads.interstitial.InterstitialAd
import java.text.SimpleDateFormat
import java.util.*


class ChatScreen : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null

    private lateinit var binding: ActivityChatScreenBinding
    private var databaseHelper: DatabaseHelper = DatabaseHelper(this)
    private lateinit var messagesRV : RecyclerView
    private lateinit var messagesAdapter : MessagesAdapter
    private lateinit var profileImage : ByteArray


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        setOnClickListeners()
        getIntentExtrasAndBindWithViews()



        /**  -- Current User Details() -- **/
        val id = intent.getLongExtra("idKey", 0)

        val cursor: Cursor = databaseHelper.getUser(id)
        if (cursor.count == 0) {
            Toast.makeText(this, "Failed To Get Image", Toast.LENGTH_SHORT).show()
        }
        else {
            while (cursor.moveToNext()) {
                profileImage = cursor.getBlob(3)
                pp = DbBitmapUtility.getImage(profileImage)
                binding.chatProfileImage.setImageBitmap(pp)
                binding.messagesRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
        getMessages()
    }
    /**  ---- **/


    companion object{
        var pp : Bitmap ?= null
    }


    /** Messages RV **/

    private fun initRecyclerView() {

        messagesRV = binding.messagesRecyclerView
        messagesAdapter =  MessagesAdapter(this)
        messagesRV.adapter = messagesAdapter
        messagesRV.layoutManager = LinearLayoutManager(this)

    }

    /** Get Messages And Update When New Msg is Added **/

    private fun getMessages() {

        val id = intent.getLongExtra("idKey", 0)

        val messageList = databaseHelper.getChatMessages(id)
        messagesAdapter.addItems(messageList)

    }


    /** Fun: When Item Clicked VALUES in Intent **/

    private fun getIntentExtrasAndBindWithViews() {
        try {
            val id = intent.getLongExtra("idKey", 0)
            //Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()
            val name = intent.getStringExtra("nameKey")
            binding.chatName.text = name

            getMessages()

        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            Log.d("ffff", "Error : ${e.message.toString()} ")
        }

    }


    /**  FUN : New Message -- Calling getMessages() **/

    private fun newMessage(messageType : String) {

        val getMessage = binding.messageTV.text.toString().trim()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = Date()
        val currentTime = formatter.format(date)

        val id = intent.getLongExtra("idKey", 0)

        val newMessage = Message(id, getMessage, messageType, 0,
            currentTime)

        val isSend: Boolean = databaseHelper.insertMessage(newMessage)

        if (isSend) {
            //Toast.makeText(this, "Message Added Successfully", Toast.LENGTH_SHORT).show()
            binding.messageTV.text = null

            getMessages()

        } else {
            binding.messageTV.text = null
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }


    }

    /** on CLick Listeners **/
    private fun setOnClickListeners() {

        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.buttonSend.setOnClickListener {
            if (Admob2.mInterstitialAd != null) {
                Admob2.mInterstitialAd.show(this@ChatScreen)
            } else {


            }

            val getMessage = binding.messageTV.text.toString()
            if (getMessage.isEmpty()) {

                Toast.makeText(this, "Type Some Text", Toast.LENGTH_SHORT).show()

            } else {

                val popupMenu  = PopupMenu(this, binding.buttonSend)
                popupMenu.menuInflater.inflate(R.menu.chat_message_type, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                    when (item.itemId) {


                        R.id.menuSend ->
                           newMessage("send")
                        
                        R.id.menuReceive ->
                            newMessage("receiver")

                    }
                    true
                })
                popupMenu.show()

            }
        }

        binding.info.setOnClickListener {
            if (Admob2.mInterstitialAd != null) {
                Admob2.mInterstitialAd.show(this@ChatScreen)
            } else {


            }

            val popupMenu  = PopupMenu(this@ChatScreen, binding.info)
            popupMenu.menuInflater.inflate(R.menu.chat_screen_info, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                when (item.itemId) {
                    R.id.infoClearChat -> clearMessages()
                    R.id.infoDeleteChat -> deleteChat()
                }
                true
            })
            popupMenu.show()
        }
    }

    /** Clear Messages **/

    private fun clearMessages() {
        if (Admob2.mInterstitialAd != null) {
            Admob2.mInterstitialAd.show(this@ChatScreen)
        } else {


        }

        val id = intent.getLongExtra("idKey", 0)

        val isDeleted : Boolean = databaseHelper.deleteMessages(id)
        if (isDeleted) {
            Toast.makeText(this, "Chat Clear", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }

        val messageList = databaseHelper.getChatMessages(id)
        messagesAdapter.addItems(messageList)
    }

    /** Delete Chat  **/

    private fun deleteChat() {
        if (Admob2.mInterstitialAd != null) {
            Admob2.mInterstitialAd.show(this@ChatScreen)
        } else {


        }

        val id = intent.getLongExtra("idKey", 0)

        val isChatDeleted : Boolean = databaseHelper.deleteChat(id)
        if (isChatDeleted) {
            Toast.makeText(this, "Chat Deleted", Toast.LENGTH_SHORT).show()
            clearMessages()
            startActivity(Intent(this@ChatScreen, ChatListActivity::class.java))
            this.finish()
        } else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }
    }

}

