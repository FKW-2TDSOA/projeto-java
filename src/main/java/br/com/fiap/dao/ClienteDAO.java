package br.com.fiap.dao;

import br.com.fiap.exception.IdNaoEncontradoException;
import br.com.fiap.model.Cliente;
import br.com.fiap.factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Método de cadastrar um cliente
    public void cadastrar(Cliente cliente) {
        Connection conexao = null;
        PreparedStatement stmt = null;

        try {
            conexao = ConnectionFactory.getConnection();
            String sql = "INSERT INTO T_CLIENTE (NOME, EMAIL, SENHA) VALUES (?, ?, ?)";
            stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getSenha());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                cliente.setId(generatedKeys.getString(1));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente no banco de dados.");
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método de listar clientes
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM T_CLIENTE";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getString("id_cliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSenha(rs.getString("senha"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    // Método de pesquisar por ID
    public Cliente buscarPorEmail(String email) throws SQLException {
        Cliente cliente = null;
        String sql = "SELECT * FROM T_CLIENTE WHERE LOWER(email) = LOWER(?)";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, email.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                        rs.getString("id_cliente"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                );
            }
        }
        return cliente;
    }

    // Método de pesquisar por ID
    public Cliente pesquisarPorId(String id) {
        Cliente cliente = null;
        String sql = "SELECT * FROM T_CLIENTE WHERE id_cliente = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                        rs.getString("id_cliente"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                );
            } else {
                throw new IdNaoEncontradoException("Cliente não encontrado com id: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao pesquisar cliente", e);
        }

        return cliente;
    }

    // Método de atualizar cliente
    public void atualizar(Cliente cliente) {
        String sql = "UPDATE T_CLIENTE SET nome = ?, email = ?, senha = ? WHERE id_cliente = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getSenha());
            stmt.setString(4, cliente.getId());

            System.out.println("Executando atualização para o cliente: " + cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método de remover cliente
    public Cliente remover(String id) {
        String sql = "DELETE FROM T_CLIENTE WHERE id_cliente = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método de verificar se o cliente já existe
    public boolean clienteExiste(String email) {
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();

            String sql = "SELECT COUNT(*) FROM T_CLIENTE WHERE EMAIL = ?";
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, email);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    // Método de buscar login cliente
    public Cliente buscarPorLogin(String email, String senha) throws SQLException {
        Cliente cliente = null;
        String sql = "SELECT * FROM T_CLIENTE WHERE EMAIL = ? AND SENHA = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                        rs.getString("NOME"),
                        rs.getString("EMAIL"),
                        rs.getString("SENHA")
                );
            } else {
                throw new SQLException("Email não encontrado ou senha incorreta.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
            throw e;
        }

        return cliente;
    }

}

