package br.adricarr.game.sputvich6Alfa.dao;


import java.util.ArrayList;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DaoHelper extends SQLiteOpenHelper{
    private final static String LOG_TAG = "BANCO DE DADOS"; 
    private String[] gSQL_CREATE;
    private ArrayList<String> gSQL_UPDATE;
    
    public DaoHelper(Context pContext, String pName, CursorFactory pFactory,
	    int pVersion, String [] pSQL_Create) {
	super(pContext, pName, pFactory, pVersion);
	 this.gSQL_CREATE = pSQL_Create; 
	 this.gSQL_UPDATE = new ArrayList<String>();
    }
    
    @Override
    public void onCreate(SQLiteDatabase pBanco) {
	pBanco.beginTransaction();
	try {
	    ExecutarComandosSQL(pBanco, this.gSQL_CREATE);
	    pBanco.setTransactionSuccessful();
	} catch (SQLException e) {
	    Log.e("Erro ao criar as tabelas e testar os dados", e.toString());
	} finally {
	    pBanco.endTransaction();
	}
    }

    @Override
    public void onUpgrade(SQLiteDatabase pBanco, int pOldVersion,
	    int pNewVersion) {
	Log.w(DaoHelper.LOG_TAG, "Atualizando a base de dados da versão "
		+ pOldVersion + " para " + pNewVersion);
	pBanco.beginTransaction();
	try {
	    ExecutarComandosSQL(pBanco, (String[]) this.gSQL_UPDATE.toArray());
	    pBanco.setTransactionSuccessful();
	} catch (SQLException e) {
	    Log.e("Erro ao atualizar as tabelas e testar os dados",
		    e.toString());
	    throw e;
	} finally {
	    pBanco.endTransaction();
	}
    }

    public static void ExecutarComandosSQL(final SQLiteDatabase pBanco,
	    final String[] pSql) {
	for (String s : pSql)
	    if (s.trim().length() > 0)
		pBanco.execSQL(s);
    }
    
    public void addSqlUpdate(String pSql){
	this.gSQL_UPDATE.add(pSql);
    }

}
