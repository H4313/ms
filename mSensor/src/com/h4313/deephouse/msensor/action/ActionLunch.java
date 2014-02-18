package com.h4313.deephouse.msensor.action;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.SensorType;

public class ActionLunch
{
	private static volatile ActionLunch instance = null;
	
	private ActionLunch()
	{
	}
	
	/**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static ActionLunch getInstance() {
        if (ActionLunch.instance == null) {
           synchronized(ActionLunch.class) {
             if (ActionLunch.instance == null) {
            	 ActionLunch.instance = new ActionLunch();
             }
           }
        }
        return ActionLunch.instance;
    }
	
	public void run()
	{
		House.getInstance().updateSensor(SensorType.PRESENCE, (Boolean)false);
		House.getInstance().updateSensor(SensorType.LIGHT, (Boolean)false);
		House.getInstance().updateSensor(RoomConstants.ID_LIVING_ROOM, SensorType.PRESENCE, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_LIVING_ROOM, SensorType.LIGHT, (Boolean)true);
	}
}
