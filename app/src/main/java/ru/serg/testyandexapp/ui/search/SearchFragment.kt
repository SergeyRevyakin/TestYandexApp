package ru.serg.testyandexapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyBrief
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.databinding.FragmentSearchBinding
import ru.serg.testyandexapp.helper.*
import ru.serg.testyandexapp.ui.common.CompanyCardAdapter
import ru.serg.testyandexapp.ui.search.adapter.PopularStocksAdapter
import ru.serg.testyandexapp.ui.search.adapter.SuggestionsCompanyAdapter


class SearchFragment : Fragment() {

    private val companyBriefList = mutableListOf<CompanyBrief>()
    private val searchViewModel: SearchViewModel by activityViewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        observeUI()
        setUpAutocomplete()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding?.let {
            it.backIv.setOnClickListener {
                findNavController().popBackStack()
            }

            it.closeIv.setOnClickListener { _ ->
                it.searchInputTv.text = null
            }
        }
    }

    private fun observeUI() {

        val compList = mutableListOf<CompanyCard?>()
        observeHistory()
        showPopular()

        searchViewModel.companyInfo.observe(viewLifecycleOwner, { result ->
            when (result.status) {
                Resource.Status.SUCCESS -> {

                    isLoading(false)

                    binding?.searchResults?.visible()

                    compList.add(result?.data)

                    binding?.stocksRecycler?.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = CompanyCardAdapter(compList, this@SearchFragment::onFavouriteItemClick, this@SearchFragment::onCompanyCardClick)
                    }
                }
                Resource.Status.ERROR -> {
                }
                Resource.Status.LOADING -> {
                    isLoading(true)
                }
            }
        })

//        searchViewModel.companyInfoList.observe(viewLifecycleOwner, { it ->
//            it.forEach { result->
//                when (result.status) {
//                    Resource.Status.SUCCESS -> {
//                        isLoading(false)
////                    binding?.startPopup?.gone()
//                        binding?.searchResults?.visible()
//
//                        compList.add(result?.data)
//
//                        binding?.stocksRecycler?.apply {
//                            layoutManager = LinearLayoutManager(context)
//                            adapter = CompanyCardAdapter(compList)
//                        }
//                    }
//                    Resource.Status.ERROR -> {
//                    }
//                    Resource.Status.LOADING -> {
//                        isLoading(true)
//                    }
//                }
//            }
//        })

        searchViewModel.predictionsData.observe(viewLifecycleOwner, { result ->

            when (result.status) {
                Resource.Status.SUCCESS -> {
                    isLoading(false)

                    binding?.searchResults?.visible()

                    result.data?.forEach {
                        searchViewModel.getCompanyBaseInfo(it.ticker)
                    }

                }

                Resource.Status.ERROR -> {
                    Toast.makeText(
                        context,
                        "There's some problem with API or Internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Resource.Status.LOADING -> {
                    isLoading(true)
                }
            }
        })
    }

    private fun observeHistory() {
        searchViewModel.history.observe(viewLifecycleOwner, { list ->
            var rows = 1

            if (list.size > 1) {
                rows = 2
            }
            binding?.searchRequestsRecycler?.layoutManager = StaggeredGridLayoutManager(
                rows,
                StaggeredGridLayoutManager.HORIZONTAL
            )
            binding?.searchRequestsRecycler?.layoutManager
            binding?.searchRequestsRecycler?.adapter =
                SuggestionsCompanyAdapter(
                    list.map { it.request }.reversed(),
                    this::onHistoryItemClick
                )
        })
    }

    private fun addPopularRequests() {
        companyBriefList.add(CompanyBrief("Apple Inc.", "AAPL"))
        companyBriefList.add(CompanyBrief("Microsoft Corp", "MSFT"))
        companyBriefList.add(CompanyBrief("Amazon.com", "AMZN"))
        companyBriefList.add(CompanyBrief("Facebook Inc A", "FB"))
        companyBriefList.add(CompanyBrief("Alphabet Inc A", "GOOGL"))
        companyBriefList.add(CompanyBrief("Tesla, Inc", "TSLA"))
        companyBriefList.add(CompanyBrief("Berkshire Hathaway", "BRK.B"))
        companyBriefList.add(CompanyBrief("JP Morgan Chase & Co", "JPM"))
        companyBriefList.add(CompanyBrief("Johnson & Johnson", "JNJ"))
    }

    private fun showPopular() {
        addPopularRequests()
        binding?.popularRequestsRecycler?.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.HORIZONTAL
        )
        binding?.popularRequestsRecycler?.adapter =
            PopularStocksAdapter(companyBriefList, this::onPopularItemClick)
    }

    private fun setUpAutocomplete() {
        binding?.searchInputTv?.let { it ->
            it.requestFocus()
            view?.showKeyboard()
            it.textChanges()
                .distinctUntilChanged()
                .debounce(500)
                .onEach {
                    if (!it.isNullOrBlank() &&
                        it.length > 1
                    ) {
//                        searchViewModel.getPredictions(it.toString())
                        searchViewModel.getPredictionsAlpha(it.toString())
//                        searchViewModel.getCompanyBaseInfo(it.toString())
                    }
                }
                .launchIn(lifecycleScope)
        }

    }

    private fun onHistoryItemClick(request: String) {
        binding?.searchInputTv?.setText(request)
    }

    private fun onPopularItemClick(ticker: String) {
        searchViewModel.getCompanyBaseInfo(ticker)
    }

    private fun onFavouriteItemClick(companyCard: CompanyCard){
        searchViewModel.saveOrRemoveFavourite(companyCard)
    }

    private fun onCompanyCardClick(companyCard: CompanyCard){
        view?.findNavController()?.navigate(R.id.detailedIInformationFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.closeIv?.invisible()
            binding?.loadingPb?.visible()
        } else {
            binding?.closeIv?.visible()
            binding?.loadingPb?.invisible()
        }
    }
}