package com.imkaem.android.svarc.expenses.domain.use_cases

import com.imkaem.android.svarc.expenses.data.repositories.ExpensesRepository
import com.imkaem.android.svarc.expenses.utils.values.CreateExpenseValue
import javax.inject.Inject

class CreateExpenseUseCase @Inject constructor(
    private val expensesRepository: ExpensesRepository,
) {
    suspend operator fun invoke(
        /* TODO this could be nullable, too. all of them? */
        amount: Long,
        categoryId: Int,
        description: String,
        date: Long,
        hour: Int,
        minute: Int,
//        expenseValue: CreateExpenseValue
        /* TODO lets have it accept all arguments instead , so the use case will create value , and maybe even validate? */
    ): Long {

        val hourInMillis = hour * 60 * 60 * 1000  // hour * 60 minutes * 60 seconds * 1000 millis
        val minuteInMillis = minute * 60 * 1000 // minute * 60 seconds * 1000 millis

        val dateTimeMillis = date + hourInMillis + minuteInMillis

        val createExpenseValue = CreateExpenseValue(
            amount = amount,
            currency = "EUR",
            description = description,
            categoryId = categoryId,
            dateTimeMillis = dateTimeMillis,
        )


        val id = expensesRepository.addExpense(createExpenseValue)

        return id
    }
}