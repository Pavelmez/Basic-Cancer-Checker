package com.dicoding.asclepius.view.history

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setupDeleteButton()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter()

        val recyclerView: RecyclerView = findViewById(R.id.HistoryList)
        recyclerView.adapter = historyAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        historyViewModel.getAllHistory().observe(this) { historyList ->
            historyAdapter.submitList(historyList)
        }
    }

    private fun setupDeleteButton() {
        val deleteButton: FloatingActionButton = findViewById(R.id.DeleteButton)
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete All History")
            .setMessage("Are you sure you want to delete all history?")
            .setPositiveButton("Yes") { dialog, _ ->
                // Launch a coroutine on the IO dispatcher to delete all history
                GlobalScope.launch(Dispatchers.IO) {
                    historyViewModel.deleteAllHistory()
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}