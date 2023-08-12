package com.project.studiopatyzinha.pattern;

import java.util.ArrayList;

public class Pedidospattern {
    String quantidade;

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    String dia;
    String status;

    public String getPedidoretirado() {
        return Pedidoretirado;
    }

    public void setPedidoretirado(String pedidoretirado) {
        Pedidoretirado = pedidoretirado;
    }

    String Pedidoretirado;
    String idProduto;
    String dataCompra;
    ArrayList<Produtopattern> produtoslist;
    String produtosString;
    String idPedido;
    String nomePessoa;

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    String dataRetirada;
    String idPessoa;
    String valorTotal;
    String formaPagamento;

    public String getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(String dataRetirada) {
        this.dataRetirada = dataRetirada;
    }


    public String getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(String idPessoa) {
        this.idPessoa = idPessoa;
    }


    public ArrayList<Produtopattern> getProdutoslist() {
        return produtoslist;
    }

    public String getProdutosString() {
        return produtosString;
    }

    public void setProdutosString(String produtosString) {
        this.produtosString = produtosString;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void setProdutolist(ArrayList<Produtopattern> pp) {
        produtoslist = pp;
    }

    public Pedidospattern() {
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }


    public Pedidospattern(String quantidade,
                          String dataCompra, String idPedido,
                          String valorTotal, String formaPagamento, String status,
                          String Pedidospatterns, String dataRetirada, String pedidoretirado) {
        this.produtosString = Pedidospatterns;
        this.idPedido = idPedido;
        this.quantidade = quantidade;
        this.Pedidoretirado = pedidoretirado;
        this.dataRetirada = dataRetirada;
        this.dataCompra = dataCompra;
        this.valorTotal = valorTotal;
        this.status = status;
        this.formaPagamento = formaPagamento;
    }


}
