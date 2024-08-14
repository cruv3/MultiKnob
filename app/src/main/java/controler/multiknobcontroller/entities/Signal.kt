package controler.multiknobcontroller.entities

enum class Signal {;
    enum class GlobalSignal{
        LEFT,
        RIGHT,
        UP,
        DOWN,
        GO_BACK,
        OPEN_GOOGLE_MAPS,
        OPEN_SPOTIFY;
        enum class ButtonSignal{
            SHORT_TAP,
            LONG_PRESS,
            DOUBLE_TAP,
            ALL_APPS,
        }
    }
    enum class GoogleMaps{
        LEFT,
        RIGHT,
        UP,
        DOWN,
        ZOOM_IN,
        ZOOM_OUT;
        enum class ButtonSignal{
            SHORT_TAP,
            LONG_PRESS,
            DOUBLE_TAP,
        }
    }
}