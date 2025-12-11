package com.apptolast.lifetimejournal.data.repositories

import com.apptolast.lifetimejournal.data.datamodel.StoryBook
import com.apptolast.lifetimejournal.database.dao.StoryBookDao
import com.apptolast.lifetimejournal.database.entities.toStoryBook
import com.apptolast.lifetimejournal.database.entities.toStoryBookEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoryBookRepository(
    private val storyBookDao: StoryBookDao,
    private val aiService: AIService,
) {
    fun getAllStoryBooks(): Flow<List<StoryBook>> {
        return storyBookDao.getAllStoryBooks().map { list ->
            list.map { it.toStoryBook() }
        }
    }

    fun getStoryBookById(id: String): Flow<StoryBook?> {
        return storyBookDao.getStoryBookById(id).map { it?.toStoryBook() }
    }

    fun getStoryBooksByJournalId(journalId: String): Flow<List<StoryBook>> {
        return storyBookDao.getStoryBooksByJournalId(journalId).map { list ->
            list.map { it.toStoryBook() }
        }
    }

    suspend fun createStoryBook(storyBook: StoryBook) {
        storyBookDao.insertStoryBook(storyBook.toStoryBookEntity())
    }

    suspend fun updateStoryBook(storyBook: StoryBook) {
        storyBookDao.updateStoryBook(storyBook.toStoryBookEntity())
    }

    suspend fun deleteStoryBook(storyBook: StoryBook) {
        storyBookDao.deleteStoryBook(storyBook.toStoryBookEntity())
    }

    suspend fun generateStoryText(
        entries: List<com.apptolast.lifetimejournal.data.datamodel.JournalEntry>,
        storyStyle: String,
    ): Result<String> {
        return aiService.generateStoryBook(entries, storyStyle)
    }

    suspend fun modifyStoryText(
        currentText: String,
        modificationPrompt: String,
    ): Result<String> {
        return aiService.modifyStory(currentText, modificationPrompt)
    }
}
