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
package com.jandyho.healthconnectsample.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.records.ExerciseSessionRecord
import com.jandyho.healthconnectsample.presentation.theme.HealthConnectTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * Displays summary information about the [ExerciseSessionRecord]
 */

@Composable
fun StepSessionInfoColumn(
    time: ZonedDateTime,
    uid: String,
    steps: String,
    onClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.clickable {
            onClick(uid)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val formatter = DateTimeFormatter.ofPattern("M월d일")
            Text(
                color = MaterialTheme.colors.primary,
                text = time.format(formatter),
                style = MaterialTheme.typography.caption
            )
            Text(String.format(" %,d steps", steps.toInt()))
        }
    }
}

@Preview
@Composable
fun StepSessionInfoColumnPreview() {
    HealthConnectTheme {
        StepSessionInfoColumn(
            ZonedDateTime.now(),
            UUID.randomUUID().toString(),
            "3000"
        )
    }
}
