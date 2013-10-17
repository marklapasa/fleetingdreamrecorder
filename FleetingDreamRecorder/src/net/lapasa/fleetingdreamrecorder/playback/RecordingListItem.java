package net.lapasa.fleetingdreamrecorder.playback;

import net.lapasa.fleetingdreamrecorder.R;
import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecordingListItem extends RelativeLayout
{

	private View v;
	private DreamRecording rc;

	
	
	public RecordingListItem(Context context)
	{
		super(context);
		v = inflate(getContext(), R.layout.recording_list_item, this);

	}

	
	/**
	 * 
	 * @param view	container
	 */
	public void setSelectedListItem(boolean isSelected)
	{
		TextView trackName = ((TextView)v.findViewById(R.id.title));
		if (isSelected)
		{
			trackName.setTypeface(null, Typeface.BOLD);
		}
		else
		{
			trackName.setTypeface(null, Typeface.NORMAL);
		}
	}
	
	public void init(DreamRecording rc)
	{
		this.rc = rc;
		
		
		try
		{
		// Set the title
		TextView tv = ((TextView)v.findViewById(R.id.title));
		tv.setText(rc.title);
		
		// Set the date
		tv = ((TextView)v.findViewById(R.id.date));
		tv.setText(rc.getLastUpdatedAsLongStr());
		
		// Set the duration
		tv = ((TextView)v.findViewById(R.id.duration));
		tv.setText(rc.duration);
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		
		setSelectedListItem(false);
	}
}
