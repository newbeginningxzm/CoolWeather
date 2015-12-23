package service;

import utils.HttpUtilsWithListener;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class AutoUpdateService extends Service {
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
		long hour=1000*60*60;
		long triger=hour+SystemClock.elapsedRealtime();
		Intent service=new Intent(this,AutoUpdateReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, service, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triger, pi);
		return super.onStartCommand(intent, flags, startId);
	}

}
