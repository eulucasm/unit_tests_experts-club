package com.expertsclub.expertsauthentication.domain.usecase

import com.expertsclub.expertsauthentication.base.ResultStatus
import com.expertsclub.expertsauthentication.base.ResultUseCase
import com.expertsclub.expertsauthentication.data.repository.UserRepository
import com.expertsclub.expertsauthentication.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class LoginUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) : ResultUseCase<LoginUseCase.LoginParams, Unit>() {

    override suspend fun doWork(params: LoginParams): ResultStatus<Unit> {
        return withContext(dispatcher) {
            val user = User(UUID.randomUUID().toString(), "User1")
            userRepository.saveUserId(user.id)
            ResultStatus.Success(Unit)
        }
    }

    data class LoginParams(val email: String, val password: String)
}
