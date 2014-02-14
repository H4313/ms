package com.h4313.deephouse.msensor.action;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.SensorType;

public class ActionDinner implements Action
{
	private static volatile ActionDinner instance = null;
	
	private ActionDinner()
	{
	}
	
	/**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static ActionDinner getInstance() {
        if (ActionDinner.instance == null) {
           synchronized(ActionDinner.class) {
             if (ActionDinner.instance == null) {
            	 ActionDinner.instance = new ActionDinner();
             }
           }
        }
        return ActionDinner.instance;
    }
	
	public void run()
	{
		House.getInstance().updateSensor(SensorType.PRESENCE, (Boolean)false);
		House.getInstance().updateSensor(SensorType.LIGHT, (Boolean)false);
		House.getInstance().updateSensor(RoomConstants.ID_CORRIDOR, SensorType.PRESENCE, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_CORRIDOR, SensorType.LIGHT, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_LIVING_ROOM, SensorType.PRESENCE, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_LIVING_ROOM, SensorType.LIGHT, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_KITCHEN, SensorType.PRESENCE, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_KITCHEN, SensorType.LIGHT, (Boolean)true);
	}
}