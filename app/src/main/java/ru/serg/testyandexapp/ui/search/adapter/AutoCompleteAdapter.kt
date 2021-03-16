package ru.serg.testyandexapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.data.SuggestionItem
import ru.serg.testyandexapp.databinding.ItemAutocompleteBinding

class AutoCompleteAdapter(
    private val suggestionList: List<SuggestionItem>?,
    private val onItemClicked: (companyCard: CompanyCard) -> Unit
) : RecyclerView.Adapter<AutoCompleteAdapter.AutoCompleteViewHolder>() {

    class AutoCompleteViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemAutocompleteBinding.bind(itemView)
        val root = binding.root

        fun bind(suggestionItem: SuggestionItem) {
            binding.apply {
                companyNameTv.text = suggestionItem.name
                tickerNameTv.text = suggestionItem.ticker
                currencyTv.text = suggestionItem.currency
                typeTv.text = suggestionItem.type
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_autocomplete, parent, false)
        return AutoCompleteViewHolder(view)
    }

    override fun onBindViewHolder(holder: AutoCompleteViewHolder, position: Int) {
        suggestionList?.get(position)?.let { card ->
            holder.bind(card)

            holder.root.setOnClickListener {
//                onItemClicked.invoke(card)
            }
        }
    }

    override fun getItemCount() = suggestionList?.size ?: 0
}