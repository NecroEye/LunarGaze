package com.muratcangzm.lunargaze.ui.fragments

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import com.muratcangzm.lunargaze.ui.adapters.CategoryAdapter
import com.muratcangzm.lunargaze.viewmodels.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeFragmentTest {


    @Mock
    private lateinit var mockViewModel: HomeViewModel

    @Mock
    private lateinit var mockCategoryAdapter: CategoryAdapter

    @Mock
    private lateinit var fragmentScenario: FragmentScenario<HomeFragment>


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){

        Dispatchers.setMain(Dispatchers.Unconfined)

        fragmentScenario = launchFragmentInContainer {
            HomeFragment().apply {
                mockViewModel = viewModel
                mockCategoryAdapter = categoryAdapter
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun testFragmentViews(){


        val recyclerView = mock(RecyclerView::class.java)

        fragmentScenario.onFragment{ fragment ->
            fragment.setupViews()
        }


        verify(recyclerView).apply {
            adapter = mockCategoryAdapter
            hasFixedSize()
        }
    }



}