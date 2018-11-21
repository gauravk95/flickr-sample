package com.github.flickrsample.ui.gallery

import com.github.flickrsample.data.models.local.PhotoItem
import com.github.flickrsample.data.source.repository.AppRepository
import com.github.flickrsample.utils.FlickrUtils
import com.github.flickrsample.utils.DataGenerator
import com.github.flickrsample.utils.rx.TestSchedulerProvider
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.*

import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit test for GalleryPresenter
 * TODO: Improve Coverage
 *
 * Created by gk
 */
@RunWith(MockitoJUnitRunner::class)
class GalleryPresenterTest {

    @Mock
    internal var mMockGalleryMvpView: GalleryContract.View? = null

    @Mock
    internal var mMockAppRepository: AppRepository? = null

    private lateinit var mGalleryPresenter: GalleryPresenter

    private lateinit var dummyList: List<PhotoItem>

    @Before
    fun setUp() {
        dummyList = DataGenerator.getDummyGalleryList(85)

        mGalleryPresenter = GalleryPresenter(
                mMockAppRepository as AppRepository,
                TestSchedulerProvider(),
                CompositeDisposable())

        mGalleryPresenter.onAttach(mMockGalleryMvpView!!)
    }

    @Test
    fun loadFirstPhotos_ItemsAvailable_showItems() {

        //set current page as one
        mGalleryPresenter.page = 1

        val items = dummyList.subList(0, FlickrUtils.DEFAULT_PAGE_SIZE)
        given(mMockAppRepository?.getPhotoItemList(
                FlickrUtils.API_KEY,
                FlickrUtils.DEFAULT_QUERY,
                1, FlickrUtils.DEFAULT_PAGE_SIZE))
                .willReturn(Flowable.just(items))

        mGalleryPresenter.loadFirstPhotos(false, FlickrUtils.API_KEY, FlickrUtils.DEFAULT_QUERY)

        verify(mMockGalleryMvpView)?.showProgressDialog()
        verify(mMockGalleryMvpView)?.dismissProgressDialog()
        verify(mMockGalleryMvpView)?.initItemList(items)
    }

    @Test
    fun loadFirstPhotos_ItemsUnavailable_showEmpty() {
        given(mMockAppRepository?.getPhotoItemList(FlickrUtils.API_KEY,
                FlickrUtils.DEFAULT_QUERY,
                1, FlickrUtils.DEFAULT_PAGE_SIZE))
                .willReturn(Flowable.just(listOf()))

        mGalleryPresenter.loadFirstPhotos(false, FlickrUtils.API_KEY, FlickrUtils.DEFAULT_QUERY)

        verify(mMockGalleryMvpView)?.showProgressDialog()
        verify(mMockGalleryMvpView)?.dismissProgressDialog()
        verify(mMockGalleryMvpView)?.showEmptyListUI()
    }

    @Test
    fun loadNextPhotos_ItemsAvailable_refreshItems() {

        //make current page as 1
        mGalleryPresenter.page = 1

        val listOne = dummyList.subList(
                FlickrUtils.DEFAULT_PAGE_SIZE,
                2 * FlickrUtils.DEFAULT_PAGE_SIZE)
        given(mMockAppRepository?.getPhotoItemList(FlickrUtils.API_KEY,
                FlickrUtils.DEFAULT_QUERY,
                2, FlickrUtils.DEFAULT_PAGE_SIZE))
                .willReturn(Flowable.just(listOne))

        val listTwo = dummyList.subList(
                2 * FlickrUtils.DEFAULT_PAGE_SIZE,
                dummyList.size)
        given(mMockAppRepository?.getPhotoItemList(FlickrUtils.API_KEY,
                FlickrUtils.DEFAULT_QUERY,
                3, FlickrUtils.DEFAULT_PAGE_SIZE))
                .willReturn(Flowable.just(listTwo))

        //test load page one
        mGalleryPresenter.loadNextPhotos(false, FlickrUtils.API_KEY, FlickrUtils.DEFAULT_QUERY)
        assertEquals(mGalleryPresenter.isLastPage, false)
        assertEquals(mGalleryPresenter.page, 2)

        //test load page two
        mGalleryPresenter.loadNextPhotos(false, FlickrUtils.API_KEY, FlickrUtils.DEFAULT_QUERY)
        assertEquals(mGalleryPresenter.isLastPage, false)
        assertEquals(mGalleryPresenter.page, 3)

        //test load page three
        given(mMockAppRepository?.getPaginationStatus()).willReturn(true)
        mGalleryPresenter.loadNextPhotos(false, FlickrUtils.API_KEY, FlickrUtils.DEFAULT_QUERY)
        assertEquals(mGalleryPresenter.isLastPage, true)
        assertEquals(mGalleryPresenter.page, 3)

        verify(mMockGalleryMvpView, times(2))?.showBottomLoading()
        verify(mMockGalleryMvpView, times(2))?.hideBottomLoading()
        verify(mMockGalleryMvpView, times(2))?.refreshItemList()
    }

    @Test
    fun loadNextPhotos_ItemsUnavailable_notRefreshItems() {

        //set current page to 3
        mGalleryPresenter.page = 3

        given(mMockAppRepository?.getPaginationStatus()).willReturn(false)
        given(mMockAppRepository?.getPhotoItemList(FlickrUtils.API_KEY,
                FlickrUtils.DEFAULT_QUERY,
                4, FlickrUtils.DEFAULT_PAGE_SIZE))
                .willReturn(Flowable.just(listOf()))

        //test load page three
        mGalleryPresenter.loadNextPhotos(false, FlickrUtils.API_KEY, FlickrUtils.DEFAULT_QUERY)

        verify(mMockGalleryMvpView)?.showBottomLoading()
        verify(mMockGalleryMvpView)?.hideBottomLoading()
        verify(mMockGalleryMvpView, never())?.refreshItemList()
    }

    @Test
    fun onImageClick_launchPhotoViewActivity() {

        //test with no elements in photo list
        mGalleryPresenter.photoList = mutableListOf()
        mGalleryPresenter.onImageClicked(0)

        //test with elements in photo list
        mGalleryPresenter.photoList = dummyList.toMutableList()

        mGalleryPresenter.onImageClicked(0)

        mGalleryPresenter.onImageClicked(dummyList.size)

        //should one be invoked once
        verify(mMockGalleryMvpView)?.launchImageViewActivity(dummyList[0].id, FlickrUtils.DEFAULT_QUERY)
    }

}