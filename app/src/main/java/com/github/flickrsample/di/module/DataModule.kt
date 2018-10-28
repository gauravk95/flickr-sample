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
package com.github.flickrsample.di.module

import com.github.flickrsample.data.source.db.AppDatabase
import com.github.flickrsample.data.source.db.AppDbOpenHelper
import com.github.flickrsample.data.source.repository.local.AppLocalDataSource
import com.github.flickrsample.data.source.prefs.AppPreferencesHelper
import com.github.flickrsample.data.source.prefs.PreferencesHelper
import com.github.flickrsample.data.source.repository.remote.AppRemoteDataSource
import com.github.flickrsample.data.source.repository.AppDataRepository
import com.github.flickrsample.data.source.repository.AppDataSource
import com.github.flickrsample.data.source.repository.AppRepository
import com.github.flickrsample.di.DatabaseInfo
import com.github.flickrsample.di.Local
import com.github.flickrsample.di.PreferenceInfo
import com.github.flickrsample.di.Remote
import com.github.flickrsample.utils.AppConstants

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Modules related to data and repository
 *
 * Created by gk.
 */

@Module
class DataModule {

    @Provides
    @Singleton
    @DatabaseInfo
    internal fun provideDatabaseName(): String {
        return AppConstants.DB_NAME
    }

    @Provides
    @Singleton
    @PreferenceInfo
    internal fun providePreferenceName(): String {
        return AppConstants.PREF_NAME
    }

    @Provides
    @Singleton
    @Local
    internal fun provideAppLocalDataSource(appLocalDataSource: AppLocalDataSource): AppDataSource {
        return appLocalDataSource
    }

    @Provides
    @Singleton
    @Remote
    internal fun provideAppRemoteDataSource(appRemoteDataSource: AppRemoteDataSource): AppDataSource {
        return appRemoteDataSource
    }

    @Provides
    @Singleton
    internal fun provideAppRepository(dataRepository: AppDataRepository): AppRepository {
        return dataRepository
    }

    @Provides
    @Singleton
    internal fun provideAppDb(appDbOpenHelper: AppDbOpenHelper): AppDatabase {
        return appDbOpenHelper.database
    }

    @Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }

}
