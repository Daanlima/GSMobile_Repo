package com.danielfortes.globalsolution_carcontrol

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class PagerAdapter(gerenciador: FragmentManager): FragmentPagerAdapter(gerenciador) {
    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0)
            Agendamento()
        else if (position == 2)
            LavaRapido()
        else if (position == 3)
            Tutoriais()
        else
            Veiculos()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0)
            "Agendamento"
        else if (position == 2)
            "Lava Rápido"
        else if (position == 3)
            "Tutoriais"
        else
            "Veiculos"
    }

}
