package colon.jonathan.buttoncodechallenge

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import colon.jonathan.buttoncodechallenge.data.Repository
import colon.jonathan.buttoncodechallenge.model.User
import colon.jonathan.buttoncodechallenge.ui.home.HomeViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class HomeViewModelTest {

    var repository: Repository? = null

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var homeViewModel: HomeViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {
        repository = Mockito.mock(Repository::class.java)
        MockitoAnnotations.initMocks(this)
            homeViewModel = HomeViewModel(repository!!)
    }

    @Test
    fun getLiveUserListTest() {
        val observer: Observer<*>? = Mockito.mock(Observer::class.java)
        val userList = MutableLiveData<List<User>>()

        Mockito.`when`(repository?.getUsers()).thenReturn(userList)
        homeViewModel.getLiveUserList().observeForever(observer as Observer<List<User>>)

        Mockito.verify(repository)?.getUsers()
    }
}
