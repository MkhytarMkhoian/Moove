package com.moove.shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moove.core.kotlin.coroutines.executeUseCase

suspend inline fun <R> ViewModel.executeUseCase(block: () -> R): Result<R> =
    viewModelScope.executeUseCase(block)
