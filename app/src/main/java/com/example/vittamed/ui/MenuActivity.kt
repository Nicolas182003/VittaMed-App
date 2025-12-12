package com.example.vittamed.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vittamed.R
import com.example.vittamed.io.ApiService
import com.example.vittamed.util.PreferenceHelper
import com.example.vittamed.util.PreferenceHelper.set
import com.example.vittamed.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.cardview.widget.CardView
import com.google.firebase.messaging.FirebaseMessaging

class MenuActivity : AppCompatActivity() {

    private val apiService by lazy{
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val storeToken = intent.getBooleanExtra("store_token", false)
        if (storeToken)
            storeToken()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvGreeting = findViewById<TextView>(R.id.tv_greeting)
        val name = preferences["name", ""]
        if (name.isNotEmpty()){
            tvGreeting.text = "Hola, $name"
        }

        val btnLogout = findViewById<Button>(R.id.btn_logout)
        btnLogout.setOnClickListener{
            performLogout()
        }
        val btnReservarCita = findViewById<CardView>(R.id.btn_reservar_cita)
        btnReservarCita.setOnClickListener{
            gotoCreateAppointment()
        }
        val btnMisCitas = findViewById<CardView>(R.id.btn_mis_citas)
        btnMisCitas.setOnClickListener{
            goToMyAppointments()
        }
    }

    private fun storeToken() {
        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            val deviceToken = it.result
            val call = apiService.postToken(authHeader, deviceToken)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(
                    call: Call<Void?>,
                    response: Response<Void?>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Token registrado correctamente")
                    } else {
                        Log.d(TAG, "Hubo un problema al registrar el token")
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(this@MenuActivity, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                }


            })
        }
    }


    private fun goToMyAppointments() {
        val intent = Intent(this, AppointmentsActivity::class.java)
        startActivity(intent)
    }
    private fun gotoCreateAppointment(){
        val i = Intent(this, CreateAppointmentActivity::class.java)
        startActivity(i)
    }

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun performLogout(){
        val  jwt = preferences["jwt", ""]
        val call = apiService.postLogout("Bearer $jwt")
        call.enqueue(object: Callback<Void>{
            override fun onResponse(
                call: Call<Void?>,
                response: Response<Void?>
            ) {
                clearSessionPreference()
                goToLogin()
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun clearSessionPreference(){
        preferences["jwt"] = ""
        preferences["name"] = ""
    }

    companion object{
        private  const val TAG = "MenuActivity"
    }
}