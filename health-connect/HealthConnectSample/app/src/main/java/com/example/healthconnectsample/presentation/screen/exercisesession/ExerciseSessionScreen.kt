/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.healthconnectsample.presentation.screen.exercisesession

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.ExerciseSessionRecord
import com.example.healthconnectsample.R
import com.example.healthconnectsample.data.ExerciseSession
import com.example.healthconnectsample.data.HealthConnectAppInfo
import com.example.healthconnectsample.data.StepSession
import com.example.healthconnectsample.presentation.component.ExerciseSessionRow
import com.example.healthconnectsample.presentation.component.StepSessionRow
import com.example.healthconnectsample.presentation.theme.HealthConnectTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Shows a list of [ExerciseSessionRecord]s from today.
 */
@Composable
fun ExerciseSessionScreen(
    permissions: Set<String>,
    permissionsGranted: Boolean,
    sessionsList: List<ExerciseSession>,
    stepsList: List<StepSession>,
    uiState: ExerciseSessionViewModel.UiState,
    onInsertClick: () -> Unit = {},
    onDetailsClick: (String) -> Unit = {},
    onDeleteClick: (String) -> Unit = {},
    onError: (Throwable?) -> Unit = {},
    onPermissionsResult: () -> Unit = {},
    onPermissionsLaunch: (Set<String>) -> Unit = {}
) {

    // Remember the last error ID, such that it is possible to avoid re-launching the error
    // notification for the same error when the screen is recomposed, or configuration changes etc.
    val errorId = rememberSaveable { mutableStateOf(UUID.randomUUID()) }

    LaunchedEffect(uiState) {
        // If the initial data load has not taken place, attempt to load the data.
        if (uiState is ExerciseSessionViewModel.UiState.Uninitialized) {
            onPermissionsResult()
        }

        // The [ExerciseSessionViewModel.UiState] provides details of whether the last action was a
        // success or resulted in an error. Where an error occurred, for example in reading and
        // writing to Health Connect, the user is notified, and where the error is one that can be
        // recovered from, an attempt to do so is made.
        if (uiState is ExerciseSessionViewModel.UiState.Error && errorId.value != uiState.uuid) {
            onError(uiState.exception)
            errorId.value = uiState.uuid
        }
    }

    if (uiState != ExerciseSessionViewModel.UiState.Uninitialized) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!permissionsGranted) {
                item {
                    Button(
                        onClick = {
                            onPermissionsLaunch(permissions)
                        }
                    ) {
                        Text(text = stringResource(R.string.permissions_button_label))
                    }
                }
            } else if (sessionsList.isNotEmpty()) {
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(4.dp),
                        onClick = {
                            onInsertClick()
                        }
                    ) {
                        Text(stringResource(id = R.string.insert_exercise_session))
                    }
                }

                items(sessionsList) { session ->
                    val appInfo = session.sourceAppInfo
                    ExerciseSessionRow(
                        start = session.startTime,
                        end = session.endTime,
                        uid = session.id,
                        name = session.title ?: stringResource(R.string.no_title),
                        "0",
                        sourceAppName = appInfo?.appLabel ?: stringResource(R.string.unknown_app),
                        sourceAppIcon = appInfo?.icon,
                        onDeleteClick = { uid ->
                            onDeleteClick(uid)
                        },
                        onDetailsClick = { uid ->
                            onDetailsClick(uid)
                        }
                    )
                }
            }
        }
        if (stepsList.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp)
            ) {
                val appInfo = stepsList.first().sourceAppInfo
                appInfo?.let {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(4.dp, 2.dp)
                                    .height(16.dp)
                                    .width(16.dp),
                                painter = rememberDrawablePainter(drawable = it.icon),
                                contentDescription = "App Icon"
                            )
                            Text(
                                text = it.appLabel,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val sum = stepsList.sumOf { it.count.toInt() }
                            Text(
                                text = String.format("%,d steps", sum),
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }

                items(items = stepsList) { steps ->
                    steps.sourceAppInfo
                    StepSessionRow(
                        time = steps.endTime,
                        uid = steps.id,
                        "Walking",
                        steps.count
                    ) { uid ->
                        onDetailsClick(uid)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ExerciseSessionScreenPreview() {
    val context = LocalContext.current
    HealthConnectTheme {
        val runningStartTime = ZonedDateTime.now()
        val runningEndTime = runningStartTime.plusMinutes(30)
        val walkingStartTime = ZonedDateTime.now().minusMinutes(120)
        val walkingEndTime = walkingStartTime.plusMinutes(30)

        val appInfo = HealthConnectAppInfo(
            packageName = "com.example.myfitnessapp",
            appLabel = "My Fitness App",
            icon = context.getDrawable(R.drawable.ic_launcher_foreground)!!
        )

        ExerciseSessionScreen(
            permissions = setOf(),
            permissionsGranted = true,
            sessionsList = listOf(
//                ExerciseSession(
//                    title = "Running",
//                    startTime = runningStartTime,
//                    endTime = runningEndTime,
//                    id = UUID.randomUUID().toString(),
//                    sourceAppInfo = appInfo
//                ),
//                ExerciseSession(
//                    title = "Running",
//                    startTime = walkingStartTime,
//                    endTime = walkingEndTime,
//                    id = UUID.randomUUID().toString(),
//                    sourceAppInfo = appInfo
//                )
            ),
            uiState = ExerciseSessionViewModel.UiState.Done,
            stepsList = listOf(
                StepSession(
                    title = "Walking",
                    startTime = runningStartTime,
                    endTime = runningEndTime,
                    id = UUID.randomUUID().toString(),
                    count = "3000",
                    sourceAppInfo = appInfo
                ),
                StepSession(
                    title = "Walking",
                    startTime = walkingStartTime,
                    endTime = walkingEndTime,
                    id = UUID.randomUUID().toString(),
                    count = "5000",
                    sourceAppInfo = appInfo
                )
            )
        )
    }
}
