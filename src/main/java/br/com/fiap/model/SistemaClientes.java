package br.com.fiap.model;

import br.com.fiap.dao.ClienteDAO;

import java.sql.SQLException;

public class SistemaClientes {

    private ClienteDAO clienteDAO;

    // Construtor
    public SistemaClientes() {
        this.clienteDAO = new ClienteDAO();
    }

    // Método de cadastrar usuário
    public boolean cadastrarUsuario(String nome, String email, String senha) {
        email = email.trim();
        nome = nome.trim();

        if (clienteDAO.clienteExiste(email)) {
            return false;
        }
        Cliente novoCliente = new Cliente(nome, email, senha);
        clienteDAO.cadastrar(novoCliente);

        return true;
    }

    // Método de login usuário
    public Cliente loginUsuario(String email, String senha) throws SQLException {
        email = email.trim();

        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente clienteBanco = clienteDAO.buscarPorLogin(email, senha);

        if (clienteBanco != null && clienteBanco.verificarSenha(senha)) {
            return clienteBanco;
        }

        return null;
    }

}
