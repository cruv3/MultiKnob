//
// Created by Joel Neumann on 02.01.24.
//

#ifndef DREA_V2_TOUCHCOUNTSUBJECT_H
#define DREA_V2_TOUCHCOUNTSUBJECT_H

#include <vector>
#include "TouchCountObserver.h"

class TouchCountSubject {
private:
    std::vector<TouchCountObserver *> observers;
    unsigned int touch_count;

public:
    void attach(TouchCountObserver *observer) {
        observers.push_back(observer);
    }

    void detach(TouchCountObserver *observer) {
        observers.erase(std::remove(observers.begin(), observers.end(), observer), observers.end());
    }

    void setTouchCount(unsigned int newState) {
        touch_count = newState;
        notifyObservers();
    }

    unsigned int getTouchCount() const {
        return touch_count;
    }

    void notifyObservers() {
        for (TouchCountObserver *observer: observers) {
            observer->touch_count_update(touch_count);
        }
    }
};


#endif //DREA_V2_TOUCHCOUNTSUBJECT_H
