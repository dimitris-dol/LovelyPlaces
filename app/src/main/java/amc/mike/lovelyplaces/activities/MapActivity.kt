package amc.mike.lovelyplaces.activities

import amc.mike.lovelyplaces.R
import amc.mike.lovelyplaces.models.LovelyPlaceModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mLovelyPlaceDetail : LovelyPlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            mLovelyPlaceDetail = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS)
                    as LovelyPlaceModel
        }

        if(mLovelyPlaceDetail != null) {
            setSupportActionBar(toolbar_map)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mLovelyPlaceDetail!!.title

            toolbar_map.setNavigationOnClickListener {
                onBackPressed()
            }

            val supportMapFragment: SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            supportMapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        val position = LatLng(mLovelyPlaceDetail!!.latitude, mLovelyPlaceDetail!!.longitude)
        p0!!.addMarker(MarkerOptions().position(position).title(mLovelyPlaceDetail!!.location))
        val Zoomer = CameraUpdateFactory.newLatLngZoom(position, 10f)
        p0.animateCamera(Zoomer)
    }
}