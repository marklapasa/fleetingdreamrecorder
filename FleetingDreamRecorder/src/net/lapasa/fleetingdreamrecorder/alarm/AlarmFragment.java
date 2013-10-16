package net.lapasa.fleetingdreamrecorder.alarm;

import net.lapasa.fleetingdreamrecorder.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;

public class AlarmFragment extends Fragment
{
	private static final String TAG = AlarmFragment.class.getName();
	private AQuery aq;
	private MediaPlayer player;
	private Handler handler;
	private long delayMillis = 200;

	private ImageView alarmView0;
	private ImageView alarmView1;

	private int alarmFlagState;
	private Vibrator vibrator;
	private AudioManager audioMgr;
	private int currentVol;
	private static final int QUIT = -1;
	private static final int VISIBLE_0 = 0;
	private static final int VISIBLE_1 = 1;
	private static final long NEXT_INCREASE = 9000;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.alarm_view, container, false);
		return view;
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		aq = new AQuery(getActivity());

		aq.id(R.id.dismiss).clicked(this, "onDismissClicked");

		alarmView0 = aq.id(R.id.alarm_icon1).invisible().getImageView();
		alarmView1 = aq.id(R.id.alarm_icon2).invisible().getImageView();

		playAlarmSound();
		playAnimation();
				
	};

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

	}
	


	private void playAlarmSound()
	{
		currentVol = 0;
		audioMgr = (AudioManager) (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
		audioMgr.setStreamVolume(AudioManager.STREAM_ALARM, currentVol, AudioManager.FLAG_PLAY_SOUND);
		
		try
		{
			AssetFileDescriptor afd = getActivity().getAssets().openFd("NonJeNeRegretteRien.mp3");

			player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());						
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			player.prepare();
			player.setLooping(true);
			player.start();
		}
		catch (Exception e)
		{
			Log.e(TAG, "Could not play alarm sound");
			e.printStackTrace();
		}
		

		
		handler = getIncrementingVolHandler();
		handler.sendEmptyMessageDelayed(0, NEXT_INCREASE);
		
		
	}

	private Handler getIncrementingVolHandler()
	{
		return new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				if (currentVol++ <= audioMgr.getStreamMaxVolume(AudioManager.STREAM_ALARM))
				{
					audioMgr.setStreamVolume(AudioManager.STREAM_ALARM, currentVol, AudioManager.FLAG_VIBRATE);
					sendEmptyMessageDelayed(msg.what, NEXT_INCREASE);
					
				}
				if (currentVol == 3)
				{
					if (alarmFlagState != QUIT)
					{
						fireVibration();
					}
				}
			}
		};
	}

	private void playAnimation()
	{
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == QUIT)
				{
					alarmView0.setVisibility(View.VISIBLE);
					alarmView1.setVisibility(View.INVISIBLE);
					stopVibrator();
					alarmFlagState = QUIT;
					return;
				}
				else
				{
					if (msg.what == VISIBLE_0)
					{
						alarmView0.setVisibility(View.VISIBLE);
						alarmView1.setVisibility(View.INVISIBLE);
						alarmFlagState = VISIBLE_1;

					}
					else if (msg.what == VISIBLE_1)
					{
						alarmView0.setVisibility(View.INVISIBLE);
						alarmView1.setVisibility(View.VISIBLE);
						alarmFlagState = VISIBLE_0;
					}
					handler.sendEmptyMessageDelayed(alarmFlagState, delayMillis);

				}

			}
		};

		alarmFlagState = 0;
		handler.sendEmptyMessageDelayed(alarmFlagState, delayMillis);
	}
	
	private void fireVibration()
	{
		long[] pattern =
		{ 0, 200, 500 };
		vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(pattern, 0);

	}

	protected void stopVibrator()
	{
		if (vibrator != null)
		{
			vibrator.cancel();
			vibrator = null;
		}
	}

	/**
	 * Disable animation; disable sound
	 * 
	 * @param view
	 */
	public void onDismissClicked(View view)
	{
		// Turn off audio
		player.stop();
		player = null;

		// Stop animation
		handler.removeMessages(VISIBLE_0);
		handler.removeMessages(VISIBLE_1);
		handler.sendEmptyMessage(QUIT);
		
		// Stop vibration
		stopVibrator();
		
		( (AlarmActivity)getActivity()).onDismiss();
	}
}
