package com.example.aismarinetracker.decoder.reports;

public interface IDynamicPositionReport extends IPositionReport {
    float getLatitude();
    float getLongitude();
    float getSpeedOverGround();
    float getCourseOverGround();
}
