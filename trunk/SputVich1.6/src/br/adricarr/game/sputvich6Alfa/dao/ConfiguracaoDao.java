package br.adricarr.game.sputvich6Alfa.dao;

import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

public class ConfiguracaoDao extends DaoTabela {

    public ConfiguracaoDao(SQLiteOpenHelper pBanco) {
	super(pBanco);
    }
    
    public static class ConfiguracaoCursor extends DaoCursor {

	public ConfiguracaoCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
		String editTable, SQLiteQuery query) {
	    super(db, driver, editTable, query);
	    // TODO Auto-generated constructor stub
	}
	
	public long getID() {
	    return  getLongColuna("CD_CONFIGURACAO");
	}
	
	public long getTipoControle(){
	    return getLongColuna("TP_CONTROLES");
	}
    }
}
