package com.s24642.shoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_row.view.*

class ItemAdapter(val context: Context, val items: ArrayList<ShpngListModelClass>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.items_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.tvName.text = item.name
        holder.tvAmount.text = item.amount.toString()
        holder.tvPrice.text = item.price.toString()

// Updating the background color according to the odd/even positions in list.
        //TODO: Zdecydować co z kolorami
        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLightGray
                )
            )
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        }

//TODO:do zrobienia setOnClickListener na ivEdit

//        holder.ivEdit.setOnClickListener { view ->
//            if (context is ShoppingList) {
//                context.updateRecordDialog(item)
//            }
//        }

    }


//pobranie ilości wpisów na liście
    override fun getItemCount(): Int {
        return items.size
    }

//lista elementów RecyclerView
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val llMain = view.llMain
        val tvName = view.tvName
        val tvAmount = view.tvAmount
        val tvPrice = view.tvPrice
        val ivEdit = view.ivEdit
        val ivDelete = view.ivDelete
    }
}