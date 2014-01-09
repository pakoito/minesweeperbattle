package com.zengate.minesweeperbattle.Notifications;

import com.badlogic.gdx.utils.Array;

public class NotificationManager {

	private static Array<Notification> notificationQue = new Array<Notification>();
	
	private static float time = 0;
	private static float showTime = 3;
	
	private static Notification currentNotification;
	
	public static void Init(){
		
	}
	
	public static void Update(float dt){
		if (notificationQue.size != 0){
			
			if (currentNotification == null){
				currentNotification = notificationQue.get(0);
			}
			
			if (time < showTime){
				time += 1 * dt;
			}else{
				time %= showTime;
				
				currentNotification.fadeOut();
				notificationQue.removeIndex(0);
			}
		}
		
		if (currentNotification != null){
			currentNotification.displayNotification(dt);
			if (currentNotification.isDone()){
				currentNotification = null;
			}
		}
	}
	
	public static void addNotification(Notification _aNotification){
		notificationQue.add(_aNotification);
	}
}
