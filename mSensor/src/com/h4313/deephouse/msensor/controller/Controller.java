package com.h4313.deephouse.msensor.controller;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.h4313.deephouse.actuator.Actuator;
import com.h4313.deephouse.actuator.ActuatorType;
import com.h4313.deephouse.frame.Frame;
import com.h4313.deephouse.housemodel.House;
import com.h4313.deephouse.housemodel.Room;
import com.h4313.deephouse.housemodel.RoomConstants;
import com.h4313.deephouse.msensor.action.*;
import com.h4313.deephouse.sensor.Sensor;
import com.h4313.deephouse.sensor.SensorType;
import com.h4313.deephouse.util.DeepHouseCalendar;
import com.h4313.deephouse.vue.MainVue;

public final class Controller extends Thread {
	private volatile boolean alive;

	private static volatile Controller instance = null;

	private Double previousTime;

	private ActuatorListener actuatorListener;

	private ServerSender serverSender;

	private MainVue vueSensor = null;

	private int previousCase;

	/**
	 * Constructeur de l'objet.
	 */
	private Controller() {
		super();
		vueSensor = MainVue.init(MainVue.VUE_SENSOR);
		this.alive = true;
		this.previousCase = -1;
	}

	/**
	 * Méthode permettant de renvoyer une instance de la classe Singleton
	 * 
	 * @return Retourne l'instance du singleton.
	 */
	public final static Controller getInstance() {
		if (Controller.instance == null) {
			synchronized (Controller.class) {
				if (Controller.instance == null) {
					Controller.instance = new Controller();
				}
			}
		}
		return Controller.instance;
	}

	private void runSimulator() {
		Calendar cal = DeepHouseCalendar.getInstance().getCalendar();
		int currentCase = -1;

		// VACANCES
		if ((cal.get(Calendar.MONTH) == Calendar.MARCH
				&& cal.get(Calendar.DAY_OF_MONTH) >= 2 && cal
				.get(Calendar.DAY_OF_MONTH) <= 15)
				|| (cal.get(Calendar.MONTH) == Calendar.MAY
						&& cal.get(Calendar.DAY_OF_MONTH) >= 4 && cal
						.get(Calendar.DAY_OF_MONTH) <= 9)
				|| (cal.get(Calendar.MONTH) == Calendar.AUGUST
						&& cal.get(Calendar.DAY_OF_MONTH) >= 11 && cal
						.get(Calendar.DAY_OF_MONTH) <= 22)
				|| (cal.get(Calendar.MONTH) == Calendar.DECEMBER
						&& cal.get(Calendar.DAY_OF_MONTH) >= 22 && cal
						.get(Calendar.DAY_OF_MONTH) <= 29)) {
			currentCase = 0;

			if (this.previousCase != currentCase)
				ActionGetOut.getInstance().run();
		} else {
			switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SATURDAY:
				switch (cal.get(Calendar.HOUR_OF_DAY)) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
					currentCase = 1;

					if (this.previousCase != currentCase)
						ActionSleep.getInstance().run();
					break;
				case 10:
					if (cal.get(Calendar.MINUTE) >= 0
							&& cal.get(Calendar.MINUTE) <= 10) {
						currentCase = 2;

						if (this.previousCase != currentCase)
							ActionGetUp.getInstance().run();
					} else if (cal.get(Calendar.MINUTE) >= 10
							&& cal.get(Calendar.MINUTE) <= 30) {
						currentCase = 3;

						if (this.previousCase != currentCase)
							ActionBreakFast.getInstance().run();
					} else {
						currentCase = 4;

						if (this.previousCase != currentCase)
							ActionGetOut.getInstance().run(); // Il va faire des
																// courses
					}
					break;
				case 12:
					if (cal.get(Calendar.MINUTE) <= 45) {
						currentCase = 5;

						if (this.previousCase != currentCase)
							ActionCook.getInstance().run();
					} else {
						currentCase = 6;

						if (this.previousCase != currentCase)
							ActionLunch.getInstance().run();
					}
				case 13:
					if (cal.get(Calendar.MINUTE) <= 30) {
						currentCase = 7;

						if (this.previousCase != currentCase)
							ActionLunch.getInstance().run();
					} else {
						currentCase = 8;

						if (this.previousCase != currentCase)
							ActionWorkOffice.getInstance().run();
					}
					break;
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19: {
					currentCase = 9;

					if (this.previousCase != currentCase) {
						ActionWorkOffice.getInstance().run();
						ActionWalkAtHome.getInstance().run();
					}
				}
					break;
				case 20:
					if (cal.get(Calendar.MINUTE) >= 30) {
						currentCase = 10;

						if (this.previousCase != currentCase)
							ActionCook.getInstance().run();
					} else {
						currentCase = 11;

						if (this.previousCase != currentCase)
							ActionDinner.getInstance().run();
					}
					break;
				case 21:
				case 22:
					currentCase = 12;

					if (this.previousCase != currentCase) {
						ActionWatchTv.getInstance().run();
						ActionWalkAtHome.getInstance().run();
					}
					break;
				case 23:
					currentCase = 13;

					if (this.previousCase != currentCase)
						ActionSleep.getInstance().run();
					break;
				default:
				}
				break;
			case Calendar.SUNDAY:
				switch (cal.get(Calendar.HOUR_OF_DAY)) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
					currentCase = 14;

					if (this.previousCase != currentCase)
						ActionSleep.getInstance().run();
					break;
				case 9:
					currentCase = 15;

					if (this.previousCase != currentCase)
						ActionGetOut.getInstance().run();
					break;
				case 22:
					if (cal.get(Calendar.MINUTE) >= 30) {
						currentCase = 16;

						if (this.previousCase != currentCase)
							ActionWalkAtHome.getInstance().run();
					}
					break;
				case 23:
					currentCase = 17;

					if (this.previousCase != currentCase)
						ActionSleep.getInstance().run();
					break;
				default:
				}
				break;
			default:
				switch (cal.get(Calendar.HOUR_OF_DAY)) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
					currentCase = 18;

