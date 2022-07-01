package es.ilerna.drivetest.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PreguntasConRespuestas(

    @Embedded val preguntas: Preguntas,
    @Relation(
        parentColumn = "idPregunta",
        entityColumn = "preguntaId"
    )
    val respuestas: List<Respuestas>
)
