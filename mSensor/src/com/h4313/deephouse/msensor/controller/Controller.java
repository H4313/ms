package com.h4313.deephouse.msensor.controller;

import java.util.Calendar;
import java.util.List;

import com.h4313.deephouse.msensor.action.Action;
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
     * MÃ©thode permettant de renvoyer une instance de la classe Singleton
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
    	Action act = null;
    	
    	Calendar cal = DeepHouseCalendar.getInstance().getCalendar();
    	switch(cal.get(Calendar.HOUR_OF_DAY))
    	{
    		case 7:
    			if(cal.get(Calendar.MINUTE) >= 0 && cal.get(Calendar.MINUTE) <= 10)
    				act = ActionGetUp.getInstance();
    			else if(cal.get(Calendar.MINUTE) >= 10 && cal.get(Calendar.MINUTE) <= 30)
    				act = ActionBreakFast.getInstance();
    		break;
    		case 9: case 10:
    			act = ActionWorkOffice.getInstance();
    		break;
    		case 11:
    			act = ActionSleep.getInstance();
    		break;
    		default:
    			act = ActionGetOut.getInstance();
    	}
    	
    	
    	if(act != null)
    	{
			List<Sensor<Object>> sensorList = act.getSensorList();
			System.out.println("SensorList = " + sensorList);
			for(Sensor<Object> sensor : sensorList)
			{
				System.out.println("Envoye : " + sensor.getFrame());
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
				vueSensor.refresh(); //Met à jour la vue 
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
