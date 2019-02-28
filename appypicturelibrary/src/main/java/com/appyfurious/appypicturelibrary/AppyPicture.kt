package com.appyfurious.appypicturelibrary

import android.app.Application
import android.graphics.Bitmap
import java.io.File
import java.net.URL

object AppyPicture {

    private lateinit var application: Application
    private lateinit var directory: File

    private lateinit var picturesManager: PicturesManager

    @JvmStatic
    fun init(application: Application) {
        this.application = application
        directory = Config.getDirectory(application)
        picturesManager = PicturesManager(application)
    }

    @JvmStatic
    fun getPicturesManager() = picturesManager

    @JvmStatic
    @Synchronized
    fun load(path: URL) = Request(path)

    @JvmStatic
    fun load(path: String) = AppyPicture.load(URL(path))

    class Request(private val url: URL) {

        fun request(target: Target) {
            val loader = NativeLoader(application, picturesManager)
            loader.listener = object : NativeLoader.Listener {
                override fun onProgress(countPercent: Int?) = target.onProgress(Progress(countPercent, url))
                override fun onCompleted(bitmap: Bitmap) = target.onBitmapLoaded(Result(bitmap, url))
                override fun onFailed(ex: Exception) = target.onBitmapFailed(ex)
            }
            loader.loadImage(url)
        }

        fun request(completed: Completed) {
            request { completed.onBitmapLoaded(it) }
        }

        fun request(completed: (Result) -> Unit) {
            request(object : Target {
                override fun onBitmapLoaded(result: Result) = completed(result)
                override fun onProgress(progress: Progress) {}
                override fun onBitmapFailed(ex: Exception) {}
            })
        }
    }

    class Result(val bitmap: Bitmap, val uri: URL)
    class Progress(var progress: Int?, val url: URL)

    public interface Target {
        fun onBitmapLoaded(result: Result)
        fun onProgress(progress: Progress)
        fun onBitmapFailed(ex: Exception)
    }

    public interface Completed {
        fun onBitmapLoaded(result: Result)
    }
}