package net.lapasa.fleetingdreamrecorder;

import net.lapasa.alarmlib.models.Alarm;
import net.lapasa.alarmlib.ui.AlarmsListFragment;
import net.lapasa.fleetingdreamrecorder.alarm.AlarmBroadcastReceiver;
import net.lapasa.fleetingdreamrecorder.recording.AudioRecordingFragment.IAudioRecordingListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements IAudioRecordingListener
{
	private ViewPager viewPager;
	private MainSlidePagerAdapter pagerAdapter;
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Alarm.ALARM_BROADCAST_RECEIVER = AlarmBroadcastReceiver.class;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.screen_slide);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		pagerAdapter = new MainSlidePagerAdapter(getFragmentManager(), this);

		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				invalidateOptionsMenu();
			}
		});
		
		viewPager.setCurrentItem(1);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		int index = viewPager.getCurrentItem();
		Fragment f = pagerAdapter.getItem(index);
		
		if (f instanceof AlarmsListFragment)
		{
			AlarmsListFragment alarmsFrag = (AlarmsListFragment) f;
			return alarmsFrag.onCreateOptionsMenu(menu);
		}
		
		// Otherwise
		menu.clear();
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int index = viewPager.getCurrentItem();
		Fragment f = pagerAdapter.getItem(index);
		
		if (f instanceof AlarmsListFragment)
		{
			AlarmsListFragment alarmsFrag = (AlarmsListFragment) f;
			return alarmsFrag.onOptionsItemSelected(item); 
		}
		
		
		return true;
	}

	@Override
	public void onRecordingSaved()
	{
		// DO NOTHING
	}

	@Override
	public void onRecordingComplete()
	{
		// DO NOTHING
	}
}
