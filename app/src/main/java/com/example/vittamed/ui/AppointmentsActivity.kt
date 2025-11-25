package com.example.vittamed.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vittamed.R
import com.example.vittamed.io.ApiService
import com.example.vittamed.model.Appointment
import com.example.vittamed.util.PreferenceHelper
import com.example.vittamed.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentsActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private val appointmentAdapter = AppointmentAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        val rvAppointments = findViewById<RecyclerView>(R.id.rv_appointments)

        rvAppointments.layoutManager = LinearLayoutManager(this)
        rvAppointments.adapter = appointmentAdapter

        loadAppointments()
    }

    private fun loadAppointments() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getAppointments("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Appointment>> {
            override fun onResponse(
                call: Call<ArrayList<Appointment>>,
                response: Response<ArrayList<Appointment>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        appointmentAdapter.appointments = it
                        appointmentAdapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Appointment>>, t: Throwable) {
                Toast.makeText(this@AppointmentsActivity, "Error: no se pudo cargar las citas médicas.",
                    Toast.LENGTH_SHORT).show()
            }

        })
    }

}