package aniket.testapplication

import android.app.Application
import aniket.testapplication.di.component.ApplicationComponent
import aniket.testapplication.di.component.DaggerApplicationComponent

class CameraApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(applicationContext)
    }
}