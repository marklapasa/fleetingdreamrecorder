package net.lapasa.fleetingdreamrecorder.alarm;

import net.lapasa.fleetingdreamrecorder.R;
import net.lapasa.fleetingdreamrecorder.recording.AudioRecordingFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/***
 * When this activity gets launched, 
 * Visual: Display the alarm icon shaking: there is a button that says "dismiss" 
 * Audio: Play the Non, regret rien mp3
 * Physically: Device vibrates
 * 
 * @author mlapasa
 * 
 */
public class AlarmActivity extends Activity implements AudioRecordingFragment.IAudioRecordingListener
{

	private static final String TAG = AlarmActivity.class.getName();
	private FragmentTransaction ftxn;
	private AlarmFragment alarmFrag;
	private WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Full-Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | 
			    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
			    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | 
			    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
			    WindowManager.LayoutParams.FLAG_FULLSCREEN | 
			    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
			    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | 
			    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
        // Wake up if asleep
        PowerManager powMgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powMgr.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
        wakeLock.acquire();
		
		alarmFrag = new AlarmFragment();
		FragmentManager fragmentManager = getFragmentManager();
		ftxn = fragmentManager.beginTransaction();
		ftxn.replace(android.R.id.content, alarmFrag);
		ftxn.commit();
	}

	public void onDismiss()
	{
		Fragment newFragment = AudioRecordingFragment.create();
		Bundle args = new Bundle();

		ftxn = getFragmentManager().beginTransaction();
		ftxn.setCustomAnimations(R.anim.fragment_animation_fade_in, R.anim.fragment_animation_fade_out);
		ftxn.hide(alarmFrag);

		ftxn.add(android.R.id.content, newFragment);
		ftxn.commit();
	}

	@Override
	public void onRecordingSaved()
	{
		Toast.makeText(this, "Recording successfully saved. At a later time, playback the audio and take notes.", Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public void onRecordingComplete()
	{
		
	}
	
	@Override
	public void onBackPressed()
	{
		// Do not leave this activity otherwise the music and alarm will go on forever
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		if (wakeLock != null && wakeLock.isHeld())
		{
			wakeLock.release();
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		if (wakeLock != null && wakeLock.isHeld())
		{
			wakeLock.release();
		}
	}

}
