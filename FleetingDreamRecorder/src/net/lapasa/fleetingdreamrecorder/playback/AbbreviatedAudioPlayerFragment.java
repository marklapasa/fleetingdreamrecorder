package net.lapasa.fleetingdreamrecorder.playback;

import java.util.Observable;
import java.util.Observer;

import net.lapasa.fleetingdreamrecorder.R;
import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import net.lapasa.fleetingdreamrecorder.models.DreamRecordingsModel;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AbbreviatedAudioPlayerFragment extends Fragment implements OnCheckedChangeListener, Observer
{
	private View view;
	private TextView titleTextView;
	private TextView dreamerTextView;
	private ToggleButton playToggle;
	private AudioPlaybackService audioPlaybackSvc;
	private DreamRecordingsModel model;
	private boolean pause;

	/**
	 * Factory Constructor
	 * 
	 * @return
	 */
	public static AbbreviatedAudioPlayerFragment create()
	{
		return new AbbreviatedAudioPlayerFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		model = DreamRecordingsModel.getInstance(getActivity());
		model.addObserver(this);
		audioPlaybackSvc = AudioPlaybackService.getInstance(getActivity());
		audioPlaybackSvc.addObserver(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.abbr_audio_player, container, false);
		titleTextView = (TextView) vg.findViewById(R.id.title);
		dreamerTextView = (TextView) vg.findViewById(R.id.dreamer);
		playToggle = (ToggleButton) vg.findViewById(R.id.playPauseBtn);
		playToggle.setOnCheckedChangeListener(this);

		return vg;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		displayTrackInfo(model.getSelectedRecording());
	}
	

	public void displayTrackInfo(DreamRecording rc)
	{
		if (rc != null)
		{
			// Get title
			titleTextView.setText(rc.title);

			// Get dreamer
			// dreamerTextView.setText(rc.dreamer);
			dreamerTextView.setText(rc.getLastUpdatedAsLongStr());
			playToggle.setVisibility(View.VISIBLE);
		}
		else
		{
			titleTextView.setText("Not Available");
			dreamerTextView.setText("Not Avaiable");
			playToggle.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		// Play Audio
		if (isChecked)
		{
			if (!audioPlaybackSvc.isPlaying() && !audioPlaybackSvc.isPaused())
			{
				// If it's not already playing, then play something
				audioPlaybackSvc.play();
				pause = false;
			}
			else if (audioPlaybackSvc.isPaused())
			{
				audioPlaybackSvc.resume();
			}
			else if (audioPlaybackSvc.isPlaying())
			{
				// If it is already playing then we want to pause it
				audioPlaybackSvc.pause();
			}
		}
		else
		{
			// Don't play any audio
			if (audioPlaybackSvc.isPlaying() && !audioPlaybackSvc.isPaused())
			{
				audioPlaybackSvc.pause();
			}
		}

	}

	@Override
	public void update(Observable observable, Object data)
	{
		DreamRecording selectedRecording = model.getSelectedRecording();
		if (data instanceof String)
		{
			String flagStr = (String) data;
			if (observable instanceof DreamRecordingsModel)
			{
				if (flagStr.equals(DreamRecordingsModel.RECORDING_CREATED))
				{
					displayTrackInfo(selectedRecording);
				}
				if (flagStr.equals(DreamRecordingsModel.PLAY_NOW))
				{
					playToggle.setChecked(true);
					displayTrackInfo(selectedRecording);
				}
			}
			else if (observable instanceof AudioPlaybackService)
			{
				if (flagStr.equals(DreamRecordingsModel.PLAYBACK_COMPLETE))
				{
					playToggle.setChecked(false);
				}
			}
		}

	}
}
