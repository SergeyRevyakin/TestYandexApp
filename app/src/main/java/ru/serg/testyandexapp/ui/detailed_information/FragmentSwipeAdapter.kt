package ru.serg.testyandexapp.ui.detailed_information

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.ui.detailed_information.fragments.GraphFragment
import ru.serg.testyandexapp.ui.search.SearchFragment

class FragmentSwipeAdapter(private val fragment: Fragment):FragmentStateAdapter(fragment) {
    companion object{
        val sections_list = listOf("Chart", "Summary", "News", "Forecasts", "Ideas", "Todos")
    }
    override fun getItemCount() = sections_list.size

    override fun createFragment(position: Int): Fragment {
        when(position){
            1->{
                return GraphFragment()
            }
            0->{
                return SearchFragment()
            }

            else->{
                return GraphFragment()
            }
        }
    }
}