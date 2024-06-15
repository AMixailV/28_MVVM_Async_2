package ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mixail_akulov.a28_mvvm_async_2.databinding.FragmentCurrentColorBinding
import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.BaseFragment
import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.BaseScreen
import ru.mixail_akulov.a28_mvvm_async_2.foundation.views.screenViewModel
import ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.views.onTryAgain
import ru.mixail_akulov.a28_mvvm_async_2.simplemvvm.views.renderSimpleResult


class CurrentColorFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)
        viewModel.currentColor.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    binding.colorView.setBackgroundColor(it.value)
                }
            )
        }

        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        return binding.root
    }
}