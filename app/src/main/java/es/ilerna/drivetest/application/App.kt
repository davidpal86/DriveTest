package es.ilerna.drivetest.application

import android.app.Application
import es.ilerna.drivetest.database.AppDatabase

class App : Application() {

    companion object{
        private var db: AppDatabase? = null
        fun getDb(): AppDatabase{
            return db!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDB(applicationContext)
    }
}