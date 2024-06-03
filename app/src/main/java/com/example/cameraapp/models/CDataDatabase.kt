package com.example.cameraapp.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cameraapp.data.Constants.APP_SQL_DB_PROPER_NAME
import java.util.concurrent.Executors

@Database(entities = [CDataEntity::class], version = 1, exportSchema = false)
internal abstract class CDataDatabase : RoomDatabase() {
    abstract fun cdataDAO(): CDataDAO

    companion object {
        // marking the instance as volatile to ensure atomic access to the variable
        @Volatile
        private var INSTANCE: CDataDatabase? = null
        private const val NUMBER_OF_THREADS = 4

        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getDatabase(context: Context, user_email: String): CDataDatabase? {
            if (INSTANCE == null) {
                synchronized(CDataDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext, CDataDatabase::class.java, "${APP_SQL_DB_PROPER_NAME}_${user_email}")
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriteExecutor.execute {
                    // Populate the database in the background.
                    val dao: CDataDAO = INSTANCE!!.cdataDAO()
                    dao.deleteAll()
                }
            }
        }
    }
}