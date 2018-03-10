package colon.jonathan.buttoncodechallenge.ui.home.createUser;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CreateUserFragmentProvider {

    @ContributesAndroidInjector(modules = CreateUserFragmentModule.class)
    abstract CreateUserFragment provideCreateUserFragmentFactory();

}
