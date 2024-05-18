package com.dicoding.asclepius.view.result

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.data.repository.HistoryRepository
import com.dicoding.asclepius.databinding.ActivityResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var historyRepository: HistoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.

        historyRepository = HistoryRepository(application)

        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val results = intent.getStringExtra(EXTRA_RESULTS)

        val resultText = getString(R.string.result) + "\n" + results
        binding.resultText.text = resultText

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            binding.resultImage.setImageURI(imageUri)
        }

        val id = generateId()

        if (!imageUriString.isNullOrEmpty() && !results.isNullOrEmpty()) {
            val history = History(id, imageUriString, results)
            saveToDatabase(history)
        }
    }

    private fun generateId(): Long {
        return System.currentTimeMillis()
    }

    private fun saveToDatabase(history: History) {
        GlobalScope.launch(Dispatchers.IO) {
            historyRepository.insert(history)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULTS = "extra_results"
    }
}
