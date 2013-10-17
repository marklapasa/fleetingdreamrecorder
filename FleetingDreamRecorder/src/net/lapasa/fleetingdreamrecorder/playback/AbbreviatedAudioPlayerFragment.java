package net.lapasa.fleetingdreamrecorder.playback;

import net.lapasa.fleetingdreamrecorder.R;
import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AbbreviatedAudioPlayerFragment extends ObservingAudioPlayerFragment
{
	private View view;
	private TextView titleTextView;
	private TextView dreamerTextView;
	
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
	protected void displayTrackInfo(DreamRecording rc)
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
}
