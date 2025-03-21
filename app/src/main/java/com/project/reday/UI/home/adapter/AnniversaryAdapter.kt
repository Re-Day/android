package com.project.reday.UI.home.adapter

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.project.reday.Utils.Utils.formatDateToKorean
import com.project.reday.databinding.RowAnniversaryBinding

class AnniversaryAdapter(
    private var activity: Activity,
    private var anniversaries: List<Anniversary>?
) :
    RecyclerView.Adapter<AnniversaryAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newAnniversaries: List<Anniversary>?) {
        anniversaries = newAnniversaries
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            RowAnniversaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.anniversary.text = anniversaries?.get(position)?.title ?: "0일"
        holder.date.text = formatDateToKorean(anniversaries?.get(position)?.date.toString())
        holder.dday.text = "D-${anniversaries?.get(position)?.daysLeft}"
    }

    override fun getItemCount() = (anniversaries?.size ?: 0)

    inner class ViewHolder(val binding: RowAnniversaryBinding) : RecyclerView.ViewHolder(binding.root) {
        val anniversary = binding.textViewAnniversaryTitle
        val date = binding.textViewAnniversaryDate
        val dday = binding.textViewAnniversaryDday

    }
}