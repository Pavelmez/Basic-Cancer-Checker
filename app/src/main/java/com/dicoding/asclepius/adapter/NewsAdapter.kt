package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.response.ArticlesItem

class NewsAdapter(private val onItemClick: (ArticlesItem) -> Unit) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var items: List<ArticlesItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = items[position]
        holder.bind(newsItem)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(items: List<ArticlesItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val resultImage: ImageView = itemView.findViewById(R.id.result_image)
        private val resultText: TextView = itemView.findViewById(R.id.result_text)
        private lateinit var currentItem: ArticlesItem

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClick(currentItem)
        }

        fun bind(newsItem: ArticlesItem) {
            currentItem = newsItem // Save the current item for click handling
            Glide.with(itemView.context)
                .load(newsItem.urlToImage)
                .into(resultImage)

            resultText.text = newsItem.title
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}