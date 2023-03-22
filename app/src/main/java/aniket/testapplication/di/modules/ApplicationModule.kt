package aniket.testapplication.di.modules

import android.content.Context
import androidx.room.Room
import aniket.testapplication.di.annotations.ApplicationScope
import aniket.testapplication.repository.ProjectRepository
import aniket.testapplication.repository.db.ProjectDB
import aniket.testapplication.repository.db.ProjectDao
import aniket.testapplication.repository.service.APIService
import aniket.testapplication.ui.imageProcessor.URIResolver
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @ApplicationScope
    @Provides
    fun getProjectDB(
        context: Context
    ) : ProjectDB = Room.databaseBuilder(context, ProjectDB::class.java, "ProjectDB").build()
    @ApplicationScope
    @Provides
    fun getProjectRepository(
        roomDB: ProjectDB,
        service: APIService
    ) = ProjectRepository(roomDB, service)

    @ApplicationScope
    @Provides
    fun getURIResolver() = URIResolver()

}