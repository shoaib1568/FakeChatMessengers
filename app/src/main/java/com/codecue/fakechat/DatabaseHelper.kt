package com.codecue.fakechat

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) :

    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // END
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createChatsTable)
        db.execSQL(createMessagesTable)
        db.execSQL(createUserImageTable)
        //onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $tableChats")
        db.execSQL("DROP TABLE IF EXISTS $tableMessages")
        db.execSQL("DROP TABLE IF EXISTS $createUserImageTable")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    private var createChatsTable = ("CREATE TABLE " + tableChats + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Name + " TEXT NOT NULL,"
            + createdAT + " TEXT NOT NULL,"
            + profileImage + " BLOB NOT NULL)")

    private var createMessagesTable = ("CREATE TABLE " + tableMessages + " ("
            + chatID + " INTEGER,"
            + messageBody + " TEXT NOT NULL,"
            + messageType + " TEXT NOT NULL,"
            + messageUniqueID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + createdAT_MSG + " TEXT NOT NULL)")

    private var createUserImageTable  = ("CREATE TABLE " + tableUserImage + "( "
            + imageID + "INTEGER ,"
            + Image_COL + "BLOB NOT NULL) ")


    //

    companion object {

        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FakeChat.db"
        const val tableChats = "Chats_Table"
        const val ID = "ID"
        const val Name = "Name"
        const val createdAT = "Created_At"
        const val profileImage = "Image"

        //
        const val tableMessages = "Messages_Table"
        const val chatID = "chatID"
        const val messageBody = "MessageBody"
        const val messageType = "MessageType"
        const val messageUniqueID = "MessageUniqueID"
        const val createdAT_MSG = "Created_At"

        //
        const val tableUserImage = "Image_Table"
        const val Image_COL = "Image"
        const val imageID = "imageID"
    }


    /** FUN: Insert Chat **/

    @Throws(SQLiteException::class)
    fun insertChat(chat: Chat): Boolean {

        val database = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Name, chat.Name)
        contentValues.put(createdAT, chat.createdAT)
        contentValues.put(profileImage, chat.image)

        val id: Long = database.insert(tableChats, null, contentValues)
        database.close()

        return id > 0    // If < 0 No data inserted
    }

    /** FUN: Get All Chats **/

    @Throws(SQLiteException::class)
    fun getAllChats(): ArrayList<Chat> {

        val chatList: ArrayList<Chat> = ArrayList()
        val selectQuery = "SELECT * FROM $tableChats ORDER BY $createdAT DESC"
        val database = this.readableDatabase

        val chatsCursor: Cursor?

        try {
            chatsCursor = database.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            e.printStackTrace()
            database.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Long
        var name: String
        var createdAt: String
        var image: ByteArray


        if (chatsCursor.moveToFirst()) {
            do {

                id = chatsCursor.getLong(chatsCursor.getColumnIndexOrThrow("ID"))
                name = chatsCursor.getString(chatsCursor.getColumnIndexOrThrow("Name"))
                createdAt = chatsCursor.getString(chatsCursor.getColumnIndexOrThrow("Created_At"))
                image = chatsCursor.getBlob(chatsCursor.getColumnIndexOrThrow("Image"))

                val chatItem = Chat(id, name, createdAt, image)
                chatList.add(chatItem)

            } while (chatsCursor.moveToNext())
        }
        return chatList

    }


    /** FUN: Delete Chat By ID **/

    fun deleteChat(id: Long): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(tableChats, "ID = $id", null)
        db.close()

        return success > 0

    }

    /** FUN : Get User **/

    fun getUser(id: Long): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $tableChats WHERE $ID = $id", null)
    }


    /**        >>>>>   MESSAGES  <<<<<<        **/




    fun deleteMessages(id: Long): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(tableMessages, "chatID = $id", null)
        db.close()

        return success > 0

    }

    fun getSingleMessage(id: Long) :Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $tableMessages WHERE $ID = $id LIMIT 1", null)
    }


    /** FUN: Insert New Message **/

    @Throws(SQLiteException::class)
    fun insertMessage(message: Message): Boolean {

        val database = this.writableDatabase

        val messageCV = ContentValues()
        messageCV.put(chatID, message.chatID)
        messageCV.put(messageBody, message.messageBody)
        messageCV.put(messageType, message.messageType)
        //messageCV.put(messageUniqueID, message.messageUniqueID)
        messageCV.put(createdAT_MSG, message.messageTimestamp)

        val id: Long = database.insert(tableMessages, null, messageCV)
        database.close()

        return id > 0 // If < 0 No data inserted
    }


    /** FUN: Delete Chat TABLE **/

    fun deleteMessagesTable() {

        val db = this.writableDatabase

        db.execSQL("DROP TABLE IF EXISTS $tableMessages")
        db.close()

    }


    /** FUN: Delete Chat TABLE **/

    fun getChatMessages (chatIDParameter : Long ) : ArrayList<Message> {

        val messagesList : ArrayList<Message> = ArrayList()
        val selectMessagesQuery = "SELECT * FROM $tableMessages WHERE $chatID = $chatIDParameter"
        val database = this.readableDatabase

        val getMessagesCursor : Cursor?

        try {

            getMessagesCursor = database.rawQuery(selectMessagesQuery, null)

        } catch (e: SQLiteException) {

            e.printStackTrace()
            database.execSQL(selectMessagesQuery)
            return ArrayList()
        }

        var chatID : Long
        var messageBody : String
        var messageID : Long
        var messageTime : String
        var messageType : String

        if(getMessagesCursor.moveToFirst()) {

            do {

                chatID = getMessagesCursor.getLong(getMessagesCursor.getColumnIndexOrThrow("chatID"))
                messageBody = getMessagesCursor.getString(getMessagesCursor.getColumnIndexOrThrow("MessageBody"))
                messageID = getMessagesCursor.getLong(getMessagesCursor.getColumnIndexOrThrow("MessageUniqueID"))
                messageTime = getMessagesCursor.getString(getMessagesCursor.getColumnIndexOrThrow("Created_At"))
                messageType = getMessagesCursor.getString(getMessagesCursor.getColumnIndexOrThrow("MessageType"))

                val msgItem = Message(chatID, messageBody, messageType, messageID, messageTime)
                messagesList.add(msgItem)

            } while (getMessagesCursor.moveToNext())
        }

        return messagesList

    }

}