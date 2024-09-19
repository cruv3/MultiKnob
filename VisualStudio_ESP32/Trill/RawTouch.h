//
// Created by Joel Neumann on 18.01.24.
//

#ifndef DREA_V2_RAWTOUCH_H
#define DREA_V2_RAWTOUCH_H

#include "Trill/Touch.h"

using namespace std;


class RawTouch : public vector<unsigned int> {
private:
    unsigned int start_index = -1;
public:

    RawTouch() = default;

    explicit RawTouch(unsigned int start_index) : start_index(start_index) {};

    Touch create_touch(unsigned int shift_count) {
        int channels = (int) size();
        int pressure = 0;
        unsigned int weighted_index_pressure = 0;

        for (int i = 0; i < size(); ++i) {
            weighted_index_pressure += i * (*this)[i];
            pressure += (int) (*this)[i];
        }

        float position = (float) weighted_index_pressure / (float) pressure;
        position += (float) start_index + (float) shift_count; //TODO: +0.5?
        if (position >= MAX_CHANNELS) position -= MAX_CHANNELS;
        return {position, channels, pressure};
    }

    vector<RawTouch> split_by_peek() {
        vector<RawTouch> group;
        RawTouch current_raw_touch;

        unsigned int new_start_index = start_index;
        unsigned int peek = 0;
        int peek_index = -1;
        unsigned int last_value = 0;

        for (int i = 0; i < size(); ++i) {
            if (peek_index == -1 || peek_index == i - 1 && (*this)[i] >= peek) {//new peek
                peek = (*this)[i];
                peek_index = i;
                last_value = (*this)[i];
            }else if (peek_index < i - 1 && (*this)[i] > last_value) {//new low point
                current_raw_touch.start_index = new_start_index;
                group.push_back(current_raw_touch);
                new_start_index += current_raw_touch.size()-1;
                current_raw_touch.clear();
                current_raw_touch.push_back(last_value);
                peek = 0;
                peek_index = -1;
                last_value = 0;
            }else{// not a peek or a low
                last_value = (*this)[i];
            }
            current_raw_touch.push_back((*this)[i]);
        }

        if(!current_raw_touch.empty()){
            current_raw_touch.start_index = new_start_index;
            group.push_back(current_raw_touch);
        }

        return group;
    }

    unsigned int get_start_index() const {
        return start_index;
    }

    void set_start_index(unsigned int startIndex) {
        start_index = startIndex;
    }

    static RawTouch build_raw_touch(unsigned int values[], int length, int start_index){
        RawTouch raw_touch = RawTouch(start_index);
        for (int i = 0; i < length; ++i) {
            raw_touch.push_back(values[i]);
        }
        return raw_touch;
    }
};


#endif //DREA_V2_RAWTOUCH_H
