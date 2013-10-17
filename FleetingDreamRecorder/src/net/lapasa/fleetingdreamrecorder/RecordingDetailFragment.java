package net.lapasa.fleetingdreamrecorder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecordingDetailFragment extends Fragment
{

	public static RecordingDetailFragment create()
	{
		return new RecordingDetailFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return (ViewGroup)inflater.inflate(R.layout.recording_detail, container, false);
	}

}
