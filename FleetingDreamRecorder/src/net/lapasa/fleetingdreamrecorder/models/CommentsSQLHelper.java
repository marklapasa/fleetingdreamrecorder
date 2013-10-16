package net.lapasa.fleetingdreamrecorder.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CommentsSQLHelper extends SQLiteOpenHelper
{

	public static final String TABLE_COMMENTS = "comments";
	private static final String DATABASE_NAME = "comments.db";	
	private static final int DATABASE_VERSION = 1;

	public static final String ID = "_id";
	public static final String DREAM_RECORDING_ID = "dreamRecordingId";
	public static final String BODY = "body";
	public static final String DATE = "created";
	public static final String OWNER_ID = "ownerId";
	

	public CommentsSQLHelper(Context context)
	{		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

	}

}
