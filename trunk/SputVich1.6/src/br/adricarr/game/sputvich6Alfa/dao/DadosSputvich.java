package br.adricarr.game.sputvich6Alfa.dao;

import android.content.Context;

public class DadosSputvich extends DaoHelper {

    private static String[] gSqlCreate = {
	    "CREATE TABLE IF NOT EXISTS CONFIGURACAO "
		    + "(CD_CONFIGURACAO INTEGER PRIMARY KEY AUTOINCREMENT, TP_CONTROLES NUMERIC",
	    "CREATE TABLE IF NOT EXISTS PONTUACAO" };

    public DadosSputvich(Context pContext) {
	super(pContext, "DADOS_SPUTVICH", null, 1, gSqlCreate);
    }

}
