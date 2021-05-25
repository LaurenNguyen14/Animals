package com.example.animals.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.animals.R

//extend Image View class, allow automatic view load

fun getProgressDrawable(context: Context): CircularProgressDrawable {
/*
give littel spinner in image while it's loading
 */
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius= 50f
        start()
    }
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions().placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}