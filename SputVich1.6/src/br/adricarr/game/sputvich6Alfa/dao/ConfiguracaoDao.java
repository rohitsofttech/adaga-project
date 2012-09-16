package br.adricarr.game.sputvich6Alfa.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

public class ConfiguracaoDao extends DaoTabela {

    public ConfiguracaoDao(SQLiteOpenHelper pBanco) {
	super(pBanco);
    }
    
    public ConfiguracaoCursor getDaoCursor(String pSql, String[] pSelectionArgs,
	    String pEditTable) {
	SQLiteDatabase vBanco = this.gOpenHelper.getReadableDatabase();
	ConfiguracaoCursor vCursor = (ConfiguracaoCursor) vBanco.rawQueryWithFactory(
		new ConfiguracaoCursor.Factory(), pSql, pSelectionArgs, pEditTable);
	vCursor.moveToFirst();
	return vCursor;
    }

    public static class ConfiguracaoCursor extends SQLiteCursor {

	public static class Factory implements CursorFactory {

	    @Override
	    public Cursor newCursor(SQLiteDatabase pBanco,
		    SQLiteCursorDriver pDriver, String pEditTable,
		    SQLiteQuery pQuery) {
		// TODO Auto-generated method stub
		return new ConfiguracaoCursor(pBanco, pDriver, pEditTable, pQuery);
	    }

	}

	public ConfiguracaoCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
		String editTable, SQLiteQuery query) {
	    super(db, driver, editTable, query);
	    // TODO Auto-generated constructor stub
	}

	public long getID() {
	    return getLong(getColumnIndexOrThrow("CD_CONFIGURACAO"));
	}

	public int getTipoControle() {
	    return getInt(getColumnIndexOrThrow("TP_CONTROLES"));
	}
	
	public String getNomeJogador() {
	    return getString(getColumnIndexOrThrow("DS_JOGADOR"));
	}
    }
}
