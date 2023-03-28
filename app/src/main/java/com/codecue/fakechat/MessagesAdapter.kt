package com.codecue.fakechat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codecue.fakechat.activities.ChatScreen

class MessagesAdapter(val context: ChatScreen) :
    RecyclerView.Adapter<MessagesAdapter.MyViewHolder>() {

    private var messageList = ArrayList<Message>()

    private val senderLayout = 0
    private val receiverLayout = 1


    override fun getItemViewType(position: Int): Int {

        val currentItem: Message = messageList[position]

        return if (currentItem.messageType == "send") {
            senderLayout
        } else {
            receiverLayout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = if (viewType == senderLayout) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.send_message_layout, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.receive_messages_layout, parent, false)

        }
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem: Message = messageList[position]
        holder.message.text = currentItem.messageBody

                    holder.image.setImageBitmap(ChatScreen.pp)

        Log.d("myImageBitmap", ChatScreen.pp.toString())
    }

    override fun getItemCount(): Int {
        return messageList.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: ArrayList<Message>) {
        messageList.clear()
        this.messageList = items
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(index: Int) {
        this.messageList.removeAt(index)
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val message: TextView = itemView.findViewById(R.id.message)
        val image : ImageView = itemView.findViewById(R.id.profileImageRecLayout1)
    }


}