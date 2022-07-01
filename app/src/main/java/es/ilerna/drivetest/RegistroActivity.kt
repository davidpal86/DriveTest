package es.ilerna.drivetest

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.ilerna.drivetest.databinding.ActivityRegistroBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Registro"
        var sexo = ""

        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Masculino", "Femenino"))
        binding.spinner.adapter = adaptador

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                sexo = parent?.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.btnBack.setOnClickListener {
            val logIntent = Intent(this, LogActivity::class .java)
            startActivity(logIntent)
        }

        binding.btnRegistrar.setOnClickListener {

            //Compruebo que los campos mail, password, nombre y apellido1 no estan vacios, y que es un mail valido
            if (binding.etCorreo.text.isNotEmpty() && binding.etPass.text.isNotEmpty() && binding.etPass2.text.isNotEmpty() && binding.etNombre.text.isNotEmpty() && binding.etApellido1.text.isNotEmpty()) {
                if (mailCorrecto(binding.etCorreo.text.toString())) {
                    if (binding.etPass.text.toString() == binding.etPass2.text.toString()) {
                        if (binding.etPass.text.toString().length >= 6) {
                            //Almaceno el usuario(mail y constraseña)
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                                binding.etCorreo.text.toString(),
                                binding.etPass.text.toString()
                            ).addOnCompleteListener {

                                if (it.isSuccessful) {
                                    //Si se ha registrado correctamente incio la sesion del usuario registrado
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                                        binding.etCorreo.text.toString(),
                                        binding.etPass.text.toString()
                                    ).addOnCompleteListener {
                                        if (it.isSuccessful) {

                                            //Guardo los datos del usuario en la base de datos de firebase
                                            val bd =
                                                Firebase.database.reference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            bd.child("Nombre").setValue(binding.etNombre.text.toString())
                                            bd.child("Primer apellido").setValue(binding.etApellido1.text.toString())
                                            bd.child("Segundo apellido").setValue(binding.etApellido2.text.toString())
                                            bd.child("Edad").setValue(binding.etEdad.text.toString())
                                            bd.child("Sexo").setValue(sexo)
                                            bd.child("Provincia").setValue(binding.etProvincia.text.toString())
                                            bd.child("eMail").setValue(binding.etCorreo.text.toString())

                                            //Creo tambien el campo puntuacion para que no apunte
                                            //a nulo en la lista(Usuario,puntuacion) al crear el usuario
                                            bd.child("Puntuacion").setValue(0)

                                            sesionIniciada(it.result?.user?.email ?: "")

                                            Toast.makeText(
                                                applicationContext,
                                                "Usuario registrado correctamente",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else muestraErrorRegistro()
                            }
                        } else muestraErrorPassword()
                    }else muestraErrorNoCoinciden()
                } else muestraErrorMail()
            } else muestraErrorCampoVacio()
        }
    }

    //Muestra un error si el email introducido es incorrecto
    private fun muestraErrorMail(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El mail introducido no es válido.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Muestra un error al intentar registrar un usuario ya registrado
    private fun muestraErrorRegistro(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El usuario introducido ya está registrado.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Comprueba que el email introducido es correcto
    private fun mailCorrecto(mail: String):Boolean{
        val patron: Pattern = Pattern.compile("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$")
        val compara: Matcher =patron.matcher(mail)
        return compara.find()
    }

    //La contraseña debe tener 6 o mas caracteres
    private fun muestraErrorPassword(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("La contraseña debe tener al menos 6 caracteres.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun muestraErrorNoCoinciden(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Las contraseñas no coinciden.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun sesionIniciada(email:String){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }

    private fun muestraErrorCampoVacio(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Es obligatorio rellenar todos los apartados marcados con *.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}