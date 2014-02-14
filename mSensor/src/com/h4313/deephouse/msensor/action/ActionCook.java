package com.h4313.deephouse.msensor.action;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.SensorType;

public class ActionCook implements Action
{
	private static volatile ActionCook instance = null;
	
	private ActionCook()
	{
	}
	
	/**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static ActionCook getInstance() {
        if (ActionCook.instance == null) {
           synchronized(ActionCook.class) {
             if (ActionCook.instance == null) {
            	 ActionCook.instance = new ActionCook();
             }
           }
        }
        return ActionCook.instance;
    }
	
	public void run()
	{
		House.getInstance().updateSensor(SensorType.PRESENCE, (Boolean)false);
		House.getInstance().updateSensor(SensorType.LIGHT, (Boolean)false);
		House.getInstance().updateSensor(RoomConstants.ID_KITCHEN, SensorType.PRESENCE, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_KITCHEN, SensorType.LIGHT, (Boolean)true);
	}
}
