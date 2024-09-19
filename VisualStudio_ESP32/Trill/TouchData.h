//
// Created by Joel Neumann on 18.01.24.
//

#ifndef DREA_V2_TOUCHDATA_H
#define DREA_V2_TOUCHDATA_H

#include "Touch.h"
#include "RawTouch.h"
#include "RawTouchGroup.h"

using namespace std;


class TouchData : public array<unsigned int, MAX_CHANNELS> {
protected:
    array<unsigned int, MAX_CHANNELS> empty_array = {};

    int find_index_of_first_zero() const {
        for (int i = 0; i < size(); ++i) {
            if ((*this)[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    unsigned int shift_to_first_zero() {
        int shift = find_index_of_first_zero();
        if (shift > 0) {
            rotate(begin(), begin() + shift, end());
            return shift;
        }
        //TODO: Error handling if (shift == -1)
        return 0;
    }

    RawTouchGroup get_raw_touch_groups() {
        RawTouchGroup groups;
        RawTouch current_group;

        for (unsigned int i = 0; i < size(); ++i) {
            if ((*this)[i] != 0) {
                current_group.push_back((*this)[i]);
            } else if (!current_group.empty()) {
                current_group.set_start_index(i - current_group.size());
                groups.push_back(current_group);
                current_group.clear();
            }
        }

        if (!current_group.empty()) {
            current_group.set_start_index((size()) - current_group.size());
            groups.push_back(current_group);
        }

        return groups;
    }

public:
    TouchVector extract_touches() {
        if(*this == empty_array){
            return {};
        }

        unsigned int shifted = shift_to_first_zero();
        RawTouchGroup groups = get_raw_touch_groups();
        groups.split_by_peeks();
        return groups.create_touches(shifted);;
    }

};


#endif //DREA_V2_TOUCHDATA_H
