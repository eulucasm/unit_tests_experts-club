package com.expertsclub.expertsauthentication.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.expertsclub.expertsauthentication.data.manager.LocalPersistenceManager
import com.expertsclub.expertsauthentication.data.repository.UserRepository
import com.expertsclub.expertsauthentication.data.repository.UserRepositoryImpl
import com.expertsclub.expertsauthentication.domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

   @get:Rule
   var instantExecutorRule = InstantTaskExecutorRule()

   private var testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

   @Mock
   private lateinit var repository: UserRepository

   @Mock
   private lateinit var loginStateDataObserver: Observer<LoginViewModel.LoginState>

   private lateinit var viewModel: LoginViewModel

   @Before
   fun setupDispatcher() {
      Dispatchers.setMain(testDispatcher)
   }

   @Before
   fun initViewModel() {

      val loginUseCase = LoginUseCase(repository, Dispatchers.Main)
      viewModel = LoginViewModel(loginUseCase)
      viewModel.loginStateData.observeForever(loginStateDataObserver)
   }

   @Test
   fun `should return login success state when login is successfully`() {
      //arrange
      val email = "teste@user.com"
      val password = "1234"

      //act
      viewModel.login(email, password)

      //assert
      verify(loginStateDataObserver).onChanged(LoginViewModel.LoginState.LoginSuccess)
      //assertEquals(LoginViewModel.LoginState.LoginSuccess, viewModel.loginStateData.value)
   }

   @Test
   fun `should return error state when login is invalid due to empty email`() {
      //arrange
      val email = ""
      val password = "1234"

      //act
      viewModel.login(email, password)

      //assert
      verify(loginStateDataObserver).onChanged(LoginViewModel.LoginState.ShowError)
      //assertEquals(LoginViewModel.LoginState.ShowError, viewModel.loginStateData.value)
   }

   @Test
   fun `should return error state when login is invalid due to empty password`() {
      //arrange
      val email = "teste@user.com"
      val password = ""

      //act
      viewModel.login(email, password)

      //assert
      verify(loginStateDataObserver).onChanged(LoginViewModel.LoginState.ShowError)
      //assertEquals(LoginViewModel.LoginState.ShowError, viewModel.loginStateData.value)
   }

   @Test
   fun `should return error state when login is invalid due to empty password and email`() {
      //arrange
      val email = ""
      val password = ""

      //act
      viewModel.login(email, password)

      //assert
      verify(loginStateDataObserver).onChanged(LoginViewModel.LoginState.ShowError)
      //assertEquals(LoginViewModel.LoginState.ShowError, viewModel.loginStateData.value)
   }

   @Test
   fun `should return error state when login is invalid due to empties email and password`() =
      runBlockingTest {
         //arrange
         val email = "teste@user.com"
         val password = "1234"

         whenever(repository.saveUserId(any())).thenThrow(IllegalStateException::class.java)

         //act
         viewModel.login(email, password)

         //assert

         verify(loginStateDataObserver).onChanged(LoginViewModel.LoginState.ShowError)
         //assertEquals(LoginViewModel.LoginState.ShowError, viewModel.loginStateData.value)
      }

   @After
   fun tearDownDispatcher() {
      Dispatchers.resetMain()
      testDispatcher.cleanupTestCoroutines()
   }


}