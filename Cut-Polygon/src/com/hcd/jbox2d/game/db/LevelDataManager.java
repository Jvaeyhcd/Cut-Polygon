package com.hcd.jbox2d.game.db;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
public class LevelDataManager {

	public SQLiteDatabase db;
	
	public LevelDataManager(Activity activity) {
		//打开或创建数据库
		db = activity.openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS LEVELS (_id INTEGER EXISTS PRIMARY KEY AUTOINCREMENT, level INTEGER, score INTEGER, success INTEGER)");
	}
	
	public void insertLevelInfo(int level, int score, int success){
		db.execSQL("INSERT INTO person VALUES (NULL, ？, ?, ?)", new Object[]{level, score, success});
	}
	
	public void updateLevelInfo(int level, int score, int success){
		db.execSQL("UPDATE LEVELS SET score=?,success=? WHERE level=? ", new Object[]{score,success,level});
	}
	
	
}
