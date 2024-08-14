package controler.multiknobcontroller.studie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import controler.multiknobcontroller.R

class MapActivity : AppCompatActivity(){

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.mapView)
        mapView.mapboxMap.loadStyleUri(Style.MAPBOX_STREETS)

    }
}