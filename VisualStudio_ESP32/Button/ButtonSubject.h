//
// Created by Joel Neumann on 22.12.23.
//

#ifndef DREA_V2_BUTTONSUBJECT_H
#define DREA_V2_BUTTONSUBJECT_H

#include <vector>
#include "ButtonObserver.h"

class ButtonSubject {
private:
    std::vector<ButtonObserver *> observers;
    int state;

public:
    void attach(ButtonObserver *observer) {
        observers.push_back(observer);
    }

    void detach(ButtonObserver *observer) {
        observers.erase(std::remove(observers.begin(), observers.end(), observer), observers.end());
    }

    void setState(int newState) {
        state = newState;
        notifyObservers();
    }

    int getState() const {
        return state;
    }

    void notifyObservers() {
        for (ButtonObserver *observer: observers) {
            observer->button_update(state);
        }
    }
};

#endif //DREA_V2_BUTTONSUBJECT_H
