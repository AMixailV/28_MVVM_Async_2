package ru.mixail_akulov.a28_mvvm_async_2

import android.app.Application
import ru.mixail_akulov.a28_mvvm_async_2.foundation.BaseApplication
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.Repository
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.task.SimpleTasksFactory
import ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.model.colors.InMemoryColorsRepository

/**
 * Здесь мы храним экземпляры классов слоя модели.
 */
class App : Application(), BaseApplication {

    private val tasksFactory = SimpleTasksFactory()

    /**
     * Размещайте здесь свои репозитории, сейчас у нас всего 1 репозиторий
     */
    override val repositories: List<Repository> = listOf(
        tasksFactory,

        InMemoryColorsRepository(tasksFactory)
    )
}