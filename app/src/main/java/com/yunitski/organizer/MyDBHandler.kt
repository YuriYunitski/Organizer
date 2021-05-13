package com.yunitski.organizer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHandler(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createNotesTable = ("CREATE TABLE " +
                TABLE_NOTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE
                + " TEXT," + COLUMN_MESSAGE + " TEXT" + ")")
        db.execSQL(createNotesTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase, oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }
    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "notesDB.db"
        val TABLE_NOTES = "notes"

        val COLUMN_ID = "_id"
        val COLUMN_TITLE = "title"
        val COLUMN_MESSAGE = "message"
    }
}