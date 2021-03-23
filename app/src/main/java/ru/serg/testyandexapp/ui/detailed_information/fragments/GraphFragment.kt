package ru.serg.testyandexapp.ui.detailed_information.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.databinding.FragmentGraphBinding
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.helper.gone
import ru.serg.testyandexapp.helper.invisible
import ru.serg.testyandexapp.helper.visible
import ru.serg.testyandexapp.ui.detailed_information.DetailedInformationViewModel
import ru.serg.testyandexapp.ui.detailed_information.tools.ChartMarkerView

class GraphFragment : Fragment() {

    private val detailedInformationViewModel: DetailedInformationViewModel by activityViewModels()
    private lateinit var binding: FragmentGraphBinding
    private lateinit var chart: LineChart
    private val entries = ArrayList<Entry>()
    private val lineDataSet = LineDataSet(entries, "")
    private val socketEntries = ArrayList<Entry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGraphBinding.bind(requireView())
        chart = binding.lineChart
        setUpChart()
        setOnClickListeners()

        observeTrades()

//        binding.apply {
//            companyNameTv.text = companyCard.name
//            tickerNameTv.text = companyCard.ticker
//
//        }

//        setUpChart()
    }

    private fun setUpChart() {

        lineDataSet.apply {
            fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.chart_gradient_background)
            setDrawFilled(true)
            setDrawCircles(false)
            mode = (LineDataSet.Mode.CUBIC_BEZIER)
            color = ContextCompat.getColor(requireContext(), R.color.theme_black)
            lineWidth = 1.6f
            setDrawValues(false)
            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)
        }

        chart.apply {
            data = LineData(lineDataSet)
            xAxis.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            minOffset = 3f
            extraTopOffset = 75f
            description.isEnabled = false
            legend.isEnabled = false
            marker = ChartMarkerView(context, R.layout.item_chart_mark)
        }
    }

    private fun setOnClickListeners() {
        binding.apply {

            radioGroup.setOnCheckedChangeListener { _, i ->
                when (i) {
                    liveButton.id -> {
                        observeTrades()
                    }

                    dayButton.id -> {
                        observeHistory(1, "5")
                    }
                    weekButton.id -> {
                        observeHistory(7, "D")
                    }
                    monthButton.id -> {
                        observeHistory(30, "D")
                    }
                    halfYearButton.id -> {
                        observeHistory(183, "W")
                    }
                    yearButton.id -> {
                        observeHistory(365, "W")
                    }
                }
            }

        }
    }

    private fun observeTrades() {
        lineDataSet.mode = (LineDataSet.Mode.LINEAR)
        lineDataSet.values = socketEntries
        detailedInformationViewModel.dayData.removeObservers(viewLifecycleOwner)
        detailedInformationViewModel.getLivePriceUpdate()
        entries.clear()
        chart.invalidate()

//        var socetEntries = ArrayList<Entry>()

        detailedInformationViewModel.tradeData.observe(viewLifecycleOwner, {
//            entries.clear()
//            for (i in 0..it.size){
//                if (i%5==0){
//                    entries.add(Entry(it[i].x, num))
//                }
//
//            }
            if (it != null) {
                try {
                    socketEntries.add(Entry(it[0].x, it[0].y))
                } catch (_: Exception) {
                }
//                entries.addAll(it)
            }
//            entries.addAll(it)
            lineDataSet.notifyDataSetChanged()
//
            chart.data = LineData(lineDataSet)

            chart.notifyDataSetChanged()
            chart.invalidate()
        })
    }


    private fun observeHistory(days:Int, resolution:String) {
        lineDataSet.mode = (LineDataSet.Mode.CUBIC_BEZIER)
        binding.lineChart.invisible(false)
        lineDataSet.values = entries
//        detailedInformationViewModel.tradeData.removeObservers(viewLifecycleOwner)
        detailedInformationViewModel.getDayData(days, resolution)
        detailedInformationViewModel.dayData.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.lineChart.visible()
                    binding.progressBar.gone()
                    entries.clear()
                    entries.addAll(it.data!!)
                }
                Resource.Status.LOADING -> {
                    binding.lineChart.invisible()
                    binding.progressBar.visible()
                }
            }

            lineDataSet.notifyDataSetChanged()
//
            chart.data = LineData(lineDataSet)

            chart.notifyDataSetChanged()
            chart.invalidate()
        })
    }

    override fun onDestroy() {
        detailedInformationViewModel.closeWebSockets()
        super.onDestroy()
    }

}