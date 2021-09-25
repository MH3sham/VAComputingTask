package com.mahmoudh3sham.vatask

import androidx.core.util.Supplier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import javax.inject.Singleton

@Singleton
class ViewModelProviderFactory<T : ViewModel?>(
    private val viewModelClass: Class<T>,
    private val viewModelSupplier: Supplier<T>
) :
    NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(viewModelClass)) {
            viewModelSupplier.get() as T
        } else {
            throw IllegalArgumentException("Unknown Class name " + viewModelClass.name)
        }
    }

}