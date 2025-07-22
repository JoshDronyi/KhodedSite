package com.probro.khoded.data.state

import androidx.compose.runtime.*
import com.probro.khoded.data.api.*
import com.probro.khoded.data.models.*
import kotlinx.coroutines.*

/**
 * State Management System
 * 
 * Comprehensive state management with:
 * - Form state management with validation
 * - Loading and error states
 * - Optimistic updates
 * - State persistence
 * - Undo/Redo functionality
 */

// =============================================================================
// FORM STATE MANAGEMENT
// =============================================================================

@Composable
fun <T> rememberFormState(
    initialData: T,
    validate: (T) -> ValidationResult<T> = { ValidationResult.Success(it) }
): FormStateManager<T> {
    return remember { FormStateManager(initialData, validate) }
}

class FormStateManager<T>(
    private val initialData: T,
    private val validate: (T) -> ValidationResult<T>
) {
    private val _state = mutableStateOf(
        FormState(
            data = initialData,
            fields = emptyMap(),
            isSubmitting = false,
            submitError = null,
            submitSuccess = false
        )
    )
    
    val state: State<FormState<T>> = _state
    
    fun updateData(newData: T) {
        val validationResult = validate(newData)
        val fields = when (validationResult) {
            is ValidationResult.Success -> emptyMap()
            is ValidationResult.Error -> validationResult.errors.associate { error ->
                error.field to FormFieldState(
                    value = getFieldValue(newData, error.field),
                    error = error.message,
                    touched = true
                )
            }
        }
        
        _state.value = _state.value.copy(
            data = newData,
            fields = fields,
            submitError = null,
            submitSuccess = false
        )
    }
    
    fun updateField(fieldName: String, value: Any?, touched: Boolean = true) {
        val currentFields = _state.value.fields.toMutableMap()
        val currentField = currentFields[fieldName] ?: FormFieldState(value = value)
        
        // Validate single field
        val error = validateSingleField(fieldName, value)
        
        currentFields[fieldName] = currentField.copy(
            value = value,
            error = error,
            touched = touched
        )
        
        _state.value = _state.value.copy(
            fields = currentFields,
            submitError = null
        )
    }
    
    suspend fun submit(onSubmit: suspend (T) -> ApiResult<*>): Boolean {
        if (_state.value.isSubmitting) return false
        
        _state.value = _state.value.copy(isSubmitting = true, submitError = null)
        
        return try {
            val result = onSubmit(_state.value.data)
            when (result) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        submitSuccess = true,
                        submitError = null
                    )
                    true
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        submitError = result.error.getUserMessage()
                    )
                    false
                }
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isSubmitting = false,
                submitError = "An unexpected error occurred. Please try again."
            )
            false
        }
    }
    
    fun reset() {
        _state.value = FormState(
            data = initialData,
            fields = emptyMap(),
            isSubmitting = false,
            submitError = null,
            submitSuccess = false
        )
    }
    
    fun clearErrors() {
        val clearedFields = _state.value.fields.mapValues { (_, field) ->
            field.copy(error = null)
        }
        
        _state.value = _state.value.copy(
            fields = clearedFields,
            submitError = null
        )
    }
    
    private fun getFieldValue(data: T, fieldName: String): Any? {
        // Simple reflection-like field access
        // In a real implementation, this would use actual reflection or property delegates
        return when (data) {
            is ContactFormData -> when (fieldName) {
                "fullName" -> data.fullName
                "email" -> data.email
                "phoneNumber" -> data.phoneNumber
                "company" -> data.company
                "message" -> data.message
                else -> null
            }
            is NewsletterSubscription -> when (fieldName) {
                "email" -> data.email
                "firstName" -> data.firstName
                else -> null
            }
            else -> null
        }
    }
    
    private fun validateSingleField(fieldName: String, value: Any?): String? {
        val fullValidation = validate(_state.value.data)
        if (fullValidation is ValidationResult.Error) {
            return fullValidation.errors.find { it.field == fieldName }?.message
        }
        return null
    }
}

