package ru.mixail_akulov.a28_mvvm_async_2.foundation.navigator

import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.BaseScreen
import ru.mixail_akulov.a28_mvvm_async_2.foundation.utils.ResourceActions

/**
 * Посредник, который удерживает навигационные действия в очереди, если реальный навигатор не активен.
 */
class IntermediateNavigator : Navigator {

    private val targetNavigator = ResourceActions<Navigator>()

    override fun launch(screen: BaseScreen) = targetNavigator {
        it.launch(screen)
    }

    override fun goBack(result: Any?) = targetNavigator {
        it.goBack(result)
    }

    fun setTarget(navigator: Navigator?) {
        targetNavigator.resource = navigator
    }

    fun clear() {
        targetNavigator.clear()
    }

}