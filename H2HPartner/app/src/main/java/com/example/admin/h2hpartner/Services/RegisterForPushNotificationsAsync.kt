package com.example.admin.h2hpartner.Services

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast

import com.example.admin.h2hpartner.App
import com.example.admin.h2hpartner.prefs

import me.pushy.sdk.Pushy

/**
 * Created by harshagulati on 25/07/17.
 */

class RegisterForPushNotificationsAsync : AsyncTask<Void, Void, Exception>() {
    internal lateinit var context: Context
    var deviceToken:String=""

    fun setContext(context: Context) {
        this.context = context
    }

    override fun doInBackground(vararg params: Void): Exception? {
        try {
            // Assign a unique token to this device

            /*String deviceToken = null;

                if(deviceToken == null)
                {*/
            deviceToken = Pushy.register(context)
          prefs.devicetoken=deviceToken

            //}

            // Log it for debugging purposes
            Log.d("MyApp", "Pushy device token: $deviceToken")

            //App.setDeviceId(deviceToken);

            // Send the token to your backend server via an HTTP GET request
            // new URL("https://{YOUR_API_HOSTNAME}/register/device?token=" + deviceToken).openConnection();
        } catch (exc: Exception) {
            // Return exc to onPostExecute
            return exc
        }

        // Success
        return null
    }

    override fun onPostExecute(exc: Exception?) {
        // Failed?
        if (exc != null) {
            // Show error as toast message
            Toast.makeText(context, "MyApp"+exc.toString(), Toast.LENGTH_LONG).show()
            Log.d("MyApp: ", exc.toString())

            return
        }


        // Succeeded, do something to alert the user
    }
}