// =============================================================================
// ASYNC STATE MANAGEMENT
// =============================================================================

sealed class AsyncState<out T> {
    object Idle : AsyncState<Nothing>()
    object Loading : AsyncState<Nothing>()
    data class Success<T>(val data: T) : AsyncState<T>()
    data class Error(val error: String) : AsyncState<Nothing>()
    
    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isIdle: Boolean get() = this is Idle
    
    fun getOrNull(): T? = if (this is Success) data else null
    fun getErrorOrNull(): String? = if (this is Error) error else null
}

@Composable
fun <T> rememberAsyncState(
    initialState: AsyncState<T> = AsyncState.Idle
): MutableState<AsyncState<T>> {
    return remember { mutableStateOf(initialState) }
}

@Composable
fun <T> useAsyncOperation(
    operation: suspend () -> ApiResult<T>,
    dependencies: Array<Any> = emptyArray()
): AsyncState<T> {
    var state by remember { mutableStateOf<AsyncState<T>>(AsyncState.Idle) }
    
    LaunchedEffect(*dependencies) {
        state = AsyncState.Loading
        
        try {
            when (val result = operation()) {
                is ApiResult.Success -> {
                    state = AsyncState.Success(result.data)
                }
                is ApiResult.Error -> {
                    state = AsyncState.Error(result.error.getUserMessage())
                }
            }
        } catch (e: Exception) {
            state = AsyncState.Error("An unexpected error occurred")
        }
    }
    
    return state
}

// =============================================================================
// OPTIMISTIC UPDATES
// =============================================================================

class OptimisticStateManager<T> {
    private val _state = mutableStateOf<T?>(null)
    private val _pendingUpdates = mutableMapOf<String, T>()
    
    val state: State<T?> = _state
    
    fun getCurrentState(): T? = _state.value
    
    fun optimisticUpdate(id: String, newState: T) {
        _pendingUpdates[id] = _state.value ?: newState
        _state.value = newState
    }
    
    fun confirmUpdate(id: String) {
        _pendingUpdates.remove(id)
    }
    
    fun revertUpdate(id: String) {
        _pendingUpdates[id]?.let { previousState ->
            _state.value = previousState
            _pendingUpdates.remove(id)
        }
    }
    
    fun setState(newState: T) {
        _state.value = newState
    }
}

@Composable
fun <T> rememberOptimisticState(): OptimisticStateManager<T> {
    return remember { OptimisticStateManager<T>() }
}

// =============================================================================
// PAGINATION STATE
// =============================================================================

data class PaginationState(
    val currentPage: Int = 1,
    val pageSize: Int = 10,
    val totalItems: Int = 0,
    val totalPages: Int = 0,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val canLoadNext: Boolean get() = hasNext && !isLoading
    val canLoadPrev: Boolean get() = hasPrev && !isLoading
    val startItem: Int get() = (currentPage - 1) * pageSize + 1
    val endItem: Int get() = minOf(currentPage * pageSize, totalItems)
}

@Composable
fun rememberPaginationState(
    initialPageSize: Int = 10
): MutableState<PaginationState> {
    return remember { mutableStateOf(PaginationState(pageSize = initialPageSize)) }
}

// =============================================================================
// SEARCH STATE MANAGEMENT
// =============================================================================

data class SearchState<T>(
    val query: String = "",
    val results: List<T> = emptyList(),
    val isSearching: Boolean = false,
    val error: String? = null,
    val suggestions: List<String> = emptyList(),
    val totalResults: Int = 0,
    val searchTime: Long = 0
) {
    val hasQuery: Boolean get() = query.isNotBlank()
    val hasResults: Boolean get() = results.isNotEmpty()
    val isEmpty: Boolean get() = hasQuery && results.isEmpty() && !isSearching
}

@Composable
fun <T> rememberSearchState(): MutableState<SearchState<T>> {
    return remember { mutableStateOf(SearchState()) }
}

