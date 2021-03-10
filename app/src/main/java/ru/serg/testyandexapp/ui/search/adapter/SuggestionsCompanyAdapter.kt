package ru.serg.testyandexapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.databinding.ItemSuggestionsCompanyBinding


class SuggestionsCompanyAdapter(
    private val suggestions: List<String>,
    private val onItemClicked:(request:String)->Unit
) : RecyclerView.Adapter<SuggestionsCompanyAdapter.SuggestionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_suggestions_company, parent, false)
        return SuggestionsViewHolder(view)
    }

    override fun getItemCount() = suggestions.size

    override fun onBindViewHolder(holder: SuggestionsViewHolder, position: Int) {
        holder.bind(suggestions[position])
    }


    inner class SuggestionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemSuggestionsCompanyBinding.bind(itemView)
        fun bind(name: String) {
            binding.companyName.text = name
            binding.root.setOnClickListener {
                onItemClicked.invoke(name)
            }
        }
    }
}