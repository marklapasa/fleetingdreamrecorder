package net.lapasa.fleetingdreamrecorder.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.content.Context;

public class DreamRecordingsModel extends Observable
{
	public static final String RECORDING_CREATED = "Recording Created";
	public static final String RECORDING_SELECTED = "Recording Selected";
	public static final String PLAYBACK_COMPLETE = "Playback Complete";
	public static final String PLAY_NOW = "Play Now";
	private static DreamRecordingsModel _instance;
	private DreamRecordingsDTO dto;
	private List<DreamRecording> recordings;
	private DreamRecording selectedRecording;
	private int selectedRecordingIndex;
	
	private DreamRecordingsModel(Context context)
	{
		this.dto = new DreamRecordingsDTO(context);
		this.recordings = new ArrayList<DreamRecording>();
		
		recordings.addAll(dto.getAllRecordings());
	}
	
	public static DreamRecordingsModel getInstance(Context context)
	{
		if (_instance == null)
		{
			_instance = new DreamRecordingsModel(context);
		}
		return _instance;
	}
	
	
	public DreamRecording create(String fileLocation, String duration)
	{
		DreamRecording newRecording = dto.create(fileLocation, duration);
		recordings.add(newRecording);
		
		setChanged();
		notifyObservers(RECORDING_CREATED);
		
		return newRecording;
		
	}
	
	public List<DreamRecording> getRecodings()
	{
		return recordings;
	}
	
	public DreamRecording update(DreamRecording rc)
	{
		DreamRecording updatedRec = dto.update(rc);
		setChanged();
		notifyObservers();
		return updatedRec;
	}
	
	
	/**
	 * @return true if the selectedIndex is <= recordings.length() 
	 */
	public boolean hasNextDreamRecording()
	{
		return getSelectedRecordingIndex() < recordings.size();
	}
	
	
	public boolean hasPrevDreamRecording()
	{
		return getSelectedRecordingIndex() >= 0;
	}

	public DreamRecording getSelectedRecording()
	{
		return selectedRecording;
	}

	public void setSelectedRecording(DreamRecording selectedRecording)
	{
		this.selectedRecording = selectedRecording;
		
		// Update the index position
		for (int i = 0; i < recordings.size(); i++)
		{
			if (recordings.get(i) == selectedRecording)
			{
				selectedRecordingIndex = i;
				break;
			}
		}
		
		setChanged();
		notifyObservers(RECORDING_SELECTED);
	}
	
	public void loadNextRecording()
	{
		if (selectedRecordingIndex < recordings.size())
		{
			selectedRecordingIndex += 1;
		}
		else
		{
			selectedRecordingIndex = 0;
		}
		setSelectedRecording(recordings.get(selectedRecordingIndex));
		setChanged();
		notifyObservers(PLAY_NOW);
	}
	
	public void loadPrevRecording()
	{
		if (selectedRecordingIndex > -1)
		{
			selectedRecordingIndex -= 1;
		}
		else
		{
			selectedRecordingIndex = recordings.size() - 1;
		}
		
		setSelectedRecording(recordings.get(selectedRecordingIndex));
		setChanged();
		notifyObservers(PLAY_NOW);
	}	

	public int getSelectedRecordingIndex()
	{
		return selectedRecordingIndex;
	}
	
	public void playNow(DreamRecording selectedRecording)
	{
		setSelectedRecording(selectedRecording);
		setChanged();
		notifyObservers(PLAY_NOW);
	}

	
	
}
