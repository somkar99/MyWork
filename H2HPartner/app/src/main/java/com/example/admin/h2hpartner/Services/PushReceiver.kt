package com.example.admin.h2hpartner.Services

/**
 * Created by harshagulati on 25/07/17.
 */

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Message
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.admin.h2hpartner.R
import com.example.admin.h2hpartner.UI.Home
import com.example.admin.h2hpartner.UI.MyCases
import com.example.admin.h2hpartner.UI.NewLeads
import org.json.JSONObject


class PushReceiver : BroadcastReceiver() {
    var TAG = "Messaging"
    override fun onReceive(context: Context, intent: Intent) {
        var notificationTitle = "Pushy"
        var notificationText = "Test notification"

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra(StaticRefs.NOTIFICATIONTYPE) != null) {

            val notifciationtype = intent.getStringExtra(StaticRefs.NOTIFICATIONTYPE)
            if (notifciationtype.equals(StaticRefs.NOTIFICATIONMESSAGE)) {
                if (intent.getStringExtra(StaticRefs.NOTIFICATIONDATA) != null) {
                    val notificationdata = JSONObject(intent.getStringExtra(StaticRefs.NOTIFICATIONDATA))
                    if (notificationdata.length() > 0) {
                        val sendertype = notificationdata.getString(StaticRefs.CHATSENDER)
                        val chatid = notificationdata.getString(StaticRefs.CHAT_ID)
                        val chatmessage = notificationdata.getString(StaticRefs.CHATMESSAGE)
                        val chattxnid = notificationdata.getString(StaticRefs.CHATTXNID)
                        val chatcustomerid = notificationdata.getString(StaticRefs.CHATCUSTOMERID)
                        val chatvendorid = notificationdata.getString(StaticRefs.CHATVENDORID)
                        if (sendertype.equals("C")) {
                            notificationTitle = notificationdata.getString(StaticRefs.CUSTOMERNAME).toString()
                            notificationText = chatmessage
                        } else {
                            notificationTitle = "Message from Customer"
                        }
                        // Prepare a notification with vibration, sound and lights
                        val intent = Intent(context, com.example.admin.h2hpartner.UI.Message::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra(StaticRefs.KEY, StaticRefs.NOTIFICATIONTYPE)
                        intent.putExtra(StaticRefs.CHATTXNID, chattxnid)
                        intent.putExtra(StaticRefs.CHATCUSTOMERID, chatcustomerid)
                        intent.putExtra(StaticRefs.CHATVENDORID, chatvendorid)
                        intent.putExtra(StaticRefs.CUSTOMERFIRSTNAME, notificationTitle)

                        val builder = NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.contactname_icon)
                                .setContentTitle(notificationTitle)
                                .setContentText(notificationText)
                                .setLights(Color.RED, 1000, 1000)
                                .setVibrate(longArrayOf(0, 400, 250, 400))
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                        // .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, Schedule.class), PendingIntent.FLAG_UPDATE_CURRENT));

                        // Get an instance of the NotificationManager service
                        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        // Build the notification and display it
                        notificationManager.notify(1, builder.build())
                    } else {
                        Log.d(TAG, "DATA IS NULL")
                    }


                } else {
                    Log.d(TAG, "No Data For Message")
                }
            }
            //   notificationText = intent.getStringExtra("message")
            else if (notifciationtype.equals(StaticRefs.NOTIFICATIONNEWLEADS)) {
                if (intent.getStringExtra(StaticRefs.NOTIFICATIONDATA) != null) {

                    val notificationdata = JSONObject(intent.getStringExtra(StaticRefs.NOTIFICATIONDATA))
                    if (notificationdata.length() > 0) {

                        val intent = Intent(context, NewLeads::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra(StaticRefs.KEY, StaticRefs.NOTIFICATIONTYPE)

                        val builder = NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.contactname_icon)
                                .setContentTitle(notificationTitle)
                                .setContentText(notificationText)
                                .setLights(Color.RED, 1000, 1000)
                                .setVibrate(longArrayOf(0, 400, 250, 400))
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                        // .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, Schedule.class), PendingIntent.FLAG_UPDATE_CURRENT));

                        // Get an instance of the NotificationManager service
                        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        // Build the notification and display it
                        notificationManager.notify(2, builder.build())
                    } else {
                        Log.d(TAG, "DATA IS NULL")
                    }

                } else {

                    Log.d(TAG, "No Data For New Leads")
                }
            } else if (notifciationtype.equals(StaticRefs.NOTIFICATIONMYCASES)) {
                if (intent.getStringExtra(StaticRefs.NOTIFICATIONDATA) != null) {

                    val notificationdata = JSONObject(intent.getStringExtra(StaticRefs.NOTIFICATIONDATA))
                    if (notificationdata.length() > 0) {

                        val intent = Intent(context, MyCases::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra(StaticRefs.KEY, StaticRefs.NOTIFICATIONTYPE)

                        val builder = NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.contactname_icon)
                                .setContentTitle(notificationTitle)
                                .setContentText(notificationText)
                                .setLights(Color.RED, 1000, 1000)
                                .setVibrate(longArrayOf(0, 400, 250, 400))
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                        // .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, Schedule.class), PendingIntent.FLAG_UPDATE_CURRENT));

                        // Get an instance of the NotificationManager service
                        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        // Build the notification and display it
                        notificationManager.notify(2, builder.build())
                    } else {
                        Log.d(TAG, "DATA IS NULL")
                    }

                } else {

                    Log.d(TAG, "No Data For New Leads")
                }
            }

        } else {

            Log.d(TAG, "Notification Type is missing")

        }


    }
}

