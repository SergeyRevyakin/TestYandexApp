package ru.serg.testyandexapp.ui.detailed_information.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.databinding.FragmentNewsBinding
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.ui.detailed_information.DetailedInformationViewModel
import java.text.SimpleDateFormat
import java.util.*

class NewsFragment : Fragment() {
    val detailedInformationViewModel: DetailedInformationViewModel by activityViewModels()

    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)

        getCompanyNews()

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, day ->
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd",
                ConfigurationCompat.getLocales(resources.configuration)[0]
            )
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val sDate: String = sdf.format(calendar.getTime())
            detailedInformationViewModel.getCompanyNews(sDate)
        }

    }

    private fun getCompanyNews() {

        detailedInformationViewModel.companyNews.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.newsRv.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = NewsAdapter(it.data!!)
                    }
                }

                Resource.Status.LOADING -> {

                }

                Resource.Status.ERROR -> {

                }
            }
        })
    }

}