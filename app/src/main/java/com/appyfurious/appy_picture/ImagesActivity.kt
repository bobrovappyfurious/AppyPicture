package com.appyfurious.appy_picture

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.appyfurious.appypicturelibrary.AppyPicture
import kotlinx.android.synthetic.main.activity_images.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        GlobalScope.launch(Dispatchers.Main) {
            imagesRecycler.adapter = ImagesAdapter(this@ImagesActivity)
        }

        buttonImageSize.setOnClickListener {
            textViewImageSize.text.toString().toIntOrNull()?.let { size ->
                AppyPicture.getPicturesManager().maxImagesInCache = size
                Toast.makeText(this, "new size: $size", Toast.LENGTH_LONG).show()
            }
        }
    }
}