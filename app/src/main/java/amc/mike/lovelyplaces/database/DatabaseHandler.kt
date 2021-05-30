package amc.mike.lovelyplaces.database

import amc.mike.lovelyplaces.models.LovelyPlaceModel
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "LovelyPlacesDatabase" // Database name
        private const val TABLE_LOVELY_PLACE = "LovelyPlacesTable" // Table Name

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_LOVELY_PLACE_TABLE = ("CREATE TABLE " + TABLE_LOVELY_PLACE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_LOVELY_PLACE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_LOVELY_PLACE")
        onCreate(db)
    }

    /**
     * Function to insert a Lovely Place details to SQLite Database.
     */
    fun addLovelyPlace(lovelyPlace: LovelyPlaceModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, lovelyPlace.title) // LovelyPlaceModelClass TITLE
        contentValues.put(KEY_IMAGE, lovelyPlace.image) // LovelyPlaceModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            lovelyPlace.description
        ) // LovelyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_DATE, lovelyPlace.date) // LovelyPlaceModelClass DATE
        contentValues.put(KEY_LOCATION, lovelyPlace.location) // LovelyPlaceModelClass LOCATION
        contentValues.put(KEY_LATITUDE, lovelyPlace.latitude) // LovelyPlaceModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, lovelyPlace.longitude) // LovelyPlaceModelClass LONGITUDE

        // Inserting Row
        val result = db.insert(TABLE_LOVELY_PLACE, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }

    fun updateLovelyPlace(lovelyPlace: LovelyPlaceModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, lovelyPlace.title) // LovelyPlaceModelClass TITLE
        contentValues.put(KEY_IMAGE, lovelyPlace.image) // LovelyPlaceModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            lovelyPlace.description
        ) // LovelyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_DATE, lovelyPlace.date) // LovelyPlaceModelClass DATE
        contentValues.put(KEY_LOCATION, lovelyPlace.location) // LovelyPlaceModelClass LOCATION
        contentValues.put(KEY_LATITUDE, lovelyPlace.latitude) // LovelyPlaceModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, lovelyPlace.longitude) // LovelyPlaceModelClass LONGITUDE

        val success = db.update(TABLE_LOVELY_PLACE,
            contentValues,
            KEY_ID + "=" +lovelyPlace.id,
            null)

        db.close() // Closing database connection
        return success
    }

    fun deleteLovelyPlace(lovelyPlace: LovelyPlaceModel): Int{
        val db = this.writableDatabase
        val success = db.delete(TABLE_LOVELY_PLACE, KEY_ID + "=" + lovelyPlace.id, null)
        db.close()
        return success
    }

    fun getLovelyPlacesList():ArrayList<LovelyPlaceModel>{
        val lovelyPlaceList = ArrayList<LovelyPlaceModel>()
        val selectQuery = "SELECT * FROM $TABLE_LOVELY_PLACE"
        val db = this.readableDatabase

        try{
            val cursor: Cursor = db.rawQuery(selectQuery, null)

            if(cursor.moveToFirst()){
                do {
                    val place = LovelyPlaceModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                    )
                    lovelyPlaceList.add(place)

                }while (cursor.moveToNext())
            }
            cursor.close()
        }catch(e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        return lovelyPlaceList
    }
}