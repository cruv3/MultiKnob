//
// Created by Joel Neumann on 18.01.24.
//

#ifndef DREA_V2_RAWTOUCHGROUP_H
#define DREA_V2_RAWTOUCHGROUP_H

#include "RawTouch.h"

using namespace std;

class RawTouchGroup : public vector<RawTouch> {
public:
    void split_by_peeks() {
        RawTouchGroup new_groups{};

        for (RawTouch raw_touch: *this) {
            vector<RawTouch> split = raw_touch.split_by_peek();
            new_groups.insert(new_groups.end(), split.begin(), split.end());
        }
        *this = new_groups;
    }

    TouchVector create_touches(unsigned int shift_count) {
        TouchVector touches{};
        for (RawTouch raw_touch: *this) {
            touches.push_back(raw_touch.create_touch(shift_count));
        }
        return touches;
    }
};


#endif //DREA_V2_RAWTOUCHGROUP_H
