package ru.serg.testyandexapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.databinding.FragmentSearchBinding
import ru.serg.testyandexapp.helper.*
import ru.serg.testyandexapp.ui.search.adapter.CompanyCardAdapter
import ru.serg.testyandexapp.ui.search.adapter.SuggestionsCompanyAdapter


class SearchFragment:Fragment() {

//    companion object {
//        fun newInstance() = SearchFragment()
//    }
    var testList1 = mutableListOf<String>()

    private val searchViewModel:SearchViewModel by activityViewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
//        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        observeUI()
        setUpAutocompleteAdapter()
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

    private fun observeUI(){
        var popularCompanyAdapter = SuggestionsCompanyAdapter(emptyList())
        var searchCompanyAdapter = SuggestionsCompanyAdapter(listOf("Amazon", "Tesla", "IBM"))
        val companyCardAdapter = CompanyCardAdapter(mutableListOf())
        val compList = mutableListOf<CompanyCard?>()

//        searchViewModel.getCompanyBaseInfo("MA")

        searchViewModel.getCompanyBaseInfo("AAPL")

        searchViewModel.companyInfo.observe(viewLifecycleOwner, { result ->
            when (result.status){
                Resource.Status.SUCCESS -> {
                    compList.add(result?.data)
//                    binding?.startPopup?.gone()
                    binding?.searchResults?.visible()

                    binding?.stocksRecycler?.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = companyCardAdapter
                    }
                }
                Resource.Status.ERROR -> {}
                Resource.Status.LOADING -> {}
            }
        })

        searchViewModel.companyInfo2.observe(viewLifecycleOwner, { result ->
            when (result.status){
                Resource.Status.SUCCESS -> {
                    isLoading(false)
//                    binding?.startPopup?.gone()
                    binding?.searchResults?.visible()

                    compList.add(result?.data)

                    binding?.stocksRecycler?.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = CompanyCardAdapter(compList)
                    }
                }
                Resource.Status.ERROR -> {}
                Resource.Status.LOADING -> {
                    isLoading(true)
                }
            }
        })

        searchViewModel.predictionsData.observe(viewLifecycleOwner, { result ->

            when (result.status) {
                Resource.Status.SUCCESS -> {
                    val testList = mutableListOf<String>()
//                    view?.findViewById<TextView>(R.id.message)?.text = result.predictionsData?.bestMatches.toString()
                    result.data?.bestMatches?.forEach {
                        testList.add(it.name)
                    }

                    binding?.searchRequestsRecycler?.layoutManager = StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.HORIZONTAL)
                    binding?.searchRequestsRecycler?.adapter = SuggestionsCompanyAdapter(testList)

                    binding?.popularRequestsRecycler?.layoutManager = StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.HORIZONTAL)
                    binding?.popularRequestsRecycler?.adapter = SuggestionsCompanyAdapter(testList)

                    testList1 = testList
                    isLoading(false)

//                    binding?.startPopup?.invisible()
                    binding?.searchResults?.visible()

                }

                Resource.Status.ERROR -> {
//                    val layout = binding.mainLayout
//                    Snackbar.make(
//                        layout,
//                        getString(R.string.something_wrong),
//                        Snackbar.LENGTH_LONG
//                    )
//                        .withColor(ContextCompat.getColor(this, R.color.dark_red))
//                        .setTextColor(ContextCompat.getColor(this, R.color.white))
//                        .show()
//
//                    binding.prgLoading.visibility = View.GONE
//                    binding.btnConvert.visibility = View.VISIBLE
                }
                Resource.Status.LOADING -> {
                    isLoading(true)
//                    binding.prgLoading.visibility = View.VISIBLE
//                    binding.btnConvert.visibility = View.GONE
                }
            }
        })
    }

    private fun setUpAutocompleteAdapter(){
        binding?.searchInputTv?.let { it ->
            it.textChanges()
                .distinctUntilChanged()
                .debounce(500)
                .onEach {
                    if (!it.isNullOrBlank()) {
                        searchViewModel.getPredictions(it)
                    }
                }
                .launchIn(lifecycleScope)
        }

    }

    private fun createAdapters(){

        val popularCompanyAdapter = SuggestionsCompanyAdapter(emptyList())

        val searchCompanyAdapter = SuggestionsCompanyAdapter(emptyList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isLoading(isLoading: Boolean){
        if (isLoading) {
            binding?.closeIv?.invisible()
            binding?.loadingPb?.visible()
        } else{
            binding?.closeIv?.visible()
            binding?.loadingPb?.invisible()
        }
    }
}