package ru.serg.testyandexapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.databinding.FragmentMainBinding
import ru.serg.testyandexapp.helper.hideKeyboard
import ru.serg.testyandexapp.ui.common.CompanyCardAdapter

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        binding?.appBar?.setOnClickListener {
            view.findNavController().navigate(R.id.searchFragment)
        }

        view.hideKeyboard()

        setUpFavouritesAdapter()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpFavouritesAdapter() {
        mainViewModel.favourites.observe(viewLifecycleOwner, { list ->
            binding?.mainRecycler?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = CompanyCardAdapter(
                    list,
                    this@MainFragment::onFavouriteItemClick,
                    this@MainFragment::onCompanyCardClick
                )
            }
        })
    }

    private fun onFavouriteItemClick(companyCard: CompanyCard) {
        mainViewModel.removeFavourite(companyCard)
    }

    private fun onCompanyCardClick(companyCard: CompanyCard) {
        val navArgs = MainFragmentDirections.pass(companyCard)
        view?.findNavController()?.navigate(navArgs)
    }
}