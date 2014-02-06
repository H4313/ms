package com.h4313.deephouse.msensor.controller;

import java.util.ArrayList;

import com.h4313.deephouse.network.CallBack;
import com.h4313.deephouse.network.TcpSender;

public class ServerSender implements CallBack
{
	private ArrayList<String> messages;
	
	private TcpSender tcpSender;
	
	public ServerSender(String host, int port)
	{
		messages = new ArrayList<String>();
		
		tcpSender = null;
		try {
			tcpSender = new TcpSender(host, port, this);
			tcpSender.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopSender() throws Exception
	{
		this.tcpSender.closeSender();
	}
	
	public void submitMessage(String s)
	{
		messages.add(s);
	}
	
	public String callBack(String s)
	{
 		String value = null;
		
		if(messages.size() > 0)
		{
			// Takes 1st message
			value = messages.get(0);
			messages.remove(0);
		}
		
		return value;
	}
}

