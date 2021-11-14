package com.s26462.shoppinglist

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.s24642.shoppinglist.R
import com.s26462.shoppinglist.Constants.PREFERENCE_NAME
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_shopping_list.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_update.*
import kotlinx.android.synthetic.main.items_row.*
import kotlinx.android.synthetic.main.settings.*

class MainActivity : AppCompatActivity() {

    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//zainicjowanie SharedPreferences - nazwa z objektu Constants,
// ustawienia MODE_PRIVATE - tylko dla tej aplikacji
        mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE)
        initializeSettings(mSharedPreferences)

        btnShoppingList.setOnClickListener {
            Toast.makeText(this,"Wybrałeś listę zakupów",Toast.LENGTH_LONG).show()
            val intentShoppngList = Intent(this, ShoppingList::class.java)
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
//inicjalizacja edytora
        val spEditor = mSharedPreferences.edit()

        var background: String
        settingsDialog.rbBackground.setOnCheckedChangeListener { backgroundGroup, checkedId ->
            if (checkedId > -1) {
                when(checkedId) {
                    R.id.rbDark -> background = "dark"
                    R.id.rbGradient -> background = "gradient"
                    R.id.rbBlue -> background = "blue"
                    else -> background = "dark"
                }
                spEditor.putString(Constants.PREFERENCE_BACKGROUND, background)
                spEditor.apply()
            }
        }

        settingsDialog.tvSaveSettings.setOnClickListener(View.OnClickListener {
            val listName = settingsDialog.etSetListName.text.toString()

            if (!listName.isEmpty()) {
                spEditor.putString(Constants.PREFERENCE_SL_NAME, listName)

                Toast.makeText(applicationContext, "Dane zapisane", Toast.LENGTH_LONG).show()

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
        val ShoppingListName = mSharedPreferences.getString(Constants.PREFERENCE_SL_NAME, "Twoja Lista zakupów")
        val ShoppingListBackground = mSharedPreferences.getString(Constants.PREFERENCE_BACKGROUND,"dark")

        btnShoppingList.text = ShoppingListName

//Ustawienie wartości każdego tła
        when(ShoppingListBackground){
            "dark" -> llActivityMain.setBackgroundResource(R.drawable.background_img)
            "gradient" -> llActivityMain.setBackgroundResource(R.drawable.background_gradient)
            "blue" -> llActivityMain.setBackgroundResource(R.drawable.background_blue)
            else -> llActivityMain.setBackgroundResource(R.drawable.background_img)
        }

    }

}