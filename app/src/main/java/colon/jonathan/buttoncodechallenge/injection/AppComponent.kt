package colon.jonathan.buttoncodechallenge.injection

import android.app.Application
import colon.jonathan.buttoncodechallenge.application.ButtonApplication
import colon.jonathan.buttoncodechallenge.injection.activitybuilder.ActivityBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityBuilder::class,
        NetworkModule::class,
        ViewModelModule::class
))

interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: ButtonApplication)

}