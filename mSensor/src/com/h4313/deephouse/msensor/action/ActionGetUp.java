package com.h4313.deephouse.msensor.action;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.SensorType;

public final class ActionGetUp implements Action
{
	private static volatile ActionGetUp instance = null;
	
	private ActionGetUp()
	{
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
	
	public void run()
	{
		House.getInstance().updateSensor(RoomConstants.ID_BEDROOM, SensorType.FLAP, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_BEDROOM, SensorType.WINDOW, (Boolean)true);
		
		House.getInstance().updateSensor(RoomConstants.ID_BEDROOM, SensorType.PRESENCE, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_BEDROOM, SensorType.LIGHT, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_BEDROOM, SensorType.DOOR, (Boolean)true);
	}
}