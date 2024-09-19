//
// Created by Joel Neumann on 15.05.23.
//

#ifndef DREA_V2_MAIN_H
#define DREA_V2_MAIN_H

#include <iostream>
#include <sstream>
#include <vector>

#include "Motor/MotorController.h"
#include "Button/ButtonHandler.h"
#include "Trill/TouchHandler.h"
#include "Serial/SerialCommunication.h"
#include "BluetoothSerial.h"

[[noreturn]] void run_motor_task(void * param);

#endif //DREA_V2_MAIN_H