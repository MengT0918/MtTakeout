package com.mt.takeout.dagger2.module

import com.mt.takeout.presenter.impl.HomePresenterImpl
import com.mt.takeout.ui.fragment.HomeFragment
import com.mt.takeout.view.HomeView
import dagger.Module
import dagger.Provides

@Module
class HomeFragmentModule(private val homeFragment: HomeFragment) {
    @Provides fun providesHomePresenter(): HomePresenterImpl {
        return HomePresenterImpl(homeFragment)
    }
}