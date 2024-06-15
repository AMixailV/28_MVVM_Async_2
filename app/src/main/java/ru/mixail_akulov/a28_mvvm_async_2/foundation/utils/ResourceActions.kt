package ru.mixail_akulov.a28_mvvm_async_2.foundation.utils

typealias ResourceAction<T> = (T) -> Unit

/**
 * Очередь действий, где действия выполняются только при наличии ресурса.
 * Если это не так, действие добавляется в очередь и ожидает, пока ресурс не станет доступным.
 */
class ResourceActions<T> {

    var resource: T? = null
        set(newValue) {
            field = newValue
            if (newValue != null) {
                actions.forEach { it(newValue) }
                actions.clear()
            }
        }

    private val actions = mutableListOf<ResourceAction<T>>()

    /**
     * Вызывайте действие только тогда, когда [resource] существует (не нуль).
     * В противном случае действие откладывается до тех пор, пока [resource] не будет присвоено какое-либо ненулевое значение.
     */
    operator fun invoke(action: ResourceAction<T>) {
        val resource = this.resource
        if (resource == null) {
            actions += action
        } else {
            action(resource)
        }
    }

    fun clear() {
        actions.clear()
    }
}