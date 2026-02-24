package com.apptolast.lifetimejournal

///////////////////////////////////////////////////////////////////////////
// SERVER
///////////////////////////////////////////////////////////////////////////
const val SERVER_PORT = 8080

///////////////////////////////////////////////////////////////////////////
// DATABASE
///////////////////////////////////////////////////////////////////////////
const val DATABASE_NAME = "lifetime_journal.db"

// Tables
const val TABLE_JOURNAL = "journal"
const val TABLE_JOURNAL_ENTRY = "journal_entry"
const val TABLE_STORY_BOOK = "story_book"

// Columns
const val COLUMN_ID = "id"
const val COLUMN_TITLE = "title"
const val COLUMN_DESCRIPTION = "description"
const val COLUMN_DATE = "date"
const val COLUMN_JOURNAL_ID = "journalId"
const val COLUMN_LAST_SYNC_TIMESTAMP = "last_sync_timestamp"
