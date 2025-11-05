/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jandyho.healthconnectsample.presentation

import android.app.Application
import androidx.health.connect.client.HealthConnectClient

class BaseApplication : Application() {
    val healthConnectManager by lazy {
        com.jandyho.healthconnectsample.data.HealthConnectManager(HealthConnectClient.getOrCreate(this))
            .also { it.checkAvailability(this) }
    }
    val healthConnectAppsManager by lazy {
        com.jandyho.healthconnectsample.data.HealthConnectAppsManager(this)
    }
}
