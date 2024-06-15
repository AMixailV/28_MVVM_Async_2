package ru.mixail_akulov.a28_mvvm_async_2.foundation.model.task

import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.FinalResult

typealias TaskListener<T> = (FinalResult<T>) -> Unit

/**
 * Base interface for all async operations.
 */
interface Task<T> {

    /**
     * Метод блокировки ожидания и получения результатов.
     * Выдает исключение в случае ошибки.
     * @throws [IllegalStateException] if task has been already executed
     */
    fun await(): T

    /**
     * Неблокирующий метод прослушивания результатов задания.
     * Если задача отменяется до завершения, прослушиватель не вызывается.
     *
     * Слушатель вызывается в основном потоке.
     * @throws [IllegalStateException] если задача уже выполнена.
     */
    fun enqueue(listener: TaskListener<T>)

    /**
     * Cancel this task and remove listener assigned by [enqueue].
     */
    fun cancel()

}