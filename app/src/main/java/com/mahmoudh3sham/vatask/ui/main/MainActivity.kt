package com.mahmoudh3sham.vatask.ui.main

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.mahmoudh3sham.vatask.R
import com.mahmoudh3sham.vatask.data.models.EquationModel
import com.mahmoudh3sham.vatask.databinding.ActivityMainBinding
import com.mahmoudh3sham.vatask.di.component.ActivityComponent
import com.mahmoudh3sham.vatask.ui.base.BaseActivity
import com.mahmoudh3sham.vatask.ui.location.MyLocationActivity
import com.mahmoudh3sham.vatask.ui.main.adapter.TasksAdapter
import com.mahmoudh3sham.vatask.utils.AppConstants.DIVISION
import com.mahmoudh3sham.vatask.utils.AppConstants.MINUS
import com.mahmoudh3sham.vatask.utils.AppConstants.MULTIPLICATION
import com.mahmoudh3sham.vatask.utils.AppConstants.NONE
import com.mahmoudh3sham.vatask.utils.AppConstants.PLUS
import com.mahmoudh3sham.vatask.utils.FormValidator
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>(), MainNavigator {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var linearLayoutManagerPending: LinearLayoutManager

    @Inject
    lateinit var linearLayoutManagerDone: LinearLayoutManager

    @Inject
    lateinit var pendingTasksAdapter: TasksAdapter

    @Inject
    lateinit var doneTasksAdapter: TasksAdapter

    var selectedOperation = NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
    }

    private fun setUp() {
        mViewModel.setNavigator(this)
        subscribeViewModel()
        binding.toolbarHome.locationBtn.setOnClickListener {
            startActivity(MyLocationActivity.newIntent(this))
        }

        binding.rvPending.layoutManager = linearLayoutManagerPending
        binding.rvPending.adapter = pendingTasksAdapter

        binding.rvDone.layoutManager = linearLayoutManagerDone
        binding.rvDone.adapter = doneTasksAdapter

        handleOperatorsSelection()

        binding.btnCalculate.setOnClickListener { handleOnCalculateClick() }

    }

    override fun onResume() {
        super.onResume()
        pendingTasksAdapter.clearItems()
        doneTasksAdapter.clearItems()
        mViewModel.getPendingEquations()
        mViewModel.getDoneEquations()
    }

    private fun handleOnCalculateClick() {
        val fNum: String = binding.etFirstNumber.text.toString()
        val sNum: String = binding.etSecondNumber.text.toString()
        val delay: String = binding.etDelay.text.toString()

        val error: Int = FormValidator.validate(fNum, sNum, delay, selectedOperation)
        if (error != -1) {
            showErrorMessage(getString(error))
            return
        }


        val equationModel = EquationModel()
        equationModel.id = System.currentTimeMillis()
        equationModel.fNum = fNum.toDouble()
        equationModel.sNum = sNum.toDouble()
        equationModel.operation = getSelectedOperation(selectedOperation)
        equationModel.delay = delay.toInt()
        equationModel.status = false

        mViewModel.insertEquation(equationModel)


        //Adding WorkManager to handle tasks with delay in the background
        val data = Data.Builder()
        data.putLong("id", equationModel.id)
        data.putDouble("fNum", fNum.toDouble())
        data.putDouble("sNum", sNum.toDouble())
        data.putInt("operator", selectedOperation)
        val calculateRequest: WorkRequest =
            OneTimeWorkRequestBuilder<CalculateWorker>()
                .setInitialDelay(delay.toLong(), TimeUnit.SECONDS)
                .setInputData(data.build())
                .build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(calculateRequest)

        workManager.getWorkInfoByIdLiveData(calculateRequest.id)
            .observe(this, Observer {
                if (it.state.isFinished){
                    val mData = it.outputData
                    val equationId = mData.getLong("id", 0)
                    val equationResult = mData.getDouble("result", 0.0)
                    Log.e(TAG, "doWorkMain: $equationId >> $equationResult")
                    mViewModel.updateEquation(equationId, true, equationResult)
                }
            })

    }

    private fun getSelectedOperation(selectedOperation: Int): String {
        when (selectedOperation) {
            PLUS -> {
                return "+"
            }
            MINUS -> {
                return "-"
            }
            MULTIPLICATION -> {
                return "x"
            }
            DIVISION -> {
                return "/"
            }
        }
        return ""
    }

    private fun subscribeViewModel() {
        mViewModel.getInsertEqLiveData().observe(this, {
            if (it == 200) {
                showSuccessMessage("New Operation Added")
                mViewModel.getPendingEquations()
                clearData();
            } else {
                showErrorMessage("Failed Adding New Operation")
            }
        })

        mViewModel.getUpdateEqLiveData().observe(this, {
            if (it == 200) {
                showSuccessMessage("Operation Updated")
                pendingTasksAdapter.clearItems()
                doneTasksAdapter.clearItems()
                mViewModel.getDoneEquations()
                mViewModel.getPendingEquations()
            } else {
                showErrorMessage("Failed Updating Operation")
            }
        })

        mViewModel.getPendingEqsLiveData().observe(this, {
            pendingTasksAdapter.addItems(it)
        })

        mViewModel.getDoneEqsLiveData().observe(this, {
            doneTasksAdapter.addItems(it)
        })
    }

    private fun clearData() {
        binding.etFirstNumber.text?.clear()
        binding.etSecondNumber.text?.clear()
        binding.etDelay.text?.clear()
        selectedOperation = NONE
        binding.btnPlus.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
        binding.btnMinus.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
        binding.btnMulti.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
        binding.btnDiv.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
        binding.etFirstNumber.clearFocus()
        binding.etSecondNumber.clearFocus()
        binding.etDelay.clearFocus()
    }

    private fun handleOperatorsSelection() {
        binding.btnPlus.setOnClickListener {
            selectedOperation = PLUS
            binding.btnPlus.background =
                ContextCompat.getDrawable(this, R.drawable.round_bg_selected)
            binding.btnMinus.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
            binding.btnMulti.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
            binding.btnDiv.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
        }
        binding.btnMinus.setOnClickListener {
            selectedOperation = MINUS
            binding.btnPlus.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
            binding.btnMinus.background =
                ContextCompat.getDrawable(this, R.drawable.round_bg_selected)
            binding.btnMulti.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
            binding.btnDiv.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
        }
        binding.btnMulti.setOnClickListener {
            selectedOperation = MULTIPLICATION
            binding.btnPlus.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
            binding.btnMinus.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
            binding.btnMulti.background =
                ContextCompat.getDrawable(this, R.drawable.round_bg_selected)
            binding.btnDiv.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
        }
        binding.btnDiv.setOnClickListener {
            selectedOperation = DIVISION
            binding.btnPlus.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
            binding.btnMinus.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
            binding.btnMulti.background = ContextCompat.getDrawable(this, R.drawable.round_bg_grey)
            binding.btnDiv.background =
                ContextCompat.getDrawable(this, R.drawable.round_bg_selected)
        }
    }

    override fun performDependencyInjection(buildComponent: ActivityComponent?) {
        buildComponent?.inject(this)
    }

}