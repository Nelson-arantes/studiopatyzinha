package com.project.studiopatyzinha.banco;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.studiopatyzinha.pattern.Events;
import com.project.studiopatyzinha.pattern.Produtopattern;

import java.util.ArrayList;
import java.util.List;

public class bancolocal extends SQLiteOpenHelper {
    public static final String DB_NAME = "teste";
    public static final int bd_version = 3;
    private static bancolocal INSTANCE;

    public static bancolocal getINSTANCE(Context context) {
        INSTANCE = new bancolocal(context);
        return INSTANCE;
    }

    public bancolocal(Context context) {
        super(context, DB_NAME, null, bd_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Lembretes (idinterno INTEGER PRIMARY KEY, evento TEXT, time TEXT,day TEXT,month TEXT,year TEXT," +
                "weekOfYear TEXT,Requestcode TEXT,frequencia TEXT,duracao TEXT,descricao TEXT,atualProgresso TEXT,local TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Carrinho (idinterno INTEGER PRIMARY KEY,idexterno TEXT, nome TEXT, modelo TEXT,fabricante TEXT," +
                "disconto TEXT,preco_original TEXT,quant_per_cx TEXT,usosindicados TEXT," +
                "usosNindicados TEXT,imgbitmap TEXT,quant_produto TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        System.out.println("fazer o onUpgrade");
    }


    public void atualizarProduto(Produtopattern mip) {
        Log.d("banco", "atualizarProduto");
        try {
            SQLiteDatabase db = getReadableDatabase();

            db.execSQL("UPDATE Carrinho SET quant_produto = '" + mip.getQuant_produto() + "' WHERE idexterno = " + mip.getIdexterno());
        } catch (Error | Exception e) {
            System.out.println("Erro na atualização do produto " + e);
        }
    }


    public void deleteItemlistCarrinho(String id) {
        Log.d("banco", "deleteItemCarrinho");
        SQLiteDatabase db = getWritableDatabase();
        String Selection = "idinterno=? ";
        String[] Selectionargs = {id};
        db.delete("Carrinho", Selection, Selectionargs);


    }

    public void deletelistCarrinho() {
        Log.d("banco", "deleteItemCarrinho");
        SQLiteDatabase db = getWritableDatabase();
        String[] Selectionargs = {};
        db.delete("Carrinho", "", Selectionargs);


    }

    @SuppressLint("Range")
    public ArrayList<Produtopattern> getListCarrinho() {
        Log.d("banco", "getListCarrinho");
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM Carrinho", new String[]{});
        ArrayList<Produtopattern> carrinho = new ArrayList<>();
        while (cursor.moveToNext()) {
            String idexterno = cursor.getString(cursor.getColumnIndex("idexterno"));
            String idinterno = cursor.getString(cursor.getColumnIndex("idinterno"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            String modelo = cursor.getString(cursor.getColumnIndex("modelo"));
            String fabricante = cursor.getString(cursor.getColumnIndex("fabricante"));
            String disconto = cursor.getString(cursor.getColumnIndex("disconto"));
            String preco_original = cursor.getString(cursor.getColumnIndex("preco_original"));
            String Quant_per_cx = cursor.getString(cursor.getColumnIndex("quant_per_cx"));
            String usosindicados = cursor.getString(cursor.getColumnIndex("usosindicados"));
            String usosNindicados = cursor.getString(cursor.getColumnIndex("usosNindicados"));
            String imgbitmap = cursor.getString(cursor.getColumnIndex("imgbitmap"));
            String quant_produto = cursor.getString(cursor.getColumnIndex("quant_produto"));
            Produtopattern remedio = new Produtopattern(
                    idinterno, idexterno, nome, modelo, fabricante,
                    String.valueOf(disconto), String.valueOf(preco_original),
                    Quant_per_cx, usosindicados, usosNindicados,
                    imgbitmap,
                    quant_produto, "", "");

            carrinho.add(remedio);
        }
        return carrinho;
    }

    @SuppressLint("Range")
    public Produtopattern getmipbyid(String idinterno) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM Carrinho WHERE idinterno=?", new String[]{idinterno});
        cursor.moveToFirst();
        String idexterno = "";
        String nome = "";
        String modelo = "";
        String fabricante = "";
        String disconto = "";
        String preco_original = "";
        String Quant_per_cx = "";
        String usosindicados = "";
        String usosNindicados = "";
        String imgbitmap = "";
        String quant_produto = "";
        if (cursor.getCount() == 1) {
            nome = cursor.getString(cursor.getColumnIndex("nome"));
            idexterno = cursor.getString(cursor.getColumnIndex("idexterno"));
            modelo = cursor.getString(cursor.getColumnIndex("modelo"));
            fabricante = cursor.getString(cursor.getColumnIndex("fabricante"));
            disconto = cursor.getString(cursor.getColumnIndex("disconto"));
            preco_original = cursor.getString(cursor.getColumnIndex("preco_original"));
            Quant_per_cx = cursor.getString(cursor.getColumnIndex("quant_per_cx"));
            usosindicados = cursor.getString(cursor.getColumnIndex("usosindicados"));
            usosNindicados = cursor.getString(cursor.getColumnIndex("usosNindicados"));
            imgbitmap = cursor.getString(cursor.getColumnIndex("imgbitmap"));
            quant_produto = cursor.getString(cursor.getColumnIndex("quant_produto"));
        }
        return new Produtopattern(idinterno, idexterno, nome, modelo, fabricante,
                String.valueOf(disconto),
                String.valueOf(preco_original),
                Quant_per_cx,
                usosindicados,
                usosNindicados,
                imgbitmap,
                quant_produto, "", "");

    }

    public long addlistcarrinho(Produtopattern mip) {
        Log.d("banco", "addItemCarrinho");
        long resultEvento = 0;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("idexterno", mip.getIdexterno());
            valores.put("nome", mip.getNome());
            valores.put("usosindicados", mip.getUsosindicados());
            valores.put("usosNindicados", mip.getUsosNindicados());
            valores.put("modelo", mip.getModelo());
            valores.put("fabricante", mip.getFabricante());
            valores.put("disconto", mip.getDesconto());
            valores.put("preco_original", mip.getPreco_original());
            valores.put("quant_produto", mip.getQuant_produto());
            valores.put("Quant_per_cx", mip.getQuant_per_cx());
            valores.put("imgbitmap", mip.getBitmapImg());
            resultEvento = db.insert("Carrinho", null, valores);

        } catch (Exception e) {
            System.out.println("erro na insersao dos Produtos " + e);
        }
        return resultEvento;
    }

    @SuppressLint("Range")
    public Events getLembreteByID(String id) {
        Events evento = null;
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM Lembretes WHERE Requestcode=? ", new String[]{id});
        while (cursor.moveToNext()) {
            String day = cursor.getString(cursor.getColumnIndex("day"));
            String Evento = cursor.getString(cursor.getColumnIndex("evento"));
            String Time = cursor.getString(cursor.getColumnIndex("time"));
            String ID = cursor.getString(cursor.getColumnIndex("Requestcode"));
            String weekOfYear = cursor.getString(cursor.getColumnIndex("weekOfYear"));
            String month = cursor.getString(cursor.getColumnIndex("month"));
            String year = cursor.getString(cursor.getColumnIndex("year"));
            String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
            String duracao = cursor.getString(cursor.getColumnIndex("duracao"));
            String frequencia = cursor.getString(cursor.getColumnIndex("frequencia"));
            String local = cursor.getString(cursor.getColumnIndex("local"));
            String atualProgresso = cursor.getString(cursor.getColumnIndex("atualProgresso"));
            evento = new Events(Evento, ID, Time, day, month, year, weekOfYear,
                    frequencia, descricao, duracao, atualProgresso, local);
        }
        return evento;
    }

    public long addLembrete(Events evento) {
        Log.d("banco", "addLembrete");
        long resultEvento = 0;
        try {
            SQLiteDatabase db = getReadableDatabase();

            ContentValues valores = new ContentValues();
            valores.put("evento", evento.getEVENT());
            valores.put("time", evento.getTIME());
            valores.put("day", evento.getDAY());
            valores.put("month", evento.getMONTH());
            valores.put("frequencia", evento.getFrequencia());
            valores.put("descricao", evento.getDescricao());
            valores.put("local", evento.getLocal());
            valores.put("duracao", evento.getDuracao());
            valores.put("atualProgresso", evento.getAtualProgresso());
            valores.put("year", evento.getYEAR());
            valores.put("Requestcode", evento.getRequestCode());
            valores.put("weekOfYear", evento.getWeekOfYear());

            resultEvento = db.insert("Lembretes", null, valores);


        } catch (Exception e) {
            System.out.println("erro na insersao dos Lembretes " + e);
        }
        return resultEvento;
    }


    public void updateLembreteAll(Events evento) {
        Log.d("banco", "updateLembreteAll");
        SQLiteDatabase db = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("evento", evento.getEVENT());
        contentValues.put("time", evento.getTIME());
        contentValues.put("day", evento.getDAY());
        contentValues.put("month", evento.getMONTH());
        contentValues.put("frequencia", evento.getFrequencia());
        contentValues.put("descricao", evento.getDescricao());
        contentValues.put("duracao", evento.getDuracao());
        contentValues.put("atualProgresso", evento.getAtualProgresso());
        contentValues.put("year", evento.getYEAR());
        contentValues.put("weekOfYear", evento.getWeekOfYear());
        String Selection = "Requestcode=?";
        String[] Selectionargs = {evento.getRequestCode()};
        db.update("Lembretes", contentValues, Selection, Selectionargs);
    }

    public void deleteLembrete(String id) {
        Log.d("banco", "deleteLembrete");
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Lembretes", "Requestcode=? ", new String[]{id});
    }

    @SuppressLint("Range")
    public List<Events> get_Lembrete() {
        Log.d("banco", "get_Lembrete");
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Lembretes", new String[]{});
        List<Events> evento = new ArrayList<>();
        while (cursor.moveToNext()) {
            String Evento = cursor.getString(cursor.getColumnIndex("evento"));
            String Time = cursor.getString(cursor.getColumnIndex("time"));
            String WeekOfYear = cursor.getString(cursor.getColumnIndex("weekOfYear"));
            String ID = cursor.getString(cursor.getColumnIndex("Requestcode"));
            String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
            String duracao = cursor.getString(cursor.getColumnIndex("duracao"));
            String frequencia = cursor.getString(cursor.getColumnIndex("frequencia"));
            String day = cursor.getString(cursor.getColumnIndex("day"));
            String month = cursor.getString(cursor.getColumnIndex("month"));
            String year = cursor.getString(cursor.getColumnIndex("year"));
            String local = cursor.getString(cursor.getColumnIndex("local"));
            String atualProgresso = cursor.getString(cursor.getColumnIndex("atualProgresso"));
            Events events = new Events(Evento, ID, Time, day, month, year, WeekOfYear,
                    frequencia, descricao, duracao, atualProgresso, local);
            evento.add(events);
        }
        return evento;
    }


}
