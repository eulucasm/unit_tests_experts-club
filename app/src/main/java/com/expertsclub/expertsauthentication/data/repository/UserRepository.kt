package com.expertsclub.expertsauthentication.data.repository

import com.expertsclub.expertsauthentication.data.manager.LocalPersistenceManager

class UserRepository(
    private val localPersistenceManager: LocalPersistenceManager
) {

    suspend fun saveUserId(id: String) {
        localPersistenceManager.saveUserId(id)
    }

    suspend fun clearUser() {
        localPersistenceManager.clearUser()
    }
}