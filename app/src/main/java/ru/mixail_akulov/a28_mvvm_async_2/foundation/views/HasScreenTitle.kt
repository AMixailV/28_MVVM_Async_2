package ru.mixail_akulov.a28_mvvm_async_2.foundation.views

/**
 * Если ваш фрагмент хочет отображать собственный заголовок экрана на панели инструментов,
 * реализуйте этот интерфейс и переопределите метод [getScreenTitle].
 *
 * Обратите внимание, что если заголовок экрана может быть изменен динамически, пока фрагмент активен,
 * вам следует вызвать метод [BaseFragment.notifyScreenUpdates] для повторного рендеринга панели инструментов активности.
 */
interface HasScreenTitle {

    fun getScreenTitle(): String?
}