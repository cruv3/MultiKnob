//
// Created by Joel Neumann on 22.12.23.
//

#ifndef DREA_V2_TOUCH_H
#define DREA_V2_TOUCH_H


#define MAX_CHANNELS 60

class Touch {
private:
    float position;
    int channels;
    int pressure;

public:
    Touch(float position, int channels, int pressure) : position(position), channels(channels), pressure(pressure) {}

    float get_position() const {
        return position;
    }

    int get_channels() const {
        return channels;
    }

    int get_pressure() const {
        return pressure;
    }

    float get_position_in_degrees() const {
        return position * 360 / MAX_CHANNELS;
    }

    float get_relative_position_in_degrees(float relative) const {
        if(relative < 0){
            relative = 360 - fmodf(relative, 360);
        }
        return fmod(relative + get_position_in_degrees(), 360.0f);
    }
};

typedef std::vector<Touch> TouchVector;


#endif //DREA_V2_TOUCH_H
