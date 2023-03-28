package com.codecue.fakechat

//fun readChats() : ArrayList<Chat>  {
//
//    // on below line we are creating a
//    // database for reading our database.
//    val db  = this.getReadableDatabase();
//
//    // on below line we are creating a cursor with query to read data from database.
//    val chatsCursor : Cursor  = db.rawQuery("SELECT * FROM Chats_Table", null);
//
//    // on below line we are creating a new array list.
//    val chatsArrayList = ArrayList<Chat>()
//
//    // moving our cursor to first position.
//    if (chatsCursor.moveToFirst()) {
//        do {
//
//        } while (chatsCursor.moveToNext());
//        // moving our cursor to next.
//    }
//    // at last closing our cursor
//    // and returning our array list.
//    chatsCursor.close();
//    return chatsArrayList;
//}