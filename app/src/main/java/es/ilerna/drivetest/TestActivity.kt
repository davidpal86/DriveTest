package es.ilerna.drivetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.ilerna.drivetest.application.App
import es.ilerna.drivetest.database.entities.Respuestas
import es.ilerna.drivetest.databinding.ActivityTestBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding
    private lateinit var bd: DatabaseReference
    private lateinit var bdUsuario: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="Tests"
        val bundle = intent.extras

        //Los valores que recibo de TiposActivity me determinan que y cuantas preguntas mostrar
        //dependiendo del tipo de test elegido
        val indice1 = bundle!!.getInt("i")
        val indice2 = bundle.getInt("j")
        val final = bundle.getInt("fin")

        //Recupero los datos de las tablas con el uso de corrutinas
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                var i = indice1   //Indice para las preguntas
                var j = indice2   //Indice para las respuestas(en lista)
                var respuestasCorrectas = 0
                var respuestasIncorrectas = 0
                var numeracion = 1 //Indice para numerar las preguntas de cada test
                var aprobado = false
                var puntos = 0

                do{
                //Asigno a variables el contenido de las tablas, una pregunta con tres respuestas asociadas y una explicacion

                    val pregunta = withContext(Dispatchers.IO) {
                        App.getDb().preguntasDao().findAllById(i)
                    }

                    val respuesta1 = withContext(Dispatchers.IO) {
                        App.getDb().preguntasConRespuestasDao().getPreguntasConRespuestas()
                            .get(j).respuestas.component1()
                    }

                    val respuesta2 = withContext(Dispatchers.IO) {
                        App.getDb().preguntasConRespuestasDao().getPreguntasConRespuestas()
                            .get(j).respuestas.component2()
                    }

                    val respuesta3 = withContext(Dispatchers.IO) {
                        App.getDb().preguntasConRespuestasDao().getPreguntasConRespuestas()
                            .get(j).respuestas.component3()
                    }

                    val explicacion = withContext(Dispatchers.IO){
                        App.getDb().explicacionDao().findAllById(i)
                    }

                    //Asigno la descripcion (pregunta y respuestas) al textview y radiobuttons
                    //para que se muestre por pantalla
                    binding.tvPregunta.text = "$numeracion. "+pregunta.descripcion
                    binding.rbRespuesta1.text = respuesta1.descripcion
                    binding.rbRespuesta2.text = respuesta2.descripcion
                    binding.rbRespuesta3.text = respuesta3.descripcion

                    //Compruebo si la respuesta marcada es correcta cuando hace click en el boton comprueba
                    //y actualizo los contadores de respuestasCorrectas/inconrrectas
                    binding.btnComprueba.setOnClickListener {

                        //Lo primero, activar el boton Siguiente y hacer visible el boton para la explicacion
                        binding.btnSiguiente.isEnabled = true
                        binding.btnExplicacion.visibility = View.VISIBLE
                        if (compruebaRespuestas(respuesta1,respuesta2,respuesta3)){
                            respuestasCorrectas++
                            binding.tvAciertos.text= "ACIERTOS: $respuestasCorrectas"
                            binding.tvRespuesta.setTextColor(resources.getColor(R.color.VERDE))
                            binding.tvRespuesta.text="¡RESPUESTA CORRECTA!"
                        }else{
                            respuestasIncorrectas++
                            binding.tvFallos.text= "FALLOS: $respuestasIncorrectas"
                            binding.tvRespuesta.setTextColor(resources.getColor(R.color.ROJO))
                        }
                        binding.btnExplicacion.setOnClickListener {
                            binding.tvExplicacion.text = explicacion.descripcion
                        }
                    }

                    //Pasamos a la siguiente pregunta
                    binding.btnSiguiente.setOnClickListener {
                        binding.rgGroup.clearCheck()
                        binding.tvRespuesta.text=""
                        binding.tvExplicacion.text=""
                        activaBotones()
                        numeracion++
                        i++
                        if (j<final-2) j++   //Controlo este indice de la lista ya que en determinadas ocasiones lanzaba error de OutOfBounds
                        if (i==(final-1)){     //En la ultima pregunta cambio el texto del boton SIGUIENTE por FIN
                            binding.btnSiguiente.text = "FIN"
                        }
                    }
                }while(i<final)

                aprobado = respuestasIncorrectas<4

                //Mostramos un mensaje al finalizar el test con los aciertos y fallos
                mostrarResumen(respuestasCorrectas,respuestasIncorrectas, aprobado)

                //Almaceno en puntos los tests suspensos
                if (aprobado) puntos++

                //Guardamos en una tabla el usuario que esta logado y el numero de test suspendidos
                bd = Firebase.database.reference

                val correo = FirebaseAuth.getInstance().currentUser!!.email
                bdUsuario = bd.child(FirebaseAuth.getInstance().currentUser!!.uid)

                val usuario = FirebaseAuth.getInstance().currentUser!!.uid

                //Aqui almaceno el valor del campo Puntuacion y asi sumarle luego los test suspendidos
                //manteniendo el ranking de puntuaciones asi actualizado cada vez que alguien hace un test
                var valor:Long
                bd.child(usuario).child("Puntuacion").get().addOnSuccessListener {
                    valor = (it.value as Long)
                    bdUsuario.child("Puntuacion").setValue(valor + puntos)
                }

                val tiposIntent = Intent(this@TestActivity,TiposActivity::class.java).apply {
                    putExtra("email", correo)
                }
                startActivity(tiposIntent)
            }
        }
    }

    private fun compruebaRespuestas(respuesta1: Respuestas, respuesta2: Respuestas, respuesta3: Respuestas): Boolean{

        if (binding.rbRespuesta1.isChecked && respuesta1.esCorrecta){
            desactivaBotones()
            return true
        }else if (binding.rbRespuesta2.isChecked && respuesta2.esCorrecta){
            desactivaBotones()
            return true
        }else if (binding.rbRespuesta3.isChecked && respuesta3.esCorrecta){
            desactivaBotones()
            return true
        }else {
            desactivaBotones()
            if (respuesta1.esCorrecta) {
                binding.tvRespuesta.text = "RESPUESTA INCORRECTA. La respuesta correcta es:\n"+respuesta1.descripcion
                return false
            }else if (respuesta2.esCorrecta){
                binding.tvRespuesta.text = "RESPUESTA INCORRECTA. La respuesta correcta es:\n"+respuesta2.descripcion
                return false
            }else{
                binding.tvRespuesta.text = "RESPUESTA INCORRECTA. La respuesta correcta es:\n"+respuesta3.descripcion
                return false
            }
        }
    }

    //Cuando finaliza el test se muestra un Toast con los aciertos y fallos totales
    private fun mostrarResumen(respuestasCorrectas: Int, respuestasIncorrectas: Int, aprobado: Boolean) {

        val aprueba = "¡¡¡EXAMEN APROBADO!!!\nAciertos: $respuestasCorrectas\nFallos: $respuestasIncorrectas"
        val suspende = "EXAMEN SUSPENDIDO:\nAciertos: $respuestasCorrectas\nFallos: $respuestasIncorrectas"
        if (aprobado) Toast.makeText(applicationContext, aprueba, Toast.LENGTH_LONG).show()
        else Toast.makeText(applicationContext, suspende, Toast.LENGTH_LONG).show()
    }

    //Una vez marcada la respuesta y confirmada, se desactivan las opciones de radiobutton y
    //el boton de comprobar para no poder modificar la respuesta
    private fun desactivaBotones() {
        binding.rbRespuesta1.isEnabled = false
        binding.rbRespuesta2.isEnabled = false
        binding.rbRespuesta3.isEnabled = false
        binding.btnComprueba.isEnabled = false
    }

    private fun activaBotones(){
        binding.rbRespuesta1.isEnabled = true
        binding.rbRespuesta2.isEnabled = true
        binding.rbRespuesta3.isEnabled = true
        binding.btnComprueba.isEnabled = true
        binding.btnSiguiente.isEnabled = false
        binding.btnExplicacion.visibility = View.INVISIBLE
    }
}