package com.h4313.deephouse.msensor.main;

import java.util.List;
import java.util.Scanner;

import com.h4313.deephouse.actuator.ActuatorType;
import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.Room;
import com.h4313.deephouse.msensor.controller.Controller;
import com.h4313.deephouse.sensor.SensorType;
import com.h4313.deephouse.util.DecToHexConverter;
import com.h4313.deephouse.util.DeepHouseCalendar;

public class Main
{
	public static void main(String[] args)
	{
		if(args.length == 3)
		{
		// Initialisation de l'horloge de simulation
		DeepHouseCalendar.getInstance().init();
		
		// Initialisation de la maison // TODO : RETIRER POUR LA PRODUCTION
		try
		{
			List<Room> rooms = House.getInstance().getRooms();
			int id = 0;
			for(Room room : rooms)
			{
				room.addSensor(DecToHexConverter.decToHex(id++), SensorType.TEMPERATURE);
				room.addSensor(DecToHexConverter.decToHex(id++), SensorType.WINDOW);
				room.addSensor(DecToHexConverter.decToHex(id++), SensorType.LIGHT);
				room.addSensor(DecToHexConverter.decToHex(id++), SensorType.PRESENCE);

				room.addActuator(DecToHexConverter.decToHex(id++), ActuatorType.LIGHTCONTROL);
				//room.addActuator(DecToHexConverter.decToHex(id++), ActuatorType.AIRCONDITION);
				room.addActuator(DecToHexConverter.decToHex(id++), ActuatorType.RADIATOR);
				room.addActuator(DecToHexConverter.decToHex(id++), ActuatorType.WINDOWCLOSER);
			}
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// Initialisation du reseau
		Controller.getInstance().initServerListener(Integer.valueOf(args[0]).intValue());
		Controller.getInstance().initSensorSender(args[1], Integer.valueOf(args[2]).intValue());
		Controller.getInstance().start();
		
		
		// En attente de l'arret de la machine
		String str = "";
		Scanner scExit;
		do {
			scExit = new Scanner(System.in);
			System.out.println("/// Tapez 'EXIT' pour arreter la machine ///");
			str = scExit.nextLine();
		} while (!str.toLowerCase().contains((CharSequence) "exit"));
		scExit.close();
	
		Controller.getInstance().stopController();
		
		
		System.out.println("Arret du serveur");

		System.exit(0);
		}
	}
}
