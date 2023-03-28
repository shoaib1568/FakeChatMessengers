package com.codecue.fakechat


data class Message(

    val chatID: Long?,
    val messageBody: String ,
    val messageType: String ,
    val messageID : Long,
    val messageTimestamp: String,

)
