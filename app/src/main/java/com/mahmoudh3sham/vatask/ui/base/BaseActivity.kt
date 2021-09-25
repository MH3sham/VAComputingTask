package com.mahmoudh3sham.vatask.ui.base

import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mahmoudh3sham.vatask.MvvmApp
import com.mahmoudh3sham.vatask.di.component.ActivityComponent
import com.mahmoudh3sham.vatask.di.component.DaggerActivityComponent
import com.mahmoudh3sham.vatask.di.module.ActivityModule
import com.mahmoudh3sham.vatask.utils.Utils
import es.dmoral.toasty.Toasty
import javax.inject.Inject

abstract class BaseActivity<V : BaseViewModel<*>> : AppCompatActivity() {
    private var mProgressDialog: ProgressDialog? = null
    @Inject
    lateinit var mViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection(buildComponent)
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private val buildComponent: ActivityComponent
        get() = DaggerActivityComponent.builder()
            .appComponent((application as MvvmApp).appComponent)
            .activityModule(ActivityModule(this))
            .build()

    abstract fun performDependencyInjection(buildComponent: ActivityComponent?)

    fun getIntentWithClearHistory(c: Class<*>?): Intent {
        val intent = Intent(this, c)
        intent.flags = intent.flags or
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }

    fun showLoading() {
        hideLoading()
        mProgressDialog = Utils.showLoadingDialog(this)
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    open fun showErrorMessage(m: String?) {
        Toasty.error(this, m!!, Toast.LENGTH_LONG, false).show()
    }

    open fun showSuccessMessage(m: String?) {
        Toasty.success(this, m!!, Toast.LENGTH_LONG, true).show()
    }

    protected open fun showNoteMessage(m: String?) {
        Toasty.info(this, m!!, Toast.LENGTH_LONG, true).show()
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }


}

