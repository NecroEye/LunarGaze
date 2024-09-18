import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.ViewModelProvider
import com.muratcangzm.lunargaze.R
import com.muratcangzm.models.remote.giphy.CategoryModel
import com.muratcangzm.lunargaze.ui.adapters.CategoryAdapter
import com.muratcangzm.lunargaze.ui.fragments.HomeFragment
import com.muratcangzm.lunargaze.common.NetworkChecking
import com.muratcangzm.lunargaze.viewmodels.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
    private lateinit var mockCategoryModel: com.muratcangzm.models.remote.giphy.CategoryModel


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
        var scenario = FragmentScenario.launchInContainer(
            HomeFragment::class.java,
            null,
            R.style.Theme_LunarGaze,
            null
        )

        /**  remove comment line to test
        scenario.onFragment { fragment ->
            fragment.viewModel.categoriesResult.value = mockCategoryModel

            // assertNotNull(fragment.binding.categoryRecycler)
            Mockito.verify(categoryAdapter).submitCategory(mockCategoryModel)
        }
        **/

    }
}
