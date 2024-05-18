package com.dicoding.asclepius.view.news

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R

class NewsActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var titleText: TextView
    private lateinit var descripText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        imageView = findViewById(R.id.imageView)
        titleText = findViewById(R.id.titleText)
        descripText = findViewById(R.id.descripText)

        // Get data from intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set data to views
        titleText.text = title
        descripText.text = description
        Glide.with(this).load(imageUrl).into(imageView)
    }
}