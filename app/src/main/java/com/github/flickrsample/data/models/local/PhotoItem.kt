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
package com.github.flickrsample.data.models.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Describes the Photo Item data to be modeled for local storage and regular use
 *
 * Note:
 * Uses both the Room specific annotations for Local DB
 * Uses Gson annotations for Json serialization for Network requests
 *
 * Created by gk
 */

@Entity(tableName = "photos")
data class PhotoItem(
        @field:PrimaryKey
        var id: String,
        var owner: String,
        var secret: String,
        var server: String,
        var farm: Int,
        var title: String?,
        var ispublic: Short,
        var url_n: String?,
        var width_n: String?,
        var height_n: String?)