package com.yunitski.organizer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHandlerArchive(context: Context, name: String?,
                         factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createNotesTable = ("CREATE TABLE " +
                TABLE_NOTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE
                + " TEXT, " + COLUMN_MESSAGE + " TEXT, " + COLUMN_DATE + " TEXT, " + COLUMN_TIME + " TEXT" + ")")
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

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "notesDB.db"
        val TABLE_NOTES = "notes"

        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_MESSAGE = "message"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
    }
}