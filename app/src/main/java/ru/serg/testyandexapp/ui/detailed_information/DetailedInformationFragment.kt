package ru.serg.testyandexapp.ui.detailed_information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.databinding.FragmentDetailedInformationBinding
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.helper.gone
import ru.serg.testyandexapp.helper.invisible
import ru.serg.testyandexapp.helper.visible
import ru.serg.testyandexapp.ui.detailed_information.tools.ChartMarkerView

class DetailedInformationFragment : Fragment() {

    private val detailedInformationViewModel: DetailedInformationViewModel by activityViewModels()
    private lateinit var binding: FragmentDetailedInformationBinding
    private val args: DetailedInformationFragmentArgs by navArgs()

    private lateinit var companyCard: CompanyCard

    private lateinit var chart: LineChart
    val entries = ArrayList<Entry>()
    val lineDataSet = LineDataSet(entries, "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detailed_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailedInformationViewModel.companyCard = args.companyCard
        binding = FragmentDetailedInformationBinding.bind(requireView())
//        chart = binding.lineChart
//        setUpChart()
        setOnClickListeners()

//        observeTrades()
//
//        observeHistory(1)

        binding.apply {
            companyNameTv.text = detailedInformationViewModel.companyCard.name
            tickerNameTv.text = detailedInformationViewModel.companyCard.ticker

        }

//        setUpChart()
    }

    private fun setUpChart() {
//        val chart = binding.lineChart
//        val entries = ArrayList<Entry>()
//
//        entries.add(Entry(1f, 10f, "LOL"))
//        entries.add(Entry(2f, 2f))
//        entries.add(Entry(3f, 7f))
//        entries.add(Entry(4f, 20f))
//        entries.add(Entry(5f, 16f))
//
//
//        val lineDataSet = LineDataSet(entries, "")
//
//        lineDataSet.apply {
//            fillDrawable =
//                ContextCompat.getDrawable(requireContext(), R.drawable.chart_gradient_background)
//            setDrawFilled(true)
//            setDrawCircles(false)
//            mode = (LineDataSet.Mode.CUBIC_BEZIER)
//            color = ContextCompat.getColor(requireContext(), R.color.theme_black)
//            lineWidth = 1.6f
//            setDrawValues(false)
//        }
//
//        chart.apply {
//            data = LineData(lineDataSet)
//            xAxis.isEnabled = false
//            axisLeft.isEnabled = false
//            axisRight.isEnabled = false
////            axisRight.setDrawGridLines(false)
////            axisRight.setDrawAxisLine(false)
//            minOffset = 1f
//            description.isEnabled = false
//            legend.isEnabled = false
//        }
//
//
//
//        chart.invalidate()


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
            viewPager.adapter = FragmentSwipeAdapter(requireParentFragment())
            TabLayoutMediator(tab, viewPager){ tabB, position ->
                tabB.setCustomView(R.layout.item_tab_text)
                tabB.view.findViewById<MaterialTextView>(R.id.text_tab).text = FragmentSwipeAdapter.sections_list[position]
            }.attach()


            backIv.setOnClickListener {
                findNavController().popBackStack()
            }

            //            favouriteStarIv.setOnClickListener {
//                //TODO
//            }
//
        }
    }

    override fun onDestroy() {
        detailedInformationViewModel.closeWebSockets()
        super.onDestroy()
    }

}