package com.raywenderlich.android.librarian.database.dao

import androidx.room.*
import com.raywenderlich.android.librarian.model.ReadingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingListDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addReadingList(readingList: ReadingList)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  suspend fun updateReadingList(readingList: ReadingList)

  @Query("SELECT * FROM readinglist")
  suspend fun getReadingLists(): List<ReadingList>

  @Query("SELECT * FROM readinglist")
  fun getReadingListsFlow(): Flow<List<ReadingList>>

  @Delete
  suspend fun removeReadingList(readingList: ReadingList)

  @Delete
  suspend fun removeReadingLists(readingLists: List<ReadingList>)

  @Query("SELECT * FROM readinglist WHERE id = :id")
  suspend fun getReadingListById(id: String): ReadingList

  @Query("SELECT * FROM readinglist WHERE id = :id")
  fun getReadingListByIdFlow(id: String): Flow<ReadingList>
}