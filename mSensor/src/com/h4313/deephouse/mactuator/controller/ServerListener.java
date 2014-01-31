package com.h4313.deephouse.mactuator.controller;

import java.util.ArrayList;

import com.h4313.deephouse.network.CallBack;
import com.h4313.deephouse.network.TcpReceiver;

public class ServerListener implements CallBack
{
	private volatile boolean record;

	private ArrayList<String> messages;
	
	private TcpReceiver tcpReceiver;
	
	public ServerListener(int port)
	{
		this.messages = new ArrayList<String>();
		this.record = true;
		
		tcpReceiver = null;
		try {
			tcpReceiver = new TcpReceiver(port, this);
			tcpReceiver.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(tcpClient.receive());
//		try {
//			tcpReceiver.closeReceiver();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		this.record = true;
	}
	
	public void stopListener() throws Exception
	{
		this.tcpReceiver.closeReceiver();
	}
	
	public void listen()
	{
		this.record = true;
	}
	
	public void pause()
	{
		this.record = false;
	}
	
	public String callBack(String s)
	{
		if(this.record && s != null)
		{
			messages.add(s);
//			System.out.println(messages.get(messages.size()-1));
		}
		
		return null;
	}
	
	
	public String getMessage()
	{
		if(this.messages.size() > 0)
		{
			String message = this.messages.get(0);
			this.messages.remove(0);
			return message;
		}
		
		return null;
	}
}
