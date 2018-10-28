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
package com.github.flickrsample.data.source.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import com.github.flickrsample.data.models.local.PhotoItem

import io.reactivex.Flowable

/**
 * PhotoItem Data access object for connection to SQLite DB using Room
 *
 * Created by gk
 */

@Dao
interface PhotoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleItem(photoItem: PhotoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultipleItem(photoItemList: List<PhotoItem>)

    @Query("SELECT * FROM photos WHERE id = :itemId")
    fun fetchItemByItemId(itemId: Int): Flowable<PhotoItem>

    @Query("SELECT * FROM photos")
    fun fetchItems(): Flowable<List<PhotoItem>>

    @Update
    fun updateItem(photoItem: PhotoItem)

    @Delete
    fun deleteItem(photoItem: PhotoItem)

}