package ru.serg.testyandexapp.ui.detailed_information.tools

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import ru.serg.testyandexapp.R
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*

class ChartMarkerView(context: Context, layoutResource: Int):MarkerView(context, layoutResource){

    private var priceTv:TextView = findViewById(R.id.price_tv)
    private var dateTv:TextView = findViewById(R.id.date_tv)

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        priceTv.text = e.y.toString()
        val date = Date(e.x.toLong()*1000L)
        val dateTime = SimpleDateFormat("dd.MM HH:mm").format(date)
        dateTv.text = dateTime.toString()
        super.refreshContent(e, highlight)// set the entry-priceTv as the display text
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height+10).toFloat())
    }
}