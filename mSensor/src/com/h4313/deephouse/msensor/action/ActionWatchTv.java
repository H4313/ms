package com.h4313.deephouse.msensor.action;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.SensorType;

public class ActionWatchTv 
{
	private static volatile ActionWatchTv instance = null;
	
	private ActionWatchTv()
	{
	}
	
	/**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static ActionWatchTv getInstance() {
        if (ActionWatchTv.instance == null) {
           synchronized(ActionWatchTv.class) {
             if (ActionWatchTv.instance == null) {
            	 ActionWatchTv.instance = new ActionWatchTv();
             }
           }
        }
        return ActionWatchTv.instance;
    }
	
	public void run()
	{
		House.getInstance().updateSensor(SensorType.PRESENCE, (Boolean)false);
		House.getInstance().updateSensor(SensorType.LIGHT, (Boolean)false);
		House.getInstance().updateSensor(RoomConstants.ID_LIVING_ROOM, SensorType.PRESENCE, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_LIVING_ROOM, SensorType.LIGHT, (Boolean)true);
	}
}
