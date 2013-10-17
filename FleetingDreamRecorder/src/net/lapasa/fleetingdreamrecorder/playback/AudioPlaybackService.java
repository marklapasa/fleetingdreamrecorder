package net.lapasa.fleetingdreamrecorder.playback;

import java.io.IOException;
import java.util.Observable;

import net.lapasa.fleetingdreamrecorder.models.DreamRecordingsModel;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.widget.Toast;

public class AudioPlaybackService extends Observable implements OnCompletionListener
{
	private static int REFRESH_INTERVAL = 1000;
	private static AudioPlaybackService _instance;
	public static final String PLAYBACK_ELAPSED = "Playback Elapsed";
	private Context context;
	private MediaPlayer mediaPlayer;
	private DreamRecordingsModel model;
	private boolean paused;
	private Handler handler;
	private Runnable onPlaybackElapsed;
	private int duration = -1;

	private AudioPlaybackService(Context context)
	{
		this.context = context;
		this.model = DreamRecordingsModel.getInstance(context);
		this.handler = new Handler();
		this.onPlaybackElapsed = getPlayBackElapsedRunnable();
	}

	private Runnable getPlayBackElapsedRunnable()
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				setChanged();
				notifyObservers(PLAYBACK_ELAPSED);
				handler.postDelayed(this, REFRESH_INTERVAL);

			}
		};
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
		
		duration = mediaPlayer.getDuration();
		
		handler.postDelayed(onPlaybackElapsed, REFRESH_INTERVAL);
		
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
		
		handler.removeCallbacks(onPlaybackElapsed);
		setChanged();
		notifyObservers(isPlaying());
	}
	
	public void resume()
	{
		paused = false;
		mediaPlayer.start();
		handler.postDelayed(onPlaybackElapsed, REFRESH_INTERVAL);
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
		handler.removeCallbacks(onPlaybackElapsed);
		duration = -1;
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
		handler.removeCallbacks(onPlaybackElapsed);
		setChanged();
		notifyObservers(DreamRecordingsModel.PLAYBACK_COMPLETE);		
	}
	
	
	/**
	 * 
	 * @param position	integer between 0..100
	 */
	public void seek(int position)
	{
		if (mediaPlayer != null)
		{
			// Get total seconds
			int durationInMillis = mediaPlayer.getDuration();
			
			/*
			userPosition       seekTo?
			============== x =========
			     100	       duration
			*/
			
			mediaPlayer.seekTo((position * durationInMillis)/100);
			
			if (paused)
			{
				// Don't do anything
			}
			else
			{
				// Play at this new position
				mediaPlayer.start();
			}
		}
	}
	
	public int getCurrentPosition()
	{
		if (mediaPlayer != null)
		{
			return mediaPlayer.getCurrentPosition();
		}
		else
		{
			return -1;
		}				
	}
	
	public int getDuration()
	{
		return duration;
	}

}
