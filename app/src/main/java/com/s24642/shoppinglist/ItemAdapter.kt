package com.s24642.shoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
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

//przypisanie każdego elementu z ArrayList do widoku
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    val item = items[position]

    holder.tvName.text = item.name
    holder.tvAmount.text = item.amount
    holder.tvPrice.text = item.price
    if (item.bougth == 1) {
        holder.cbBougth.isChecked = true
    } else {
        holder.cbBougth.isChecked = false
    }


//co drugi wiersz jest inaczej pokolorowany, dla zachowania czytelności
    if (position % 2 == 0) {
        holder.llMain.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.gray
            )
        )
    } else {
        holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.gray2))
    }

//obsługa przycisku do aktualizacji rekordu
    holder.ivEdit.setOnClickListener { view ->
        if (context is ShoppingList) {
            context.updateRecordDialog(item)
        }
    }

    holder.ivDelete.setOnClickListener { view ->
        if (context is ShoppingList) {
            context.deleteRecordAlertDialog(item)
        }
    }
//obsługa checkboxa
    holder.cbBougth.setOnClickListener { view ->
        if (context is ShoppingList) {
            if (holder.cbBougth.isChecked) {
                context.updateBougth(1, item)
            } else {
                context.updateBougth(0, item)
            }
        }

    }

}


//pobranie ilości wpisów na liście
    override fun getItemCount(): Int {
        return items.size
    }

//lista elementów RecyclerView
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val llMain = view.llMain
        val tvName = view.tvName
        val tvAmount = view.tvAmount
        val tvPrice = view.tvPrice
        val cbBougth = view.cbBought
        val ivEdit = view.ivEdit
        val ivDelete = view.ivDelete
    }
}