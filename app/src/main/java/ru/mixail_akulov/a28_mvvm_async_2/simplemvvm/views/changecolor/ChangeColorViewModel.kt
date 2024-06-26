package ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.views.changecolor

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mixail_akulov.a28_mvvm_async_2.R
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.ErrorResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.FinalResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.PendingResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.SuccessResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.task.TasksFactory
import ru.mixail_akulov.a28_mvvm_async_2.foundation.navigator.Navigator
import ru.mixail_akulov.a28_mvvm_async_2.foundation.uiactions.UiActions
import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.BaseViewModel
import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.LiveResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.MediatorLiveResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.MutableLiveResult
import ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.model.colors.ColorsRepository
import ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.model.colors.NamedColor
import ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.views.changecolor.ChangeColorFragment.Screen

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    private val tasksFactory: TasksFactory,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors = MutableLiveResult<List<NamedColor>>(PendingResult())
    private val _currentColorId = savedStateHandle.getLiveData("currentColorId", screen.currentColorId)
    private val _saveInProgress = MutableLiveData(false)

    // основной пункт назначения (содержит объединенные значения from _availableColors & _currentColorId)
    private val _viewState = MediatorLiveResult<ViewState>()
    val viewState: LiveResult<ViewState> = _viewState

    // побочное назначение, также тот же результат может быть достигнут с использованием Transformations.map() function.
    val screenTitle: LiveData<String> = Transformations.map(viewState) { result ->
        if (result is SuccessResult) {
            val currentColor = result.data.colorsList.first { it.selected }
            uiActions.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
        } else {
            uiActions.getString(R.string.change_color_screen_title_simple)
        }
    }

    init {
        load()

        // initializing MediatorLiveData
        _viewState.addSource(_availableColors) { mergeSources() }
        _viewState.addSource(_currentColorId) { mergeSources() }
        _viewState.addSource(_saveInProgress) { mergeSources() }
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if (_saveInProgress.value == true) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() {
        _saveInProgress.postValue(true)

        tasksFactory.async {
            // этот код запускается асинхронно в другом потоке
            val currentColorId = _currentColorId.value ?: throw IllegalStateException("Color ID should not be NULL")
            val currentColor = colorsRepository.getById(currentColorId).await()
            colorsRepository.setCurrentColor(currentColor).await()
            return@async currentColor
        }
            // метод onSaved вызывается в основном потоке, когда асинхронный код завершен
            .safeEnqueue(::onSaved)
    }

    fun onCancelPressed() {
        navigator.goBack()
    }

    fun tryAgain() {
        load()
    }

    /**
     * [MediatorLiveData] может прослушивать другие экземпляры LiveData (даже более 1) и комбинировать их значения.
     *
     * Здесь мы слушаем список доступных цветов ([_availableColors] live-data) + current color id
     * ([_currentColorId] live-data), затем мы используем оба этих значения для создания списка
     * [NamedColorListItem], это список, который будет отображаться в RecyclerView.
     */
    private fun mergeSources() {
        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return
        val saveInProgress = _saveInProgress.value ?: return

        // map Result<List<NamedColor>> to Result<ViewState>
        _viewState.value = colors.map { colorsList ->
            ViewState(
                // map List<NamedColor> to List<NamedColorListItem>
                colorsList = colorsList.map { NamedColorListItem(it, currentColorId == it.id) },
                showSaveButton = !saveInProgress,
                showCancelButton = !saveInProgress,
                showSaveProgressBar = saveInProgress
            )
        }
    }

    private fun load() {
        colorsRepository.getAvailableColors().into(_availableColors)
    }

    private fun onSaved(result: FinalResult<NamedColor>) {
        _saveInProgress.value = false
        when (result) {
            is SuccessResult -> navigator.goBack(result.data)
            is ErrorResult -> uiActions.toast(uiActions.getString(R.string.error_happened))
        }
    }

    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean
    )
}