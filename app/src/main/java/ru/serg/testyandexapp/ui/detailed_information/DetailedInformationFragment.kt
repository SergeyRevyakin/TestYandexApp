package ru.serg.testyandexapp.ui.detailed_information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.databinding.FragmentDetailedInformationBinding
import ru.serg.testyandexapp.helper.invisible
import ru.serg.testyandexapp.ui.detailed_information.tools.ChartMarkerView
import ru.serg.testyandexapp.ui.detailed_information.tools.DotMarkerView

class DetailedInformationFragment : Fragment() {

    private val detailedInformationViewModel: DetailedInformationViewModel by activityViewModels()
    private lateinit var binding: FragmentDetailedInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detailed_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailedInformationBinding.bind(requireView())

        setOnClickListeners()

        observeTrades()

//        setUpChart()
    }

    private fun setUpChart() {
        val chart = binding.lineChart
        val entries = ArrayList<Entry>()

        entries.add(Entry(1f, 10f,"LOL"))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 7f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 16f))


        val lineDataSet = LineDataSet(entries, "")

        lineDataSet.apply {
            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_gradient_background)
            setDrawFilled(true)
            setDrawCircles(false)
            mode = (LineDataSet.Mode.CUBIC_BEZIER)
            color = ContextCompat.getColor(requireContext(), R.color.theme_black)
            lineWidth = 1.6f
            setDrawValues(false)
        }

        chart.apply {
            data = LineData(lineDataSet)
            xAxis.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
//            axisRight.setDrawGridLines(false)
//            axisRight.setDrawAxisLine(false)
            minOffset = 1f
            description.isEnabled = false
            legend.isEnabled = false
        }



        chart.invalidate()
    }

    private fun setOnClickListeners() {
        binding.apply {
            backIv.setOnClickListener {
                findNavController().popBackStack()
            }

            favouriteStarIv.setOnClickListener {
                //TODO
            }

            radioGroup.setOnCheckedChangeListener { _, i ->
                when(i) {
                    first.id -> {
//                        first.invisible()
                    }
                }
            }

        }
    }

    private fun observeTrades(){
        val chart = binding.lineChart
        val entries = ArrayList<Entry>()
        val lineDataSet = LineDataSet(entries, "")

        lineDataSet.apply {
            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_gradient_background)
            setDrawFilled(true)
            setDrawCircles(false)
            mode = (LineDataSet.Mode.CUBIC_BEZIER)
            color = ContextCompat.getColor(requireContext(), R.color.theme_black)
            lineWidth = 1.6f
            setDrawValues(false)
            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)
            highLightColor = ContextCompat.getColor(requireContext(), R.color.teal_700)
        }

        chart.apply {
            data = LineData(lineDataSet)
            xAxis.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
//            axisRight.setDrawGridLines(false)
//            axisRight.setDrawAxisLine(false)
            minOffset = 3f
            extraTopOffset = 65f
            description.isEnabled = false
            legend.isEnabled = false
            marker = ChartMarkerView(context, R.layout.item_chart_mark)
//            marker = DotMarkerView(context, R.layout.chart_mark_dot)
        }
        var number = 0
//        chart.invalidate()
        lifecycleScope.launch {
            detailedInformationViewModel.tradeData.observe(viewLifecycleOwner,{
                it?.forEach {
                    binding.favouriteTv.text = "PRICE = ${it.lastPrice} \n"



                    if (number%10==0) {
                        entries.add(Entry(number.toFloat(), it.lastPrice.toFloat(),))
                    }
                    lineDataSet.notifyDataSetChanged()

                    chart.data = LineData(lineDataSet)

                    chart.notifyDataSetChanged()
                    chart.invalidate()

                    number++
                }
            })
        }
    }


}