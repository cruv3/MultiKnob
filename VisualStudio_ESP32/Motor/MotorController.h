//
// Created by Joel Neumann on 21.12.23.
//

#ifndef DREA_V2_MOTORCONTROLLER_H
#define DREA_V2_MOTORCONTROLLER_H

#define SENSOR_PIN 16
#define MOTOR_POLE_PAIRS 11
#define DRIVER_PINS 32, 33, 25, 26, 27, 14

#include <Preferences.h>
#include "SimpleFOC.h"
#include "Button/ButtonHandler.h"
#include "../Configuration/Configuration.h"
#include "../Trill/observer/TouchCountObserver.h"

class MotorController : public ButtonObserver, public TouchCountObserver {
protected:
    const Configuration &configuration;
    TouchSnapConfiguration current_motor_configuration = configuration.get_touch_snap_configuration(0);

    MagneticSensorSPI sensor = MagneticSensorSPI(AS5048_SPI, SENSOR_PIN);
    BLDCMotor* motor = new BLDCMotor(MOTOR_POLE_PAIRS);
    BLDCDriver6PWM driver = BLDCDriver6PWM(DRIVER_PINS);

    float current_motor_angle = 0;
    float snap_current_angle = 0;
    int snap_point = 0;

    bool motor_on = false;

    float calculate_force(float current_position, float target_position) const {
        float modifier = current_motor_configuration.strength * 20; //motor.velocity_limit
        float position_difference = target_position - current_position;
        float force = position_difference * modifier;

        return force;
    }

    float normalize_angle(float angle){
        angle = fmod(angle, 2 * PI);
        if(angle > PI){
            angle -= 2 * PI;
        }else if(angle < -PI){
            angle += 2 * PI;
        }
        return angle;
    }

    float calculate_snap_target(float current_angle) const {
        float degree_per_snap_point = 360.0f / (float) current_motor_configuration.snap_point;
        int closest_snap_point_index = (int) round(degrees(current_angle) / degree_per_snap_point);
        float closest_snap_point_degree = (float) closest_snap_point_index * degree_per_snap_point;
        return radians(closest_snap_point_degree);
    }

    void snap_to_target(float force) {
        float P_value = fabsf(force);
        motor->PID_velocity.P = P_value;
        motor->move(motor->PID_velocity(force));
    }

    void update_snap_point(float target){
        snap_point = (int) round(degrees(target) / (360.0f / (current_motor_configuration.snap_point)));
    }

public:
    explicit MotorController(const Configuration &config) : configuration(config) {}

    void haptic_feedback(float strength, uint8_t direction_length) {
        float P_value = fabsf(strength);

        motor->PID_velocity.P = P_value;
        motor->move(strength);
        for (uint8_t i = 0; i < direction_length; i++) {
            motor->loopFOC();
            delay(1);
        }
        motor->move(-strength);
        for (uint8_t i = 0; i < direction_length; i++) {
            motor->loopFOC();
            delay(1);
        }
        motor->PID_velocity.P = 0;
        motor->move(0);
    }


    void setup() {
        sensor.init();

        driver.voltage_power_supply = 5;
        driver.init();
        motor->linkDriver(&driver);
        motor->linkSensor(&sensor);

        motor->foc_modulation = FOCModulationType::SpaceVectorPWM;
        motor->controller = MotionControlType::torque;
        motor->voltage_limit = 2;

        motor->PID_velocity.P = 0;
        motor->PID_velocity.I = 0;
        motor->PID_velocity.D = 0;
        motor->PID_velocity.output_ramp = 10000;
        //motor->PID_velocity.limit = 12;

        Preferences flash_storage;
        flash_storage.begin("motor", false);
        const char *namespace_zero_electric_angle = "zea";

        //If you want to reset the ESP32 run this code one time and then upload without this code!
        //flash_storage.clear();

        motor->init();
        if (flash_storage.isKey(namespace_zero_electric_angle)) {
            motor->zero_electric_angle = flash_storage.getFloat(namespace_zero_electric_angle);
            motor->sensor_direction = Direction::CCW;
        }
        motor->initFOC();
        if (!flash_storage.isKey(namespace_zero_electric_angle)) {
            Serial.print("motor zero electric angle: ");
            Serial.println(motor->zero_electric_angle);
            flash_storage.putFloat(namespace_zero_electric_angle, motor->zero_electric_angle);
        }
        motor->move(0);
        run_motor();
    }

    void button_update(const bool &is_button_pressed) override {
        //TODO: What with a call from another Thread
        haptic_feedback(5, 3);
    }

    void touch_count_update(const unsigned int &touch_count) override {
        current_motor_configuration = configuration.get_touch_snap_configuration(touch_count);
        if(motor_on && configuration.get_knob_state() == MULTIKNOB){
            if(touch_count > 0){
                haptic_feedback(10, 8);
            }
        }
    }

    //TODO: Add idle Motor when the force is nearly 0 for a given time
    void run_motor() {
        motor->loopFOC();
        if(motor_on){
            current_motor_angle = motor->shaft_angle;
            float target = calculate_snap_target(current_motor_angle);
            update_snap_point(target);
            snap_to_target(calculate_force(current_motor_angle, target));
        }else{
            motor->move(0);
        }
    }

    float get_motor_angle_in_degree() const {
        return float degrees(current_motor_angle);
    }

    int get_snap_point() const{
        return snap_point;
    }

    void activate(){
        motor_on = true;
    }

    void deactivate(){
        motor_on = false;
    }
};


#endif //DREA_V2_MOTORCONTROLLER_H