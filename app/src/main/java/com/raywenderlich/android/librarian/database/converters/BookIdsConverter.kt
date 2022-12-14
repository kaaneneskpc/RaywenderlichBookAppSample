package com.raywenderlich.android.librarian.database.converters

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.raywenderlich.android.librarian.App

class BookIdsConverter {

  @TypeConverter
  fun fromBookIds(list: List<String>): String? = App.gson.toJson(list)

  @TypeConverter
  fun toBookIds(json: String): List<String> {
    val listType = object : TypeToken<List<String>>() {}.type

    return try {
      App.gson.fromJson(json, listType)
    } catch (error: Throwable) {
      emptyList()
    }
  }
}