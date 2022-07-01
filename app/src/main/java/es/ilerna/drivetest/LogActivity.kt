package es.ilerna.drivetest

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import es.ilerna.drivetest.databinding.ActivityLogBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class LogActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_DriveTest)    // Con esto hacemos que vuelva al tema principal de la aplicación, después de mostrar la primera imagen de carga

        super.onCreate(savedInstanceState)
        binding= ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registro()
        login()
        session()
        binding.tvOlvido.setOnClickListener { olvidada() }
        binding.btnSalir.setOnClickListener { finishAffinity()}

    }

    override fun onStart() {
        super.onStart()
        binding.loginLayout.visibility = View.VISIBLE
    }

    private fun session(){

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email",null)

        if (email!=null){
            binding.loginLayout.visibility = View.INVISIBLE
            sesionIniciada(email)
        }
    }

    //Para el registro abrimos un nuevo activity para registrar datos del usuario
    private fun registro() {

        title = "Autenticación"

        //Registro por mail y contraseña
        binding.btnRegister.setOnClickListener {

            val registroIntent = Intent(this, RegistroActivity::class.java)
            startActivity(registroIntent)
        }
    }

    //Comprobamos que ha introducido un mail correcto y una contraseña para hacer el login
    private fun login(){

        binding.btnLogin.setOnClickListener{
            if(binding.etEmail.text!!.isNotEmpty()&&binding.etPassword.text!!.isNotEmpty()){
                if(mailCorrecto(binding.etEmail.text.toString())) {
                    if(binding.etPassword.text.toString().length >= 6) {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            binding.etEmail.text.toString(),
                            binding.etPassword.text.toString()
                        ).addOnCompleteListener {
                            if (it.isSuccessful) {
                                sesionIniciada(it.result?.user?.email ?: "")
                            } else muestraErrorLogin()
                        }
                    }else muestraErrorPassword()
                }else muestraErrorMail()
            }else muestraErrorMailVacio()
        }
    }

    private fun olvidada(){
        val olvidoIntent = Intent(this, OlvidoActivity::class.java)
        startActivity(olvidoIntent)
    }

    //Muestra un error al intentar hacer login con un usuario no registrado
    private fun muestraErrorLogin(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El usuario introducido no está registrado.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun muestraErrorPassword(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("La contraseña debe tener al menos 6 caracteres.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Comprueba que el email introducido es correcto
    private fun mailCorrecto(mail: String):Boolean{
        val patron: Pattern=Pattern.compile("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$")
        val compara:Matcher=patron.matcher(mail)
        return compara.find()
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

    private fun muestraErrorMailVacio(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No has introducido ningun mail o contraseña.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Nos lleva a la siguiente activity
    private fun sesionIniciada(email:String){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }
}