@Composable
fun <T> useDebounceSearch(
    query: String,
    searchFunction: suspend (String) -> ApiResult<List<T>>,
    debounceMs: Long = 300
): SearchState<T> {
    var searchState by remember { mutableStateOf(SearchState<T>()) }
    
    LaunchedEffect(query) {
        if (query.isBlank()) {
            searchState = SearchState()
            return@LaunchedEffect
        }
        
        searchState = searchState.copy(query = query, isSearching = true, error = null)
        
        delay(debounceMs)
        
        if (query == searchState.query) { // Check if query is still current
            val startTime = System.currentTimeMillis()
            
            try {
                when (val result = searchFunction(query)) {
                    is ApiResult.Success -> {
                        val searchTime = System.currentTimeMillis() - startTime
                        searchState = searchState.copy(
                            results = result.data,
                            isSearching = false,
                            error = null,
                            totalResults = result.data.size,
                            searchTime = searchTime
                        )
                    }
                    is ApiResult.Error -> {
                        searchState = searchState.copy(
                            results = emptyList(),
                            isSearching = false,
                            error = result.error.getUserMessage(),
                            totalResults = 0
                        )
                    }
                }
            } catch (e: Exception) {
                searchState = searchState.copy(
                    results = emptyList(),
                    isSearching = false,
                    error = "Search failed. Please try again.",
                    totalResults = 0
                )
            }
        }
    }
    
    return searchState
}

// =============================================================================
// UNDO/REDO STATE MANAGEMENT
// =============================================================================

class UndoRedoManager<T>(
    initialState: T,
    private val maxHistory: Int = 50
) {
    private val history = mutableListOf(initialState)
    private var currentIndex = 0
    
    private val _currentState = mutableStateOf(initialState)
    val currentState: State<T> = _currentState
    
    val canUndo: Boolean get() = currentIndex > 0
    val canRedo: Boolean get() = currentIndex < history.size - 1
    
    fun pushState(newState: T) {
        // Remove any states after current index (when creating new history after undo)
        if (currentIndex < history.size - 1) {
            history.subList(currentIndex + 1, history.size).clear()
        }
        
        // Add new state
        history.add(newState)
        currentIndex = history.size - 1
        
        // Limit history size
        if (history.size > maxHistory) {
            history.removeAt(0)
            currentIndex--
        }
        
        _currentState.value = newState
    }
    
    fun undo(): T? {
        return if (canUndo) {
            currentIndex--
            val state = history[currentIndex]
            _currentState.value = state
            state
        } else {
            null
        }
    }
    
    fun redo(): T? {
        return if (canRedo) {
            currentIndex++
            val state = history[currentIndex]
            _currentState.value = state
            state
        } else {
            null
        }
    }
    
    fun clear() {
        val current = _currentState.value
        history.clear()
        history.add(current)
        currentIndex = 0
    }
    
    fun getHistory(): List<T> = history.toList()
}

@Composable
fun <T> rememberUndoRedo(
    initialState: T,
    maxHistory: Int = 50
): UndoRedoManager<T> {
    return remember { UndoRedoManager(initialState, maxHistory) }
}

// =============================================================================
// PERSISTENT STATE
// =============================================================================

@Composable
fun <T> rememberPersistentState(
    key: String,
    initialValue: T,
    serializer: (T) -> String = { it.toString() },
    deserializer: (String) -> T? = { null }
): MutableState<T> {
    val state = remember(key) {
        val stored = try {
            kotlinx.browser.window.localStorage.getItem(key)?.let(deserializer)
        } catch (e: Exception) {
            null
        }
        mutableStateOf(stored ?: initialValue)
    }
    
    LaunchedEffect(state.value) {
        try {
            kotlinx.browser.window.localStorage.setItem(key, serializer(state.value))
        } catch (e: Exception) {
            console.warn("Failed to persist state for key: $key", e)
        }
    }
    
    return state
}