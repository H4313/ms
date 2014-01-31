package com.h4313.deephouse.mactuator.controller;

import com.h4313.deephouse.housemodel.House;

public class Controller extends Thread
{	
	private volatile boolean alive;
	
	private static volatile Controller instance = null;
	
	private ServerListener serverListener;
	
	private SensorSender sensorSender;
	
	private House house;
	
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
    
    public void initServerListener(int port)
    {
    	serverListener = new ServerListener(port);
    }
    
    public void initSensorSender(String host, int port)
    {
    	sensorSender = new SensorSender(host, port);
    }
    
    @Override
	public void run()
	{
		try
		{			
			while(alive)
			{
				String message = this.serverListener.getMessage();
				
				if(message != null)
				{
					// TODO : AFFICHER LES INFOS SOUS FORME EXPLICITE
			    	System.out.println("Message : " + message);
			    	
			    	sensorSender.submitMessage(message);	
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
    
    public void stopController()
    {
    	this.alive = false;
    	
    	try
    	{
	    	this.serverListener.stopListener();
	    	this.sensorSender.stopSender();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
