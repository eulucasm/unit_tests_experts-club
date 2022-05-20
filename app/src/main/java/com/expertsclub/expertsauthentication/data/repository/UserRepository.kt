package com.expertsclub.expertsauthentication.data.repository

import com.expertsclub.expertsauthentication.data.manager.LocalPersistenceManager

interface UserRepository{

    suspend fun saveUserId(id: String)

    suspend fun clearUser()
}

class UserRepositoryImpl(
    private val localPersistenceManager: LocalPersistenceManager
): UserRepository {

    override suspend fun saveUserId(id: String) {
        localPersistenceManager.saveUserId(id)
    }

    override suspend fun clearUser() {
        localPersistenceManager.clearUser()
    }
}