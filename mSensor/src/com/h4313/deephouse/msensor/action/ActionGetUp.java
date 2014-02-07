package com.h4313.deephouse.msensor.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.Room;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.msensor.controller.Controller;
import com.h4313.deephouse.sensor.Sensor;
import com.h4313.deephouse.sensor.SensorType;

public class ActionGetUp implements Action
{
	private List<Sensor<Object>> sensorList;

	private static volatile ActionGetUp instance = null;
	
	private ActionGetUp()
	{
		// Initialisation
		this.sensorList = new ArrayList<Sensor<Object>>();
		
		// Process
		Room bedRoom = House.getInstance().getRooms().get(RoomConstants.ID_BEDROOM);
		
		Set<Map.Entry<String, Sensor<Object>>> set = bedRoom.getSensors().entrySet();

		for(Map.Entry<String, Sensor<Object>> entry : set)
		{
			Sensor<Object> sensor = entry.getValue();
			
			// LIGHT ON
			if(sensor.getType().equals(SensorType.LIGHT))
			{
				sensor.setLastValue(true);
				this.sensorList.add(sensor);
			}
		}
	}
	
	/**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static ActionGetUp getInstance() {
        if (ActionGetUp.instance == null) {
           synchronized(ActionGetUp.class) {
             if (ActionGetUp.instance == null) {
            	 ActionGetUp.instance = new ActionGetUp();
             }
           }
        }
        return ActionGetUp.instance;
    }
	
	public List<Sensor<Object>> getSensorList()
	{
		return this.sensorList;
	}
}