package com.apptolast.lifetimejournal.features.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import coil3.compose.AsyncImage
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.data.datamodel.User
import com.apptolast.lifetimejournal.features.settings.data.SettingState
import com.apptolast.lifetimejournal.resources.Res
import com.apptolast.lifetimejournal.resources.settings_change_name
import com.apptolast.lifetimejournal.resources.settings_change_password
import com.apptolast.lifetimejournal.resources.settings_delete_account
import com.apptolast.lifetimejournal.resources.settings_logout
import com.apptolast.lifetimejournal.resources.settings_manage_subscription
import com.apptolast.lifetimejournal.resources.settings_privacy_policy
import com.apptolast.lifetimejournal.resources.settings_section_account
import com.apptolast.lifetimejournal.resources.settings_section_support
import com.apptolast.lifetimejournal.resources.settings_support_developer
import com.apptolast.lifetimejournal.resources.settings_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingScreenRoot(
    viewModel: SettingViewModel = viewModel { SettingViewModel() },
    navigateTo: (Destination, NavOptions?) -> Unit = { _, _ -> },
    navigateToLogin: () -> Unit = {},
    onBack: () -> Unit = {},
    onPrivacyPolicy: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.user) {
        if (state.user?.isLoggedIn == false) {
            navigateToLogin()
        }
    }

    SettingScreen(
        state = state,
        onBack = onBack,
        signOut = viewModel::signOut,
        deleteAccount = viewModel::deleteAccount,
        onPrivacyPolicy = onPrivacyPolicy,
    )
}

@Composable
fun SettingScreen(
    state: SettingState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    signOut: () -> Unit = {},
    deleteAccount: () -> Unit = {},
    onChangeName: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onManageSubscription: () -> Unit = {},
    onPrivacyPolicy: () -> Unit = {},
    onSupportDeveloper: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            SettingsTopBar(
                title = stringResource(Res.string.settings_title),
                onBack = onBack,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        SettingContent(
            user = state.user,
            modifier = Modifier.padding(paddingValues),
            signOut = signOut,
            deleteAccount = deleteAccount,
            onChangeName = onChangeName,
            onChangePassword = onChangePassword,
            onManageSubscription = onManageSubscription,
            onPrivacyPolicy = onPrivacyPolicy,
            onSupportDeveloper = onSupportDeveloper,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier,
    )
}

@Composable
fun SettingContent(
    user: User?,
    modifier: Modifier = Modifier,
    signOut: () -> Unit = {},
    deleteAccount: () -> Unit = {},
    onChangeName: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onManageSubscription: () -> Unit = {},
    onPrivacyPolicy: () -> Unit = {},
    onSupportDeveloper: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Profile section
        ProfileHeader(user = user)

        Spacer(modifier = Modifier.height(32.dp))

        // Account section
        SettingsSection(title = stringResource(Res.string.settings_section_account)) {
            SettingsItem(
                icon = Icons.Outlined.Person,
                title = stringResource(Res.string.settings_change_name),
                onClick = onChangeName,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHigh)
            SettingsItem(
                icon = Icons.Outlined.Lock,
                title = stringResource(Res.string.settings_change_password),
                onClick = onChangePassword,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHigh)
            SettingsItem(
                icon = Icons.Outlined.WorkspacePremium,
                title = stringResource(Res.string.settings_manage_subscription),
                onClick = onManageSubscription,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Support section
        SettingsSection(title = stringResource(Res.string.settings_section_support)) {
            SettingsItem(
                icon = Icons.Outlined.PrivacyTip,
                title = stringResource(Res.string.settings_privacy_policy),
                onClick = onPrivacyPolicy,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHigh)
            SettingsItem(
                icon = Icons.Outlined.FavoriteBorder,
                title = stringResource(Res.string.settings_support_developer),
                onClick = onSupportDeveloper,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout button
        Button(
            onClick = signOut,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            shape = MaterialTheme.shapes.large,
        ) {
            Text(
                text = stringResource(Res.string.settings_logout),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delete account button
        TextButton(
            onClick = deleteAccount,
            modifier = Modifier.padding(bottom = 32.dp),
        ) {
            Text(
                text = stringResource(Res.string.settings_delete_account),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    user: User?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            // Lógica para mostrar Imagen o Placeholder (ambos con el estilo nuevo)
            val imageModifier = Modifier
                .size(100.dp)
                .shadow(4.dp, CircleShape) // Elevación de 4dp
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = CircleShape,
                )
                .border(4.dp, Color.White, CircleShape) // Borde blanco de 2dp

            if (user?.photoUrl?.isNotBlank() == true) {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = null,
                    modifier = imageModifier.padding(4.dp),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier = imageModifier.background(
                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                        shape = CircleShape,
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person2,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        modifier = Modifier.size(58.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = user?.name ?: "",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = user?.email ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
        )

        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.large,
                ),
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Preview
@Composable
private fun SettingContentPreview(modifier: Modifier = Modifier) {
    LifetimeJournalTheme {
        SettingScreen(
            state = SettingState(
                isLoading = false,
                user = User(
                    id = "1",
                    name = "Jessica Miller",
                    email = "jessica@email.com",
                    photoUrl = "",
                    phoneNumber = "",
                    isEmailVerified = true,
                    isLoggedIn = true,
                ),
            ),
            modifier = modifier.background(color = MaterialTheme.colorScheme.background),
        )
    }
}
