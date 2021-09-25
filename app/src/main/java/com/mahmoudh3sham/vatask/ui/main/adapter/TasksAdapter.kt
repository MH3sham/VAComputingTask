package com.mahmoudh3sham.vatask.ui.main.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudh3sham.vatask.R
import com.mahmoudh3sham.vatask.data.models.EquationModel
import com.mahmoudh3sham.vatask.databinding.ItemOperationBinding
import com.mahmoudh3sham.vatask.ui.base.BaseViewHolder

class TasksAdapter(private val mTasksList: MutableList<EquationModel>?) :
    RecyclerView.Adapter<BaseViewHolder>() {

    companion object {
        private const val VIEW_TYPE_EMPTY = 0
        private const val VIEW_TYPE_TASK = 1
    }

    private var mCallback: Callback? = null

    fun setCallback(callback: Callback?) {
        mCallback = callback
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {

            VIEW_TYPE_TASK -> NotificationViewHolder(
                ItemOperationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_EMPTY -> EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_empty_view, parent, false)
            )
            else -> EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_empty_view, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mTasksList != null && mTasksList.size > 0) {
            VIEW_TYPE_TASK
        } else {
            VIEW_TYPE_EMPTY
        }
    }

    override fun getItemCount(): Int {
        return if (mTasksList != null && mTasksList.size > 0) {
            mTasksList.size
        } else {
            1
        }
    }

    fun addItems(list: List<EquationModel>?) {
        mTasksList?.clear()
        mTasksList?.addAll(list!!)
        notifyDataSetChanged()
    }

    fun addItem(eq: EquationModel) {
        mTasksList?.add(eq)
        notifyDataSetChanged()
    }

    fun clearItems() {
        mTasksList?.clear()
        notifyDataSetChanged()
    }

    interface Callback {
        fun onNotificationClicked(model: EquationModel?)
    }

    class EmptyViewHolder internal constructor(itemView: View?) :
        BaseViewHolder(itemView!!) {
        override fun onBind(position: Int) {}
    }

    @SuppressLint("NonConstantResourceId")
    inner class NotificationViewHolder(private val binding: ItemOperationBinding) :
        BaseViewHolder(binding.root) {


        @SuppressLint("SetTextI18n")
        override fun onBind(position: Int) {
            val equation: EquationModel = mTasksList?.get(position)!!

            if (equation.status) {
                binding.tvOperation.text = "${equation.fNum} ${equation.operation} ${equation.sNum} = ${equation.result}"
                binding.tvOperation.setTextColor(itemView.resources.getColor(R.color.green))
                binding.lContainer.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_done)
            } else {
                binding.tvOperation.text = "${equation.fNum} ${equation.operation} ${equation.sNum}"
                binding.tvOperation.setTextColor(itemView.resources.getColor(R.color.red))
                binding.lContainer.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_pending)
            }
        }

    }
}