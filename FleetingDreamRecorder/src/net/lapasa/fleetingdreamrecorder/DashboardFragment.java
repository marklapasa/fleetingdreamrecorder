package net.lapasa.fleetingdreamrecorder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DashboardFragment extends Fragment
{
	/**
	 * Factor method/Constructor
	 * 
	 * @return
	 */
	public static DashboardFragment create()
	{
		return new DashboardFragment();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// Extract metadata from arguments bundle, store the information as instance variables
	}	
	
	
	/**
	 * Apply any metadata against an instance of the instantiated view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		
		return super.onCreateView(inflater, container, savedInstanceState);
		
	}
}
