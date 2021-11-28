package com.s26462.shoppinglist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
//import com.s26462.shoppinglist.Manifest
import com.s26462.shoppinglist.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_shopping_list.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_update.*

class ShoppingList : AppCompatActivity() {

    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)
        setSupportActionBar(toolbar)
        mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE)
        initializeSettings(mSharedPreferences)

        btnAdd.setOnClickListener { view ->
            addItem()
        }

        setupListofDataIntoRecyclerView()
    }

    //funkcja przekazanie danych do widoku
    private fun setupListofDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

// Ustawienie LayoutManager dla RecyclerViwe
            rvItemsList.layoutManager = LinearLayoutManager(this)
// zainicjalizowanie klasy adapter
            val itemAdapter = ItemAdapter(this, getItemsList())
// wypełnienie RecycleView danymi z instancji adapter
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
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


    //metoda do zapisu produktów w bazie danych
    private fun addItem() {
        val name = etName.text.toString()
        val amount = etAmount.text.toString()
        val price = etPrice.text.toString()

        val databaseHandler = DatabaseHandler(this)
        if (!name.isEmpty() && !amount.isEmpty() && !price.isEmpty()) {
            val status = databaseHandler.addItem(ShpngListModelClass(0, name, amount, price, 0))
            if (status > -1) {
                Toast.makeText(applicationContext, "Produkt dodany", Toast.LENGTH_SHORT).show()
                etName.text.clear()
                etAmount.text.clear()
                etPrice.text.clear()

                setupListofDataIntoRecyclerView()
                Toast.makeText(applicationContext, "Before sendBroadcast", Toast.LENGTH_SHORT).show()

//utworzenie intentu rozgłoszeniowego i przekazanie treści do wyświetlenia notyfikacji
                val broadcastIntent = Intent("PRODUCT_ADDED")
                    .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                    .putExtra("message","Do Twojej listy zakupów dano nowy produkt: $name w ilości $amount w cenie $price")

                val message = broadcastIntent.getStringExtra("message")
                Toast.makeText(applicationContext, "Dodanie extra $message", Toast.LENGTH_SHORT).show()
                sendBroadcast(broadcastIntent)
//                sendOrderedBroadcast(broadcastIntent,"MY_PERMISSION")
                Toast.makeText(applicationContext, "After sendBroadcast", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext,"Musisz uzupełnić wszystkie pola", Toast.LENGTH_LONG).show()
        }


    }

    //okienko do aktualizacji danych
    fun updateRecordDialog(shpngListModelClass: ShpngListModelClass) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
//ustawienie widoku na dialog_update
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etUpdateName.setText(shpngListModelClass.name)
        updateDialog.etUpdateAmount.setText(shpngListModelClass.amount)
        updateDialog.etUpdatePrice.setText(shpngListModelClass.price)

        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener {

            val name = updateDialog.etUpdateName.text.toString()
            val amount = updateDialog.etUpdateAmount.text.toString()
            val price = updateDialog.etUpdatePrice.text.toString()

            val databaseHandler = DatabaseHandler(this)

            if (!name.isEmpty() && !amount.isEmpty() && !price.isEmpty()) {
                val status =
                    databaseHandler.updateItem(ShpngListModelClass(shpngListModelClass.id, name, amount, price, 0))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Pozycja zaktualizowana", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Pola nie mogą być puste",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })

        updateDialog.show()
    }

    //metoda wywołująca alert
    fun deleteRecordAlertDialog(shpngListModelClass: ShpngListModelClass) {
        val builder = AlertDialog.Builder(this)
//nagłówek alerta
        builder.setTitle("Usunięcie rekordu")
        builder.setMessage("Czy na pewno usunąć ${shpngListModelClass.name} z listy?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

//co jeżeli tak
        builder.setPositiveButton("Yes") { dialogInterface, which ->

//stworzenie instancji klasty DatabaseHandler
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
//wywołanie metody deleteItem z DatabaseHandler
            val status = databaseHandler.deleteItem(ShpngListModelClass(shpngListModelClass.id, "", "","",0))
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Pozycja została usunięta",
                    Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss()
        }
//co jeżeli nie
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
//utworzenie alertu
        val alertDialog: AlertDialog = builder.create()
//TODO do rozważenia, czy zamykać po kliknięciu w dowolne miejsce
//zablokowanie możliwości zamknięcia po kliknięciu poza alert
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun updateBougth(bougth: Int, shpngListModelClass: ShpngListModelClass)  {

        val databaseHandler = DatabaseHandler(this)

        val status = databaseHandler.updateItem(
            ShpngListModelClass(
                shpngListModelClass.id, shpngListModelClass.name, shpngListModelClass.amount, shpngListModelClass.price, bougth)
        )
        if (status > -1) {
            if(bougth == 1) {
                Toast.makeText(applicationContext,"Kupiłeś ${shpngListModelClass.name}",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext,"Nie kupiłeś ${shpngListModelClass.name}",Toast.LENGTH_LONG).show()
            }

            setupListofDataIntoRecyclerView()
        }
    }
    fun initializeSettings(mSharedPreferences: SharedPreferences){
//
        val ShoppingListBackground = mSharedPreferences.getString(Constants.PREFERENCE_BACKGROUND,"dark")

//Ustawienie wartości każdego tła
        when(ShoppingListBackground){
            "dark" -> llShoppingList.setBackgroundResource(R.drawable.background_img)
            "gradient" -> llShoppingList.setBackgroundResource(R.drawable.background_gradient)
            "blue" -> llShoppingList.setBackgroundResource(R.drawable.background_blue)
            else -> llShoppingList.setBackgroundResource(R.drawable.background_img)
        }

    }

}
