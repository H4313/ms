package com.h4313.deephouse.msensor.action;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.SensorType;

public final class ActionSleep implements Action
{
	private static volatile ActionSleep instance = null;
	
	private ActionSleep()
	{
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
//		House.getInstance().updateSensor(RoomConstants.ID_BEDROOM, SensorType.FLAP, (Boolean)false);
	}
}
