package ru.serg.testyandexapp.ui.detailed_information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.databinding.FragmentDetailedInformationBinding

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
    }

    private fun setOnClickListeners() {
        binding.apply {
            backIv.setOnClickListener {
                findNavController().popBackStack()
            }

            favouriteStarIv.setOnClickListener {
                //TODO
            }

        }
    }

}