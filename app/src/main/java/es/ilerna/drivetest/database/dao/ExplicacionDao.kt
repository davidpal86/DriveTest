package es.ilerna.drivetest.database.dao

import androidx.room.*
import es.ilerna.drivetest.database.entities.Explicacion

@Dao
interface ExplicacionDao {
    @Query("SELECT * from Explicacion")
    fun findAll():List<Explicacion>

    @Query("SELECT * from Explicacion WHERE idExplicacion=:idExplicacion")
    fun findAllById(idExplicacion:Int): Explicacion

    @Insert
    fun save(explicacion: Explicacion)

    @Update
    fun update(explicacion: Explicacion)

    @Delete
    fun delete(explicacion: Explicacion)
}