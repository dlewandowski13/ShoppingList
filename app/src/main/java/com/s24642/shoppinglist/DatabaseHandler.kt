package com.s24642.shoppinglist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, "sp_list_db", null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1

        //Nazwa tabeli
        private const val TABLE_SHOPPING_LIST = "ShoppingList"

        //Lista kolumn
        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_AMOUNT = "amount"
        private const val KEY_PRICE = "price"
        private const val KEY_BOUGTH = "bougth"

    }

//utworzenie tabeli
    override fun onCreate(db: SQLiteDatabase?) {
    //polecenie tworzące tabelę
    val SHOPPINGLIST_TABLE_CREATE = ("CREATE TABLE " + TABLE_SHOPPING_LIST + " (" +
            KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_AMOUNT + " TEXT, " +
            KEY_PRICE + " TEXT, " + KEY_BOUGTH + " INTEGER)")
        db?.execSQL(SHOPPINGLIST_TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_SHOPPING_LIST")
        onCreate(db)
    }

    fun addItem(item: ShpngListModelClass): Long {
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(KEY_NAME, item.name)
        cv.put(KEY_AMOUNT, item.amount)
        cv.put(KEY_PRICE, item.price)
        cv.put(KEY_BOUGTH, item.bougth)

//Dodanie wiersza do tabeli
        val success = db.insert(TABLE_SHOPPING_LIST, null, cv)
//Zamknięcie połączenia do db
        db.close()
        return success
    }

//Metoda odczytująca rekordy z db
    fun viewItem(): ArrayList<ShpngListModelClass> {

        val itemList: ArrayList<ShpngListModelClass> = ArrayList<ShpngListModelClass>()

// Polecenie select pobierające dane z tabeli
        val selectQuery = "SELECT  * FROM $TABLE_SHOPPING_LIST"

        val db = this.readableDatabase
//Cursor do pobierania pojedynczych rekordów
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
        var bougth: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                amount = cursor.getString(cursor.getColumnIndex(KEY_AMOUNT))
                price = cursor.getString(cursor.getColumnIndex(KEY_PRICE))
                bougth = cursor.getInt(cursor.getColumnIndex(KEY_BOUGTH))

                val item = ShpngListModelClass(
                    id = id,
                    name = name,
                    amount = amount,
                    price = price,
                    bougth = bougth
                )
                itemList.add(item)

            } while (cursor.moveToNext())
        }
        return itemList
    }

//funckja do aktualizacji rekordu
    fun updateItem(item: ShpngListModelClass): Int {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(KEY_NAME, item.name)
        cv.put(KEY_AMOUNT, item.amount)
        cv.put(KEY_PRICE, item.price)
        cv.put(KEY_BOUGTH, item.bougth)

//aktualizacja rekordu w db
        val success = db.update(TABLE_SHOPPING_LIST, cv, KEY_ID + "=" + item.id, null)

        db.close()
        return success
    }

//funkcja do usuwania rekordu
    fun deleteItem(item: ShpngListModelClass): Int {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(KEY_ID, item.id)
//usunięcie rekordu na podstawie id
        val success = db.delete(TABLE_SHOPPING_LIST, KEY_ID + "=" + item.id, null)

        db.close()
        return success
    }
}