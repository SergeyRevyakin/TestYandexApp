package ru.serg.testyandexapp.ui.detailed_information.tools

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import ru.serg.testyandexapp.R

class ChartMarkerView(context: Context?, layoutResource: Int):MarkerView(context, layoutResource){

    private var priceTv:TextView
    init {
        priceTv = findViewById(R.id.price_tv)
    }

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        priceTv.setText("" + e.y)
        super.refreshContent(e, highlight)// set the entry-priceTv as the display text
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height-30).toFloat())
    }
}