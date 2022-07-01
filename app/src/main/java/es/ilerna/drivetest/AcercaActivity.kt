package es.ilerna.drivetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.ilerna.drivetest.databinding.ActivityAcercaBinding

class AcercaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcercaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcercaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="Acerca de"
        val bundle = intent.extras
        val email = bundle?.getString("email")

        binding.btnHome.setOnClickListener {
            val homeIntent = Intent(this, HomeActivity::class.java).apply {
                putExtra("email", email)
            }
            startActivity(homeIntent)
        }
    }
}