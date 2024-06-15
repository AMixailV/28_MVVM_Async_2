package ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.views.currentcolor

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mixail_akulov.a28_mvvm_async_2.R
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.PendingResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.SuccessResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.takeSuccess
import ru.mixail_akulov.a28_mvvm_async_2.foundation.navigator.Navigator
import ru.mixail_akulov.a28_mvvm_async_2.foundation.uiactions.UiActions
import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.BaseViewModel
import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.LiveResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.MutableLiveResult
import ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.model.colors.ColorListener
import ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.model.colors.ColorsRepository
import ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.model.colors.NamedColor
import ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.views.changecolor.ChangeColorFragment

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResult(it))
    }

    // --- пример результатов прослушивания через модельный слой

    init {
        colorsRepository.addListener(colorListener)
        load()
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- пример прослушивания результатов прямо с экрана

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    fun tryAgain() {
        load()
    }

    private fun load() {
        colorsRepository.getCurrentColor().into(_currentColor)
    }
}