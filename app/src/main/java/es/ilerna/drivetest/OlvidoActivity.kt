package es.ilerna.drivetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import es.ilerna.drivetest.databinding.ActivityOlvidoBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class OlvidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOlvidoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOlvidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRecuperar.setOnClickListener { recupera(binding.etEmail.text.toString()) }
        binding.btnVuelta.setOnClickListener{opcionVolver()}
    }

    private fun recupera(mail: String){
        if(mailCorrecto(mail)){
            val auth = FirebaseAuth.getInstance()

            auth.sendPasswordResetEmail(mail).addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(this, "Correo enviado!",Toast.LENGTH_SHORT).show()
                    val logIntent = Intent(this, LogActivity::class.java)
                    startActivity(logIntent)
                }else{
                    Toast.makeText(this, "Correo invalido o no registrado!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mailCorrecto(mail: String):Boolean{
        val patron: Pattern = Pattern.compile("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$")
        val compara: Matcher =patron.matcher(mail)
        return compara.find()
    }

    private fun opcionVolver() {
        val logIntent = Intent(this, LogActivity::class.java)
        startActivity(logIntent)
    }
}