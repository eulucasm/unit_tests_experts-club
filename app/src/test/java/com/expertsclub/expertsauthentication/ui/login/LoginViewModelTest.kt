package com.expertsclub.expertsauthentication.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.expertsclub.expertsauthentication.data.manager.LocalPersistenceManager
import com.expertsclub.expertsauthentication.data.repository.UserRepository
import com.expertsclub.expertsauthentication.domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginViewModelTest {

   @get:Rule
   var instantExecutorRule = InstantTaskExecutorRule()

   private var testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

   private lateinit var viewModel: LoginViewModel

   @Before
   fun setupDispatcher() {
      Dispatchers.setMain(testDispatcher)
   }

   @Before
   fun initViewModel() {
      val repository = UserRepository(object : LocalPersistenceManager {
         private val storeDate = mutableMapOf<String, String>()

         override suspend fun getUserId(): Flow<String> {
            return flowOf(storeDate["userId"] ?: "")
         }

         override suspend fun saveUserId(id: String) {
            storeDate["userId"] = id
         }

         override suspend fun clearUser() {
            storeDate.clear()
         }
      })

      val loginUseCase = LoginUseCase(repository, Dispatchers.Main)
      viewModel = LoginViewModel(loginUseCase)
   }

   @Test
   fun `should return login success state when login is successfully`() {
      //arrange
      val email = "teste@user.com"
      val password = "1234"

      //act
      viewModel.login(email, password)

      //assert
      assertEquals(LoginViewModel.LoginState.LoginSuccess, viewModel.loginStateData.value)
   }

   @After
   fun tearDownDispatcher() {
      Dispatchers.resetMain()
      testDispatcher.cleanupTestCoroutines()
   }


}