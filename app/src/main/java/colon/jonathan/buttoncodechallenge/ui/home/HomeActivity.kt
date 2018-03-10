package colon.jonathan.buttoncodechallenge.ui.home


import DEBUG
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.view.View
import android.view.animation.AnimationUtils
import colon.jonathan.buttoncodechallenge.BR
import colon.jonathan.buttoncodechallenge.R
import colon.jonathan.buttoncodechallenge.data.livedata.LoadingState.LOADING
import colon.jonathan.buttoncodechallenge.data.livedata.LoadingState.NOT_LOADING
import colon.jonathan.buttoncodechallenge.data.livedata.NetworkError
import colon.jonathan.buttoncodechallenge.databinding.ActivityHomeBinding
import colon.jonathan.buttoncodechallenge.injection.DaggerViewModelFactory
import colon.jonathan.buttoncodechallenge.model.User
import colon.jonathan.buttoncodechallenge.ui.base.BaseActivity
import colon.jonathan.buttoncodechallenge.ui.home.createUser.CreateUserFragment
import colon.jonathan.buttoncodechallenge.utilities.DiffCallback
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import toast
import javax.inject.Inject

class HomeActivity :
        BaseActivity<ActivityHomeBinding, HomeViewModel>(),
        HasSupportFragmentInjector {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @Inject
    lateinit var adapter: HomeAdapter

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override val viewModel: HomeViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(CreateUserFragment.TAG)
        when (fragment) {
            null -> super.onBackPressed()
            else -> onFragmentDetached(CreateUserFragment.TAG)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    private fun setUp() {
        setUpFloatingActionButton()
        setUpRecyclerView()
        setUpSwipeRefresh()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        subscribeToLoadingStateData()
        subscribeToNetworkErrorLiveData()
        subscribeToUserData()
    }

    /**
     * @description: Subscribes to live LoadingStatus data in the HomeViewModel.
     * @see HomeViewModel
     */
    private fun subscribeToLoadingStateData() {
        viewModel.getLiveLoadingState().observe(this, Observer { loadingState ->
            loadingState?.let {
                DEBUG("LOADING STATE: ${loadingState.name}")
                when (loadingState) {
                    NOT_LOADING -> viewDataBinding.swipeRefreshLayout.isRefreshing = false
                    LOADING -> viewDataBinding.swipeRefreshLayout.isRefreshing = true
                }
            }
        })
    }

    /**
     * @description: Subscribes to live NetworkError data in the HomeViewModel.
     * @see HomeViewModel
     */
    private fun subscribeToNetworkErrorLiveData() {
        viewModel.getLiveNetworkData().observe(this, Observer { error ->
            error?.let {
                DEBUG("NETWORK STATUS: ${error.name}")
                when (error) {

                    NetworkError.DOWNLOAD_SUCCESS -> {
                        viewDataBinding.rvUserList.visibility = View.VISIBLE
                        viewDataBinding.fabCreateUser.visibility = View.VISIBLE
                        viewDataBinding.errorMessageContainer?.visibility = View.GONE
                    }

                    NetworkError.DOWNLOAD_FAILURE -> {
                        showErrorLayout()
                    }

                    NetworkError.POST_SUCCESS -> {
                        onFragmentDetached(CreateUserFragment.TAG)
                    }

                    NetworkError.POST_FAILURE -> {
                        this.toast(getString(R.string.error_create_user_failure))
                    }

                    NetworkError.NO_CONNECTION -> {
                        showErrorLayout()
                        this.toast(getString(R.string.error_no_internet_connection))

                    }
                }
            }
        })
    }

    private fun showErrorLayout() {
        viewDataBinding.rvUserList.visibility = View.GONE
        viewDataBinding.fabCreateUser.visibility = View.GONE
        viewDataBinding.errorMessageContainer?.visibility = View.VISIBLE
    }

    /**
     * @description: Subscribes to live User data in the HomeViewModel and populates list onChange.
     * @see HomeViewModel
     */
    private fun subscribeToUserData() {
        viewModel.getLiveUserList().observe(this, Observer { users ->
            users?.let {
                updateList(users)
            }
        })
    }

    /**
     * @description: Compares a new and old list of users and populates the HomeAdapter with
     * newest data.
     * @see DiffCallback
     * @see HomeAdapter
     */
    private fun updateList(newUsers: List<User>) {
        val oldUsers = adapter.users
        val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(DiffCallback(newUsers, oldUsers))
        adapter.clearItems()
        adapter.addItems(newUsers)
        result.dispatchUpdatesTo(adapter)
    }

    private fun setUpRecyclerView() {
        viewDataBinding.rvUserList.adapter = adapter
    }

    private fun setUpFloatingActionButton() {
        viewDataBinding.fabCreateUser.setOnClickListener({ showCreateUserFragment() })
    }

    private fun setUpSwipeRefresh() {
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener { refreshItems() }
        viewDataBinding.swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark))
    }

    private fun refreshItems() {
        viewModel.refreshData()
    }

    private fun showCreateUserFragment() {
        supportFragmentManager
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .add(R.id.root_view, CreateUserFragment.newInstance(), CreateUserFragment.TAG)
                .commit()

        hideFloatingActionButton()
    }

    private fun onFragmentDetached(tag: String) {
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                    .remove(fragment)
                    .commitNow()

            showFloatingActionButton()
        }
    }

    private fun showFloatingActionButton() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.slide_left)
        viewDataBinding.fabCreateUser.startAnimation(anim)
        viewDataBinding.fabCreateUser.visibility = View.VISIBLE
    }

    private fun hideFloatingActionButton() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.slide_right)
        viewDataBinding.fabCreateUser.startAnimation(anim)
        viewDataBinding.fabCreateUser.visibility = View.GONE
    }

}

