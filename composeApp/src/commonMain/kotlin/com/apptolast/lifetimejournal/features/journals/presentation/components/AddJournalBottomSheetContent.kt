package com.apptolast.lifetimejournal.features.journals.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.resources.Res
import com.apptolast.lifetimejournal.resources.create_journal_button
import com.apptolast.lifetimejournal.resources.create_journal_description
import com.apptolast.lifetimejournal.resources.create_journal_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddJournalBottomSheetContent(
    modifier: Modifier = Modifier,
    onCreateJournal: (String, String) -> Unit = { _, _ -> },
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "New Diary",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = {
                Text(
                    text = stringResource(Res.string.create_journal_title),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = {
                Text(
                    text = stringResource(Res.string.create_journal_description),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = MaterialTheme.shapes.small,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onCreateJournal(title, description) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Text(
                text = stringResource(Res.string.create_journal_button),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Preview
@Composable
fun AddJournalBottomSheetContentPreview() {
    LifetimeJournalTheme {
        AddJournalBottomSheetContent(modifier = Modifier.background(color = MaterialTheme.colorScheme.surface))
    }
}
