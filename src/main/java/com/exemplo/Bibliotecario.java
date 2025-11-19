package com.exemplo;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Bibliotecario extends Usuario {

    private String cargo;

    private static final List<String> CARGOS_VALIDOS = Arrays.asList(
            "estagiario",
            "supervisor",
            "atendente");

    private Bibliotecario(String nome, String cpf, String login, String senha, String cargo) {
        super(nome, cpf, login, senha);
        this.cargo = cargo;
    }

    public static Bibliotecario criarBibliotecario(String nome, String cpf, String login, String senha, String cargo) {

        if (!CARGOS_VALIDOS.contains(cargo.toLowerCase())) {
            System.out.println("Cargo inválido!");
        }

        return new Bibliotecario(nome, cpf, login, senha, cargo);
    }

    public static void editarBibliotecario(Bibliotecario bibliotecario, String nome, String cpf, String login, String senha, boolean ativo, String cargo) {
        if(bibliotecario == null){
            System.out.println("Erro: Bibliotecário inválido.");
            return;
        }

        Usuario.editarUsuario(bibliotecario, nome, cpf, login, senha, ativo);

        bibliotecario.setCargo(cargo);
        System.out.println("Dados do bibliotecário atualizados.");      
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo){
        if(!CARGOS_VALIDOS.contains(cargo.toLowerCase())){
            this.cargo = cargo;
        } else{
            System.out.println("Erro ao editar: O cargo '" + cargo + "' não é válido. Mantendo o anterior.");
        }
    }

    @Override
    public void verUsuario(){
        super.verUsuario();
        System.out.println("Cargo: " + this.cargo);
    }
}