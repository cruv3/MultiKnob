package controler.multiknobcontroller.utils.entities

import android.graphics.Rect
import com.mapbox.geojson.Point
import com.mapbox.maps.ScreenCoordinate

data class CityWrapper(
    val name: String,
    val point: Point,
    val screenCoordinate: ScreenCoordinate,
    var steps: Int?,
    var angle: Float?,
    var segment: Int?,
    var isHighlighted: Boolean = false,
    var drawableBounds: Rect? = null  // Add a field for storing the drawable boundaries
)
