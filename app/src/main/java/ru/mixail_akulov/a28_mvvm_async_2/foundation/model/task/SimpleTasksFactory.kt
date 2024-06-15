package ru.mixail_akulov.a28_mvvm_async_2.foundation.model.task

import android.os.Handler
import android.os.Looper
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.ErrorResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.FinalResult
import ru.mixail_akulov.a28_mvvm_async_2.foundation.model.SuccessResult
import java.lang.Exception

private val handler = Handler(Looper.getMainLooper())

// todo!!!
// Temp implementation for TasksFactory and Task
class SimpleTasksFactory : TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SimpleTask(body)
    }

    // todo!!!

    class SimpleTask<T>(
        private val body: TaskBody<T>
    ) : Task<T> {

        var thread: Thread? = null
        var cancelled = false

        override fun await(): T = body()

        override fun enqueue(listener: TaskListener<T>) {
            thread = Thread {
                try {
                    val data = body()
                    publishResult(listener, SuccessResult(data))
                } catch (e: Exception) {
                    publishResult(listener, ErrorResult(e))
                }
            }.apply { start() }
        }

        override fun cancel() {
            cancelled = true
            thread?.interrupt()
            thread = null
        }

        private fun publishResult(listener: TaskListener<T>, result: FinalResult<T>) {
            handler.post {
                if (cancelled) return@post
                listener(result)
            }
        }
    }

}