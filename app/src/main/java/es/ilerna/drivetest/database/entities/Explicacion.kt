package es.ilerna.drivetest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Explicacion")
class Explicacion(
    @PrimaryKey(autoGenerate = true)
    val idExplicacion: Int = 0,
    val descripcion: String,
    val preguntaId: Int
)