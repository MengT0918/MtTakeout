package com.mt.takeout.dagger2.component

import com.mt.takeout.dagger2.module.HomeFragmentModule
import com.mt.takeout.ui.fragment.HomeFragment
import com.mt.takeout.view.HomeView
import dagger.Component

@Component(modules = [HomeFragmentModule::class])
interface HomeFragmentComponent {
    fun inject(homeFragment: HomeFragment)
}