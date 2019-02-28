package com.appyfurious.appy_picture

import android.app.Application
import com.appyfurious.appypicturelibrary.AppyPicture

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        AppyPicture.init(this)
    }
}