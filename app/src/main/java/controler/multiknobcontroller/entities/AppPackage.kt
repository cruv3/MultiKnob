package controler.multiknobcontroller.entities

enum class AppPackage(val packageName: String) {
    HOME_SCREEN(""),
    GOOGLE_MAPS("com.google.android.apps.maps"),
    SPOTIFY("com.spotify.music"),
    CONTACTS("com.android.contacts");

    companion object {
        fun fromPackageName(packageName: String): AppPackage {
            return entries.find { it.packageName == packageName } ?: HOME_SCREEN
        }
    }
}