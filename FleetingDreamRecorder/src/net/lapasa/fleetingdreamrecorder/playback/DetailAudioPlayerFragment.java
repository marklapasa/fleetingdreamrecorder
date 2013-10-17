package net.lapasa.fleetingdreamrecorder.playback;

import java.util.Observable;

import net.lapasa.fleetingdreamrecorder.R;
import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import net.lapasa.fleetingdreamrecorder.models.DreamRecordingsModel;
import net.lapasa.fleetingdreamrecorder.recording.AudioRecordingService;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androidquery.AQuery;

public class DetailAudioPlayerFragment extends ObservingAudioPlayerFragment implements OnClickListener
{
	private static final String TAG = DetailAudioPlayerFragment.class.getName();
	private AQuery aq;
	private SeekBar seekBar;
	private TextView currentPos;
	private TextView duration;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.detail_audio_player, container, false);
		View audioBtns = view.findViewById(R.id.audioBtns);
		aq = new AQuery(audioBtns);
		aq.id(R.id.prevTrack).clicked(this);
		aq.id(R.id.nextTrack).clicked(this);
		playToggle = (ToggleButton) audioBtns.findViewById(R.id.playPauseBtn);
		playToggle.setOnCheckedChangeListener(this);
		currentPos = aq.id(R.id.curPosition).getTextView();
		duration = aq.id(R.id.duration).getTextView();
		
		
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(getSeekBarChangeListener());
		return view;
	}


	private OnSeekBarChangeListener getSeekBarChangeListener()
	{
		return new OnSeekBarChangeListener()
		{

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				// DO NOTHING
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				// DO NOTHING
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				// progress = 0..100
				// Tell AudioPlaybackSvc to jump to this percentage in the
				if (fromUser)
				{
					audioPlaybackSvc.seek(progress);
				}

			}
		};
	}

	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		audioPlaybackSvc.stopPlayback();
		reset();
		
		if (id == R.id.prevTrack)
		{
			// Tell model to go next track in listings
			model.loadPrevRecording();
			
		}
		else if (id == R.id.nextTrack)
		{
			// Tell model to go previous track in listings
			model.loadNextRecording();
		}

	}


	private void reset()
	{		
		audioPlaybackSvc.stopPlayback();
		seekBar.setProgress(0);
		currentPos.setText("00:00");
		duration.setText("00:00");
	}


	/**
	 * Ask the audio service to get the duration of the track that it is playing
	 */
	@Override
	protected void displayTrackInfo(DreamRecording selectedRecording)
	{
		// Reset seekbar to zero
		int curPos = audioPlaybackSvc.getCurrentPosition();
		int duration2 = audioPlaybackSvc.getDuration();
		int seekPosition = (curPos* 100/duration2* 100) / 100;
		seekBar.setProgress(seekPosition);
		Log.d(TAG, "Seek Bar position: " + seekPosition + "," + curPos + "," + duration2);
		
		// Reset currentPosition to zero
		String curPosition = AudioRecordingService.formatInterval(curPos);
		currentPos.setText(curPosition);
		
		// Reset duration to zero
		duration.setText(selectedRecording.duration);
	}
	
	@Override
	public void update(Observable observable, Object data)
	{
		super.update(observable, data);
		if (data instanceof String)
		{
			String flagStr = (String) data;
			if (observable instanceof AudioPlaybackService)
			{
				if (flagStr.equals(DreamRecordingsModel.PLAYBACK_COMPLETE))
				{
					seekBar.setProgress(100);
				}
			}
		}
	}
	
	
	
}
