package br.com.fiap.exception;

public class IdNaoEncontradoException extends RuntimeException {
    public IdNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
