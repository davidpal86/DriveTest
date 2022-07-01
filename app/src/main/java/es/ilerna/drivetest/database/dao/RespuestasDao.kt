package es.ilerna.drivetest.database.dao

import androidx.room.*
import es.ilerna.drivetest.database.entities.Respuestas

@Dao
interface RespuestasDao {

    @Query("SELECT * from Respuestas")
    fun findAll():List<Respuestas>

    @Query("SELECT * from Respuestas WHERE idRespuesta=:idPreguntaRespuesta")
    fun findAllById(idPreguntaRespuesta:Int):Respuestas

    @Insert
    fun save(respuestas: Respuestas)

    @Update
    fun update(respuestas: Respuestas)

    @Delete
    fun delete(respuestas: Respuestas)
}