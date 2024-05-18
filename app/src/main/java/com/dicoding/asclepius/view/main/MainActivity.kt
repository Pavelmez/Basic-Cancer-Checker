package com.dicoding.asclepius.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.result.ResultActivity
import com.dicoding.asclepius.view.history.HistoryActivity
import com.dicoding.asclepius.view.news.NewsActivity
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.model.AspectRatio
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentImageUri: Uri? = null
    private lateinit var currentImageFile: File
    private lateinit var mainViewModel: MainViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
        binding.historyButton.setOnClickListener { moveToHistory() }

        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = this
        )
        // Initialize MainViewModel
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Initialize RecyclerView and its adapter
        newsAdapter = NewsAdapter { clickedItem ->
            // Handle item click here
            val intent = Intent(this@MainActivity, NewsActivity::class.java)
            intent.putExtra("title", clickedItem.title)
            intent.putExtra("description", clickedItem.description)
            intent.putExtra("imageUrl", clickedItem.urlToImage)
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = newsAdapter
        }

        // Observe the articlesList LiveData and update the adapter
        mainViewModel.articlesList.observe(this, Observer { articles ->
            articles?.let {
                newsAdapter.submitList(it)
            }
        })
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcherGallery.launch(pickImageIntent)
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let { uri ->
                currentImageFile = createImageFile()
                val destinationUri = Uri.fromFile(currentImageFile)

                val maxWidth = 800 // Example max width
                val maxHeight = 600 // Example max height

                // Define custom aspect ratio options
                val aspectRatioOptions = listOf(
                    AspectRatio("16:9", 16f, 9f),
                    AspectRatio("4:3", 4f, 3f),
                    AspectRatio("1:1", 1f, 1f)
                )

                val options = UCrop.Options().apply {
                    setAspectRatioOptions(0, *aspectRatioOptions.toTypedArray())
                }

                UCrop.of(uri, destinationUri)
                    .withOptions(options)
                    .withMaxResultSize(maxWidth, maxHeight)
                    .start(this@MainActivity)
            }
        } else {
            showToast("No image selected")
        }
    }

    private fun createImageFile(): File {
        // Create an image file name with a unique timestamp
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "cropped_image_$timeStamp",  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                currentImageUri = it // Update currentImageUri with the URI of the cropped image
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.message?.let {
                showToast(it)
            }
        }
    }

    private fun showImage() {
        // TODO: Display the image corresponding to the currentImageUri.
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        // TODO: Analyze the selected image
        if (::currentImageFile.isInitialized) {
            currentImageFile.let { file ->
                val uri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file)
                imageClassifierHelper.classifyStaticImage(uri)
            }
        } else {
            showToast("Please select an image first")
        }
    }

    private fun resultsToString(results: List<Classifications>): String {
        val resultStringBuilder = StringBuilder()
        for (classification in results) {
            val sortedCategories = classification.categories.sortedByDescending { it.score }
            val highestLabel = sortedCategories.firstOrNull()?.label ?: "Unknown"
            val highestScore = sortedCategories.firstOrNull()?.score ?: 0f
            val highestScorePercentage = highestScore * 100
            resultStringBuilder.append("$highestLabel = ${highestScorePercentage.formatPercentage(2)}%\n")
        }
        return resultStringBuilder.toString()
    }

    private fun Float.formatPercentage(digits: Int): String {
        return String.format("%.${digits}f", this)
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        if (results != null && results.isNotEmpty()) {
            val resultString = resultsToString(results)
            moveToResult(Uri.fromFile(currentImageFile), resultString) // Use URI from currentImageFile
        } else {
            showToast("No classification results available")
        }
    }

    private fun moveToResult(uri: Uri, results: String) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
            putExtra(ResultActivity.EXTRA_RESULTS, results)
        }
        startActivity(intent)
    }

    private fun moveToHistory() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onError(error: String) {
        showToast(error)
    }

}