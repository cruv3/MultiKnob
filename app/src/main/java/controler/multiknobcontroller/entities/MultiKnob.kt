package controler.multiknobcontroller.entities

import android.os.Parcel
import android.os.Parcelable

data class FingerData(val position: Float, val pressure: Int, val channelCount: Int) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(position)
        parcel.writeInt(pressure)
        parcel.writeInt(channelCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FingerData> {
        override fun createFromParcel(parcel: Parcel): FingerData {
            return FingerData(parcel)
        }

        override fun newArray(size: Int): Array<FingerData?> {
            return arrayOfNulls(size)
        }
    }
}

data class MultiKnob(
    val rotation: Float,
    val snapPoint: Int,
    val buttonPress: Int,
    val fingerCount: Int,
    val fingers: List<FingerData>,
    val characterCount: Int
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.createTypedArrayList(FingerData)!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(rotation)
        parcel.writeInt(snapPoint)
        parcel.writeInt(buttonPress)
        parcel.writeInt(fingerCount)
        parcel.writeTypedList(fingers)
        parcel.writeInt(characterCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MultiKnob> {
        override fun createFromParcel(parcel: Parcel): MultiKnob {
            return MultiKnob(parcel)
        }

        override fun newArray(size: Int): Array<MultiKnob?> {
            return arrayOfNulls(size)
        }
    }
}