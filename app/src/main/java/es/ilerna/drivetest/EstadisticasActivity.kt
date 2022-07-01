package es.ilerna.drivetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.ilerna.drivetest.database.entities.Usuario
import es.ilerna.drivetest.databinding.ActivityEstadisticasBinding
import kotlin.collections.ArrayList

class EstadisticasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEstadisticasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstadisticasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="Puntuaciones"
        val bundle = intent.extras
        val email = bundle?.getString("email")

        val jugador = Usuario("","")
        val usuarios  = ArrayList<String>()
        val adaptador: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usuarios)

        binding.lvUsuarios.adapter = adaptador


        //Recupero los datos de la tabla en Firebase guardandolos en una lista
        val database = FirebaseDatabase.getInstance().reference.orderByChild("Puntuacion")
        database.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                //Primero almaceno en posicion el numero de usuarios, asi tendre el numero de la ultima posicion
                var posicion = snapshot.childrenCount

                for (e in snapshot.children){
                    jugador.puntuacion = e.child("Puntuacion").value.toString()
                    jugador.nombre = e.child("Nombre").value.toString()
                    jugador.apellido = e.child("Primer apellido").value.toString()
                    adaptador.add("$posicion. "+jugador.nombre+" "+jugador.apellido+" ----> "+jugador.puntuacion+" tests aprobados")
                    posicion-- //Decremento la posicion hasta que llegue al 1
                }
                usuarios.reverse() //Invierto el orden de la lista para mostrarlo ascendente
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        binding.btnVolver.setOnClickListener{opcionVolver(email)}
    }

    private fun opcionVolver(email:String?) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }
}