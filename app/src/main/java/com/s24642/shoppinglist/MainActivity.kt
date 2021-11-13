package com.s24642.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnShoppingList.setOnClickListener {
            Toast.makeText(this,"Wybrałeś listę zakupów",Toast.LENGTH_LONG).show()
            val intentShoppngList = Intent(this,ShoppingList::class.java)
            startActivity(intentShoppngList)
        }
    }


}