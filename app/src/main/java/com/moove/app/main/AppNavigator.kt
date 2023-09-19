package com.moove.app.main

import android.content.Context
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope

class AppNavigator(
    private val navController: NavController,
    private val coroutineScope: CoroutineScope,
    private val context: Context,
)