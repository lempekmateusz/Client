// IVehicleAidlInterface.aidl
package com.example.sensorserver;

// Declare any non-default types here with import statements

interface IVehicleAidlInterface {
    float getMagneticFieldValueX();
    float getMagneticFieldValueY();
    float getMagneticFieldValueZ();
}