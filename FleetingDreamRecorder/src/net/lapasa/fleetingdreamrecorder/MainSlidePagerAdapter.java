package net.lapasa.fleetingdreamrecorder;

import net.lapasa.alarmlib.ui.AlarmsListFragment;
import net.lapasa.fleetingdreamrecorder.alarm.AlarmActivity;
import net.lapasa.fleetingdreamrecorder.playback.RecordingSelectionFragment;
import net.lapasa.fleetingdreamrecorder.recording.AudioRecordingFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v13.app.FragmentStatePagerAdapter;

public class MainSlidePagerAdapter extends FragmentStatePagerAdapter
{
	Fragment[] frags;
	public MainSlidePagerAdapter(FragmentManager fm, Context context)
	{
		super(fm);
		frags = new Fragment[2];
		frags[0] = AlarmsListFragment.create(getPendingIntent(context));//SimpleFragment.create("-1"); 
		frags[1] = RecordingSelectionFragment.create();//DreamLibraryListFragment.create(context);//SimpleFragment.create("+1");		
//		frags[2] = 
	}

	private PendingIntent getPendingIntent(Context context)
	{
		Intent i = new Intent(context, AlarmActivity.class);
		PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
		return pi;
	}

	@Override
	public Fragment getItem(int index)
	{
		return frags[index];
	}

	@Override
	public int getCount()
	{
		return frags.length;
	}

}
