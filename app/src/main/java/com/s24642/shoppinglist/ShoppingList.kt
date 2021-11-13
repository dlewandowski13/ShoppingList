package com.s24642.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*

class ShoppingList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        btnAdd.setOnClickListener { view ->
            addItem()
        }
    }

//metoda do zapisu produktów w bazie danych
    private fun addItem() {
        val name = etName.text.toString()
        val amount = etAmount.text.toString()
        val price = etPrice.text.toString()

        val databaseHandler = DatabaseHandler(this)
        if (!name.isEmpty() && !amount.isEmpty() && !price.isEmpty()) {
            val status = databaseHandler.addItem(ShpngListModelClass(0, name, amount, price, "0"))
            if (status > -1) {
                Toast.makeText(applicationContext, "Produkt dodany", Toast.LENGTH_LONG).show()
                etName.text.clear()
                etAmount.text.clear()
                etPrice.text.clear()
            }
        } else {
            Toast.makeText(applicationContext,"Musisz uzupełnić wszystkie pola", Toast.LENGTH_LONG).show()
        }

    }

//Funkcja pobierająca listę produktów z bazy danych
    private fun getItemsList(): ArrayList<ShpngListModelClass> {
//utworzenie instancji klasy DatabaseHandler
        val databaseHandler = DatabaseHandler(this)
//wywołanie metody viewItem z klasy DatabaseHandler - odczyt rekordów
        val itemList: ArrayList<ShpngListModelClass> = databaseHandler.viewItem()

        return itemList
    }


//funkcja przekazanie danych do widoku
    private fun setupListofDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

// Ustawienie LayoutManager dla RecyclerViwe
            rvItemsList.layoutManager = LinearLayoutManager(applicationContext)
// zainicjalizowanie klasy adapter
            val itemAdapter = ItemAdapter(applicationContext, getItemsList())
// wypełnienie RecycleView danymi z instancji adapter
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }
}