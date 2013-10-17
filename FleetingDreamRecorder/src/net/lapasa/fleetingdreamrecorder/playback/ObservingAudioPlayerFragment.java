package net.lapasa.fleetingdreamrecorder.playback;

import java.util.Observable;
import java.util.Observer;

import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import net.lapasa.fleetingdreamrecorder.models.DreamRecordingsModel;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public abstract class ObservingAudioPlayerFragment extends Fragment implements Observer, OnCheckedChangeListener
{
	protected ToggleButton playToggle;
	protected DreamRecordingsModel model;
	protected AudioPlaybackService audioPlaybackSvc;
	protected DreamRecording selectedRecording;

	
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
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		displayTrackInfo(model.getSelectedRecording());
	}	
	
	
	@Override
	public void update(Observable observable, Object data)
	{
		selectedRecording = model.getSelectedRecording();
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
					playToggle.setChecked(false);
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
				if (flagStr.equals(AudioPlaybackService.PLAYBACK_ELAPSED))
				{
					displayTrackInfo(selectedRecording);
				}
			}
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


	abstract void displayTrackInfo(DreamRecording selectedRecording);
}
