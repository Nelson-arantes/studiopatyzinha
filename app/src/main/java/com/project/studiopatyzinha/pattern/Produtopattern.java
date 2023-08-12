package com.project.studiopatyzinha.pattern;

public class Produtopattern {
    private String nome;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    private String descricao;
    private String modelo;
    private String fabricante;
    private String desconto;
    private String preco_original;
    private String quant_per_cx;
    private String idexterno;
    private String idinterno;
    private String usosindicados;
    private String usosNindicados;
    private String bitmapImg;
    private String quant_minima;
    private String quant_produto_interno;
    private String idpedido;
    private String quant_produto;

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    private String selected;




    public String getQuant_minima() {
        return quant_minima;
    }

    public void setQuant_minima(String quant_minima) {
        this.quant_minima = quant_minima;
    }

    public String getQuant_produto_interno() {
        return quant_produto_interno;
    }

    public void setQuant_produto_interno(String quant_produto_interno) {
        this.quant_produto_interno = quant_produto_interno;
    }



    public String getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(String idpedido) {
        this.idpedido = idpedido;
    }





    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getPreco_original() {
        return preco_original;
    }

    public void setPreco_original(String preco_original) {
        this.preco_original = preco_original;
    }

    public String getQuant_per_cx() {
        return quant_per_cx;
    }

    public void setQuant_per_cx(String quant_per_cx) {
        this.quant_per_cx = quant_per_cx;
    }

    public String getIdexterno() {
        return idexterno;
    }

    public void setIdexterno(String idexterno) {
        this.idexterno = idexterno;
    }

    public String getIdinterno() {
        return idinterno;
    }

    public void setIdinterno(String idinterno) {
        this.idinterno = idinterno;
    }

    public String getUsosindicados() {
        return usosindicados;
    }

    public void setUsosindicados(String usosindicados) {
        this.usosindicados = usosindicados;
    }

    public String getUsosNindicados() {
        return usosNindicados;
    }

    public void setUsosNindicados(String usosNindicados) {
        this.usosNindicados = usosNindicados;
    }

    public String getBitmapImg() {
        return bitmapImg;
    }

    public void setBitmapImg(String bitmapImg) {
        this.bitmapImg = bitmapImg;
    }

    public String getQuant_produto() {
        return quant_produto;
    }

    public void setQuant_produto(String quant_produto) {
        this.quant_produto = quant_produto;
    }
public Produtopattern(){

}
    public Produtopattern(
            String idinterno, String idexterno, String nome, String modelo, String fabricante,
            String desconto, String preco_original, String Quant_per_cx,
            String usosindicados, String usosNindicados,
            String imgbitmap, String quant_produto,String quant_minima,String quant_produto_interno) {
        this.nome = nome;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.desconto = desconto;
        this.preco_original = preco_original;
        this.quant_per_cx = Quant_per_cx;
        this.idexterno = idexterno;
        this.idinterno = idinterno;
        this.usosindicados = usosindicados;
        this.usosNindicados = usosNindicados;
        this.bitmapImg = imgbitmap;
        this.quant_produto = quant_produto;
        this.quant_produto_interno = quant_produto_interno;
        this.quant_minima = quant_minima;
    }
}
