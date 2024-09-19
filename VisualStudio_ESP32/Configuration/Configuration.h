//
// Created by Joel Neumann on 22.12.23.
//

#ifndef DREA_V2_CONFIGURATION_H
#define DREA_V2_CONFIGURATION_H

#define MAX_TOUCHES 10

enum KnobState {
    NONE,
    COMMON,
    MULTIKNOB,
    STOP
};

struct TouchSnapConfiguration{
    float strength;
    int snap_point;

    TouchSnapConfiguration(float strength, int snap_points): strength(strength), snap_point(snap_points){}
    TouchSnapConfiguration(): strength(1), snap_point(12){}
};


class Configuration {
private:
    TouchSnapConfiguration touch_snap_configurations[MAX_TOUCHES + 1];
    KnobState knob_state;

public:
    Configuration() {
        for (auto &touch_snap_configuration : touch_snap_configurations) {
            touch_snap_configuration = TouchSnapConfiguration();
        }
        knob_state = COMMON;
    }

    Configuration(float strength, int snap_points, KnobState knob_state) {
        for (auto &touch_snap_configuration : touch_snap_configurations) {
            touch_snap_configuration = TouchSnapConfiguration(strength, snap_points);
        }
        this->knob_state = knob_state;
    }

    TouchSnapConfiguration get_touch_snap_configuration(int index) const {
        if (index >= 0 && index <= MAX_TOUCHES) {
            return touch_snap_configurations[index];
        } else {
            //TODO  Return a default TouchSnapConfiguration or handle out-of-bounds cases as needed
            return {};
        }
    }

    void set_touch_snap_configuration(int index, const TouchSnapConfiguration &config) {
        if (index >= 0 && index <= MAX_TOUCHES) {
            touch_snap_configurations[index] = config;
        } else {
            //TODO Handle out-of-bounds cases or take necessary action
        }
    }

    KnobState get_knob_state() const {
        return knob_state;
    }

    void set_knob_state(KnobState new_knob_state) {
        knob_state = new_knob_state;
    }

};

#endif //DREA_V2_CONFIGURATION_H
