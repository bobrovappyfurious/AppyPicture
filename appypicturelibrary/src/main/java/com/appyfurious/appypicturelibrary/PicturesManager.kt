package com.appyfurious.appypicturelibrary

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.net.URL

class PicturesManager(context: Context) {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val directory = Config.getDirectory(context)
    private var sharedPreferences = context.getSharedPreferences(javaClass.name, Context.MODE_PRIVATE)

    private val hash = hashMapOf<String, Bitmap>()

    public var maxImagesInCache: Int = 100
        set(value) {
            if (field == value || value < 0) return
            field = value
            sharedPreferences.edit().putInt(field.toString(), field).apply()
            update()
        }
        get() {
            field = sharedPreferences.getInt(field.toString(), field)
            return field
        }

    init {
        maxImagesInCache = sharedPreferences.getInt(maxImagesInCache.toString(), maxImagesInCache)
    }

    internal fun update() {
        ioScope.launch {
            synchronized(directory) {
                directory.listFiles().sortedByDescending { it.lastModified() }
                        .filterIndexed { index, it ->
                            Log.d("SORT_1", it.lastModified().toString())
                            index >= maxImagesInCache
                        }
                        .forEach { it.delete() }
            }
        }
    }

    internal fun get(url: URL): Bitmap? {
        var bitmap = hash[url.formatPath()]
        if (bitmap != null) return bitmap
        bitmap = BitmapFactory.decodeFile((File(directory, url.formatPath()).absolutePath))
        if (bitmap != null) hash[url.formatPath()] = bitmap
        return bitmap
    }

    public fun deleteAll() {
        ioScope.launch {
            synchronized(directory) {
                directory.listFiles().forEach { it.delete() }
            }
        }
    }

    public fun delete(path: String) {
        ioScope.launch {
            synchronized(directory) {
                directory.listFiles().filter { it.name == path }.forEach { it.delete() }
            }
        }
    }

    public fun delete(vararg paths: String) {
        delete(paths.toList())
    }

    public fun delete(paths: List<String>) {
        ioScope.launch {
            synchronized(directory) {
                directory.listFiles().filter { file ->
                    file.name == paths.find { path -> path == file.name }
                }.forEach { file -> file.delete() }
            }
        }
    }
}