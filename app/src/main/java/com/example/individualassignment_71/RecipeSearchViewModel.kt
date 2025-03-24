package com.example.individualassignment_71

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeSearchViewModel : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchState: StateFlow<SearchState> = _searchState

    fun fetchRecipes(search: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            try {
                val searchResponse = ApiClient.apiService.getRecipes(search)
                _searchState.value = SearchState.Success(searchResponse)
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class SearchState {
        object Initial : SearchState()
        object Loading : SearchState()
        data class Success(val searchResponse: SearchResponse) : SearchState()
        data class Error(val errorMessage: String) : SearchState()
    }
}