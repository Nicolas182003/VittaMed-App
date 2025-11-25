package com.example.vittamed.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vittamed.databinding.ActivityRegisterBinding
import com.example.vittamed.io.ApiService
import com.example.vittamed.io.response.LoginResponse
import com.example.vittamed.util.PreferenceHelper
import com.example.vittamed.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val apiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvGoToLogin.setOnClickListener{
            goToLogin()
        }
        binding.btnConfirmRegister.setOnClickListener {
            performRegister()
        }
    }

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun performRegister() {
        val name = binding.etRegisterName.text.toString().trim()
        val email = binding.etRegisterEmail.text.toString().trim()
        val password = binding.etRegisterPassword.text.toString()
        val passwordConfirmation = binding.etRegisterPasswordConfirmation.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()){
            Toast.makeText(this, "Debe de completar todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != passwordConfirmation) {
            Toast.makeText(this, "Las contraseñas ingresadas no son iguales.", Toast.LENGTH_SHORT).show()
            return
        }

        val call = apiService.postRegister(name, email, password, passwordConfirmation)
        call.enqueue(object: Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val loginResponse = response.body()
                    if (loginResponse == null){
                        Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (loginResponse.success){
                        createSessionPreference(loginResponse.jwt)
                        goToMenu()
                    } else {
                        Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "El correo ya existe en la plataforma, Prueba con otro.", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: No se pudo registrarse", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToMenu() {
        val i = Intent(this, MenuActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun createSessionPreference( jwt: String ) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["jwt"] = jwt
    }
}
