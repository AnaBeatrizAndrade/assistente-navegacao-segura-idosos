package br.ufms.assistente.navegacao.model;

public class FamiliarResponsavel {

    private int idFamiliar;
    private String nome;
    private transient String telefone;
    private int idUsuario;

    public FamiliarResponsavel() {}

    public FamiliarResponsavel(int idFamiliar, String nome, String telefone, int idUsuario) {
        this.idFamiliar = idFamiliar;
        this.nome = nome;
        this.telefone = telefone;
        this.idUsuario = idUsuario;
    }

    public int getIdFamiliar() {
        return idFamiliar;
    }

    public void setIdFamiliar(int idFamiliar) {
        this.idFamiliar = idFamiliar;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String montarMensagemAlerta(String nomeIdoso) {
        return "Alerta de Segurança\n\n"
                + nomeIdoso + " acabou de receber um aviso de possível golpe no celular.\n"
                + "Por favor, entre em contato com " + nomeIdoso
                + " para ajudá-lo(a) a tomar uma decisão segura.\n\n"
                + "- Assistente de Navegação Segura";
    }

    public void notificarAlerta(String nomeIdoso) {

    }

    @Override
    public String toString() {
        return "FamiliarResponsavel{" +
                "idFamiliar=" + idFamiliar +
                ", nome='" + nome + '\'' +
                '}';
    }
}
