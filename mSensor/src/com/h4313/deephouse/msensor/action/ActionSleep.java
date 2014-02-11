package com.h4313.deephouse.msensor.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.Room;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.Sensor;
import com.h4313.deephouse.sensor.SensorType;

public final class ActionSleep implements Action
{
	private static volatile ActionSleep instance = null;
	
	private ActionSleep()
	{
		// Initialisation
//		this.sensorList = new ArrayList<Sensor<Object>>();
//		
//		// Process
//		Room bedRoom = House.getInstance().getRooms().get(RoomConstants.ID_BEDROOM);
//			
//		Map<String, Sensor<Object>> sensorList = bedRoom.getSensors();
//
//		// LIGHT OFF
//		Sensor<Object> light = sensorList.get(SensorType.LIGHT);
//		light.setLastValue(false);
//		this.sensorList.add(light);
//		
//		// PRESENCE ON
//		Sensor<Object> presence = sensorList.get(SensorType.PRESENCE);
//		presence.setLastValue(true);
//		this.sensorList.add(presence);
	}
	
	/**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static ActionSleep getInstance() {
        if (ActionSleep.instance == null) {
           synchronized(ActionSleep.class) {
             if (ActionSleep.instance == null) {
            	 ActionSleep.instance = new ActionSleep();
             }
           }
        }
        return ActionSleep.instance;
    }
	
	public void run()
	{
		House.getInstance().updateSensor(SensorType.PRESENCE, (Boolean)false);
		House.getInstance().updateSensor(SensorType.LIGHT, (Boolean)false);
		
		House.getInstance().updateSensor(RoomConstants.ID_BEDROOM, SensorType.PRESENCE, (Boolean)true);
	}
}
