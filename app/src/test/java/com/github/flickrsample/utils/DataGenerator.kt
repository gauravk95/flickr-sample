package com.github.flickrsample.utils

import com.github.flickrsample.data.models.local.PhotoItem
import java.util.*

object DataGenerator {

    fun getDummyGalleryList(number: Int): List<PhotoItem> {
        val list: MutableList<PhotoItem> = mutableListOf()

        for (i in 1..number) {
            list.add(
                    PhotoItem(UUID.randomUUID().toString(),
                            generateRandomString(30),
                            generateRandomString(15),
                           generateRandomString(25),
                            Random().nextInt(2),
                            generateRandomString(60),
                            1,
                            generateRandomString(20),
                            generateRandomString(20),
                            generateRandomString(20)))
        }

        return list
    }

    private fun generateRandomString(length: Int): String {
        val generator = Random()
        val randomStringBuilder = StringBuilder()
        val randomLength = generator.nextInt(length)
        var tempChar: Char
        for (i in 0 until randomLength) {
            tempChar = (generator.nextInt(96) + 32).toChar()
            randomStringBuilder.append(tempChar)
        }
        return randomStringBuilder.toString()
    }
}