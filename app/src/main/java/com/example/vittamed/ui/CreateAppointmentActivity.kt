package com.example.vittamed.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vittamed.R
import java.util.Calendar

class CreateAppointmentActivity : AppCompatActivity() {
    val selectedCalendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_appointment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnNext = findViewById<Button>(R.id.btn_siguiente)
        val btnConfirm = findViewById<Button>(R.id.btn_confirmar)
        val cvNext = findViewById<CardView>(R.id.cv_siguiente)
        val cvConfirm = findViewById<CardView>(R.id.cv_confirmar)

        btnNext.setOnClickListener {
            cvNext.visibility = View.GONE
            cvConfirm.visibility = View.VISIBLE
        }
        btnConfirm.setOnClickListener {
            Toast.makeText(applicationContext, "Cita realizada exitosamente", Toast.LENGTH_SHORT)
                .show()
            finish()

        }

        val spinnerSpecialties = findViewById<Spinner>(R.id.spinner_especialidades)
        val spinnerDoctor = findViewById<Spinner>(R.id.spinner_medico)

        val optionsSpecialties = arrayOf("Especialidad 1" ,"Especialidad 2", "Especialidad 3")
        spinnerSpecialties.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionsSpecialties)

        val optionsDoctor = arrayOf("Medico 1", "Medico 2", "Medico 3")
        spinnerDoctor.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionsDoctor)


    }
    fun onClickScheduledDate(v: View?){
        val etScheduleDate = findViewById<EditText>(R.id.et_fecha)
        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)
        val listener = DatePickerDialog.OnDateSetListener{datePicker, y, m, d ->
            selectedCalendar.set(y,m, d)
            etScheduleDate.setText("$y-$m-$d")
            displayRadioButtons()
        }
        DatePickerDialog(this, listener,year, month, dayOfMonth).show()
    }

    private fun displayRadioButtons() {
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
        radioGroup.clearCheck()
        radioGroup.removeAllViews()

        val hours = arrayOf("8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM")
        hours.forEach {
            val radioButton = RadioButton(this)
            radioButton.id = View.generateViewId()
            radioButton.text = it
            radioGroup.addView(radioButton)
        }
    }



}