package com.example.vittamed.io.fcm

import android.util.Log
import com.example.vittamed.io.ApiService
import com.example.vittamed.util.PreferenceHelper
import com.example.vittamed.util.PreferenceHelper.get
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val apiService by lazy{
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val jwt = preferences["jwt", ""]
        if (jwt.isEmpty())
            return
        val authHeader = "Bearer $jwt"

        val call = apiService.postToken(authHeader, token)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Token registrado correctamente")
                } else {
                    Log.d(TAG, "Hubo un problema al registrar el token")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "Se produjo un error en el servidor: ${t.message}")
            }
        })
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

    companion object {
        private const val TAG = "MyFMS"
    }
}