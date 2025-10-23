package com.example.catapi.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapi.data.model.CatImage
import com.example.catapi.data.repository.CatRepository
import com.example.catapi.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatListViewModel(
    private val repository: CatRepository = CatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<CatImage>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<CatImage>>> = _uiState

    init {
        fetchCats()
    }

    fun fetchCats() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.getCatImages()
            _uiState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}