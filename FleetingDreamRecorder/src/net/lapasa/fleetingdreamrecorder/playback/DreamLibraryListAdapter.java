package net.lapasa.fleetingdreamrecorder.playback;

import java.util.List;

import net.lapasa.fleetingdreamrecorder.R;
import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DreamLibraryListAdapter extends BaseAdapter
{
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
		View v;
		if (convertView != null)
		{
			v = convertView; 
		}
		else
		{
			v = inflator.inflate(R.layout.recording_list_item, parent, false);
		}
		
		// Set the title
		TextView tv = ((TextView)v.findViewById(R.id.title));
		tv.setText(rc.title);
		
		// Set the date
		tv = ((TextView)v.findViewById(R.id.date));
		tv.setText(rc.getLastUpdatedAsLongStr());
		
		// Set the duration
		tv = ((TextView)v.findViewById(R.id.duration));
		tv.setText(rc.duration);
		
		return v;
	}

}
