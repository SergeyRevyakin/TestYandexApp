package ru.serg.testyandexapp.ui.detailed_information.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.ConfigurationCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyNewsItem
import ru.serg.testyandexapp.databinding.ItemCompanyNewsBinding
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(
    private val newsList: List<CompanyNewsItem>,
    private val onItemClicked: (companyNewsItem: CompanyNewsItem) -> Unit
) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCompanyNewsBinding.bind(itemView)
        fun bind(newsItem: CompanyNewsItem) {
            binding.apply {
                headlineTv.text = newsItem.headLine
                bodyTv.text = newsItem.body
                sourceTv.text = itemView.resources.getString(R.string.source, newsItem.source)

                val date = Date(newsItem.dateTime * 1000L)
                val dateTime = SimpleDateFormat(
                    "dd.MM.yyyy",
                    ConfigurationCompat.getLocales(itemView.resources.configuration)[0]
                ).format(date)
                dateTv.text = dateTime

                Glide.with(itemView)
                    .load(newsItem.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(newsIv)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_company_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
        holder.itemView.rootView.setOnClickListener {
            onItemClicked.invoke(newsList[position])
        }
    }

    override fun getItemCount() = newsList.size
}