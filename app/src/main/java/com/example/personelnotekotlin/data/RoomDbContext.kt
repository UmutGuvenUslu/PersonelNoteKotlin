package com.example.personelnotekotlin.data

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase


@Database(entities = [Not::class], version = 1, exportSchema = false)
abstract class RoomDbContext:RoomDatabase() {

    abstract fun notDao():INotDataAccess

}

private var KOPYA:RoomDbContext? = null

fun databaseyiGetir(context: Context):RoomDbContext{
    if(KOPYA == null){
        KOPYA = Room.databaseBuilder(
            context.applicationContext,
            RoomDbContext::class.java,
            "personelnot_db"
        ).build()
    }
    return KOPYA!!
}