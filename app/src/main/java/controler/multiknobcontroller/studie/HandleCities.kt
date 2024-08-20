package controler.multiknobcontroller.studie

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.ScreenCoordinate
import controler.multiknobcontroller.utils.entities.CityWrapper
import kotlin.math.abs

object HandleCities {
    private const val TAG = "GenerateBorder"
    private const val SIZEFACTOR = 12

    private const val TOTAL_SEGMENTS = 8
    private const val CENTER_SEGMENT_INDEX = 4


    fun setupCities(mapView: MapView, cityWrappers: MutableList<CityWrapper>, centerCoordinate: ScreenCoordinate, rootLayout: FrameLayout, context: Context): MutableList<MutableList<CityWrapper>> {
        val segments = MutableList(TOTAL_SEGMENTS) { mutableListOf<CityWrapper>() }
        val screenHeight = mapView.height

        val distinctCityWrappers = cityWrappers.distinctBy { it.name }

        distinctCityWrappers.forEach { city ->
            // Assign the segment number to the city wrapper
            val segmentIndex = calculateCitySegment(city.screenCoordinate, screenHeight, centerCoordinate.y)
            city.segment = segmentIndex
            segments[segmentIndex].add(city)
        }

        segments.forEach { segment ->
            val uniqueCities = segment.distinctBy { it.name }  // You can use other properties like `screenCoordinate` if necessary
            segment.clear()  // Clear the original segment
            segment.addAll(uniqueCities)  // Add back only the unique cities
        }

        segments.forEach { segment ->
            segment.sortBy { calculateScreenDistance(it.screenCoordinate, centerCoordinate) }
        }

        distinctCityWrappers.forEach{city ->
            setDrawableBounds(city, context)
            setIsHighlighted(centerCoordinate, distinctCityWrappers)
            setStepsAndDirection(segments, city, centerCoordinate)
        }

        distinctCityWrappers.forEach { city ->
            addCityOverlay(city, rootLayout, context)
        }

        return segments
    }

    private fun calculateCitySegment(
        cityCoordinate: ScreenCoordinate,
        screenHeight: Int,
        centerY: Double,
    ): Int {
        // Calculate the height of each segment
        val segmentHeight = screenHeight / TOTAL_SEGMENTS

        // Calculate the offset from the center of the screen
        val relativeY = cityCoordinate.y - centerY + (segmentHeight / 2)

        // Adjust the relative Y to be positive (starting from the center)
        val adjustedY = (screenHeight / 2) + relativeY

        // Calculate the segment index (0 at the bottom, totalSegments-1 at the top)
        val segmentIndex = (adjustedY / segmentHeight).toInt()

        // Ensure the segment index is within bounds
        return segmentIndex.coerceIn(0, TOTAL_SEGMENTS - 1)
    }

    private fun drawSegmentLines(mapView: MapView, rootLayout: FrameLayout, context: Context, centerCoordinate: ScreenCoordinate) {
        val screenHeight = mapView.height
        val segmentHeight = screenHeight / TOTAL_SEGMENTS

        // Calculate the center Y coordinate and shift the lines so the dot is inside a segment
        val centerY = centerCoordinate.y + (segmentHeight / 2)

        // Draw lines for each segment boundary, starting from the center and going outwards
        for (i in 0 until TOTAL_SEGMENTS / 2) {
            // Line above the center
            val lineAbove = View(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(2, context) // Line height in dp
                ).apply {
                    topMargin = (centerY - (i * segmentHeight)).toInt() // Adjust for lines above
                }
                setBackgroundColor(Color.RED) // Set the color of the line
            }

            // Line below the center
            val lineBelow = View(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(2, context) // Line height in dp
                ).apply {
                    topMargin = (centerY + ((i + 1) * segmentHeight)).toInt() // Adjust for lines below
                }
                setBackgroundColor(Color.RED) // Set the color of the line
            }

