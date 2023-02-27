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
package com.example.healthconnectsample.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.ExerciseSessionRecord
import com.example.healthconnectsample.presentation.theme.HealthConnectTheme
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Creates a row to represent an [ExerciseSessionRecord]
 */

@Composable
fun StepSessionRow(
    time: ZonedDateTime,
    uid: String,
    name: String,
    steps: String,
    onDetailsClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StepSessionInfoColumn(
            time = time,
            uid = uid,
            name = name,
            steps = steps,
            onClick = onDetailsClick
        )
    }
}

@Preview
@Composable
fun StepSessionRowPreview() {
    LocalContext.current
    HealthConnectTheme {
        StepSessionRow(
            ZonedDateTime.now(),
            UUID.randomUUID().toString(),
            "Walking",
            "3000"
        )
    }
}
