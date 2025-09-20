package com.imkaem.android.svarc.core.presentation.view_models.home_screen_view_model

sealed class HomeScreenEvent {}

sealed class HomeScreenChangeTabEvent : HomeScreenEvent() {
    data class ChangeTab(val index: Int) : HomeScreenChangeTabEvent()
}


sealed class HomeScreenAddCategoryEvent: HomeScreenEvent() {
    data class UpdateName(val name: String): HomeScreenAddCategoryEvent()
    /* this is actual submission */
    object SubmitCategory: HomeScreenAddCategoryEvent()
}


sealed class HomeScreenAddExpenseEvent : HomeScreenEvent() {
    data class UpdateAmount(val amount: String) : HomeScreenAddExpenseEvent()
    data class UpdateCategory(val categoryId: Int) :
        HomeScreenAddExpenseEvent()

    data class UpdateDescription(val description: String) : HomeScreenAddExpenseEvent()
    data class UpdateDate(val date: String) : HomeScreenAddExpenseEvent()
    data class UpdateTime(val time: String) : HomeScreenAddExpenseEvent()
    /* NOTE: this is actual submission of new expense */
    object SubmitExpense : HomeScreenAddExpenseEvent()
}

/* and additional sealed classes to
* edit month's daily budget
* open dialogs and bottom sheets
* fetch next page of expenses
* ...
* */