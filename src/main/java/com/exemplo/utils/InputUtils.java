package com.exemplo.utils;

import java.util.Scanner;

public class InputUtils {

    private static final Scanner scan = new Scanner(System.in);

    public static String readString(String mensagem) {
        System.out.println(mensagem);
        return scan.nextLine();
    }

    public static int readInt(String mensagem) {
        System.out.println(mensagem);
        return Integer.parseInt(scan.nextLine());
    }
    
    public static String solicitar(String label, String atual) {
        System.out.println(label + " atual: " + atual);
        String entrada = InputUtils.readString("Novo " + label + " (ENTER mantém): ");
        return entrada.trim().isEmpty() ? atual : entrada;
    }

    public static boolean solicitarBoolean(String label, boolean atual) {
        System.out.println(label + " (atual: " + (atual ? "Sim" : "Não") + ")");
        String entrada = InputUtils.readString("s/n (ENTER mantém): ");

        if (entrada.trim().isEmpty())
            return atual;
        return entrada.equalsIgnoreCase("s");
    }
    
    public static int solicitarInt(String label, int atual) {
        System.out.println(label + " atual: " + atual);
        String entrada = InputUtils.readString("Novo " + label + " (ENTER mantém): ");

        if (entrada.trim().isEmpty())
            return atual;

        try {
            return Integer.parseInt(entrada);
        } catch (Exception e) {
            System.out.println("Valor inválido! Mantendo valor atual.");
            return atual;
        }
    }

    public static String lerNome(){
        String nome;
        while (true) {
            nome = readString("Informe o nome:");
            if (nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
                break;
            } else {
                System.out.println("Nome inválido! Use apenas letras e espaços.");
            }
        }
        return nome;
    }
    public static String lerSenha(){
        String senha;
        while (true) {
            senha = readString("Informe a senha:");
            if (senha.length() >= 5 && senha.length() <= 12 && senha.matches("^[A-Za-z]+$")) {
                break;
            } else {
                System.out.println("Senha inválida! Deve ter entre 5 e 12 caracteres e conter apenas letras.");
            }
        }
        return senha;
    }
    public static String lerEmail(){
        String email;
        while (true) {
            email = readString("Informe o email:");
            String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            if (email.matches(EMAIL_REGEX)) {
                break;
            } else {
                System.out.println("Email inválido! Tente novamente.");
            }
        }
        return email;
    }
    public static String lerTelefone(){
        String telefone;
        while (true) {
            telefone = readString("Informe o telefone:");
            String digits = telefone.replaceAll("[^0-9]", "");
            if (digits.length() >= 10 && digits.length() <= 11) {
                break;
            } else {
                System.out.println("Telefone inválido! Deve conter 10 ou 11 dígitos.");
            }
        }
        return telefone;
    }
    public static String lerCpf(){
        String cpf;
        while (true) {
            cpf = readString("Informe o CPF:");
            String digits = cpf.replaceAll("\\D", "");
            if (digits.length() == 11) {
                break;
            } else {
                System.out.println("CPF inválido! Deve conter exatamente 11 dígitos.");
            }
        }
        return cpf;
    }
    public static String lerEndereco(){
        String endereco;
        while (true) {
            endereco = readString("Informe o Endereço:");
            if (!endereco.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Endereço inválido! Não pode ser vazio.");
            }
        }
        return endereco;
    }
}
