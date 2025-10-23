package com.example.catapi.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapi.data.model.CatImage
import com.example.catapi.data.repository.CatRepository
import com.example.catapi.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: CatRepository = CatRepository
) : ViewModel() {

    // Get the "imageId" from the navigation route
    private val imageId: String = checkNotNull(savedStateHandle["imageId"])

    private val _uiState = MutableStateFlow<UiState<CatImage>>(UiState.Loading)
    val uiState: StateFlow<UiState<CatImage>> = _uiState

    init {
        fetchCatDetails()
    }

    private fun fetchCatDetails() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.getCatDetails(imageId)
            _uiState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}