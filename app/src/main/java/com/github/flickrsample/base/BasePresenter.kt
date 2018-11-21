/*
    Copyright 2018 Gaurav Kumar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.github.flickrsample.base

import com.github.flickrsample.R
import com.github.flickrsample.data.source.repository.AppRepository
import com.github.flickrsample.data.source.network.NetworkError
import com.github.flickrsample.utils.rx.SchedulerProvider

import io.reactivex.disposables.CompositeDisposable

/**
 * Base Presenter class that abstracts some of the repetitive work of a Presenter
 * Implements the basic functions as described in [BaseContract.Presenter]
 *
 * Created by gk
 */

abstract class BasePresenter<V : BaseContract.View<*>>(
        protected val dataSource: AppRepository,
        protected val schedulerProvider: SchedulerProvider,
        protected val compositeDisposable: CompositeDisposable) :
        BaseContract.Presenter<V> {

    protected var view: V? = null

    protected val isViewAttached: Boolean
        get() = view != null

    override fun onAttach(view: V) {
        this.view = view
    }

    override fun onDetach() {
        compositeDisposable.clear()
        view = null
    }

    override fun handleApiError(throwable: Throwable?) {

        if (throwable == null) {
            view?.onError(R.string.default_error_message)
            return
        }

        val networkError = NetworkError(throwable)
        val errorMsg = networkError.appErrorMessage
        if (!errorMsg.isEmpty())
            view?.onError(errorMsg)
        else
            view?.onError(R.string.default_error_message)

    }

}
