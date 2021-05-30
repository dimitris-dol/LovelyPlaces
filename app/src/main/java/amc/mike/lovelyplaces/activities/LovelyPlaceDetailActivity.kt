package amc.mike.lovelyplaces.activities

import amc.mike.lovelyplaces.R
import amc.mike.lovelyplaces.models.LovelyPlaceModel
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_lovely_place_detail.*

class LovelyPlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lovely_place_detail)

        var lovelyPlaceDetailModel : LovelyPlaceModel? = null

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            lovelyPlaceDetailModel = intent.getSerializableExtra(
                MainActivity.EXTRA_PLACE_DETAILS) as LovelyPlaceModel
        }

        if(lovelyPlaceDetailModel!= null){
            setSupportActionBar(toolbar_lovely_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = lovelyPlaceDetailModel.title

            toolbar_lovely_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_image.setImageURI(Uri.parse(lovelyPlaceDetailModel.image))
            tv_description.text = lovelyPlaceDetailModel.description
            tv_location.text = lovelyPlaceDetailModel.location

            btn_view_on_map.setOnClickListener{
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, lovelyPlaceDetailModel)
                startActivity(intent)
            }

        }
    }
}