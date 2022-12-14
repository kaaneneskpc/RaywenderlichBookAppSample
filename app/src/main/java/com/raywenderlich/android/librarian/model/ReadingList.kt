package com.raywenderlich.android.librarian.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
class ReadingList(
  @PrimaryKey
  val id: String = UUID.randomUUID().toString(),
  val name: String,
  val bookIds: List<String>
) : Parcelable