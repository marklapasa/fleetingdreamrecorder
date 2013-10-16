package net.lapasa.fleetingdreamrecorder.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Utility for creating the Audio Recordings persistance model
 */
public class DreamRecordingsSQLiteHelper extends SQLiteOpenHelper
{
	public static final String TABLE_RECORDINGS = "audioRecordings";
	public static final String ID = "_id";
	public static final int COL_ID = 0;
	public static final String TITLE = "title";
	public static final int COL_TITLE = 1;
	public static final String FILELOC = "fileLocation";
	public static final int COL_FILELOC = 2;
	public static final String CREATED = "createdDate";
	public static final int COL_CREATED = 3;
	public static final String LAST_UPDATED = "lastUpdated";
	public static final int COL_LASTUPDATED = 4;
	public static final String DREAMER = "dreamer";
	public static final int COL_DREAMER = 5;
	public static final String DREAMER_ID = "dreamerId";
	public static final int COL_DREAMER_ID = 6;
	public static final String DURATION = "duration";
	public static final int COL_DURATION = 7;
	
	

	private static final String DATABASE_NAME = "audioRecordings.db";
	private static final int DATABASE_VERSION = 4;

	private static final String DATABASE_CREATE = 
			"create table " + TABLE_RECORDINGS 
				+ "(" + ID + " integer primary key autoincrement, " 
				+ TITLE + " text not null," 
				+ FILELOC + " text not null, "
				+ CREATED + " integer, " 
				+ LAST_UPDATED + " integer,"
				+ DREAMER + " text,"
				+ DREAMER_ID + " integer,"
				+ DURATION + " text"
				+ ");";
	

	public DreamRecordingsSQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(DreamRecordingsSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDINGS);
		onCreate(db);
	}

	public static String[] getAllColumns()
	{
		return new String[]
		{ ID, TITLE, FILELOC, CREATED, LAST_UPDATED };
	}
}
