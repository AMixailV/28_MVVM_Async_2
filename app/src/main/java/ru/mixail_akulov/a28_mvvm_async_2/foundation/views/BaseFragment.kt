package ru.mixail_akulov.a28_mvvm_async_2.foundation.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.ErrorResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.PendingResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.SuccessResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.Result

/**
 * Base class for all fragments
 */
abstract class BaseFragment : Fragment() {

    /**
     * View-model that manages this fragment
     */
    abstract val viewModel: BaseViewModel

    /**
     * Call this method when activity controls (e.g. toolbar) should be re-rendered
     */
    fun notifyScreenUpdates() {
        (requireActivity() as FragmentsHolder).notifyScreenUpdates()
    }

    /**
     * Скройте все views в [root], а затем вызовите одну из предоставленных
     * лямбда-функций в зависимости от [result]:
     * - [onPending] is called when [result] is [PendingResult]
     * - [onSuccess] is called when [result] is [SuccessResult]
     * - [onError] is called when [result] is [ErrorResult]
     */
    fun <T> renderResult(root: ViewGroup, result: Result<T>,
                         onPending: () -> Unit,
                         onError: (Exception) -> Unit,
                         onSuccess: (T) -> Unit) {

        root.children.forEach { it.visibility = View.GONE }
        when (result) {
            is SuccessResult -> onSuccess(result.data)
            is ErrorResult -> onError(result.exception)
            is PendingResult -> onPending()
        }
    }
}