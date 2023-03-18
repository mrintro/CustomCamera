package aniket.testapplication.di.component

import android.app.Application
import android.content.Context
import aniket.testapplication.MainActivity
import aniket.testapplication.di.annotations.ApplicationScope
import aniket.testapplication.di.modules.ApplicationModule
import aniket.testapplication.di.modules.NetworkModule
import aniket.testapplication.ui.HomeFragment
import aniket.testapplication.ui.SingleImageFragment
import aniket.testapplication.viewmodel.MainViewModel
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

}