package com.example.a005_obecnosc

class User {
    lateinit var name : String
    lateinit var status : String

    constructor(name: String, status: String) {
        this.name = name
        this.status = status
    }

    constructor()
}