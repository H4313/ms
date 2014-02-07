package com.h4313.deephouse.msensor.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.Room;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.Sensor;
import com.h4313.deephouse.sensor.SensorType;

public final class ActionWorkOffice implements Action
{
	private List<Sensor<Object>> sensorList;

	private static volatile ActionWorkOffice instance = null;
	
	private ActionWorkOffice()
	{
		// Initialisation
		this.sensorList = new ArrayList<Sensor<Object>>();
		
		// Process
		Room office = House.getInstance().getRooms().get(RoomConstants.ID_OFFICE);
		
		Set<Map.Entry<String, Sensor<Object>>> set = office.getSensors().entrySet();

		for(Map.Entry<String, Sensor<Object>> entry : set)
		{
			Sensor<Object> sensor = entry.getValue();
			
			// LIGHT ON
			if(sensor.getType().equals(SensorType.LIGHT))
			{
				sensor.setLastValue(true);
				this.sensorList.add(sensor);
			} // TEMP 21C
			else if(sensor.getType().equals(SensorType.TEMPERATURE))
			{
				sensor.setLastValue(21);
				this.sensorList.add(sensor);
			} // PRESENCE ON
			else if(sensor.getType().equals(SensorType.PRESENCE))
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
    public final static ActionWorkOffice getInstance() {
        if (ActionWorkOffice.instance == null) {
           synchronized(ActionWorkOffice.class) {
             if (ActionWorkOffice.instance == null) {
            	 ActionWorkOffice.instance = new ActionWorkOffice();
             }
           }
        }
        return ActionWorkOffice.instance;
    }
	
	public List<Sensor<Object>> getSensorList()
	{
		return this.sensorList;
	}
}
