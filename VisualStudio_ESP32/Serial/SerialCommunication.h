//
// Created by Joel Neumann on 02.01.24.
//

#ifndef DREA_V2_SERIALCOMMUNICATION_H
#define DREA_V2_SERIALCOMMUNICATION_H

#include "Button/ButtonObserver.h"
#include "Trill/Touch.h"
#include "BluetoothSerial.h"


class SerialCommunication : public ButtonObserver {
private:
    Configuration &configuration;
    BluetoothSerial &btSerial;
    float motor_angle = 0;
    int snap_point = 0;
    int snap_point_delta = 0;
    bool is_button_pressed = false;
    bool is_motor_default_position = true;
    int touch_count = 0;
    TouchVector touches{};


public:

    explicit SerialCommunication(Configuration &config, BluetoothSerial &serial) : configuration(config), btSerial(serial){}

    void button_update(const bool &new_state) override {
        is_button_pressed = new_state;
    }


    void update_data(const int &p_touch_count, const TouchVector &p_touches, const int &p_motor_angle,
                     const int &p_snap_point) {
        touch_count = p_touch_count;
        touches = p_touches;
        motor_angle = p_motor_angle;
        snap_point_delta = p_snap_point - snap_point;
        snap_point = p_snap_point;
    }

    void write() {
        String separator = ";";
        String output = "#";
        output += motor_angle;
        output += separator;
        output += snap_point_delta;
        output += separator;
        output += is_button_pressed;
        output += separator;
        output += touch_count;
        output += separator;
        for (Touch touch: touches) {
            float relative_touch_pos = touch.get_relative_position_in_degrees(motor_angle);
            output += relative_touch_pos;
            output += ",";
            output += touch.get_pressure();
            output += ",";
            output += touch.get_channels();
            output += separator;
        }
        //Length of package
        output += output.length();
        
        //Serial.println(output);

        if(btSerial.connected()){
            btSerial.print(output);
            delay(100);
        }
    }


    KnobState read() {
        String buffer;
        while (Serial.available() > 0) {
            char receivedChar = (char) Serial.read();

            if (receivedChar == '0') {
                return COMMON;
            } else if (receivedChar == '1') {
                return MULTIKNOB;
            }else if(receivedChar == '2'){
                return STOP;
            }
        }
        return NONE;
    }

};


#endif //DREA_V2_SERIALCOMMUNICATION_H
