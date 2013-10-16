package net.lapasa.fleetingdreamrecorder.playback;

import java.util.Observable;
import java.util.Observer;

import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import net.lapasa.fleetingdreamrecorder.models.DreamRecordingsModel;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DreamLibraryListFragment extends ListFragment implements Observer
{
	private static AudioPlaybackService playbackSvc;
	private DreamRecordingsModel model;
	private DreamLibraryListAdapter listAdapter;

	public static DreamLibraryListFragment create()
	{
		return new DreamLibraryListFragment();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		playbackSvc = AudioPlaybackService.getInstance(getActivity());
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		model = DreamRecordingsModel.getInstance(getActivity());
		model.addObserver(this);
		listAdapter = new DreamLibraryListAdapter(getActivity(), model.getRecodings());
		setListAdapter(listAdapter);
		getListView().setOnItemLongClickListener(null); // TODO		
	}
	

	@Override
	public void update(Observable observable, Object data)
	{
		listAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		DreamRecording rc = (DreamRecording) listAdapter.getItem(position);
		model.playNow(rc);
	}
}
