package es.ilerna.drivetest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Preguntas")

data class Preguntas(
    @PrimaryKey(autoGenerate = true)
    val idPregunta: Int = 0,
    val descripcion: String
)