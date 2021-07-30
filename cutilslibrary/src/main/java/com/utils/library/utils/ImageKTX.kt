package com.utils.library.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.utils.library.img.GlideRequest
import com.utils.library.img.ImageLoaderDisplay
import java.io.File

fun ImageView.imageLoader(imageUrl:String,defResourceId: Int = 0,
                          errorResourceId: Int = 0){
    ImageLoaderDisplay.imageLoader(this,imageUrl,defResourceId = defResourceId,errorResourceId = errorResourceId)
}
fun ImageView.imageLoader(file: File,defResourceId: Int = 0,
                          errorResourceId: Int = 0){
    ImageLoaderDisplay.imageLoader(this,file = file,defResourceId = defResourceId,errorResourceId = errorResourceId)
}
