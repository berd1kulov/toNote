package com.bbb.noteapp.controller
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bbb.noteapp.R
import com.bbb.noteapp.bean.Note
import com.bbb.noteapp.database.DBManager

class MainActivity : AppCompatActivity() {

    var listNotes=ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load from database
        LoadQuery("%")

    }
    //Restore all datas again
    override fun onResume() {
        super.onResume()
        LoadQuery("%")

    }

    //Load data from database
    fun LoadQuery(title:String){
        var dbManager = DBManager(this)
        val projection = arrayOf("ID", "Title","Description" )
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projection, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID, Title, Description))

            }while (cursor.moveToNext())
        }
        var myNotesAdapter = MyNotesAdapter(this, listNotes)
        findViewById<ListView>(R.id.lvNotes).adapter = myNotesAdapter
    }

    //Create menu bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val sv:SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView

        val sm= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //LoadQuery("%"+query+"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                LoadQuery("%"+newText+"%")
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    //When add item selected, go to the next activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!=null){
            when(item.itemId){
                R.id.addNote ->{
                    var intent = Intent(this, AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Adapter for loading all data's into list
    inner class MyNotesAdapter:BaseAdapter{
        var context:Context?=null
        var listNotes=ArrayList<Note>()
        constructor(context: Context,listNote: ArrayList<Note>):super(){
            this.listNotes=listNote
            this.context=context
        }

        override fun getCount(): Int {
            return listNotes.size
        }

        override fun getItem(position: Int): Any {
            return listNotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var myView  = layoutInflater.inflate(R.layout.ticket,null)
            var myNote = listNotes[position]
            myView.findViewById<TextView>(R.id.tvName).text = myNote.noteName
            myView.findViewById<TextView>(R.id.tvDesc).text = myNote.noteDescription
            myView.findViewById<ImageView>(R.id.ivDelete).setOnClickListener{
                var dbManager = DBManager(this.context)
                val selectionArgs = arrayOf(myNote.noteID.toString())
                dbManager.Delete("ID=?", selectionArgs)
                LoadQuery("%")
            }
            myView.findViewById<ImageView>(R.id.ivEdit).setOnClickListener{
                GoToUpdate(myNote)
            }
            return myView

        }


    }

    //Put selected note into AddNotes activity for edit
    fun GoToUpdate(note: Note){
        var intent = Intent(this, AddNotes::class.java)
        intent.putExtra("ID",note.noteID)
        intent.putExtra("Title",note.noteName)
        intent.putExtra("Description",note.noteDescription)
        startActivity(intent)
    }
}