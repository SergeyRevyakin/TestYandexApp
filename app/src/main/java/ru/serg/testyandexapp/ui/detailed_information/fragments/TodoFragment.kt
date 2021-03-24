package ru.serg.testyandexapp.ui.detailed_information.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.databinding.FragmentTodoBinding
import ru.serg.testyandexapp.ui.detailed_information.DetailedInformationViewModel

class TodoFragment : Fragment() {
    private val detailedInformationViewModel: DetailedInformationViewModel by activityViewModels()
    private lateinit var binding: FragmentTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTodoBinding.bind(view)
    }

}