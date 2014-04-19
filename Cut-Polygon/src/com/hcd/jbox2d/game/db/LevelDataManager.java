package com.hcd.jbox2d.game.db;

import java.util.ArrayList;

import com.hcd.jbox2d.game.obj.Stage;
import com.hcd.jbox2d.game.utils.UtilsConstant;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class LevelDataManager {

	public SQLiteDatabase db;
	
	public LevelDataManager(Activity activity) {
		//打开或创建数据库
		db = activity.openOrCreateDatabase("game.db", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS stage (_id INTEGER PRIMARY KEY AUTOINCREMENT, level INTEGER, score INTEGER, success INTEGER)");
		
		//如果没有数据先写入一个默认数据
		if (getAllStages().size() == 0) {
			insertLevelInfo(1, 0, 0);
		}
	}
	
	public void insertLevelInfo(int level, int score, int success){
		db.execSQL("INSERT INTO stage (_id,level,score,success) VALUES (NULL,?,?,?)", new Object[]{level, score, success});
	}
	
	/**
	 * 更新关卡数据
	 * @param level 关卡等级
	 * @param score 关卡分数
	 * @param success 关卡成功否
	 */
	public void updateLevelInfo(int level, int score, int success){
		db.execSQL("UPDATE stage SET score=?,success=? WHERE level=? ", new Object[]{score,success,level});
		ArrayList<Stage> stages = getAllStages();
	}
	
	public ArrayList<Stage> getAllStages() {
		ArrayList<Stage> stages = new ArrayList<Stage>();
		
		Cursor c = db.rawQuery("SELECT * FROM stage", null);
		while(c.moveToNext()) {
			Stage stage = new Stage();
			int level = c.getInt(c.getColumnIndex("level"));
			int success = c.getInt(c.getColumnIndex("success"));
			int score = c.getInt(c.getColumnIndex("score"));
			stage.setLevel(level);
			stage.setSuccess(success);
			stage.setScore(score);
			Log.i("Stage", "Level:"+level+" Score:"+score+" success:"+success);
			stages.add(stage);
		}
		c.close();
		return stages;
	}
	
	public ArrayList<Stage> getStageByLevel(int level) {
		
		ArrayList<Stage> stages = new ArrayList<Stage>();
		
		Cursor c = db.rawQuery("SELECT * FROM stage WHERE level = ?", new String[]{level+""});
		while(c.moveToNext()) {
			Stage stage = new Stage();
			int success = c.getInt(c.getColumnIndex("success"));
			int score = c.getInt(c.getColumnIndex("score"));
			stage.setLevel(level);
			stage.setScore(score);
			stage.setSuccess(success);
			Log.i("Stage", "Level:"+level+" Score:"+score+" success:"+success);
			stages.add(stage);
		}
		c.close();
		return stages;
	}
}
