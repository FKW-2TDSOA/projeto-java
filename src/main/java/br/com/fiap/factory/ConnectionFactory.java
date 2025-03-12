package br.com.fiap.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
    private static final String USER = "rm558124";
    private static final String PASSWORD = "030505";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("O Driver JDBC não foi encontrado!", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try {
            Connection conexao = getConnection();

            System.out.println("Conectado!");

            conexao.close();

        } catch (SQLException e) {
            System.err.println("Não foi possível conectar no Banco de Dados");
            e.printStackTrace();
        }
    }
}