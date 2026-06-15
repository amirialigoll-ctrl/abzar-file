package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.api.Content
import com.example.data.api.GenerateContentRequest
import com.example.data.api.GenerationConfig
import com.example.data.api.Part
import com.example.data.api.RetrofitClient
import com.example.data.api.ThinkingConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AIState(
    val isLoading: Boolean = false,
    val response: String = "",
    val error: String? = null
)

class AIAssistantViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AIState())
    val uiState: StateFlow<AIState> = _uiState.asStateFlow()

    fun askQuestion(prompt: String) {
        _uiState.update { it.copy(isLoading = true, response = it.response + "\n\nYou: $prompt\n\nAI: ", error = null) }
        viewModelScope.launch {
            try {
                val apiKey = BuildConfig.GEMINI_API_KEY
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    _uiState.update { it.copy(isLoading = false, error = "Invalid API Key") }
                    return@launch
                }
                
                val request = GenerateContentRequest(
                    contents = listOf(
                        Content(parts = listOf(Part(text = prompt)))
                    ),
                    generationConfig = GenerationConfig(
                        thinkingConfig = ThinkingConfig(thinkingLevel = "HIGH")
                    )
                )

                val response = RetrofitClient.service.generateContent(apiKey, request)
                val responseText = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No response generated."
                
                _uiState.update { it.copy(isLoading = false, response = it.response + responseText) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage, response = it.response + "[Error: ${e.message}]") }
            }
        }
    }
}
