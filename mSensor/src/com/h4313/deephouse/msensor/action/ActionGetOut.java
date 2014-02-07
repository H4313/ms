package com.h4313.deephouse.msensor.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.Room;
import com.h4313.deephouse.sensor.Sensor;
import com.h4313.deephouse.sensor.SensorType;

public final class ActionGetOut implements Action
{
	private List<Sensor<Object>> sensorList;

	private static volatile ActionGetOut instance = null;
	
	private ActionGetOut()
	{
		// Initialisation
		this.sensorList = new ArrayList<Sensor<Object>>();
		
		// Process
		for(Room room : House.getInstance().getRooms())
		{
			Set<Map.Entry<String, Sensor<Object>>> set = room.getSensors().entrySet();
	
			for(Map.Entry<String, Sensor<Object>> entry : set)
			{
				Sensor<Object> sensor = entry.getValue();
				
				// LIGHT OFF
				if(sensor.getType().equals(SensorType.LIGHT))
				{
					sensor.setLastValue(false);
					this.sensorList.add(sensor);
				} // PRESENCE OFF
				else if(sensor.getType().equals(SensorType.PRESENCE))
				{
					sensor.setLastValue(false);
					this.sensorList.add(sensor);
				}
			}
		}
	}
	
	/**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static ActionGetOut getInstance() {
        if (ActionGetOut.instance == null) {
           synchronized(ActionGetOut.class) {
             if (ActionGetOut.instance == null) {
            	 ActionGetOut.instance = new ActionGetOut();
             }
           }
        }
        return ActionGetOut.instance;
    }
	
	public List<Sensor<Object>> getSensorList()
	{
		return this.sensorList;
	}
}

