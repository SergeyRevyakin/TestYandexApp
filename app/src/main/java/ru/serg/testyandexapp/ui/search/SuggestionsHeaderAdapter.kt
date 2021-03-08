package ru.serg.testyandexapp.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.databinding.ItemSuggestionsHeaderBinding

class SuggestionsHeaderAdapter(private val header: String): RecyclerView.Adapter<SuggestionsHeaderAdapter.SuggestionsHeaderHolder>() {

    class SuggestionsHeaderHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val binding = ItemSuggestionsHeaderBinding.bind(itemView)
        fun bind(header:String){
            binding.suggestionsHeader.text = header
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionsHeaderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_suggestions_header, parent, false)
        return SuggestionsHeaderHolder(view)
    }

    override fun onBindViewHolder(holder: SuggestionsHeaderHolder, position: Int) {
        holder.bind(header)
    }

    override fun getItemCount() = 1
}