package com.example.personelnotekotlin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Not::class,Kategori::class], version = 4, exportSchema = false)
abstract class RoomDbContext:RoomDatabase() {

    abstract fun notDao(): INotDataAccess
    abstract fun kategoriDao(): IKategoriDataAccess

}

@Volatile
private var KOPYA:RoomDbContext? = null

fun databaseyiGetir(context: Context):RoomDbContext{
    if(KOPYA == null){
        KOPYA = Room.databaseBuilder(
            context.applicationContext,
            RoomDbContext::class.java,
            "personelnot_db"
        ).fallbackToDestructiveMigration().build()
    }
    return KOPYA!!
}