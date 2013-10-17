package net.lapasa.fleetingdreamrecorder.playback;

import java.util.List;

import net.lapasa.fleetingdreamrecorder.R;
import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DreamLibraryListAdapter extends BaseAdapter
{
	private static final String TAG = DreamLibraryListAdapter.class.getName();
	private Context context;
	private List<DreamRecording> recordings;
	private LayoutInflater inflator;

	public DreamLibraryListAdapter(Context context, List<DreamRecording> recordings)
	{
		super();
		this.context = context;
		this.recordings = recordings;
		this.inflator = ((Activity) context).getLayoutInflater();
	}
	
	@Override
	public int getCount()
	{
		return recordings.size();
	}

	@Override
	public Object getItem(int position)
	{
		return recordings.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		DreamRecording rc = recordings.get(position);
		
		View view = null;
		
		if (convertView == null)
		{
			view = (ViewGroup) inflator.inflate(R.layout.recording_list_item, null);
		}
		else
		{
			view = convertView;
		}
		
		// Set the title
		TextView tv = ((TextView)view.findViewById(R.id.title));
		tv.setText(rc.title);
		
		// Set the date
		tv = ((TextView)view.findViewById(R.id.date));
		tv.setText(rc.getLastUpdatedAsLongStr());
		
		// Set the duration
		tv = ((TextView)view.findViewById(R.id.duration));
		tv.setText(rc.duration);		
		/*
		RecordingListItem view = null;
		if (convertView == null)
		{
			view = new RecordingListItem(context);
		}
		if (view == null)
		{
			Log.e(TAG, "This shouldn't happen");
		}
		view.init(rc);
		*/
		return view;
	}


	
	
/*
	public void setSelectedListItem(DreamRecording selectedRecording)
	{
		for (int i = 0; i < recordings.size(); i++)
		{
			if (getItem(i) == selectedRecording)
			{
				RecordingListItem rli = (RecordingListItem) getView(i, null, null);
				rli.setSelectedListItem(true);
				break;
			}
		}
	}
*/	


}
