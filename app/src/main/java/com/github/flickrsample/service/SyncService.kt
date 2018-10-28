/*
    Copyright 2018 Gaurav Kumar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.github.flickrsample.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

import com.github.flickrsample.utils.AppLogger

/**
 * This is a dummy sync class and is not registered in the manifest for the same reason
 * Modify this for your own use cases
 *
 * Created by gk
 */

class SyncService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        AppLogger.d(TAG, "SyncService started")
        return Service.START_STICKY
    }

    override fun onDestroy() {
        AppLogger.d(TAG, "SyncService stopped")
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {

        private val TAG = "SyncService"

        fun getStartIntent(context: Context): Intent {
            return Intent(context, SyncService::class.java)
        }

        fun start(context: Context) {
            val starter = Intent(context, SyncService::class.java)
            context.startService(starter)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, SyncService::class.java))
        }
    }
}
