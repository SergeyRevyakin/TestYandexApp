package ru.serg.testyandexapp.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.databinding.ItemStockInfoBinding
import ru.serg.testyandexapp.helper.roundTo

class CompanyCardAdapter(
    private val companyList: List<CompanyCard?>,
    private val onFavouriteClicked: (companyCard: CompanyCard) -> Unit,
    private val onItemClicked: (companyCard: CompanyCard) -> Unit
) : RecyclerView.Adapter<CompanyCardAdapter.CompanyCardViewHolder>() {
    class CompanyCardViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemStockInfoBinding.bind(itemView)
        val star = binding.favouriteStarIv
        val root = binding.root
        var card = binding.card
        val view = itemView
        fun bind(companyCard: CompanyCard) {
            binding.apply {
                companyNameTv.text = companyCard.name
                tickerNameTv.text = companyCard.ticker

                if (companyCard.currentPrice > 2) {
                    companyPriceTv.text = itemView.context.getString(
                        R.string.price_format,
                        companyCard.currentPrice.roundTo(2)
                    )
                } else {
                    itemView.context.getString(
                        R.string.price_format_long,
                        companyCard.currentPrice.roundTo(5)
                    )
                }

                dayDeltaTv.text = itemView.context.getString(
                    R.string.day_delta,
                    companyCard.deltaPrice.roundTo(2),
                    companyCard.deltaPricePercentage.roundTo(2)
                )

                if (companyCard.deltaPrice > 0) {
                    dayDeltaTv.setTextColor(getColor(itemView.context, R.color.price_green))
                } else {
                    dayDeltaTv.setTextColor(getColor(itemView.context, R.color.price_red))
                }

                if (companyCard.isFavourite) {
                    favouriteStarIv.setColorFilter(getColor(itemView.context, R.color.star_gold))
                } else {
                    favouriteStarIv.setColorFilter(getColor(itemView.context, R.color.star_grey))
                }

                Glide.with(itemView)
                    .load(companyCard.logoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerInside()
                    .into(stockLogoIv)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyCardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_stock_info, parent, false)
        return CompanyCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompanyCardViewHolder, position: Int) {
        companyList[position]?.let { card ->
            holder.bind(card)

            holder.root.setOnClickListener {
                onItemClicked.invoke(card)
            }
            holder.star.setOnClickListener {
                card.isFavourite = !card.isFavourite
                notifyItemChanged(position)
                onFavouriteClicked.invoke(card)
            }

            if (position % 2 == 0) {
                holder.card.setCardBackgroundColor(getColor(holder.view.context, R.color.white))
            } else {
                holder.card.setCardBackgroundColor(
                    getColor(
                        holder.view.context,
                        R.color.prompt_background_color
                    )
                )
            }
        }
    }

    override fun getItemCount() = companyList.size
}