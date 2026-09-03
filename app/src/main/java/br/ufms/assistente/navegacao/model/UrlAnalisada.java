package br.ufms.assistente.navegacao.model;

import java.util.Date;

public class UrlAnalisada {

    public static final String STATUS_SEGURO = "SEGURO";
    public static final String STATUS_PERIGOSO = "PERIGOSO";
    public static final String STATUS_SUSPEITO = "SUSPEITO";
    public static final String STATUS_PENDENTE = "PENDENTE";

    private int idUrl;
    private String urlHash;
    private transient String url;
    private String statusSeguranca;
    private Date dataVerificacao;

    public UrlAnalisada() {}

    public UrlAnalisada(String urlHash, String url, String statusSeguranca, Date dataVerificacao) {
        this.urlHash = urlHash;
        this.url = url;
        this.statusSeguranca = STATUS_PENDENTE;
        this.dataVerificacao = new Date();
    }

    public boolean isSeguro() {
        return STATUS_SEGURO.equals(statusSeguranca);
    }

    public boolean isPerigoso() {
        return STATUS_PERIGOSO.equals(statusSeguranca);
    }

    public boolean isSuspeito() {
        return STATUS_SUSPEITO.equals(statusSeguranca);
    }

    public boolean requerAlerta() {
        return isPerigoso() || isSuspeito();
    }

    public boolean isCacheValido() {
        long diff = new Date().getTime() - dataVerificacao.getTime();
        long vinteQuatroHoras = 24 * 60 * 60 * 1000L;
        return diff < vinteQuatroHoras;
    }

    public int getIdUrl() {
        return idUrl;
    }

    public void setIdUrl(int idUrl) {
        this.idUrl = idUrl;
    }

    public String getUrlHash() {
        return urlHash;
    }

    public void setUrlHash(String urlHash) {
        this.urlHash = urlHash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatusSeguranca() {
        return statusSeguranca;
    }

    public void setStatusSeguranca(String statusSeguranca) {
        this.statusSeguranca = statusSeguranca;
    }

    public Date getDataVerificacao() {
        return dataVerificacao;
    }

    public void setDataVerificacao(Date dataVerificacao) {
        this.dataVerificacao = dataVerificacao;
    }

    @Override
    public String toString() {
        return "UrlAnalisada{" +
                "idUrl=" + idUrl +
                ", urlHash='" + urlHash + '\'' +
                ", statusSeguranca='" + statusSeguranca + '\'' +
                '}';
    }
}
