package com.h4313.deephouse.msensor.controller;

import java.util.List;

import com.h4313.deephouse.msensor.action.Action;
import com.h4313.deephouse.msensor.action.ActionGetUp;
import com.h4313.deephouse.sensor.Sensor;


public class Controller extends Thread
{	
	private volatile boolean alive;
	
	private static volatile Controller instance = null;
	
	private ActuatorListener actuatorListener;
	
	private ServerSender serverSender;
	
    /**
     * Constructeur de l'objet.
     */
    private Controller() {
        super();
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
    	Action act = null;
//    	switch(0)
//    	{
//    		case 0:
//    			act = new ActionGetUp();
//    		break;
//    		default:
//    	}
    	act = new ActionGetUp();
    	
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
