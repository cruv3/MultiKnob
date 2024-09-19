//
// Created by Joel Neumann on 21.12.23.
//

#ifndef DREA_V2_BUTTONHANDLER_H
#define DREA_V2_BUTTONHANDLER_H

#include "ButtonSubject.h"

#define BUTTON_PIN 15

class ButtonHandler : public ButtonSubject {
private:
    int prevState = 0;
    unsigned long prev_millis = 0;

public:

    void setup(){
        pinMode(BUTTON_PIN, INPUT_PULLUP);
        prev_millis = millis();
    }

    void update_state() {
        int isPressed = digitalRead(BUTTON_PIN);
        unsigned long current_millis = millis();
        if (prevState != isPressed) {
            if(isPressed == 0 && current_millis - prev_millis > 70){
                setState(!isPressed);
                prev_millis = current_millis;
            }else if(isPressed == 1){
                setState(!isPressed);
            }
        }
        prevState = isPressed;
    }
};

#endif //DREA_V2_BUTTONHANDLER_H
