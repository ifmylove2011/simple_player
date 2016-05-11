package com.xter.player.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayDBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "player.db";
	public static final int DATABASE_VERSION = 1;

	public PlayDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS play_history("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,media_name VARCHAR,media_uri VARCHAR,media_lastest_date VARCHAR) ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
