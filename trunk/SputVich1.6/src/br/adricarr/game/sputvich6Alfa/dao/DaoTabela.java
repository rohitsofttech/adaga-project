package br.adricarr.game.sputvich6Alfa.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DaoTabela {
    protected SQLiteOpenHelper gOpenHelper;

    public DaoTabela(SQLiteOpenHelper pBanco) {
	this.gOpenHelper = pBanco;
    }
    
    public long insert(String pTable, String pNullColumnHack, ContentValues pValues){
	SQLiteDatabase vBanco = this.gOpenHelper.getReadableDatabase();
	try{
	    return vBanco.insert(pTable, pNullColumnHack, pValues); 
	} finally {
	    vBanco.close();
	}
    }
    
    public long update(String pTable, ContentValues pValues, String pWhereClause, String[] pWhereArgs){
	SQLiteDatabase vBanco = this.gOpenHelper.getReadableDatabase();
	try{
	    return vBanco.update(pTable, pValues, pWhereClause, pWhereArgs);
	} finally {
	    vBanco.close();
	}	
    }
}