package net.lapasa.fleetingdreamrecorder.playback;

import java.util.List;

import net.lapasa.fleetingdreamrecorder.R;
import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import net.lapasa.fleetingdreamrecorder.models.DreamRecordingsModel;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecordingSelectionFragment extends Fragment
{
	private DreamRecordingsModel model;
	
	public static RecordingSelectionFragment create()
	{
		return new RecordingSelectionFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Display the first recording if there is none
		model = DreamRecordingsModel.getInstance(getActivity());
		if (model.getSelectedRecording() == null)
		{
			List<DreamRecording> recordings = model.getRecodings();
			if (recordings != null && recordings.size() > 0)
			{
				model.setSelectedRecording(recordings.get(0));
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return (ViewGroup)inflater.inflate(R.layout.recording_selection, container, false);
	}

}
