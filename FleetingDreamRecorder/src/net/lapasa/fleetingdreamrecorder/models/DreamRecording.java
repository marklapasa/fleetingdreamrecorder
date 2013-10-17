package net.lapasa.fleetingdreamrecorder.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.text.format.DateUtils;

import net.lapasa.fleetingdreamrecorder.recording.AudioRecordingService;

public class DreamRecording
{
	public static SimpleDateFormat simpleDateFmt = new SimpleDateFormat("MMM d h:mm aa");
	public long id;
	public String title;
	public String fileLocation;
	public Calendar created = Calendar.getInstance();
	public Calendar lastUpdated;
	public String dreamer;
	public int dreamerId;
	public String duration;

	public String toString()
	{
		return title + " @ " + getFormattedDate(created);
	}

	private String getFormattedDate(Calendar cal)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(AudioRecordingService.commonDateFormat);
		return sdf.format(cal.getTime());
	}

	public Calendar getCal(long milliseconds)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(milliseconds);
		return c;
	}

	public String getLastUpdatedAsLongStr()
	{
		return simpleDateFmt.format(lastUpdated.getTime());
	}

	public String getCreatedDateFormatted()
	{
		String str = DateUtils.getRelativeTimeSpanString(created.getTimeInMillis(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
		return str;
	}

	public void addComment(String body)
	{
		// TODO
		
	}

}