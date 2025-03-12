package br.com.fiap.model;

public class Cliente {
    // Atributos
    private String id;
    private String nome;
    private String email;
    private String senha;

    // Método de verificar senha
    public boolean verificarSenha(String senha) {
        return this.senha.equals(senha);
    }


    // Construtores
    public Cliente(String id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Cliente(String id, String email, String senha) {
        this.id = id;
        this.email = email;
        this.senha = senha;
    }

    public Cliente () {}

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return  "ID       - " + id + "\n" +
                "NOME     - " + nome + "\n" +
                "EMAIL    - " + email + "\n" +
                "SENHA    - " + senha + "\n";

    }
}
