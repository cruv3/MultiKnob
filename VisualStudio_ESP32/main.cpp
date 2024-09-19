#include "main.h"

#ifndef MOTOR_STRENGTH
#define MOTOR_STRENGTH 1
#endif
#ifndef SNAP_POINTS
#define SNAP_POINTS 1
#endif

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error "Bluetooth not enabled"
#endif


Configuration configuration = Configuration(MOTOR_STRENGTH, SNAP_POINTS, COMMON);
BluetoothSerial SerialBT;
ButtonHandler button_handler;
MotorController motor_controller = MotorController(configuration);
TouchHandler touch_handler;
SerialCommunication serial_communication = SerialCommunication(configuration, SerialBT);

bool BTStatus = false;

void setup() {
    Serial.begin(115200);
    while (!Serial);
    Serial.println("Setup started");
    if (!SerialBT.begin("ESP32MultiKnob")) {
        Serial.println("An error occurred initializing Bluetooth");
    } else {
        Serial.println("Bluetooth initialized successfully");
        BTStatus = true;
    }

    button_handler.setup();
    motor_controller.setup();
    touch_handler.setup(3); //Change Prescalar

    //Observers
    //button_handler.attach(&motor_controller);
    button_handler.attach(&serial_communication);
    touch_handler.attach(&motor_controller);

    if (configuration.get_knob_state() == STOP) {
        motor_controller.deactivate();
    } else {
        motor_controller.activate();
    }


    delay(100);

    xTaskCreate(
            run_motor_task,         // Task function.
            "Motor Task",           // name of task.
            10000,                  // Stack size of task
            nullptr,                // parameter of the task
            0,                      // priority of the task
            nullptr);               // Task handle to keep track of created task
}


void multiknopLoop(){
    button_handler.update_state();
    touch_handler.process_touch_ring_sensor();

    if (configuration.get_knob_state() != STOP) {
        serial_communication.update_data(
                touch_handler.getTouchCount(),
                touch_handler.get_touches(),
                motor_controller.get_motor_angle_in_degree(),
                motor_controller.get_snap_point()
        );
        serial_communication.write();
    }


    KnobState program_state = serial_communication.read();
    if (program_state != NONE) {
        if (program_state == STOP) {
            motor_controller.deactivate();
        } else {
            motor_controller.activate();
            serial_communication.update_data(
                    touch_handler.getTouchCount(),
                    touch_handler.get_touches(),
                    motor_controller.get_motor_angle_in_degree(),
                    motor_controller.get_snap_point()
            );
        }
        configuration.set_knob_state(program_state);
    }
}

void feedBack(){
    if (!SerialBT.available()) return;
    
    String receivedData = SerialBT.readStringUntil('\n');
    if (receivedData.startsWith("FEEDBACK:")) {
        int delimiterIndex = receivedData.indexOf(':');
        int commaIndex = receivedData.indexOf(',');

        if (delimiterIndex != -1 && commaIndex != -1) {
            String strengthStr = receivedData.substring(delimiterIndex + 1, commaIndex);
            String durationStr = receivedData.substring(commaIndex + 1);

            float strength = strengthStr.toFloat();
            uint8_t duration = durationStr.toInt();

            Serial.println(strength);
            Serial.println(duration);
            // Trigger motor feedback
            motor_controller.haptic_feedback(strength, duration);
        }
    }
}

//Core 1
void loop() {
    static bool wasConnected = false;
    
    if (SerialBT.connected()) {
        if (!wasConnected) {
            Serial.println("Bluetooth Connected");
            wasConnected = true;
        }
    } else {
        if (wasConnected) {
            Serial.println("Bluetooth Disconnected");
            wasConnected = false;
        }
    }

    multiknopLoop();
    feedBack();
}

//Core 2
[[noreturn]] void run_motor_task(void *) {
    Serial.println("Starting run motor Task");
    while (true) {
        motor_controller.run_motor();
    }
}