package com.h4313.deephouse.msensor.action;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.SensorType;

public final class ActionGetOut implements Action
{
//	private List<Sensor<Object>> sensorList;

	private static volatile ActionGetOut instance = null;
	
	private ActionGetOut()
	{
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

    public void run()
    {
    	House.getInstance().updateSensor(SensorType.PRESENCE, (Boolean)false);
    	House.getInstance().updateSensor(SensorType.LIGHT, (Boolean)false);
    	House.getInstance().updateSensor(SensorType.DOOR, (Boolean)false);
    	House.getInstance().updateSensor(SensorType.FLAP, (Boolean)false);
    	House.getInstance().updateSensor(SensorType.WINDOW, (Boolean)false);
    }
}

