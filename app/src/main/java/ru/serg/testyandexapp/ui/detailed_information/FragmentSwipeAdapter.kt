package ru.serg.testyandexapp.ui.detailed_information

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.serg.testyandexapp.ui.detailed_information.fragments.GraphFragment
import ru.serg.testyandexapp.ui.detailed_information.fragments.NewsFragment
import ru.serg.testyandexapp.ui.detailed_information.fragments.SummaryFragment
import ru.serg.testyandexapp.ui.detailed_information.fragments.TodoFragment

class FragmentSwipeAdapter(private val fragment: Fragment):FragmentStateAdapter(fragment) {
    companion object{
        val sections_list = listOf("Chart", "Summary", "News", "Forecasts", "Ideas", "Todos")
    }
    override fun getItemCount() = sections_list.size

    override fun createFragment(position: Int): Fragment {
        when(position){
            0->{
                return GraphFragment()
            }
            1->{
                return SummaryFragment()
            }
            2->{
                return NewsFragment()
            }

            else->{
                return TodoFragment()
            }
        }
    }
}