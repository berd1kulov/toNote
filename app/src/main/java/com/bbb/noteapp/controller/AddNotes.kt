package com.bbb.noteapp.controller

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.bbb.noteapp.R
import com.bbb.noteapp.database.DBManager
import java.lang.Exception

class AddNotes : AppCompatActivity() {
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)


        try{//try to set data's if we come from edit button
            var bundle: Bundle? = intent.extras
            id = bundle!!.getInt("ID",0)
            if(id!=0) {
                findViewById<EditText>(R.id.etTitle).setText(bundle.getString("Title"))
                findViewById<EditText>(R.id.etDesc).setText(bundle.getString("Description"))
            }
        }catch (ex:Exception){}

    }

    //Button for add/edit note
    fun buAdd(view: View){
        var dbManager = DBManager(this)
        var values = ContentValues()
        values.put("Title", findViewById<EditText>(R.id.etTitle).text.toString())
        values.put("Description", findViewById<EditText>(R.id.etDesc).text.toString())

        if(id==0 && !findViewById<EditText>(R.id.etTitle).text.toString().equals("")) {//Checking for empty title and new note
            val ID = dbManager.Insert(values)
            if (ID > 0) {
                Toast.makeText(this, "The note is added!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "The note is not added!", Toast.LENGTH_LONG).show()
            }
        }else if(id!=0){ //Else checking for editing data
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.Update(values, "ID=?", selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "The note is updated!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "The note is not updated!", Toast.LENGTH_LONG).show()
            }
        }

    }
}