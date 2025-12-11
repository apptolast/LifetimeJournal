package com.apptolast.lifetimejournal.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.apptolast.lifetimejournal.database.entities.StoryBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryBookDao {

    @Query("SELECT * FROM story_book ORDER BY createdAt DESC")
    fun getAllStoryBooks(): Flow<List<StoryBookEntity>>

    @Query("SELECT * FROM story_book WHERE id = :id")
    fun getStoryBookById(id: String): Flow<StoryBookEntity?>

    @Query("SELECT * FROM story_book WHERE journalId = :journalId ORDER BY createdAt DESC")
    fun getStoryBooksByJournalId(journalId: String): Flow<List<StoryBookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoryBook(storyBook: StoryBookEntity)

    @Update
    suspend fun updateStoryBook(storyBook: StoryBookEntity)

    @Delete
    suspend fun deleteStoryBook(storyBook: StoryBookEntity)
}
