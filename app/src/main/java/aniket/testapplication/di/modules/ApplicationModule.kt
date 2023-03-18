package aniket.testapplication.di.modules

import android.content.Context
import androidx.room.Room
import aniket.testapplication.di.annotations.ApplicationScope
import aniket.testapplication.repository.db.ProjectDB
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @ApplicationScope
    @Provides
    fun getProjectDB(
        context: Context
    ) : ProjectDB = Room.databaseBuilder(context, ProjectDB::class.java, "ProjectDB").build()


}