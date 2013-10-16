package net.lapasa.fleetingdreamrecorder.playback;

import java.io.IOException;
import java.util.Observable;

import net.lapasa.fleetingdreamrecorder.models.DreamRecordingsModel;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.Toast;

public class AudioPlaybackService extends Observable implements OnCompletionListener
{
	private static AudioPlaybackService _instance;
	
	private Context context;
	private MediaPlayer mediaPlayer;
	private DreamRecordingsModel model;

	private boolean paused;

	private AudioPlaybackService(Context context)
	{
		this.context = context;
		this.model = DreamRecordingsModel.getInstance(context);
	}

	public static AudioPlaybackService getInstance(Context context)
	{
		if (_instance == null)
		{
			_instance = new AudioPlaybackService(context);
		}
		return _instance;
	}

	

	public void play()
	{
		String targetFileName = model.getSelectedRecording().fileLocation;
		killMediaPlayer();
		mediaPlayer = new MediaPlayer();
		try
		{
			mediaPlayer.setDataSource(targetFileName);
			mediaPlayer.prepare();

		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			onPlaybackFailure(e);
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			onPlaybackFailure(e);
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
			onPlaybackFailure(e);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			onPlaybackFailure(e);
		}

		mediaPlayer.start();
		mediaPlayer.setOnCompletionListener(this);
		paused = false;
	}
	
	public void pause()
	{
		if (mediaPlayer == null)
		{
			play();
			return;
		}
		
		mediaPlayer.pause();
		paused = true;
		
		setChanged();
		notifyObservers(isPlaying());
	}
	
	public void resume()
	{
		paused = false;
		mediaPlayer.start();		
	}

	private void onPlaybackFailure(Exception e)
	{
		Toast.makeText(context, "Could not play file; Reason = " + e.getMessage(), Toast.LENGTH_LONG).show();
	}

	private void killMediaPlayer()
	{
		if (mediaPlayer != null)
		{
			try
			{
				mediaPlayer.release();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	public void stopPlayback()
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.stop();
		}
	}
	
	public boolean isPlaying()
	{
		if (mediaPlayer == null)
		{
			return false;
		}
		return mediaPlayer.isPlaying();
	}
	
	public boolean isPaused()
	{
		return paused;
	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{
		setChanged();
		notifyObservers(DreamRecordingsModel.PLAYBACK_COMPLETE);		
	}

}
