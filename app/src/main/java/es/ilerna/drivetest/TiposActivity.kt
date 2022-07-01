package es.ilerna.drivetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.ilerna.drivetest.databinding.ActivityTiposBinding

class TiposActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTiposBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTiposBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Zona tests"
        val bundle = intent.extras
        val email = bundle?.getString("email")
        binding.tvMail.text = email

        binding.btnRapido1.setOnClickListener { rapido1() }

        binding.btnRapido2.setOnClickListener { rapido2() }

        binding.btnRapido3.setOnClickListener { rapido3() }

        binding.btnExamen1.setOnClickListener { examen1() }

        binding.btnExamen2.setOnClickListener { examen2() }

        binding.btnVuelve.setOnClickListener { volver(email) }
    }

    //Dependiendo de la opcion elegida llamo a una funcion que envia los indices que tiene que tomar
    //en la siguiente activity para mostrar un numero determinado de preguntas
    private fun examen1() {

        val testIntent = Intent(this, TestActivity::class.java).apply {
            putExtra("i", 1)
            putExtra("j",0)
            putExtra("fin", 31)
        }
        startActivity(testIntent)
    }

    private fun examen2() {
        val testIntent = Intent(this, TestActivity::class.java).apply {
            putExtra("i", 31)
            putExtra("j",30)
            putExtra("fin", 61)
        }
        startActivity(testIntent)
    }

    private fun rapido3() {
        val testIntent = Intent(this, TestActivity::class.java).apply {
            putExtra("i", 61)
            putExtra("j",60)
            putExtra("fin", 71)
        }
        startActivity(testIntent)
    }

    private fun rapido2() {
        val testIntent = Intent(this, TestActivity::class.java).apply {
            putExtra("i", 71)
            putExtra("j",70)
            putExtra("fin", 81)
        }
        startActivity(testIntent)
    }

    private fun rapido1() {
        val testIntent = Intent(this, TestActivity::class.java).apply {
            putExtra("i", 81)
            putExtra("j",80)
            putExtra("fin", 91)
        }
        startActivity(testIntent)
    }

    private fun volver(email: String?) {

        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }
}