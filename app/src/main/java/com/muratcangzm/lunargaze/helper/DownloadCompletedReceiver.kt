package com.muratcangzm.lunargaze.helper

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class DownloadCompletedReceiver: BroadcastReceiver() {

    private lateinit var downloadManager: DownloadManager

    override fun onReceive(context: Context?, intent: Intent?) {

        downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        if(intent?.action == "android.intent.action.DOWNLOAD_COMPLETE"){

            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            val query = downloadManager.query(DownloadManager.Query().setFilterById(id))

            if(id != -1L){
                Timber.tag("Download Completed").d("Download Completed")
            }
        }

    }
}