					if (this.previousCase != currentCase)
						ActionSleep.getInstance().run();
					break;
				case 7:
					if (cal.get(Calendar.MINUTE) >= 0
							&& cal.get(Calendar.MINUTE) <= 20) {
						currentCase = 19;

						if (this.previousCase != currentCase)
							ActionGetUp.getInstance().run();
					} else if (cal.get(Calendar.MINUTE) >= 20) {
						currentCase = 20;

						if (this.previousCase != currentCase)
							ActionBreakFast.getInstance().run();
					}
					break;
				case 8: case 9: case 10: case 11:
					currentCase = 21;

					if (this.previousCase != currentCase)
						ActionGetOut.getInstance().run();
					break;
				case 12:
					if (cal.get(Calendar.MINUTE) <= 30) {
						currentCase = 22;

						if (this.previousCase != currentCase)
							ActionCook.getInstance().run();
					} else {
						currentCase = 23;

						if (this.previousCase != currentCase)
							ActionLunch.getInstance().run();
					}
					break;
				case 13:
					currentCase = 24;

					if (this.previousCase != currentCase)
						ActionLunch.getInstance().run();
					break;
				case 14:
					currentCase = 25;

					if (this.previousCase != currentCase)
						ActionGetOut.getInstance().run();
					break;
				case 19:
					if (cal.get(Calendar.MINUTE) >= 30) {
						currentCase = 26;

						if (this.previousCase != currentCase) {
							ActionCook.getInstance().run();
							ActionWalkAtHome.getInstance().run();
						}
					}
					break;
				case 20:
					currentCase = 27;

					if (this.previousCase != currentCase) {
						ActionDinner.getInstance().run();
						ActionWalkAtHome.getInstance().run();
					}
					break;
				case 21:
				case 22:
					currentCase = 28;

					if (this.previousCase != currentCase) {
						ActionWatchTv.getInstance().run();
						ActionWalkAtHome.getInstance().run();
					}
					break;
				case 23:
					currentCase = 29;

					if (this.previousCase != currentCase) {
						ActionSleep.getInstance().run();
					}
					break;
				default:
				}
			}
		}

		this.previousCase = currentCase;

		for (Room room : House.getInstance().getRooms()) {
			Set<Map.Entry<String, Sensor<Object>>> set = room.getSensors()
					.entrySet();

			for (Map.Entry<String, Sensor<Object>> entry : set) {
				Sensor<Object> sensor = entry.getValue();

				// Affichage d'une valeur d'un capteur specifique
//				 if(room.getIdRoom() == RoomConstants.ID_BEDROOM &&
//				 sensor.getType() == SensorType.PRESENCE)
//				 {
//					 System.out.println(sensor.getType() + " : " +
//					 " piece : " + room.getName() + " : " + sensor.getLastValue());
//				 }

				this.serverSender.submitMessage(sensor.composeFrame());
			}
		}
	}

	private void updateSensorValue(Actuator<Object> actuator) {
		Set<Map.Entry<String, Sensor<Object>>> set = actuator.getSensors()
				.entrySet();

		for (Map.Entry<String, Sensor<Object>> entry : set) {
			Sensor<Object> sensor = entry.getValue();

			if (actuator.getType() == ActuatorType.RADIATOR) { // RADIATOR
				if (previousTime == null) {
					previousTime = (double) DeepHouseCalendar.getInstance()
							.getCalendar().getTimeInMillis() / 1000;
				}
				Double deltaTime = (double) DeepHouseCalendar.getInstance()
						.getCalendar().getTimeInMillis()
						/ 1000 - previousTime;
				previousTime = (double) DeepHouseCalendar.getInstance()
						.getCalendar().getTimeInMillis() / 1000;
				Double deltaTemp = (Double) actuator.getLastValue()
						- (Double) sensor.getLastValue();
				Random randomno = new Random();
				Double tempRoom = (Double) sensor.getLastValue() + deltaTemp
						* (1 / (1 + Math.exp(-deltaTime / 3600)))
						+ randomno.nextGaussian() / 3;
				sensor.setLastValue(tempRoom);
			} else { // BOOLEAN
				sensor.setLastValue(!((Boolean)sensor.getLastValue()));
			}
		}
	}

	@Override
	public void run() {
		String message = null;
		Frame frame = null;
		int i = 0;
		try {
			while (alive) {
				message = actuatorListener.getMessage();

				if (message != null) {
					i = 0;
					while (message != null && i <= 12) {
						frame = new Frame(message);
						Actuator<Object> actuator = House.getInstance()
								.updateActuator(frame);
						updateSensorValue(actuator);
						message = actuatorListener.getMessage();
						i++;
					}
				} else {
					Thread.sleep(2000);
				}
				Thread.sleep(10);
				runSimulator();
				vueSensor.refresh(); // Met a jour la vue
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initServerListener(int port) {
		this.actuatorListener = new ActuatorListener(port);
	}

	public void initSensorSender(String host, int port) {
		this.serverSender = new ServerSender(host, port);
	}

	public void stopController() {
		this.alive = false;

		try {
			this.actuatorListener.stopListener();
			this.serverSender.stopSender();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
