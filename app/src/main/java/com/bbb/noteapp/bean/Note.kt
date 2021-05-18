package com.bbb.noteapp.bean

class Note {
    var noteID:Int?=null
    var noteName:String?=null
    var noteDescription:String?=null

    constructor(noteID:Int, noteName:String, noteDesc:String){
        this.noteID=noteID
        this.noteName=noteName
        this.noteDescription=noteDesc
    }

}