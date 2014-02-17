package com.h4313.deephouse.msensor.controller;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import com.h4313.deephouse.actuator.Actuator;
import com.h4313.deephouse.actuator.ActuatorType;
import com.h4313.deephouse.frame.Frame;
import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.Room;
import com.h4313.deephouse.msensor.action.*;
import com.h4313.deephouse.sensor.Sensor;
import com.h4313.deephouse.sensor.SensorType;
import com.h4313.deephouse.util.DeepHouseCalendar;
import com.h4313.deephouse.vue.MainVue;


public final class Controller extends Thread
{	
	private volatile boolean alive;
	
	private static volatile Controller instance = null;
	
	private Double previousTime;
	
	private ActuatorListener actuatorListener;
	
	private ServerSender serverSender;
	
	private MainVue vueSensor=null;
	
    /**
     * Constructeur de l'objet.
     */
    private Controller() {
        super();
        vueSensor=MainVue.init(MainVue.VUE_SENSOR);
        this.alive = true;
    }

    /**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static Controller getInstance() {
        if (Controller.instance == null) {
           synchronized(Controller.class) {
             if (Controller.instance == null) {
            	 Controller.instance = new Controller();
             }
           }
        }
        return Controller.instance;
    }
    
    private void runSimulator()
    {
    	Calendar cal = DeepHouseCalendar.getInstance().getCalendar();
    	switch(cal.get(Calendar.DAY_OF_WEEK))
    	{
    		case Calendar.SATURDAY:
    			switch(cal.get(Calendar.HOUR_OF_DAY))
		    	{
		    		case 10:
		    			if(cal.get(Calendar.MINUTE) >= 0 && cal.get(Calendar.MINUTE) <= 10)
		    				ActionGetUp.getInstance().run();
		    			else if(cal.get(Calendar.MINUTE) >= 10 && cal.get(Calendar.MINUTE) <= 30)
		    				ActionBreakFast.getInstance().run();
		    			else
		    				ActionGetOut.getInstance(); // Il va faire des courses
		    		break;
		    		case 12:
		    			if(cal.get(Calendar.MINUTE) <= 30)
		    				ActionCook.getInstance().run();
		    			else
			    			ActionDinner.getInstance().run();
		    		case 13: case 14: case 15: case 16: case 17: case 18: case 19:
		    			ActionWorkOffice.getInstance().run();
		    			ActionWalkAtHome.getInstance().run();
		    		break;
		    		case 20:
		    			if(cal.get(Calendar.MINUTE) >= 30)
		    				ActionCook.getInstance().run();
		    			else
			    			ActionDinner.getInstance().run();
		    			
		    			ActionWalkAtHome.getInstance().run();
		    		break;
		    		case 21: case 22:
		    			ActionWatchTv.getInstance().run();
		    			ActionWalkAtHome.getInstance().run();
		    		break;
		    		case 23:
		    			ActionSleep.getInstance().run();
		    		break;
		    		default:
		    	}
	    	break;
    		case Calendar.SUNDAY:
    			switch(cal.get(Calendar.HOUR_OF_DAY))
		    	{
		    		case 9:
		    			ActionGetOut.getInstance();
		    		break;
		    		case 22:
		    			if(cal.get(Calendar.MINUTE) >= 30)
		    				ActionWalkAtHome.getInstance().run();
		    		break;
		    		case 23:
		    			ActionSleep.getInstance().run();
		    		break;
		    		default:
		    	}
	    	break;
	    	default:
	    		switch(cal.get(Calendar.HOUR_OF_DAY))
		    	{
		    		case 7:
		    			if(cal.get(Calendar.MINUTE) >= 0 && cal.get(Calendar.MINUTE) <= 10)
		    				ActionGetUp.getInstance().run();
		    			else if(cal.get(Calendar.MINUTE) >= 10 && cal.get(Calendar.MINUTE) <= 30)
		    				ActionBreakFast.getInstance().run();
		    			else
		    				ActionGetOut.getInstance();
		    		break;
		    		case 19:
		    			if(cal.get(Calendar.MINUTE) >= 30)
		    				ActionCook.getInstance().run();
		    			
		    			ActionWalkAtHome.getInstance().run();
		    		break;
		    		case 20:
		    			ActionDinner.getInstance().run();
		    			ActionWalkAtHome.getInstance().run();
		    		break;
		    		case 21: case 22:
		    			ActionWatchTv.getInstance().run();
		    			ActionWalkAtHome.getInstance().run();
		    		break;
		    		case 23:
		    			ActionSleep.getInstance().run();
		    		break;
		    		default:
		    	}
    	}

		for(Room room : House.getInstance().getRooms())
		{
			Set<Map.Entry<String, Sensor<Object>>> set = room.getSensors().entrySet();
	
			for(Map.Entry<String, Sensor<Object>> entry : set)
			{
				Sensor<Object> sensor = entry.getValue();
				this.serverSender.submitMessage(sensor.composeFrame());
			}
		}
    }
    
    private void updateSensorValue(Actuator<Object> actuator)
    {
    	Set<Map.Entry<String, Sensor<Object>>> set = 
    	    	actuator.getSensors().entrySet();
    	
		for(Map.Entry<String, Sensor<Object>> entry : set)
		{
			Sensor<Object> sensor = entry.getValue();

			if(actuator.getType() == ActuatorType.RADIATOR)
			{
				if(previousTime == null) {
					previousTime = (double) DeepHouseCalendar.getInstance().getCalendar().getTimeInMillis()/1000;
				}
				Double deltaTime = (double) DeepHouseCalendar.getInstance().getCalendar().getTimeInMillis()/1000 - previousTime;
				previousTime = (double) DeepHouseCalendar.getInstance().getCalendar().getTimeInMillis()/1000;
				Double deltaTemp = (Double) actuator.getLastValue() - (Double) sensor.getLastValue();
				Double tempRoom = (Double) sensor.getLastValue() + deltaTemp * (1/(1+Math.exp(-deltaTime/3600))) + 2*Math.random()-1;
				sensor.setLastValue(tempRoom);
			}
			else
			{
				// BOOLEAN
				sensor.setLastValue(actuator.getLastValue());
			}
		}
    }
    
    @Override
	public void run()
	{
    	String message = null;
		try
		{	
			while(alive)
			{			
				message = actuatorListener.getMessage();
				
				if(message != null)
				{			
					while(message != null) {
						Frame frame = new Frame(message);
						Actuator<Object> actuator = House.getInstance().updateActuator(frame);
						updateSensorValue(actuator);
						message = actuatorListener.getMessage();
					}
				}
				else
				{
					Thread.sleep(2000);
				}
				Thread.sleep(10);
				runSimulator();
				vueSensor.refresh(); // Met a jour la vue 
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
    
    public void initServerListener(int port)
    {
    	this.actuatorListener = new ActuatorListener(port);
    }
    
    public void initSensorSender(String host, int port)
    {
    	this.serverSender = new ServerSender(host, port);
    }
    
    public void stopController()
    {
    	this.alive = false;
    	
    	try
    	{
	    	this.actuatorListener.stopListener();
	    	this.serverSender.stopSender();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
