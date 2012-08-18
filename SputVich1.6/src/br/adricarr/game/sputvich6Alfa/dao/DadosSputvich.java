package br.adricarr.game.sputvich6Alfa.dao;

import android.content.Context;

public class DadosSputvich extends DaoHelper {

    private static String[] gSqlCreate = {
	    "CREATE TABLE CONFIGURACAO (ID_CONFIGURACAO NUMERIC, " +
		    "CD_CONFIGURACAO INTEGER PRIMARY KEY, DS_JOGADOR TEXT, TP_CONTROLES NUMERIC)",
		    "INSERT INTO CONFIGURACAO (ID_CONFIGURACAO, DS_JOGADOR, TP_CONTROLES) VALUES (0, 'JOGADOR' , 0)"};

    public DadosSputvich(Context pContext) {
	super(pContext, "DADOS_SPUTVICH", null, 1, gSqlCreate);
    }

}
