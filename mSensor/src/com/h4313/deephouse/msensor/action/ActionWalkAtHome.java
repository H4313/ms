package com.h4313.deephouse.msensor.action;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.Room;
import com.h4313.deephouse.sensor.SensorType;
import com.h4313.deephouse.util.Tool;

public class ActionWalkAtHome
{
	private static volatile ActionWalkAtHome instance = null;
	
	private ActionWalkAtHome()
	{
	}
	
	/**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static ActionWalkAtHome getInstance() {
        if (ActionWalkAtHome.instance == null) {
           synchronized(ActionWalkAtHome.class) {
             if (ActionWalkAtHome.instance == null) {
            	 ActionWalkAtHome.instance = new ActionWalkAtHome();
             }
           }
        }
        return ActionWalkAtHome.instance;
    }
	
	public void run()
	{
//		for(Room room : House.getInstance().getRooms())
//		{
//			if(Tool.randInt(0, 30) == 15)
//			{
//				House.getInstance().updateSensor(room.getIdRoom(), SensorType.PRESENCE, (Boolean)true);
//				House.getInstance().updateSensor(room.getIdRoom(), SensorType.LIGHT, (Boolean)true);
//			}
//		}
	}
}
