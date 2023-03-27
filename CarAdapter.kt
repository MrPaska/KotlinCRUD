package com.example.kotlincrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class CarAdapter (private val carList: ArrayList<CarDataSet>) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    private lateinit var cListiner: onItemClickListerner

    interface onItemClickListerner{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListerner(clickListerner: onItemClickListerner){
        cListiner = clickListerner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{ // viewgroup the base class for Layouts
        val carView = LayoutInflater.from(parent.context).inflate(R.layout.car_list_item, parent, false)
        return ViewHolder(carView, cListiner)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val currentCar = carList[position]
        holder.carTextView.text = currentCar.carBrand
        holder.car_modelTextView.text = currentCar.carModel
    }
    override fun getItemCount(): Int {
        return carList.size
    }
    class ViewHolder(itemView: View, clickListerner: onItemClickListerner) : RecyclerView.ViewHolder(itemView) {
        val carTextView : TextView = itemView.findViewById(R.id.carTextView)
        val car_modelTextView : TextView = itemView.findViewById(R.id.car_modelTextView)

        init { // first to be executed block
            itemView.setOnClickListener{
                clickListerner.onItemClick(adapterPosition) // returns position of item
            }
        }

    }


}