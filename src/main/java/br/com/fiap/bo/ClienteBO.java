package br.com.fiap.bo;

import br.com.fiap.model.Cliente;
import java.util.regex.Pattern;

public class ClienteBO {

    // M[etodo de validar os dados obrigatórios
    public void validarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }

        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O email é obrigatório.");
        }

        if (!cliente.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email inválido.");
        }

        if (cliente.getSenha() == null || cliente.getSenha().isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
    }

    // Método de validar email
    public boolean validarEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(regex, email);
    }
}
