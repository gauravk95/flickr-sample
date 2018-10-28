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
package com.github.flickrsample.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RawRes

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset

/**
 * Utilities for File related stuffs
 *
 * Created by gk
 */

object FileUtils {

    private const val TAG = "FileUtils"

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    /* Checks if external storage is available to at least read */
    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state)
    }

    fun getPathFromContentResolver(contentResolver: ContentResolver, uri: Uri): String {

        var path: String?
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)

        if (cursor == null) {
            path = uri.path
        } else {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
            path = cursor.getString(columnIndex)
            cursor.close()
        }

        return if (path == null || path.isEmpty()) uri.path else path
    }

    fun copyFileFromRawToOthers(context: Context, @RawRes id: Int, targetPath: String) {

        //if files already exists, no need to copy
        if (checkIfFileExists(targetPath))
            return

        AppLogger.i(TAG, "Copying files from raw to directory!")
        val inputStream = context.resources.openRawResource(id)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(targetPath)
            val buff = ByteArray(1024)
            var read: Int?
            while (true) {
                read = inputStream!!.read(buff)

                if (read == 0)
                    break

                out.write(buff, 0, read)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun fileToBytes(file: File): ByteArray {
        val size = file.length().toInt()
        val bytes = ByteArray(size)
        try {
            val buf = BufferedInputStream(FileInputStream(file))
            buf.read(bytes, 0, bytes.size)
            buf.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bytes
    }

    fun checkIfFileExists(filePath: String): Boolean {
        return File(filePath).exists()
    }

    fun loadJSONFromAsset(context: Context, fileName: String): String? {
        var json: String?
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

}
