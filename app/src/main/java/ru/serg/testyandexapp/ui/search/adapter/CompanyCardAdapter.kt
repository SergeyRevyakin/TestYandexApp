package ru.serg.testyandexapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.databinding.ItemStockInfoBinding
import ru.serg.testyandexapp.helper.roundTo

class CompanyCardAdapter(
    private val companyList: List<CompanyCard?>
) : RecyclerView.Adapter<CompanyCardAdapter.CompanyCardViewHolder>() {
    class CompanyCardViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemStockInfoBinding.bind(itemView)
        val star = binding.favouriteStarIv
        val root = binding.root
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
                        companyCard.currentPrice.roundTo(4)
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
                    .centerCrop()
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
        companyList[position]?.let {
            holder.bind(it)
            holder.root.setOnClickListener {
                Toast.makeText(it.context, "HOHO", Toast.LENGTH_LONG).show()
            }
            holder.star.setOnClickListener {_->
                it.isFavourite = !it.isFavourite
                notifyItemChanged(position)
            }}


    }

    override fun getItemCount() = companyList.size
}