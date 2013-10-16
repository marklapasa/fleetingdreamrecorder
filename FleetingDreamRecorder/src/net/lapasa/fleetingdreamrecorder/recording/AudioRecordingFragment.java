package net.lapasa.fleetingdreamrecorder.recording;

import java.io.IOException;
import java.util.Calendar;

import net.lapasa.fleetingdreamrecorder.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;

public class AudioRecordingFragment extends Fragment implements OnCheckedChangeListener
{
	private ToggleButton tb;
	private Handler handler;
	private static final String VALUE = "value";

	private AudioRecordingService audioRecSvc;
	private ProgressDialog progressDialog;
	private IAudioRecordingListener listener;

	public static AudioRecordingFragment create()
	{

		AudioRecordingFragment frag = new AudioRecordingFragment();

		Bundle args = new Bundle();
		// args.putString(VALUE, str);
		frag.setArguments(args);
		return frag;

	}

	private CountDownTimer timer;
	private AQuery aq;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// value = getArguments().getString(VALUE);
		this.audioRecSvc = new AudioRecordingService(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.recording, container, false);
		
		aq = new AQuery(vg);
		aq.id(R.id.greetings).text(getGreetingText());
		aq.id(R.id.instructions).text("Try to recall as many details of the dream you just had before you forget it all.\n\nWhen you are ready, hit the Record button.");
		
		tb = (ToggleButton) vg.findViewById(R.id.recordingBtn);
		tb.setOnCheckedChangeListener(this);

		timer = new CountDownTimer(60000 * 5, 1000)
		{

			public void onTick(long millisUntilFinished)
			{
				tb.setText("Time Remaining - " + (AudioRecordingService.formatInterval(millisUntilFinished)));
			}

			public void onFinish()
			{
				tb.setChecked(false);
				aq.id(R.id.greetings).text("Now Saving...");
			}
		};

		return vg;
	}

	private String getGreetingText()
	{
		String greeting = null;
		Calendar c = Calendar.getInstance();
		int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		
		if (hourOfDay >= 2 && hourOfDay <= 11)
		{
			greeting = "Good Morning";
		}
		else if (hourOfDay >= 12 && hourOfDay <= 16)
		{
			greeting = "Good Afternoon";
		}
		else if ((hourOfDay >= 17 && hourOfDay <= 23) || (hourOfDay >= 0 && hourOfDay <= 1))
		{
			greeting = "Good Evening";
		}				
		return greeting;
	}

	/**
	 * Enforce correct callback type at runtime
	 */
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			listener = (IAudioRecordingListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement IAudioRecordingListener");
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isRecording)
	{
		if (isRecording)
		{
			timer.start();
			try
			{
				audioRecSvc.beginRec();
			}
			catch (IllegalStateException e)
			{
				e.printStackTrace();
				handleFailedRecording(e);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				handleFailedRecording(e);
			}
			
			aq.id(R.id.greetings).text("Now Recording");
			aq.id(R.id.instructions).text("Please speak loud and clear.");			
		}
		else
		{
			timer.cancel();
			audioRecSvc.stopRec();

			String path = audioRecSvc.getLastRecordingPath();

			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Saving recording...");
			progressDialog.show();

			handler = new Handler()
			{
				public void handleMessage(Message msg)
				{
					progressDialog.dismiss();
					listener.onRecordingSaved();
				}
			};

			handler.sendEmptyMessageDelayed(0, 3000);
			
			
			aq.id(R.id.greetings).text("Saving Recording");
			aq.id(R.id.instructions).text("Thank you");			

		}
	}

	private void handleFailedRecording(Exception e)
	{
		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		tb.setChecked(false);
	}

	public interface IAudioRecordingListener
	{
		public void onRecordingSaved();

		public void onRecordingComplete();
	}
	
	

}
