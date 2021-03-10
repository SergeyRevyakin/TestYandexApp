package ru.serg.testyandexapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyBrief
import ru.serg.testyandexapp.databinding.ItemSuggestionsCompanyBinding

class PopularStocksAdapter (
    private val companyBrief: List<CompanyBrief>,
    private val onItemClicked:(request:String)->Unit
) : RecyclerView.Adapter<PopularStocksAdapter.PopularStocksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularStocksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_suggestions_company, parent, false)
        return PopularStocksViewHolder(view)
    }

    override fun getItemCount() = companyBrief.size

    override fun onBindViewHolder(holder: PopularStocksViewHolder, position: Int) {
        holder.bind(companyBrief[position])
    }


    inner class PopularStocksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemSuggestionsCompanyBinding.bind(itemView)
        fun bind(companyBrief: CompanyBrief) {
            binding.companyName.text = companyBrief.name
            binding.root.setOnClickListener {
                onItemClicked.invoke(companyBrief.ticker)
            }
        }
    }
}