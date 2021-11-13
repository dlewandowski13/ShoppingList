package com.s24642.shoppinglist

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, "shopping_list_db", null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1

        //Nazwa tabeli
        private const val TABLE_SHOPPING_LIST = "ShoppingList"

        //Lista kolumn
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_AMOUNT = "amount"
        private const val KEY_PRICE = "price"
        private const val KEY_BOUGTH = "bougth"

//polecenie tworzące tabelę
        private const val SHOPPINGLIST_TABLE_CREATE = ("CREATE TABLE " + TABLE_SHOPPING_LIST + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + "TEXT, " + KEY_AMOUNT + "TEXT, " +
                KEY_PRICE + "TEXT, " + KEY_BOUGTH + "TEXT)")
    }

//utworzenie tabeli
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SHOPPINGLIST_TABLE_CREATE)
    }

    fun addItem(item: ShpngListModelClass): Long {
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(KEY_NAME, item.name)
        cv.put(KEY_AMOUNT, item.amount)
        cv.put(KEY_PRICE, item.price)
        cv.put(KEY_BOUGTH, item.bougth)

        val success = db.insert(TABLE_SHOPPING_LIST, null, cv)

        db.close()
        return success
    }

//TODO: bazadanych onUpgrade

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_SHOPPING_LIST")
        onCreate(db)
    }

    //Metoda odczytująca rekordy z db jako ArrayList
    @SuppressLint("Range")
    fun viewItem(): ArrayList<ShpngListModelClass> {

        val itemList: ArrayList<ShpngListModelClass> = ArrayList<ShpngListModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_SHOPPING_LIST"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var amount: String
        var price: String
        var bougth: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                amount = cursor.getString(cursor.getColumnIndex(KEY_AMOUNT))
                price = cursor.getString(cursor.getColumnIndex(KEY_PRICE))
                bougth = cursor.getString(cursor.getColumnIndex(KEY_BOUGTH))

                val emp = ShpngListModelClass(
                    id = id,
                    name = name,
                    amount = amount,
                    price = price,
                    bougth = bougth
                )
                itemList.add(emp)

            } while (cursor.moveToNext())
        }
        return itemList
    }

}