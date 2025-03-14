package br.com.fiap.resource;

import br.com.fiap.bo.ClienteBO;
import br.com.fiap.dao.ClienteDAO;
import br.com.fiap.exception.IdNaoEncontradoException;
import br.com.fiap.model.Cliente;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Path("/cliente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private ClienteBO clienteBO = new ClienteBO();

    // POST - Criar cliente
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarCliente(Cliente cliente, @Context UriInfo uriInfo) {
        try {
            clienteBO.validarCliente(cliente);

            boolean usuarioCadastrado = clienteDAO.clienteExiste(cliente.getEmail());

            if (usuarioCadastrado) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Cliente já cadastrado com o email fornecido.")
                        .build();
            }

            clienteDAO.cadastrar(cliente);

            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            return Response.created(builder.path(cliente.getId()).build())
                    .entity(cliente)
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Erro ao criar cliente: " + e.getMessage())
                    .build();
        }
    }


    // GET - Login Cliente
    @GET
    @Path("/login")
    public Response loginCliente(@QueryParam("email") String email, @QueryParam("senha") String senha) {
        try {
            System.out.println("Recebida requisição de login! Email: " + email + ", Senha: " + senha);

            Cliente cliente = clienteDAO.buscarPorLogin(email, senha);

            if (cliente == null) {
                System.out.println("Login falhou: E-mail ou senha incorretos.");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("E-mail ou senha incorretos.")
                        .build();
            }

            System.out.println("Login bem-sucedido para: " + email);
            return Response.ok(cliente).build();
        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Erro de banco de dados.")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro inesperado no servidor.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro no servidor.")
                    .build();
        }
    }

    // GET - Cliente específico
    @GET
    @Path("/{email}")
    public Response getCliente(@PathParam("email") String email) {
        try {
            Cliente cliente = clienteDAO.buscarPorEmail(email);
            return Response.ok(cliente).build();
        } catch (IdNaoEncontradoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // GET - Listar clientes
    @GET
    public Response getTodosClientes() {
        List<Cliente> clientes = clienteDAO.listar();
        return Response.ok(clientes).build();

    }

    // PUT - Atualizar cliente
    @PUT
    @Path("/{email}")
    public Response atualizarCliente(@PathParam("email") String email, Cliente clienteAtualizado, @Context UriInfo uriInfo) {
        try {
            Cliente cliente = clienteDAO.buscarPorEmail(email);

            cliente.setNome(clienteAtualizado.getNome());
            cliente.setEmail(clienteAtualizado.getEmail());
            cliente.setSenha(clienteAtualizado.getSenha());

            clienteDAO.atualizar(cliente);

            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            return Response.created(builder.path(cliente.getId()).build()).entity(cliente).build();
        } catch (IdNaoEncontradoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // DELETE - Remover cliente
    @DELETE
    @Path("/{id}")
    public Response removerCliente(@PathParam("id") String id) {
        try {
            Cliente cliente = clienteDAO.pesquisarPorId(id);
            clienteDAO.remover(id);
            return Response.noContent().build();
        } catch (IdNaoEncontradoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}

