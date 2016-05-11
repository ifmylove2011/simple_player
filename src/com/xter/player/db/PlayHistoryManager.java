package com.xter.player.db;

import java.util.ArrayList;
import java.util.List;

import com.xter.player.model.PlayHistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PlayHistoryManager extends PlayDBHelper {

	private SQLiteDatabase db;

	public PlayHistoryManager(Context context) {
		super(context);
		db = new PlayDBHelper(context).getWritableDatabase();
	}

	public void add(PlayHistory ph) {
		db.beginTransaction();
		try {
			db.execSQL("INSERT INTO play_history VALUES(null,?,?,?)",
					new String[] { ph.getMovieName(), ph.getMovieUri(), ph.getMovieLastestDate() });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void update(PlayHistory ph) {
		// db.execSQL("UPDATE play_history SET movie_lastest_date WHERE
		// movie_name",
		// new String[] { ph.getMovieLastestDate(), ph.getMovieName() });
		ContentValues cv = new ContentValues();
		cv.put("movie_lastest_date", ph.getMovieLastestDate());
		db.update("play_history", cv, "movie_name=?", new String[] { ph.getMovieName() });
	}

	public List<PlayHistory> query() {
		List<PlayHistory> historys = new ArrayList<PlayHistory>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			PlayHistory ph = new PlayHistory();
			ph.setMovieName(c.getString(c.getColumnIndex("movie_name")));
			ph.setMovieUri(c.getString(c.getColumnIndex("movie_uri")));
			ph.setMovieLastestDate(c.getString(c.getColumnIndex("movie_lastest_date")));
			historys.add(ph);
		}
		c.close();
		return historys;
	}

	public Cursor queryTheCursor() {
		return db.rawQuery("SELECT * FROM play_history", null);
	}

	public void close() {
		db.close();
	}
}
