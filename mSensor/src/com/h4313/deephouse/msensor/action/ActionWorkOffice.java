package com.h4313.deephouse.msensor.action;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.sensor.SensorType;

public final class ActionWorkOffice implements Action
{
	private static volatile ActionWorkOffice instance = null;
	
	private ActionWorkOffice()
	{
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


    public void run()
    {
    	House.getInstance().updateSensor(SensorType.PRESENCE, (Boolean)false);
		House.getInstance().updateSensor(SensorType.LIGHT, (Boolean)false);
		House.getInstance().updateSensor(RoomConstants.ID_OFFICE, SensorType.PRESENCE, (Boolean)true);
		House.getInstance().updateSensor(RoomConstants.ID_OFFICE, SensorType.LIGHT, (Boolean)true);
    }
}
