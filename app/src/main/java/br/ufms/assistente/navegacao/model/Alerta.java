package br.ufms.assistente.navegacao.model;

import java.util.Date;

public class Alerta {

    public static final String URGENCIA_ALTA = "ALTA";
    public static final String URGENCIA_MEDIA = "MEDIA";
    public static final String DECISAO_SAIU = "SAIU_COM_SEGURANCA";
    public static final String DECISAO_ENTROU = "ENTROU_MESMO_ASSIM";
    public static final String DECISAO_FAMILIA = "ACIONOU_FAMILIAR";
    public static final String DECISAO_AGUARDA = "AGUARDANDO";

    private int idAlerta;
    private String mensagemAlerta;
    private String urlInterceptadaDominio;
    private String nivelUrgencia;
    private String decisaoUsuario;
    private Date criadoEm;
    private int idUsuario;
    private int idUrl;

    public Alerta() {
        this.criadoEm = new Date();
        this.decisaoUsuario = DECISAO_AGUARDA;
    }

    public static Alerta criarAlertaPerigo(int idUsuario, int idUrl, String dominio) {
        Alerta alerta = new Alerta();
        alerta.idUsuario = idUsuario;
        alerta.idUrl = idUrl;
        alerta.urlInterceptadaDominio = dominio;
        alerta.nivelUrgencia = URGENCIA_ALTA;
        alerta.mensagemAlerta = "Cuidado! Este link pode ser uma tentativa de golpe.\n\n"
                + "Ele pode tentar roubar seu dinheiro ou suas informações.\n\n"
                + "O mais seguro é sair daqui.";

        return alerta;
    }

    public static Alerta criarAlertaSuspeito(int idUsuario, int idUrl, String dominio) {
        Alerta alerta = new Alerta();
        alerta.idUsuario = idUsuario;
        alerta.idUrl = idUrl;
        alerta.urlInterceptadaDominio = dominio;
        alerta.nivelUrgencia = URGENCIA_MEDIA;
        alerta.mensagemAlerta = "Atenção! Não reconhecemos este link.\n\n"
                + "Se você não esperava receber isso, é melhor não continuar.\n\n"
                + "Em caso de dúvida, peça ajuda a um familiar.";

        return alerta;
    }

    public boolean isUrgenciaAlta() {
        return URGENCIA_ALTA.equals(nivelUrgencia);
    }

    public boolean familiarFoiAcionado() {
        return DECISAO_FAMILIA.equals(decisaoUsuario);
    }

    public int getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }

    public String getMensagemAlerta() {
        return mensagemAlerta;
    }

    public void setMensagemAlerta(String mensagemAlerta) {
        this.mensagemAlerta = mensagemAlerta;
    }

    public String getUrlInterceptadaDominio() {
        return urlInterceptadaDominio;
    }

    public void setUrlInterceptadaDominio(String urlInterceptadaDominio) {
        this.urlInterceptadaDominio = urlInterceptadaDominio;
    }

    public String getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(String nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    public String getDecisaoUsuario() {
        return decisaoUsuario;
    }

    public void setDecisaoUsuario(String decisaoUsuario) {
        this.decisaoUsuario = decisaoUsuario;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Date criadoEm) {
        this.criadoEm = criadoEm;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdUrl() {
        return idUrl;
    }

    public void setIdUrl(int idUrl) {
        this.idUrl = idUrl;
    }

    @Override
    public String toString() {
        return "Alerta{" +
                "idAlerta=" + idAlerta +
                ", nivelUrgencia='" + nivelUrgencia + '\'' +
                ", decisaoUsuario='" + decisaoUsuario + '\'' +
                '}';
    }
}
