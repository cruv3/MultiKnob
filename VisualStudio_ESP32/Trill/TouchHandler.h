//
// Created by Joel Neumann on 22.12.23.
//

#ifndef DREA_V2_TOUCHHANDLER_H
#define DREA_V2_TOUCHHANDLER_H

#include "TouchRingSensor.h"
#include "observer/TouchCountSubject.h"

class TouchHandler: public TouchCountSubject{
private:
    TouchRingSensor touch_ring_sensor;

    TouchVector touches{};

    void update_touches(const TouchVector &new_touches){
        update_touch_count(new_touches.size());
        touches = new_touches;
    }

    void update_touch_count(unsigned int new_touch_count){
        if(getTouchCount() != new_touch_count){
            setTouchCount(new_touch_count);
        }
    }

public:

    void setup(int prescaler){
        touch_ring_sensor.setup(prescaler);
    }

    void process_touch_ring_sensor(){
        TouchData data = touch_ring_sensor.read_data();
        update_touches(data.extract_touches());
    }

    TouchVector get_touches(){
        return touches;
    }

};


#endif //DREA_V2_TOUCHHANDLER_H
