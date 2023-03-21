package aniket.testapplication.di.component

import android.content.Context
import aniket.testapplication.MainActivity
import aniket.testapplication.di.annotations.ApplicationScope
import aniket.testapplication.di.modules.ApplicationModule
import aniket.testapplication.di.modules.NetworkModule
import aniket.testapplication.ui.HomeFragment
import aniket.testapplication.ui.SingleImageFragment
import aniket.testapplication.viewmodel.GlobalViewModel
import aniket.testapplication.viewmodel.MainViewModel
import aniket.testapplication.viewmodel.SingleImageViewModel
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(@BindsInstance context: Context) : ApplicationComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(homeFragment: HomeFragment)

    fun inject(singleImageFragment: SingleImageFragment)

    fun inject(viewModel: MainViewModel)

    fun inject(globalViewModel: GlobalViewModel)

    fun inject(singleImageViewModel: SingleImageViewModel)

}