//
// Created by Joel Neumann on 22.12.23.
//

#ifndef DREA_V2_TOUCHRINGSENSOR_H
#define DREA_V2_TOUCHRINGSENSOR_H

#include "../../lib/Trill/Trill.h"
#include "TouchData.h"


class TouchRingSensor {
private:
    Trill sensor1, sensor2;
    TouchData data{};

public:
    void setup(int prescaler) {
        int ret1 = sensor1.setup(Trill::TRILL_FLEX);
        int ret2 = sensor2.setup(Trill::TRILL_FLEX, 0x49);

        //TODO: Error handling
        if (ret1 != 0) {}
        if (ret2 != 0) {}

        delay(100);
        sensor1.setPrescaler(prescaler);
        delay(50);
        sensor2.setPrescaler(prescaler);
        delay(50);
        sensor1.updateBaseline();
        delay(50);
        sensor2.updateBaseline();
        delay(100);
    }


    TouchData read_data() {
        unsigned int loc = 0;
        auto readSensorData = [this, &loc](Trill &sensor) {
            sensor.requestRawData();
            while (sensor.rawDataAvailable()) {
                this->data[loc++] = sensor.rawDataRead();
                //Serial.print(data[loc-1]);
                //Serial.print(", ");
            }
        };

        readSensorData(sensor1);
        readSensorData(sensor2);
        //Serial.print(loc);
        //Serial.println();

        return this->data;
    }
};


#endif //DREA_V2_TOUCHRINGSENSOR_H
