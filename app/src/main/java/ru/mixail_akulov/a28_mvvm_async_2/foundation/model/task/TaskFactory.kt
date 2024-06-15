package ru.mixail_akulov.a28_mvvm_async_2.foundation.model.task

import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.Repository

typealias TaskBody<T> = () -> T

/**
 * Фабрика для создания экземпляров асинхронных задач ([Task]) из синхронного кода, определенного [TaskBody]
 */
interface TasksFactory : Repository {

    /**
     * Create a new [Task] instance from the specified body.
     */
    fun <T> async(body: TaskBody<T>): Task<T>

}