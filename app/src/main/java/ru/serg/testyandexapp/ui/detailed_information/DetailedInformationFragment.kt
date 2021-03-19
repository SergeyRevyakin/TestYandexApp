package ru.serg.testyandexapp.ui.detailed_information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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
import ru.serg.testyandexapp.databinding.FragmentDetailedInformationBinding
import ru.serg.testyandexapp.ui.detailed_information.tools.ChartMarkerView

class DetailedInformationFragment : Fragment() {

    private val detailedInformationViewModel: DetailedInformationViewModel by activityViewModels()
    private lateinit var binding: FragmentDetailedInformationBinding
    private val args: DetailedInformationFragmentArgs by navArgs()

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

        setOnClickListeners()

        binding.apply {
            companyNameTv.text = detailedInformationViewModel.companyCard.name
            tickerNameTv.text = detailedInformationViewModel.companyCard.ticker

            if (detailedInformationViewModel.companyCard.isFavourite){
                favouriteStarIv.setImageResource(R.drawable.ic_favourite_gold)
            }else{
                favouriteStarIv.setImageResource(R.drawable.ic_favourite_empty)
            }
        }
    }

    private fun setOnClickListeners() {

        binding.apply {
            viewPager.adapter = FragmentSwipeAdapter(requireParentFragment())
            TabLayoutMediator(tab, viewPager) { tabB, position ->
                tabB.setCustomView(R.layout.item_tab_text)
                tabB.view.findViewById<MaterialTextView>(R.id.text_tab).text =
                    FragmentSwipeAdapter.sections_list[position]
            }.attach()


            backIv.setOnClickListener {
                findNavController().popBackStack()
            }

            favouriteStarIv.setOnClickListener {
                detailedInformationViewModel.changeFavouriteStatus()
                if (detailedInformationViewModel.companyCard.isFavourite){
                    favouriteStarIv.setImageResource(R.drawable.ic_favourite_gold)
                }else{
                    favouriteStarIv.setImageResource(R.drawable.ic_favourite_empty)
                }
            }
//
        }
    }

    override fun onDestroy() {
        detailedInformationViewModel.closeWebSockets()
        super.onDestroy()
    }

}