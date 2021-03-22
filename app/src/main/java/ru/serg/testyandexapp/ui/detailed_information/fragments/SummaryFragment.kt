package ru.serg.testyandexapp.ui.detailed_information.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.databinding.FragmentSummaryBinding
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.helper.gone
import ru.serg.testyandexapp.helper.visible
import ru.serg.testyandexapp.ui.detailed_information.DetailedInformationViewModel

class SummaryFragment : Fragment() {
    private val detailedInformationViewModel: DetailedInformationViewModel by activityViewModels()
    private lateinit var binding: FragmentSummaryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSummaryBinding.bind(view)

        getCompanyOverview()

    }

    private fun getCompanyOverview() {
        detailedInformationViewModel.getCompanyOverview()
        detailedInformationViewModel.companyOverview.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    isLoading(false)
                    binding.apply {
                        companyNameTv.text = it.data?.fullName
                        descriptionTv.text = it.data?.description
                        Glide.with(requireView())
                            .load(detailedInformationViewModel.companyCard.logoUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerInside()
                            .into(stockLogoIv)
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

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visible(false)
        } else {
            binding.progressBar.gone()
        }
    }
}