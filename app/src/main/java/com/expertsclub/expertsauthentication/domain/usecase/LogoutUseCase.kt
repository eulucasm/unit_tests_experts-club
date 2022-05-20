package com.expertsclub.expertsauthentication.domain.usecase

import com.expertsclub.expertsauthentication.base.AppCoroutinesDispatchers
import com.expertsclub.expertsauthentication.base.ResultStatus
import com.expertsclub.expertsauthentication.data.repository.UserRepository
import com.expertsclub.expertsauthentication.base.ResultUseCase
import kotlinx.coroutines.withContext

class LogoutUseCase(
    private val userRepository: UserRepository,
    private val dispatchers: AppCoroutinesDispatchers
) : ResultUseCase<Unit, Unit>() {

    override suspend fun doWork(params: Unit): ResultStatus<Unit> {
        return withContext(dispatchers.io) {
            userRepository.clearUser()
            ResultStatus.Success(Unit)
        }
    }
}