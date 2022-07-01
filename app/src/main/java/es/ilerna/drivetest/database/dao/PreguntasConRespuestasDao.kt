package es.ilerna.drivetest.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import es.ilerna.drivetest.database.entities.PreguntasConRespuestas

@Dao
interface PreguntasConRespuestasDao {
    @Transaction
    @Query("SELECT * FROM Preguntas")
    fun getPreguntasConRespuestas():List<PreguntasConRespuestas>
}