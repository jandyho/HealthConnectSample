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
package com.jandyho.healthconnectsample.presentation

import android.annotation.SuppressLint
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jandyho.healthconnectsample.R
import com.jandyho.healthconnectsample.presentation.navigation.Drawer
import com.jandyho.healthconnectsample.presentation.navigation.HealthConnectNavigation
import com.jandyho.healthconnectsample.presentation.navigation.Screen
import com.jandyho.healthconnectsample.presentation.theme.HealthConnectTheme
import kotlinx.coroutines.launch

const val TAG = "Health Connect sample"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HealthConnectApp(healthConnectManager: com.jandyho.healthconnectsample.data.HealthConnectManager) {
    HealthConnectTheme {
        val scaffoldState = rememberScaffoldState()
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val availability by healthConnectManager.availability

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {
                        val titleId = when (currentRoute) {
                            Screen.ExerciseSessions.route -> Screen.ExerciseSessions.titleId
                            Screen.StepSessions.route -> Screen.StepSessions.titleId
                            Screen.SleepSessions.route -> Screen.SleepSessions.titleId
                            Screen.InputReadings.route -> Screen.InputReadings.titleId
                            Screen.DifferentialChanges.route -> Screen.DifferentialChanges.titleId
                            else -> R.string.app_name
                        }
                        Text(stringResource(titleId))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (availability == com.jandyho.healthconnectsample.data.HealthConnectAvailability.INSTALLED) {
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                stringResource(id = R.string.menu)
                            )
                        }
                    }
                )
            },
            drawerContent = {
                if (availability == com.jandyho.healthconnectsample.data.HealthConnectAvailability.INSTALLED) {
                    Drawer(
                        scope = scope,
                        scaffoldState = scaffoldState,
                        navController = navController
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(it) { data -> Snackbar(snackbarData = data) }
            }
        ) {
            HealthConnectNavigation(
                healthConnectManager = healthConnectManager,
                navController = navController,
                scaffoldState = scaffoldState
            )
        }
    }
}
