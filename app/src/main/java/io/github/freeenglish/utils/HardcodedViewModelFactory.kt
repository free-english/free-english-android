package io.github.freeenglish.utils

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@MainThread
inline fun <reified TViewModel : ViewModel> Fragment.viewModels(
    noinline factory: () -> TViewModel
) = viewModels<TViewModel> { HardcodedViewModelFactory(TViewModel::class.java, factory) }


class HardcodedViewModelFactory<TViewModel : ViewModel>(
    private val supportedType: Class<TViewModel>,
    private val factory: () -> TViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if (supportedType == modelClass) {
            factory() as T
        } else {
            throw IllegalArgumentException("can't create view model of ${modelClass.name}, only ${supportedType.name} is supported")
        }

}