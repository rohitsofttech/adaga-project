package br.adricarr.game.sputvich6Alfa.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

public class DaoTabela {
    protected SQLiteOpenHelper gOpenHelper;

    public DaoTabela(SQLiteOpenHelper pBanco) {
	this.gOpenHelper = pBanco;
    }

    public DaoCursor getDaoCursor(String pSql, String[] pSelectionArgs,
	    String pEditTable) {
	SQLiteDatabase vBanco = this.gOpenHelper.getReadableDatabase();
	DaoCursor vCursor = (DaoCursor) vBanco.rawQueryWithFactory(
		new DaoCursor.Factory(), pSql, pSelectionArgs, pEditTable);
	vCursor.moveToFirst();
	return vCursor;
    }
    
    public long insert(String pTable, String pNullColumnHack, ContentValues pValues){
	SQLiteDatabase vBanco = this.gOpenHelper.getReadableDatabase();
	try{
	    return vBanco.insert(pTable, pNullColumnHack, pValues); 
	} finally {
	    vBanco.close();
	}
    }

    public static class DaoCursor extends SQLiteCursor {

	public DaoCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
		String editTable, SQLiteQuery query) {
	    super(db, driver, editTable, query);
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
	    public Factory() {
		// TODO Auto-generated constructor stub
	    }

	    @Override
	    public Cursor newCursor(SQLiteDatabase pBanco,
		    SQLiteCursorDriver driver, String editTable,
		    SQLiteQuery query) {
		return new DaoCursor(pBanco, driver, editTable, query);
	    }
	}

	public String getStringColuna(String pColuna) {
	    return getString(getColumnIndexOrThrow(pColuna));
	}

	public long getLongColuna(String pColuna) {
	    return getLong(getColumnIndexOrThrow(pColuna));
	}

    }
}