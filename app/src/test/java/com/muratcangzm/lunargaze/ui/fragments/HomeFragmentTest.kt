import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.models.remote.CategoryModel
import com.muratcangzm.lunargaze.ui.adapters.CategoryAdapter
import com.muratcangzm.lunargaze.ui.fragments.HomeFragment
import com.muratcangzm.lunargaze.utils.NetworkChecking
import com.muratcangzm.lunargaze.viewmodels.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class HomeFragmentTest {

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var networkChecking: NetworkChecking
    private lateinit var mockCategoryModel: CategoryModel


    @Before
    fun setup() {
        viewModelFactory = Mockito.mock(ViewModelProvider.Factory::class.java)
        viewModel = Mockito.mock(HomeViewModel::class.java)
        categoryAdapter = Mockito.mock(CategoryAdapter::class.java)
        networkChecking = Mockito.mock(NetworkChecking::class.java)

        // Mocking viewModel's result flow
        Mockito.`when`(viewModel.categoriesResult).thenReturn(MutableStateFlow(null))
    }

    @Test
    fun `test fragment_view_created`() = runBlockingTest {
        // Mocking network availability
        Mockito.`when`(networkChecking.isNetworkAvailable()).thenReturn(true)

        // Launch the fragment
        val scenario = FragmentScenario.launchInContainer(
            HomeFragment::class.java,
            null,
            R.style.Theme_LunarGaze,
            null
        )

        scenario.onFragment { fragment ->
            fragment.viewModel.categoriesResult.value = mockCategoryModel

            // assertNotNull(fragment.binding.categoryRecycler)
            Mockito.verify(categoryAdapter).submitCategory(mockCategoryModel)
        }
    }
}
