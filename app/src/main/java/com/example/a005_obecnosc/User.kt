package com.example.a005_obecnosc

class User {
    lateinit var name: String
    lateinit var status: String

    constructor()
    constructor( name: String, status: String){
        this.name = name
        this.status = status
    }
    constructor( name: String, status: Status){
        this.name = name
        this.status = status.name
    }
}
