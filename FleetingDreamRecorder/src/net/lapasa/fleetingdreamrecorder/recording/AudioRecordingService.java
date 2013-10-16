package net.lapasa.fleetingdreamrecorder.recording;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import net.lapasa.fleetingdreamrecorder.models.DreamRecordingsModel;
import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

/**
 * CRUD Audio recordings using the device's microphone
 * 
 * @author mlapasa
 * 
 */
public class AudioRecordingService
{
	public static final String commonDateFormat = "yyyy_MM_dd_hhmmaa";
	private static final String TAG = AudioRecordingService.class.getName();
	private MediaRecorder recorder;
	private String targetFileName;
	private String directoryStr;
	private DreamRecordingsModel model;
	private long recordStartTime;
	private long recordEndTime;

	public AudioRecordingService(Context context)
	{
		this.directoryStr = ((Activity) context).getExternalFilesDir(Environment.DIRECTORY_PODCASTS).getPath().toString();
		model = DreamRecordingsModel.getInstance(context);
	}

	/**
	 * Return a string representing when this media will be recorded YY MM DD HH
	 * MM
	 * 
	 * @return
	 */
	private String createFormatedDateStr()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(commonDateFormat);
		String fileName = directoryStr + "/" + sdf.format(new Date()) + "_DreamRec.3gp";
		Log.d(TAG, "Audio File: " + fileName);
		return fileName;
	}

	/**
	 * Create audio file on device and begin to record data from microphone
	 * 
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void beginRec() throws IllegalStateException, IOException
	{
		targetFileName = createFormatedDateStr();
		killMediaRecorder();

		File outFile = new File(targetFileName);
		if (outFile.exists())
		{
			outFile.delete();
		}
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(targetFileName);
		recorder.prepare();
		recorder.start();

		recordStartTime = SystemClock.elapsedRealtime();

	}

	/**
	 * Halt recording of audio, at this point it would make sense to write to
	 * the DB
	 */
	public void stopRec()
	{
		if (recorder != null)
		{
			recorder.stop();
			recorder.release();
			recorder = null;

			recordEndTime = SystemClock.elapsedRealtime();
			DreamRecording newRecording = model.create(targetFileName, getDuration());
			model.setSelectedRecording(newRecording);

		}
	}

	private void killMediaRecorder()
	{
		if (recorder != null)
		{
			recorder.release();
		}
	}

	public String getLastRecordingPath()
	{
		return targetFileName;
	}

	private String getDuration()
	{
		long diff = recordEndTime - recordStartTime;
		return formatInterval(diff);
	}

	/**
	 * http://stackoverflow.com/questions/6710094/how-to-format-an-elapsed-time-interval-in-hhmmss-sss-format-in-java
	 * 
	 * @param 
	 * @return
	 */
	public static String formatInterval(final long l)
	{
		final long hr = TimeUnit.MILLISECONDS.toHours(l);
		final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
		final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		return String.format("%02d:%02d", min, sec);
	}
}
