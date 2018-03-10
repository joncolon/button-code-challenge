package colon.jonathan.buttoncodechallenge.ui.home

import dagger.Module
import dagger.Provides

@Module
class HomeActivityModule {

    @Provides
    internal fun provideHomeAdapter(homeViewModel: HomeViewModel): HomeAdapter {
        return HomeAdapter(homeViewModel)
    }

}