            // Add the lines to the root layout
            rootLayout.addView(lineAbove)
            rootLayout.addView(lineBelow)
        }
    }

    private fun setStepsAndDirection(
        segments: MutableList<MutableList<CityWrapper>>,
        city: CityWrapper,
        centerCoordinate: ScreenCoordinate,
    ) {
        if (city.segment == CENTER_SEGMENT_INDEX) {
            val currentSegment = segments[CENTER_SEGMENT_INDEX]

            // Split into left and right
            calculateStepsToLeft(currentSegment, centerCoordinate)
            calculateStepsToRight(currentSegment, centerCoordinate)

            currentSegment.add(city)

        } else {
            // If the city is in a different segment, show an up or down arrow
            city.steps = null
            city.angle = if (city.segment!! < CENTER_SEGMENT_INDEX) {
                0f // Arrow up for segments above the center
            } else {
                180f // Arrow down for segments below the center
            }
        }
    }

    private fun calculateStepsToLeft(segment: MutableList<CityWrapper>, centerCoordinate: ScreenCoordinate) {
        // Sort cities by their position to the left of the center
        val leftCities = segment.filter { it.screenCoordinate.x < centerCoordinate.x }
            .sortedByDescending { it.screenCoordinate.x }
            .distinctBy { it.name }
            .filter { !it.isHighlighted  }

        // Assign step counts to cities to the left
        leftCities.forEachIndexed { index, city ->
            city.steps = index + 1
            city.angle = null
        }
    }

    private fun calculateStepsToRight(segment: MutableList<CityWrapper>, centerCoordinate: ScreenCoordinate) {
        // Sort cities by their position to the right of the center
        val rightCities = segment.filter { it.screenCoordinate.x > centerCoordinate.x }
            .sortedBy { it.screenCoordinate.x }
            .distinctBy { it.name }
            .filter { !it.isHighlighted  }

        rightCities.forEachIndexed { index, city ->
            city.steps = index + 1
            city.angle = null
        }
    }

    private fun calculateScreenDistance(from: ScreenCoordinate, to: ScreenCoordinate): Double {
        val xDiff = to.x - from.x
        val yDiff = to.y - from.y
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff)
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun setDrawableBounds(city: CityWrapper, context: Context){
        val textSizeFactor = SIZEFACTOR
        // border size
        val nameSize = if (city.name.contains("-")) {
            city.name.split("-")[0].length  // Take the length of the part before the hyphen
        } else {
            city.name.length  // Otherwise, take the full length of the name
        }
        val width = dpToPx(textSizeFactor * nameSize, context)
        val height = dpToPx(40, context)

        val leftMargin = city.screenCoordinate.x.toInt() - width / 2
        val topMargin = city.screenCoordinate.y.toInt() - height / 2
        city.drawableBounds = Rect(leftMargin, topMargin, leftMargin + width, topMargin + height)
    }

    private fun addCityOverlay(city: CityWrapper, rootLayout: FrameLayout, context: Context) {
        val cityOverlay = FrameLayout(rootLayout.context).apply {
            tag = "cityOverlay"
            background = createBorderDrawable(dpToPx(2, context), city.isHighlighted)
            layoutParams = FrameLayout.LayoutParams(city.drawableBounds?.width() ?: 0, city.drawableBounds?.height() ?: 0).apply {
                this.leftMargin = city.drawableBounds?.left ?: 0// Center horizontally
                this.topMargin = city.drawableBounds?.top ?: 0  // Center vertically
            }
        }

        if(!city.isHighlighted){
            val indicatorLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.BOTTOM or Gravity.END  // Position in bottom-right corner
                    setMargins(0, 0, dpToPx(10, context), 0)
                }
            }

            if (city.steps != null) {
                val stepIndicator = addStepIndicator(city.steps!!,rootLayout, context)
                indicatorLayout.addView(stepIndicator)
            }

            if (city.angle != null) {
                val arrowIndicator = addArrowIndicator(city.angle!!, rootLayout, context)
                indicatorLayout.addView(arrowIndicator)
            }
            cityOverlay.addView(indicatorLayout)
        }
        rootLayout.addView(cityOverlay)
    }

    private fun addArrowIndicator(angle: Float, rootLayout: FrameLayout, context: Context) : TextView{
        val arrowIndicator = TextView(rootLayout.context).apply {
            text = "↑"
            setTextColor(Color.BLACK)
            textSize = 20f
            rotation = angle  // Rotate the arrow based on the calculated angle
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.END
                setMargins(0, 0, dpToPx(4, context), dpToPx(4, context))
            }
        }
        return arrowIndicator
    }

    private fun addStepIndicator(steps: Int, rootLayout: FrameLayout, context: Context) : TextView{
        // Add a TextView for steps and an arrow drawable for direction
        val stepIndicator = TextView(rootLayout.context).apply {
            text = steps.toString()
            setTextColor(Color.BLACK)
            textSize = 14f
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.START
                setMargins(dpToPx(4, context), dpToPx(4, context), 0, 0)
            }
        }

        return stepIndicator
    }

    fun removeCityOverlays(rootLayout: FrameLayout) {
        // Iterate through all views in the rootLayout and remove the ones we added as overlays
        val childCount = rootLayout.childCount
        val viewsToRemove = mutableListOf<View>()
        for (i in 0 until childCount) {
            val child = rootLayout.getChildAt(i)
            // Assuming the overlays are dynamically added Views and not part of the original layout
            if (child.tag == "cityOverlay") {
                viewsToRemove.add(child)  // Collect views to be removed
            }
        }

        // Remove the collected views
        viewsToRemove.forEach { rootLayout.removeView(it) }
    }

    private fun createBorderDrawable(borderWidth: Int, isHighlighted: Boolean): Drawable {
        val shapeDrawable = ShapeDrawable().apply {
            shape = RectShape()
            paint.color = if (isHighlighted) Color.GREEN else Color.RED  // Highlighted cities get a green border
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = borderWidth.toFloat()  // Set border width
        }
        return shapeDrawable
    }

    private fun setIsHighlighted(centerCoordinate: ScreenCoordinate, cityWrappers: List<CityWrapper>) {
        var firstHighlightedCity: CityWrapper? = null

        cityWrappers.forEach { city ->
            val isInsideBounds = city.drawableBounds?.contains(centerCoordinate.x.toInt(), centerCoordinate.y.toInt()) == true

            if (isInsideBounds && firstHighlightedCity == null) {
                city.isHighlighted = true
                firstHighlightedCity = city
            } else {
                city.isHighlighted = false
            }
        }
    }

    fun getLeftNextCity(segment: MutableList<MutableList<CityWrapper>>, centerCoordinate: ScreenCoordinate): CityWrapper? {
        var cityWrapper: CityWrapper? = null
        var center = CENTER_SEGMENT_INDEX

        while (cityWrapper == null && center >= 0) { // Move upwards through the segments
            segment[center].forEach {
                if (it.screenCoordinate.x < centerCoordinate.x && it.steps == 1) {
                    cityWrapper = it
                }
            }
            center-- // Move up a segment
        }

        return cityWrapper
    }

    fun getRightNextCity(segment: MutableList<MutableList<CityWrapper>>, centerCoordinate: ScreenCoordinate): CityWrapper? {
        var cityWrapper: CityWrapper? = null
        var center = CENTER_SEGMENT_INDEX

        while (cityWrapper == null && center < TOTAL_SEGMENTS) { // Move downwards through the segments
            segment[center].forEach {
                if (it.screenCoordinate.x > centerCoordinate.x && it.steps == 1) {
                    cityWrapper = it
                }
            }
            center++ // Move down a segment
        }

        return cityWrapper
    }
}