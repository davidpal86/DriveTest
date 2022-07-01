package es.ilerna.drivetest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.ilerna.drivetest.database.dao.ExplicacionDao
import es.ilerna.drivetest.database.dao.PreguntasConRespuestasDao
import es.ilerna.drivetest.database.dao.PreguntasDao
import es.ilerna.drivetest.database.dao.RespuestasDao
import es.ilerna.drivetest.database.entities.Explicacion
import es.ilerna.drivetest.database.entities.Preguntas
import es.ilerna.drivetest.database.entities.Respuestas

@Database(entities = arrayOf(Preguntas::class,Respuestas::class,Explicacion::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun preguntasDao():PreguntasDao
    abstract fun respuestasDao():RespuestasDao
    abstract fun preguntasConRespuestasDao():PreguntasConRespuestasDao
    abstract fun explicacionDao():ExplicacionDao

    companion object{
        private var db:AppDatabase? = null
        fun getDB(context: Context):AppDatabase{
            if (db==null){
                db = Room.databaseBuilder(context,AppDatabase::class.java,"Database").createFromAsset("Database.db").build()
            }
            return db!!
        }
    }
}