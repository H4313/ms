package com.h4313.deephouse.msensor.controller;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.Room;
import com.h4313.deephouse.msensor.action.ActionBreakFast;
import com.h4313.deephouse.msensor.action.ActionGetOut;
import com.h4313.deephouse.msensor.action.ActionGetUp;
import com.h4313.deephouse.msensor.action.ActionSleep;
import com.h4313.deephouse.msensor.action.ActionWorkOffice;
import com.h4313.deephouse.sensor.Sensor;
import com.h4313.deephouse.util.DeepHouseCalendar;
import com.h4313.deephouse.vue.MainVue;


public final class Controller extends Thread
{	
	private volatile boolean alive;
	
	private static volatile Controller instance = null;
	
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
    	switch(cal.get(Calendar.HOUR_OF_DAY))
    	{
    		case 7:
    			if(cal.get(Calendar.MINUTE) >= 0 && cal.get(Calendar.MINUTE) <= 10)
    				ActionGetUp.getInstance().run();
    			else if(cal.get(Calendar.MINUTE) >= 10 && cal.get(Calendar.MINUTE) <= 30)
    				ActionBreakFast.getInstance().run();
    		break;
    		case 9: case 10:
    			ActionWorkOffice.getInstance().run();
    		break;
    		case 11:
    			ActionSleep.getInstance().run();
    		break;
    		default:
    			ActionGetOut.getInstance();
    	}

		for(Room room : House.getInstance().getRooms())
		{
			Set<Map.Entry<String, Sensor<Object>>> set = room.getSensors().entrySet();
	
			for(Map.Entry<String, Sensor<Object>> entry : set)
			{
				Sensor<Object> sensor = entry.getValue();

				this.serverSender.submitMessage(sensor.getFrame());
			}
		}
    }
    
    @Override
	public void run()
	{
    	String message = null;
		try
		{	
			System.out.println("run controller");
			while(alive)
			{
				System.out.println("message : " + message);
				
				message = actuatorListener.getMessage();
				
				if(message != null)
				{
					// TODO : Que faire des messages ?
					System.out.println("Message : " + message);
				}
				else
				{
					Thread.sleep(2000);
				}
				
				Thread.sleep(1000);

				runSimulator();
				vueSensor.refresh(); //Met � jour la vue 
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
