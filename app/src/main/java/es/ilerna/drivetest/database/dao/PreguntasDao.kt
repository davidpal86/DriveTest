package es.ilerna.drivetest.database.dao

import androidx.room.*
import es.ilerna.drivetest.database.entities.Preguntas

@Dao
interface PreguntasDao {
    @Query("SELECT * from preguntas")
    fun findAll():List<Preguntas>

    @Query("SELECT * from preguntas WHERE idPregunta=:idPregunta")
    fun findAllById(idPregunta:Int):Preguntas

    @Insert
    fun save(preguntas: Preguntas)

    @Update
    fun update(preguntas: Preguntas)

    @Delete
    fun delete(preguntas: Preguntas)
}