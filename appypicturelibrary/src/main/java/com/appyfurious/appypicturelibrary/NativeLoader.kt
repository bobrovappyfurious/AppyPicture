package com.appyfurious.appypicturelibrary

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class NativeLoader(context: Context, private val picturesManager: PicturesManager) {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private val directory = Config.getDirectory(context)

    public var listener: NativeLoader.Listener? = null

    fun loadImage(url: URL) {
        uiScope.launch(Dispatchers.IO) {
            try {
                var bitmap = picturesManager.get(url)
                val file = File(directory, url.formatPath())
                if (bitmap == null) {
                    bitmap = request(url)
                }
                file.setLastModified(System.currentTimeMillis())
                picturesManager.update()
                completed(bitmap, url)
            } catch (ex: Exception) {
                uiScope.launch {
                    listener?.onFailed(ex)
                }
            }
        }
    }

    private fun request(url: URL): Bitmap? {
        val connection = url.openConnection()
        connection.connect()
        val input = BufferedInputStream(url.openStream())
        val file = File(directory, url.formatPath())
        val outputStream = FileOutputStream(file)
        val data = ByteArray(1024)
        var total: Long = 0
        var progress: Int? = null
        do {
            val count = input.read(data)
            if (count == -1) break
            total += count
            val newProgress = (total * 100 / connection.contentLength).toInt()
            progress(progress, newProgress)
            progress = newProgress
            outputStream.write(data, 0, count)
        } while (true)
        outputStream.flush()
        outputStream.close()
        input.close()
        return BitmapFactory.decodeFile(file.absolutePath)
    }

    private fun progress(old: Int?, new: Int) {
        if (old != new && new >= 0 && listener != null) {
            uiScope.launch {
                if (old != new) listener?.onProgress(new)
            }
        }
    }

    private fun completed(bitmap: Bitmap?, url: URL) {
        if (bitmap == null) throw NullPointerException("not found: $url")
        if (listener != null) {
            uiScope.launch { listener?.onCompleted(bitmap) }
        }
    }

    public fun cancel() {
        job.cancel()
    }

    interface Listener {
        fun onProgress(countPercent: Int?)
        fun onCompleted(bitmap: Bitmap)
        fun onFailed(ex: Exception)
    }
}