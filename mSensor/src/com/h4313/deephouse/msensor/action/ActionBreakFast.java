package com.h4313.deephouse.msensor.action;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.SensorType;

public final class ActionBreakFast implements Action
{
	private static volatile ActionBreakFast instance = null;
	
	private ActionBreakFast()
	{
	}
	
	/**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static ActionBreakFast getInstance() {
        if (ActionBreakFast.instance == null) {
           synchronized(ActionBreakFast.class) {
             if (ActionBreakFast.instance == null) {
            	 ActionBreakFast.instance = new ActionBreakFast();
             }
           }
        }
        return ActionBreakFast.instance;
    }
	
	public void run()
	{
		House.getInstance().updateSensor(SensorType.PRESENCE, (Boolean)false);
		House.getInstance().updateSensor(SensorType.LIGHT, (Boolean)false);
		House.getInstance().updateSensor(RoomConstants.ID_KITCHEN, SensorType.PRESENCE, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_KITCHEN, SensorType.LIGHT, (Boolean)true);
	}
}
