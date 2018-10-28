package com.github.flickrsample.ui.imageviewer

import com.github.flickrsample.data.models.local.PhotoItem
import com.github.flickrsample.data.source.repository.AppRepository
import com.github.flickrsample.utils.DataGenerator
import com.github.flickrsample.utils.FlickrUtils
import com.github.flickrsample.utils.rx.TestSchedulerProvider
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit test for ImageViewPresenter
 * TODO: Improve Coverage
 *
 * Created by gk
 */
@RunWith(MockitoJUnitRunner::class)
class ImageViewerPresenterTest {

    @Mock
    internal var mImageViewerMvpView: ImageViewerContract.View? = null

    @Mock
    internal var mMockAppRepository: AppRepository? = null

    private lateinit var mImageViewerPresenter: ImageViewerPresenter

    private lateinit var dummyList: List<PhotoItem>

    @Before
    fun setUp() {
        dummyList = DataGenerator.getDummyGalleryList(85)

        mImageViewerPresenter = ImageViewerPresenter(
                mMockAppRepository as AppRepository,
                TestSchedulerProvider(),
                CompositeDisposable())

        mImageViewerPresenter.onAttach(mImageViewerMvpView!!)
    }

    @Test
    fun loadPhotosById_ItemsAvailable_showItems() {

        //set current page as one
        mImageViewerPresenter.page = 1

        val items = dummyList.subList(0, FlickrUtils.DEFAULT_PAGE_SIZE)
        given(mMockAppRepository?.getCachedPhotoItems())
                .willReturn(Flowable.just(items))

        val itemIndex = 2
        val photoId = items[itemIndex].id
        mImageViewerPresenter.loadPhotoFromPhotoId(FlickrUtils.API_KEY, FlickrUtils.DEFAULT_QUERY, photoId)

        verify(mImageViewerMvpView)?.showProgressDialog()
        verify(mImageViewerMvpView)?.dismissProgressDialog()
        verify(mImageViewerMvpView)?.initViewPager(items)
        verify(mImageViewerMvpView)?.updateCurrentPage(itemIndex)

    }

    @Test
    fun loadPhotosById_ItemsUnavailable_showEmpty() {
        given(mMockAppRepository?.getCachedPhotoItems())
                .willReturn(Flowable.just(listOf()))

        val itemIndex = 2
        val photoId = dummyList[itemIndex].id
        mImageViewerPresenter.loadPhotoFromPhotoId(photoId, FlickrUtils.API_KEY, FlickrUtils.DEFAULT_QUERY)

        verify(mImageViewerMvpView)?.showProgressDialog()
        verify(mImageViewerMvpView)?.dismissProgressDialog()
        verify(mImageViewerMvpView)?.showEmptyUI()
    }

    @Test
    fun loadMorePhotos_ItemsAvailable_refreshItems() {

        //make current page as 1
        mImageViewerPresenter.page = 1
        mImageViewerPresenter.query = FlickrUtils.DEFAULT_QUERY

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
        mImageViewerPresenter.loadNewPhotos(FlickrUtils.API_KEY, FlickrUtils.DEFAULT_PAGE_SIZE)
        assertEquals(mImageViewerPresenter.page,2)

        //test load page two
        mImageViewerPresenter.loadNewPhotos(FlickrUtils.API_KEY, 2 * FlickrUtils.DEFAULT_PAGE_SIZE)
        assertEquals(mImageViewerPresenter.page,3)

        //test load page three
        given(mMockAppRepository?.getPaginationStatus()).willReturn(true)
        mImageViewerPresenter.loadNewPhotos(FlickrUtils.API_KEY, 3 * FlickrUtils.DEFAULT_PAGE_SIZE)
        assertEquals(mImageViewerPresenter.page,3)

        verify(mImageViewerMvpView, times(2))?.showProgressBar()
        verify(mImageViewerMvpView, times(2))?.hideProgressBar()
        verify(mImageViewerMvpView, times(2))?.refreshViewPager()
    }

    @Test
    fun loadMorePhotos_ItemsUnavailable_notRefreshViewPager() {

        //set current page to 3
        mImageViewerPresenter.page = 3
        mImageViewerPresenter.mPhotoList = dummyList.toMutableList()

        //test for view pager page end not reached
        mImageViewerPresenter.isLoading = false
        given(mMockAppRepository?.getPaginationStatus()).willReturn(false)
        mImageViewerPresenter.loadNewPhotos(FlickrUtils.API_KEY, FlickrUtils.DEFAULT_PAGE_SIZE)

        //test when view pager is not at end
        mImageViewerPresenter.isLoading = false
        given(mMockAppRepository?.getPaginationStatus()).willReturn(true)
        mImageViewerPresenter.loadNewPhotos(FlickrUtils.API_KEY, dummyList.size)

        //test when view pager is not at end
        mImageViewerPresenter.isLoading = true
        given(mMockAppRepository?.getPaginationStatus()).willReturn(false)
        mImageViewerPresenter.loadNewPhotos(FlickrUtils.API_KEY, dummyList.size)

        verify(mImageViewerMvpView, never())?.showProgressDialog()
        verify(mImageViewerMvpView, never())?.dismissProgressDialog()
        verify(mImageViewerMvpView, never())?.refreshViewPager()
    }

}