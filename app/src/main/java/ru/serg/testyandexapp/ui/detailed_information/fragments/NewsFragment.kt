package ru.serg.testyandexapp.ui.detailed_information.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyNewsItem
import ru.serg.testyandexapp.databinding.FragmentNewsBinding
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.helper.gone
import ru.serg.testyandexapp.helper.visible
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
            val sDate: String = sdf.format(calendar.time)
            detailedInformationViewModel.getCompanyNews(sDate)
        }

    }

    private fun getCompanyNews() {
        val sdf = SimpleDateFormat(
            "yyyy-MM-dd",
            ConfigurationCompat.getLocales(resources.configuration)[0]
        )
        val calendar: Calendar = Calendar.getInstance()
        detailedInformationViewModel.getCompanyNews(sdf.format(calendar.time))

        detailedInformationViewModel.companyNews.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    isLoading(false)
                    binding.newsRv.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = NewsAdapter(it.data!!, this@NewsFragment::browseNewsInBrowser)
                    }
                }

                Resource.Status.LOADING -> {
                    isLoading(true)
                }

                Resource.Status.ERROR -> {
                    isLoading(false)
                }
            }
        })
    }

    private fun browseNewsInBrowser(companyNewsItem: CompanyNewsItem) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(companyNewsItem.url)))
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visible(false)
        } else {
            binding.progressBar.gone()
        }
    }
}