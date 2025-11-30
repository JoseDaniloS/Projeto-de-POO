package com.exemplo.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.exemplo.exceptions.LoginExceptions;
import com.exemplo.repositories.UsuarioRepository;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.DynamoUtils;
import com.exemplo.utils.UsuarioUtils;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

abstract public class Usuario {
    private String nome;
    private String cpf;
    private String login;
    private String senha;
    private boolean ativo;

    protected Usuario(String nome, String cpf, String login, String senha) {
        this.nome = nome;
        this.login = login;
        this.cpf = cpf;
        this.senha = senha;
        this.ativo = true;
    }

    // Exibir dados do usuario autenticado
    public void verUsuario() {
        System.out.println("----- DADOS DO USUÁRIO -----");
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("Ativo: " + (ativo ? "Ativo" : "Inativo"));

    }

    // Editar um usuario ativo
    public static void editarUsuario(Usuario usuario, String nome, String cpf, String login, String senha,
            boolean ativo) {

        if (!usuario.isAtivo()) {
            System.out.println("Não é possível editar um usuário inativo.");
            return;
        }

        try {
            Map<String, AttributeValue> item = UsuarioUtils.toMap(usuario);
            DynamoUtils.enviarElementoBancoDeDados(item, "UsuariosPOO");
            System.out.println("Dados do usuário atualizados e salvos no banco.");
        } catch (Exception ex) {
            System.out.println("Erro ao salvar usuário no banco: " + ex.getMessage());
        }
    }

    public static boolean autenticar(String login, String senha) {
        try {
            Map<String, AttributeValue> dados = UsuarioRepository.buscarPorId(login);

            if (dados == null)
                throw new LoginExceptions("Usuario não informado!");

            String senhaHash = dados.get("senha").s();

            if (dados.get("ativo").bool() == false) {
                throw new LoginExceptions("Usuario desativado! Contate o administrador.");

            }

            if (!senha.equals(senhaHash)) {
                throw new LoginExceptions("Senha inválida!");

            }
            System.out.println("Usuario Autenticado com Sucesso!");
            return true;

        } catch (LoginExceptions e) {
            throw e;
        } catch (Exception e) {
            throw new LoginExceptions("Erro no banco de dados.");
        }
    }

    // Metodo estatico para verificar se um usuario está ativo
    private static boolean verificaUsuarioAtivo(Usuario usuario) {
        if (usuario.ativo == false) {
            System.out.println("Usuario já desativado!!");
            return false;
        }
        return true;
    }

    // Desativar um usuario
    public static void desativarUsuario(Usuario usuario) {
        if (!verificaUsuarioAtivo(usuario)) {
            return;
        }
        usuario.setAtivo(false);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    protected static List<Usuario> listarUsuarios(String tipo) {
        List<Map<String, AttributeValue>> listaUsuariosDynamo = null;
        if (tipo.equals("bibliotecario")) {
            listaUsuariosDynamo = UsuarioRepository.buscarTodosBibliotecarios();
        } else if (tipo.equals("membro")) {
            listaUsuariosDynamo = UsuarioRepository.buscarTodosMembros();
        } else {
            System.out.println("Tipo de usuário inválido para listagem.");
            return null;
        }

        List<Usuario> listaUsuarios = new ArrayList<Usuario>();

        for (Map<String, AttributeValue> item : listaUsuariosDynamo) {
            String nome = item.get("nome").s();
            String login = item.get("id").s();
            String cpf = item.get("cpf").s();
            String senha = item.get("senha").s();
            boolean ativo = item.get("ativo").bool();
            String cargo = item.containsKey("cargo") ? item.get("cargo").s() : "";
            // CAMPOS ADICIONAIS PARA MEMBRO
            String endereco = item.containsKey("endereco") ? item.get("endereco").s() : "";
            String telefone = item.containsKey("telefone") ? item.get("telefone").s() : "";
            String email = item.containsKey("email") ? item.get("email").s() : "";
            Usuario novoUsuario = null;

            if (!cargo.isEmpty()) {
                novoUsuario = Bibliotecario.criarBibliotecario(nome, cpf, login, senha, cargo);
                novoUsuario.setAtivo(ativo);
                listaUsuarios.add(novoUsuario);
                continue;
            }
            novoUsuario = Membro.criarMembro(nome, cpf, login, senha, endereco, telefone, email);
            novoUsuario.setAtivo(ativo);
            listaUsuarios.add(novoUsuario);

        }
        return listaUsuarios;
    }
}
