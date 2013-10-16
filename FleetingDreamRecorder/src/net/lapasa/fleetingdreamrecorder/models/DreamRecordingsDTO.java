package net.lapasa.fleetingdreamrecorder.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.lapasa.alarmlib.models.Alarm;
import net.lapasa.alarmlib.models.AlarmsSQLHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DreamRecordingsDTO
{
	private SQLiteDatabase database;
	private DreamRecordingsSQLiteHelper dbHelper;
	private String[] allColumns =
	{ 
		DreamRecordingsSQLiteHelper.ID,
		DreamRecordingsSQLiteHelper.TITLE,
		DreamRecordingsSQLiteHelper.FILELOC,
		DreamRecordingsSQLiteHelper.CREATED,
		DreamRecordingsSQLiteHelper.LAST_UPDATED,
		DreamRecordingsSQLiteHelper.DREAMER,
		DreamRecordingsSQLiteHelper.DREAMER_ID,
		DreamRecordingsSQLiteHelper.DURATION
	};

	public DreamRecordingsDTO(Context context)
	{
		dbHelper = new DreamRecordingsSQLiteHelper(context);
	}

	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}

	public DreamRecording create(String fileLocation, String duration)
	{
		// Prepare insert operation parameters
		ContentValues cv = new ContentValues();				
		cv.put(DreamRecordingsSQLiteHelper.FILELOC, fileLocation);
		cv.put(DreamRecordingsSQLiteHelper.CREATED, Calendar.getInstance().getTimeInMillis());
		cv.put(DreamRecordingsSQLiteHelper.LAST_UPDATED, Calendar.getInstance().getTimeInMillis());
		cv.put(DreamRecordingsSQLiteHelper.TITLE, getTitle(fileLocation));
		cv.put(DreamRecordingsSQLiteHelper.DREAMER, "Current User");		
		cv.put(DreamRecordingsSQLiteHelper.DREAMER_ID, -1);
		cv.put(DreamRecordingsSQLiteHelper.DURATION, duration);
		// Perform insert action
		long insertId = database.insert(DreamRecordingsSQLiteHelper.TABLE_RECORDINGS, null, cv);
		
		// Retrive the recently inserted recrod
		Cursor cursor = database.query(
				DreamRecordingsSQLiteHelper.TABLE_RECORDINGS, 
				allColumns, DreamRecordingsSQLiteHelper.ID + " = " + insertId, null, null, null, null);
		
		// Go to first item in result
		cursor.moveToFirst();
		
		DreamRecording newAudioRec = cursorToAudioRec(cursor);
		cursor.close();
		return newAudioRec;
	}

	/**
	 * Return the last token as the string
	 * 
	 * @param fileLocation
	 * @return
	 */
	private String getTitle(String fileLocation)
	{
		String[] strArr = fileLocation.split("/");
		
		return strArr[strArr.length - 1];
	}

	public void deleteComment(DreamRecording audioRec)
	{
		long id = audioRec.id;
		System.out.println("AudioRecording deleted with id: " + id);
		database.delete(
				DreamRecordingsSQLiteHelper.TABLE_RECORDINGS, 
				DreamRecordingsSQLiteHelper.ID + " = " + id, null);
	}

	public List<DreamRecording> getAllRecordings()
	{
		open();
		List<DreamRecording> recordings = new ArrayList<DreamRecording>();

		Cursor cursor = database.query(
			DreamRecordingsSQLiteHelper.TABLE_RECORDINGS, allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		
		while (!cursor.isAfterLast())
		{
			DreamRecording audioRec = cursorToAudioRec(cursor);
			recordings.add(audioRec);
			cursor.moveToNext();
		}

		cursor.close();
		return recordings;
	}

	private DreamRecording cursorToAudioRec(Cursor cursor)
	{
		DreamRecording audioRec = new DreamRecording();
		
		audioRec.id = cursor.getLong(DreamRecordingsSQLiteHelper.COL_ID);
		audioRec.title = cursor.getString(DreamRecordingsSQLiteHelper.COL_TITLE);
		audioRec.fileLocation = cursor.getString(DreamRecordingsSQLiteHelper.COL_FILELOC);
		audioRec.created = audioRec.getCal(cursor.getLong(DreamRecordingsSQLiteHelper.COL_CREATED));
		audioRec.lastUpdated = audioRec.getCal(cursor.getLong(DreamRecordingsSQLiteHelper.COL_LASTUPDATED));
		audioRec.dreamer = cursor.getString(DreamRecordingsSQLiteHelper.COL_DREAMER);
		audioRec.dreamerId = cursor.getInt(DreamRecordingsSQLiteHelper.COL_DREAMER_ID);
		audioRec.duration = cursor.getString(DreamRecordingsSQLiteHelper.COL_DURATION);
		return audioRec;		
	}

	public DreamRecording update(DreamRecording rc)
	{
		open();
		ContentValues cv = new ContentValues();
		cv.put(DreamRecordingsSQLiteHelper.TITLE, rc.title);
		cv.put(DreamRecordingsSQLiteHelper.FILELOC, rc.fileLocation);
				
		cv.put(DreamRecordingsSQLiteHelper.LAST_UPDATED, rc.getLastUpdatedAsLongStr());
		cv.put(DreamRecordingsSQLiteHelper.DREAMER, rc.dreamer);
		cv.put(DreamRecordingsSQLiteHelper.DREAMER_ID, rc.dreamerId);
		cv.put(DreamRecordingsSQLiteHelper.DURATION, rc.duration);
		
		long updatedId = database.update(DreamRecordingsSQLiteHelper.TABLE_RECORDINGS, cv, DreamRecordingsSQLiteHelper.ID + " = " + rc.id, null);
		
		Cursor cursor = database.query(
				DreamRecordingsSQLiteHelper.TABLE_RECORDINGS,
				DreamRecordingsSQLiteHelper.getAllColumns(),
				DreamRecordingsSQLiteHelper.ID + " = " + updatedId, null, null, null, null);
		
		cursor.moveToFirst();
		
		DreamRecording recording = cursorToAudioRec(cursor);
		cursor.close();
		close();				
		return recording;
	}
	
	public void delete(DreamRecording rc)
	{
		database.delete(DreamRecordingsSQLiteHelper.TABLE_RECORDINGS, DreamRecordingsSQLiteHelper.ID + " = " + rc.id, null);
	}
}