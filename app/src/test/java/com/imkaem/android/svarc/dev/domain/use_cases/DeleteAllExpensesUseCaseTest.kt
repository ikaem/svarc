package com.imkaem.android.svarc.dev.domain.use_cases

import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

//import org.junit.Before
//import org.junit.Test

class DeleteAllExpensesUseCaseTest {


//  private lateinit var expensesRepository: ExpensesRepository
  private lateinit var mockExpensesRepository: ExpensesRepository


  private val dispatcher = StandardTestDispatcher()
  private val scope = TestScope(dispatcher)

  @BeforeEach
  fun setUpEach() {
    mockExpensesRepository = mockk()
  }

  @Test
  fun givenRegularCall_shouldDeleteAllExpenses(): Unit = scope.runTest {
    coEvery {
        mockExpensesRepository.deleteAllExpenses()
    }.returns(Unit)

    /* given */


    /* when */
    val useCase = DeleteAllExpensesUseCase(
        mockExpensesRepository
    )
    useCase()


    /* then*/
    coVerify {
      mockExpensesRepository.deleteAllExpenses()
    }


  }

  /*
  * mockk
  * https://proandroiddev.com/introduction-to-mockk-and-mocking-basics-part-1-of-5-01467d917e2f
  * https://www.fabrizioduroni.it/blog/post/2021/01/27/kotlin-junit5-mockk
  *
  * TODO also, need before, after and so on...
  * */




}