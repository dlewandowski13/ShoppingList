package com.s24642.shoppinglist

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.s24642.shoppinglist.Constants.PREFERENCE_NAME
import com.s24642.shoppinglist.Constants.PREFERENCE_RESPONSE_DATA
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_update.*
import kotlinx.android.synthetic.main.settings.*

class MainActivity : AppCompatActivity() {

    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//zainicjowanie SharedPreferences - nazwa z objektu Constants,
// ustawienia MODE_PRIVATE - tylko dla tej aplikacji
        mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE)
//        TODO do usunięcia alert
        Toast.makeText(this,"Shared preferences",Toast.LENGTH_LONG).show()
        initializeSettings(mSharedPreferences)

        btnShoppingList.setOnClickListener {
            Toast.makeText(this,"Wybrałeś listę zakupów",Toast.LENGTH_LONG).show()
            val intentShoppngList = Intent(this,ShoppingList::class.java)
            startActivity(intentShoppngList)
        }

        btnSettings.setOnClickListener {
            Toast.makeText(this,"Ustawienia",Toast.LENGTH_LONG).show()
            settingsDialog()
        }
    }

//okienko do ustawień
    fun settingsDialog() {
        val settingsDialog = Dialog(this, R.style.Theme_Dialog)
        settingsDialog.setCancelable(false)
//ustawienie widoku na settings
        settingsDialog.setContentView(R.layout.settings)
        val spEditor = mSharedPreferences.edit()
        settingsDialog.rbBackground.setOnClickListener(View.OnClickListener {
            val background = rbBackground.checkedRadioButtonId
            Toast.makeText(this, "id = $background", Toast.LENGTH_LONG).show()
        })
        settingsDialog.tvSaveSettings.setOnClickListener(View.OnClickListener {
                val listName = settingsDialog.etSetListName.text.toString()
                val background = rbBackground.checkedRadioButtonId
                val newBackground: String

//                when (background){
//                    "rbDark" -> newBackground = "background_img"
//                }

                if (!listName.isEmpty()) {
                    spEditor.putString("ShoppingListName", listName)

                    Toast.makeText(applicationContext, "Dane zapisane", Toast.LENGTH_LONG).show()
    //                TODO zatwierdzenie tu, czy na przycisku
                    spEditor.apply()
                    initializeSettings(mSharedPreferences)

                    settingsDialog.dismiss()
                    }
                else {Toast.makeText(applicationContext,"Ustawienia są puste",Toast.LENGTH_LONG).show()
                }
        })

        with(settingsDialog) {

        tvCancelSettings.setOnClickListener(View.OnClickListener {
            dismiss()
        })

        settingsDialog.show()
    }
}

//funkcja służąca do wczytania ustawień z SharedPreferences
    fun initializeSettings(mSharedPreferences: SharedPreferences){
//
        val ShoppingListName = mSharedPreferences.getString("ShoppingListName", "Twoja Lista zakupów")
//        val ShoppingListBackground = mSharedPreferences.getString("ShoppingListBackground","background_img")

//    TODO usunięcie alerta
    Toast.makeText(this,"ShoppingListName $ShoppingListName",Toast.LENGTH_LONG).show()
        btnShoppingList.text = ShoppingListName

//Ustawienie wartości każdego tła
        llActivityMain.setBackgroundResource(R.drawable.background_blue)
    }

}