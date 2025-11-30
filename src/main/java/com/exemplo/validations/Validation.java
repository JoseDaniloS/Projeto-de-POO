package com.exemplo.utils;

import java.util.regex.Pattern;

public class Validation {

    public static boolean isNameValid(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            return false;
        }
        Pattern invalidPattern = Pattern.compile("[^\\p{L}\\s'\\-]+");
        return !invalidPattern.matcher(nome).find();
    }

    public static boolean isCpfValid(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }

        return cpf.matches("\\d{11}");
    }

    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(EMAIL_REGEX);
    }

    public static boolean isSenhaValid(String senha) {
        if (senha == null || senha.length() < 8) {
            return false;
        }

        return true;
    }

    public static boolean isTelefoneValid(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return false;
        }

        String digits = telefone.replaceAll("[^0-9]", "");
        return digits.length() >= 8;
    }

    public static boolean isEnderecoValid(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isValid(String telefone, String email, String endereco, String nome, String cpf,
            String senha) {
        if (!isNameValid(nome))
            return false;
        if (!isCpfValid(cpf))
            return false;
        if (!isEmailValid(email))
            return false;
        if (!isSenhaValid(senha))
            return false;
        if (!isTelefoneValid(telefone))
            return false;
        if (!isEnderecoValid(endereco))
            return false;

        return true;
    }
}