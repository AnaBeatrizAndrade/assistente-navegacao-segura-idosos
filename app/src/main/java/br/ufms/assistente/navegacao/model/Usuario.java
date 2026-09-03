package br.ufms.assistente.navegacao.model;

public class Usuario {

    private int idUsuario;
    private String nome;

    public Usuario() {}

    public Usuario(int idUsuario, String nome) {
        this.idUsuario = idUsuario;
        this.nome = nome;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nome='" + nome + '\'' +
                '}';
    }
}
