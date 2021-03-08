package ru.serg.testyandexapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.databinding.FragmentSearchBinding
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.helper.invisible
import ru.serg.testyandexapp.helper.textChanges
import ru.serg.testyandexapp.helper.visible
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


        searchViewModel.data.observe(viewLifecycleOwner, { result ->

            when (result.status) {
                Resource.Status.SUCCESS -> {
                    val testList = mutableListOf<String>()
//                    view?.findViewById<TextView>(R.id.message)?.text = result.data?.bestMatches.toString()
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
        val adapter = ArrayAdapter(this.requireContext(),android.R.layout.simple_dropdown_item_1line,testList1)
        binding?.searchInputTv?.let { it ->
            it.textChanges()
                .distinctUntilChanged()
                .debounce(500)
                .onEach {
                    if (it != null) {
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