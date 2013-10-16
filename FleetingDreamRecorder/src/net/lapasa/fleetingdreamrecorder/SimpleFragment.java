package net.lapasa.fleetingdreamrecorder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleFragment extends Fragment
{
	private static final String VALUE = "value";

	public static SimpleFragment create(String str)
	{
		SimpleFragment frag = new SimpleFragment();
		Bundle args = new Bundle();
		 args.putString(VALUE, str);
		frag.setArguments(args);
		return frag;
	}

	private String value;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		value = getArguments().getString(VALUE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		TextView tv = (TextView)inflater.inflate(R.layout.simple_frag, container, false);
		tv.setText(value);
		return tv;
	}
}
