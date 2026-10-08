package com.example.kitabusf1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

//List for table and schema
@Database(
    entities = [BookEntity::class, BookingEntity::class],
    version = 1,
    exportSchema = false //Turned false in order to not trigger a potential build issue, dont need Room saving a copy of the table layout
)

//Attaches converter class so Room knows how to store Bookingstatus
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    //abstract functions are used for the rest of the app to get the DAO's
    abstract fun bookDao(): BookDao
    abstract fun bookingDao(): BookingDao

    //Acts like a "static" function, makes sure to hold only the things belonging to the class
    companion object{

        //Instance - Holds one database or a null before it gets created
        @Volatile //makes sure all threads see the latest value of Instance
        private var INSTANCE: AppDatabase? = null

        private val seedScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        //returns an existing db or makes one
        fun getDatabase(context: Context): AppDatabase =

            INSTANCE ?: synchronized(this){ //the sync means only one thread runs at a time, less chance of multiple threads running and making a db
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, //uses the apps whole context
                    AppDatabase::class.java,
                    "kitabu_database" //file name on the phone
                )
                    .fallbackToDestructiveMigration(dropAllTables = true) //acts as a "save point" you can go back to
                    .addCallback(SeedCallback)
                    .build()
                    .also { INSTANCE = it } //saves db before returning it
            }

        suspend fun seed(database: AppDatabase){
            database.bookDao().insertBooks(SeedData.books)
            database.bookingDao().insertBookings(SeedData.bookings())
        }

        private object SeedCallback: Callback(){
            override fun onCreate(db: SupportSQLiteDatabase){
                super.onCreate(db)
                INSTANCE?.let { database -> seedScope.launch { seed(database) } }
            }
        }
    }
}