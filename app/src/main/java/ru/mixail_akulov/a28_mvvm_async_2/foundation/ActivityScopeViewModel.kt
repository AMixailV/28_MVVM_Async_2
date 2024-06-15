package ru.mixail_akulov.a28_mvvm_async_2.foundation

import androidx.lifecycle.ViewModel
import ru.mixail_akulov.a28_mvvm_async_2.foundation.navigator.IntermediateNavigator
import ru.mixail_akulov.a28_mvvm_async_2.foundation.navigator.Navigator
import ru.mixail_akulov.a28_mvvm_async_2.foundation.uiactions.UiActions

/**
 * Реализация [Navigator] и [UiActions].
 * Он основан на view-model активности, поскольку экземпляры [Navigator] и [UiActions] должны быть
 * доступны из view-model фрагментов (обычно они передаются конструктору view-model).
 */
class ActivityScopeViewModel(
    val uiActions: UiActions,
    val navigator: IntermediateNavigator
) : ViewModel(),
    Navigator by navigator,
    UiActions by uiActions {

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }

}