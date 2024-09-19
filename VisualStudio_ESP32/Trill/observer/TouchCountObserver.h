//
// Created by Joel Neumann on 02.01.24.
//

#ifndef DREA_V2_TOUCHCOUNTOBSERVER_H
#define DREA_V2_TOUCHCOUNTOBSERVER_H


class TouchCountObserver {
public:
    virtual void touch_count_update(const unsigned int &touch_count) = 0;
};


#endif //DREA_V2_TOUCHCOUNTOBSERVER_H
