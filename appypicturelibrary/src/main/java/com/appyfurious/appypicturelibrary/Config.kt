package com.appyfurious.appypicturelibrary

import android.content.Context
import android.content.ContextWrapper
import java.net.URL

object Config {
    fun getDirectory(context: Context) = ContextWrapper(context).getDir(this.javaClass.name, Context.MODE_PRIVATE)!!
}

fun URL.formatPath() = this.file.replace('/', '_')