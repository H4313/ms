package com.h4313.deephouse.msensor.action;

import java.util.List;

import com.h4313.deephouse.sensor.Sensor;

public interface Action
{
	public List<Sensor<Object>> getSensorList();
}
