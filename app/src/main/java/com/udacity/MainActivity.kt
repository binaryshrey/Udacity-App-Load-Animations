package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udacity.databinding.ActivityMainBinding
import com.udacity.utils.DOWNLOAD_OPTIONS
import com.udacity.utils.sendNotification
import com.udacity.viewmodel.AppViewModel
import com.udacity.viewmodel.AppViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var downloadPath : String = ""
    private var selectedRadioText : String = ""
    private val NOTIFICATION_CHANNEL_ID = "Udacity Loading Channel"
    private val NOTIFICATION_CHANNEL_NAME = "Udacity Loading"

    private lateinit var appViewModel: AppViewModel
    private lateinit var appViewModelFactory: AppViewModelFactory
    private lateinit var activityMainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)
        createChannel(NOTIFICATION_CHANNEL_ID,NOTIFICATION_CHANNEL_NAME)

        //initialize viewModel
        appViewModelFactory = AppViewModelFactory(application)
        appViewModel = ViewModelProvider(this, appViewModelFactory).get(AppViewModel::class.java)
        activityMainBinding.lifecycleOwner = this


        activityMainBinding.contentMain.custom_button.setOnClickListener{
            appViewModel.updateDownloadStatus(false)
            activityMainBinding.contentMain.custom_button.buttonState = ButtonState.Loading
            download()
        }

        appViewModel.isDownloadEventComplete.observe(this, Observer {
            if(it){
                activityMainBinding.contentMain.custom_button.buttonState = ButtonState.Completed
            }
        })
    }

    private fun download() {
        when (downloadMeRadioGroup.checkedRadioButtonId) {
            R.id.downloadOption1 -> {
                downloadPath = DOWNLOAD_OPTIONS["Option1"]?.get("URI").toString()
                selectedRadioText = DOWNLOAD_OPTIONS["Option1"]?.get("type").toString()
            }
            R.id.downloadOption2 -> {
                downloadPath = DOWNLOAD_OPTIONS["Option2"]?.get("URI").toString()
                selectedRadioText = DOWNLOAD_OPTIONS["Option2"]?.get("type").toString()
            }
            R.id.downloadOption3 -> {
                downloadPath = DOWNLOAD_OPTIONS["Option3"]?.get("URI").toString()
                selectedRadioText = DOWNLOAD_OPTIONS["Option3"]?.get("type").toString()
            }
            else -> Toast.makeText(this,"Kindly select an option to download",Toast.LENGTH_LONG).show()
        }
        if(downloadPath.isNotEmpty()){
            val request =
                DownloadManager.Request(Uri.parse(downloadPath))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
        else{
            sendNotification("Unable to download!", "failed", selectedRadioText,this)
        }
        appViewModel.updateDownloadStatus(true)
        initBR()
    }

    private fun createChannel(channelId: String, channelName: String) {
        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                setShowBadge(false)
            }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.app_name)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun initBR(){
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadID) {
                    sendNotification("$selectedRadioText has been downloaded successfully!", "Successful", selectedRadioText,this@MainActivity)
                } else {
                    sendNotification("$selectedRadioText has failed to downloaded!", "Failed", selectedRadioText,this@MainActivity)
                }
            }
        }
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
}
