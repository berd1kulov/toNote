package com.bbb.noteapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DBManager {
    val dbName = "MyNotes"
    val dbTable = "Notes"
    val colID = "ID"
    val colTitle = "Title"
    val colDesc = "Description"
    val dbVersion = 1
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS "+ dbTable +" ("+colID+" INTEGER PRIMARY KEY,"+
            colTitle +" TEXT, "+colDesc+" TEXT);"
    var sqlDB:SQLiteDatabase? = null
    constructor(context: Context?){
        var db = context?.let { DatabaseHelper(it) }
        sqlDB = db?.writableDatabase

    }

    //Creating the database
    inner class DatabaseHelper: SQLiteOpenHelper {
        var context:Context?=null
        constructor(context: Context):super(context, dbName, null, dbVersion){
            this.context = context
        }
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "Database is created!", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("ALTER TABLE "+dbTable)
        }

    }

    //Inserting data
    fun Insert(values:ContentValues):Long{
        var ID = sqlDB!!.insert(dbTable, "", values)
        return ID
    }

    //Selecting data
    fun Query(projection:Array<String>, selection:String, selectionArgs:Array<String>, sortOrder:String): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables= dbTable
        val cursor = qb.query(sqlDB,projection, selection, selectionArgs, null, null, sortOrder)
        return cursor
    }

    //Deleting data by ID
    fun Delete(selection: String, selectionArgs: Array<String>):Int{
        val count = sqlDB!!.delete(dbTable,selection, selectionArgs)
        return count
    }

    //Updating data
    fun Update(values: ContentValues, selection: String, selectionArgs: Array<String>):Int{
        val count = sqlDB!!.update(dbTable, values, selection, selectionArgs)
        return count
    }
}