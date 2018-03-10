package colon.jonathan.buttoncodechallenge.injection.activitybuilder

import colon.jonathan.buttoncodechallenge.ui.home.HomeActivityModule
import colon.jonathan.buttoncodechallenge.ui.home.HomeActivity
import colon.jonathan.buttoncodechallenge.ui.home.createUser.CreateUserFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(HomeActivityModule::class, CreateUserFragmentProvider::class))
    internal abstract fun bindHomeActivity(): HomeActivity
}