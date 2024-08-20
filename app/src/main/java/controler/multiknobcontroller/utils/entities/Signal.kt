package controler.multiknobcontroller.utils.entities

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

enum class StudieSignal{
    LEFT_NEXT_CITY,
    RIGHT_NEXT_CITY,
    GO_BACK,
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