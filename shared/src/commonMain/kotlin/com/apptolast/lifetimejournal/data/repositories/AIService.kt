package com.apptolast.lifetimejournal.data.repositories

import com.apptolast.lifetimejournal.data.datamodel.JournalEntry

/**
 * Interface for AI text generation service.
 * Prepared to integrate with Gemini, OpenAI, or other AI providers.
 */
interface AIService {
    /**
     * Generates a story book text based on journal entries.
     *
     * @param entries List of journal entries to use as source material
     * @param storyStyle The style description for the story (e.g., "like a children's fairy tale")
     * @return Generated story text
     */
    suspend fun generateStoryBook(
        entries: List<JournalEntry>,
        storyStyle: String,
    ): Result<String>

    /**
     * Modifies an existing story based on user prompt.
     *
     * @param currentText The current story text
     * @param modificationPrompt The user's modification request
     * @return Modified story text
     */
    suspend fun modifyStory(
        currentText: String,
        modificationPrompt: String,
    ): Result<String>
}

/**
 * Placeholder implementation that returns mock data.
 * Replace with actual Gemini implementation when API key is available.
 *
 * To integrate Gemini:
 * 1. Add dependency: implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
 * 2. Create GeminiAIService implementing AIService
 * 3. Use GenerativeModel with your API key
 */
class PlaceholderAIService : AIService {

    override suspend fun generateStoryBook(
        entries: List<JournalEntry>,
        storyStyle: String,
    ): Result<String> {
        // Simulate API delay
        kotlinx.coroutines.delay(2000)

        val entriesSummary = entries.joinToString("\n") { "- ${it.title}: ${it.description}" }

        return Result.success(
            """
            |Once upon a time, in a summer filled with golden sunshine and the sweet scent of saltwater, the Miller family embarked on an unforgettable adventure to the shore. The days were long and lazy, marked by the rhythm of the waves and the laughter that echoed across the sand.
            |
            |Little Leo, with his bright, curious eyes, experienced the ocean for the very first time. He giggled with delight as the cool water tickled his toes, and he spent hours building magnificent sandcastles, each one guarded by a collection of seashells he'd carefully chosen. Every sunset painted the sky in brilliant shades of pink and orange, a perfect masterpiece to end each perfect day.
            |
            |These were the moments that wove themselves into the fabric of their family story—simple, beautiful, and filled with love. A chapter of pure happiness they would cherish forever.
            |
            |---
            |Based on your entries:
            |$entriesSummary
            |
            |Style requested: $storyStyle
            """.trimMargin()
        )
    }

    override suspend fun modifyStory(
        currentText: String,
        modificationPrompt: String,
    ): Result<String> {
        // Simulate API delay
        kotlinx.coroutines.delay(1500)

        return Result.success(
            """
            |$currentText
            |
            |---
            |[Modified based on: "$modificationPrompt"]
            |
            |The story continues with even more wonder and excitement, as requested by you. The adventures grew bigger, the laughs grew louder, and the memories became even more precious.
            """.trimMargin()
        )
    }
}
