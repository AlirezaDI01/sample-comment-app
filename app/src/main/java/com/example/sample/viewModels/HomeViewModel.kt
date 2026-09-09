package com.example.sample.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sample.data.model.Comment
import com.example.sample.data.model.CommentRequest
import com.example.sample.data.remote.NetworkResult
import com.example.sample.domain.LoginState
import com.example.sample.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context : Context,
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState.LOADING)
    val loginState = _loginState
        .onStart { autoLogin() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            LoginState.LOADING
        )


    fun autoLogin() {
        viewModelScope.launch {
            when(val result = apiRepository.getMe()){
                is NetworkResult.Success -> {
                    _loginState.emit(LoginState.LOGGED_IN)
                }

                is NetworkResult.Error -> {
                    _loginState.emit(LoginState.NOT_LOGGED_IN)
                    Toast.makeText(context,result.message, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    _loginState.emit(LoginState.LOADING)
                }
            }


        }
    }



    private val _comments = MutableStateFlow<NetworkResult<List<Comment>>?>(null)
    val comments = _comments
        .onStart {
            getComments()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            null
        )

    fun getComments() {
        viewModelScope.launch {
            _comments.emit(NetworkResult.Loading())
            val comments = apiRepository.getComments()
            _comments.emit(comments)
        }
    }


    fun postComment(comment: String, parentId: Int?) {

        if (comment.isBlank()) {
            return
        }

        viewModelScope.launch {
            val commentRequest = CommentRequest(
                content = comment,
                parent_id = parentId
            )

            val result = apiRepository.postComment(commentRequest = commentRequest)


            if (result is NetworkResult.Success) {
                _comments.emit(apiRepository.getComments())
            }

        }
    }


}