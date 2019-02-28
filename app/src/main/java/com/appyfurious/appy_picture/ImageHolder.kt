package com.appyfurious.appy_picture

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.appyfurious.appypicturelibrary.AppyPicture
import kotlinx.android.synthetic.main.item_image.view.*
import java.lang.Exception

class ImageHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(bitmap: Bitmap?) {
        view.bigImage.setImageBitmap(bitmap)
    }

    fun bind(path: String, placeholder: Bitmap) {
        view.bigImage.setImageBitmap(placeholder)
        view.bigImage.setOnClickListener {
            AppyPicture.load(path).request(object : AppyPicture.Target {
                override fun onBitmapLoaded(result: AppyPicture.Result) {
                    view.bigImage.setImageBitmap(result.bitmap)
                }

                override fun onProgress(progress: AppyPicture.Progress) {
                    view.progress.text = progress.progress.toString()
                    Log.d("TAG_1", "${progress.url} ${progress.progress}")
                }

                override fun onBitmapFailed(ex: Exception) {
                    Toast.makeText(view.context, ex.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
