//
// Created by Joel Neumann on 22.12.23.
//

#ifndef DREA_V2_BUTTONOBSERVER_H
#define DREA_V2_BUTTONOBSERVER_H


class ButtonObserver {
public:
    virtual void button_update(const bool &is_button_pressed) = 0;
};

#endif //DREA_V2_BUTTONOBSERVER_H
