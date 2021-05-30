package amc.mike.lovelyplaces.activities

import amc.mike.lovelyplaces.R
import amc.mike.lovelyplaces.adapters.LovelyPlacesAdapter
import amc.mike.lovelyplaces.database.DatabaseHandler
import amc.mike.lovelyplaces.models.LovelyPlaceModel
import amc.mike.lovelyplaces.utils.SwipeToDeleteCallback
import amc.mike.lovelyplaces.utils.SwipeToEditCallback
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddLovelyPlace.setOnClickListener {
            val intent = Intent(this, AddLovelyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_RQ)
        }
        getLovelyPlacesListFromLocalDB()
    }

    private fun setupLovelyPlacesRecyclerView(
        lovelyPlaceList: ArrayList<LovelyPlaceModel>){
        rv_lovely_places_list.layoutManager = LinearLayoutManager(this)
        rv_lovely_places_list.setHasFixedSize(true)

        val placesAdapter = LovelyPlacesAdapter(this, lovelyPlaceList)
        rv_lovely_places_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : LovelyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: LovelyPlaceModel) {
                val intent = Intent(this@MainActivity, LovelyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_lovely_places_list.adapter as LovelyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_PLACE_ACTIVITY_RQ)
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_lovely_places_list)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_lovely_places_list.adapter as LovelyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getLovelyPlacesListFromLocalDB()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_lovely_places_list)
    }

    private fun getLovelyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getLovelyPlaceList: ArrayList<LovelyPlaceModel> = dbHandler.getLovelyPlacesList()

        if(getLovelyPlaceList.size > 0){
            /*for(i in getLovelyPlaceList){
                Log.e("Title", i.title)
            } */

            rv_lovely_places_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setupLovelyPlacesRecyclerView(getLovelyPlaceList)
        }else {
            rv_lovely_places_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_PLACE_ACTIVITY_RQ){
            if(resultCode == Activity.RESULT_OK){
                getLovelyPlacesListFromLocalDB()
            }else {
                Log.e("Acitivity ", "Cancelled or Back pressed")
            }
        }
    }

    companion object {
        var ADD_PLACE_ACTIVITY_RQ = 1
        var EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}