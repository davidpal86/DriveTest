package es.ilerna.drivetest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Respuestas")

data class Respuestas(
    @PrimaryKey(autoGenerate = true)
    val idRespuesta: Int = 0,
    val descripcion: String,
    val esCorrecta: Boolean,
    val preguntaId: Int
)