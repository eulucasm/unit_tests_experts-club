package com.expertsclub.expertsauthentication.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.expertsclub.expertsauthentication.ExpertsApp
import com.expertsclub.expertsauthentication.base.AppCoroutinesDispatchers
import com.expertsclub.expertsauthentication.base.ResultStatus
import com.expertsclub.expertsauthentication.data.repository.UserRepositoryImpl
import com.expertsclub.expertsauthentication.domain.usecase.LoginUseCase
import com.expertsclub.expertsauthentication.framework.preferences.datasource.PreferencesDataSourceImpl
import com.expertsclub.expertsauthentication.framework.preferences.manager.LocalPersistenceManagerImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _loginStateData = MutableLiveData<LoginState>()
    val loginStateData: LiveData<LoginState> = _loginStateData

    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            loginUseCase.invoke(LoginUseCase.LoginParams(email, password))
                .watchStatus()
        } else _loginStateData.value = LoginState.ShowError
    }

    private fun Flow<ResultStatus<Unit>>.watchStatus() = viewModelScope.launch {
        collect { status ->
            _loginStateData.value = when (status) {
                ResultStatus.Loading -> LoginState.ShowLoading
                is ResultStatus.Success -> LoginState.LoginSuccess
                is ResultStatus.Error -> LoginState.ShowError
            }
        }
    }

    sealed class LoginState {
        object ShowLoading : LoginState()
        object LoginSuccess : LoginState()
        object ShowError : LoginState()
    }

    class LoginViewModelFactory(applicationContext: Context) :
        ViewModelProvider.Factory {

        private val expertsApp = applicationContext as ExpertsApp

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                val localDataStore = PreferencesDataSourceImpl(expertsApp.localDataStore)
                val localPersistenceManager = LocalPersistenceManagerImpl(localDataStore)
                val dispatchers = AppCoroutinesDispatchers(
                    io = Dispatchers.IO,
                    computation = Dispatchers.Default,
                    main = Dispatchers.Main
                )
                val userRepository = UserRepositoryImpl(localPersistenceManager)

                val loginUseCase = LoginUseCase(userRepository, dispatchers.io)

                return LoginViewModel(loginUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}