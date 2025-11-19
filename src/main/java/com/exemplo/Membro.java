package com.exemplo;

public class Membro extends Usuario{
    private String endereco;
    private String telefone;
    private String email;

    private Membro(String nome, String cpf, String login, String senha, String endereco, String telefone, String email){
        super(nome, cpf, login, senha);
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }
    
    public static Membro criarMembro(String nome, String cpf, String login, String senha, String endereco, String telefone, String email){
        Membro membro = new Membro(nome, cpf, login, senha, endereco, telefone, email);

        System.out.println("Membro criado: " + nome);

        return membro;
    }

    public static void editarMembro(Membro membro, String nome, String cpf, String login, String senha, boolean ativo, String endereco, String telefone, String email){
        if(membro == null){
            System.out.println("Erro: Membro inválido.");
            return;
        }

        Usuario.editarUsuario(membro, nome, cpf, login, senha, ativo);

        membro.setEndereco(endereco);
        membro.setTelefone(telefone);
        membro.setEmail(email);

        System.out.println("Dados do membro atualizados.");
    }

    @Override
    public void verUsuario(){
        super.verUsuario();
        System.out.println("Endereço: " + this.endereco);
        System.out.println("Telefone: " + this.telefone);
        System.out.println("Email: " + this.email);
    }

    public void verMembro(){
        this.verUsuario();
    }

    public String getEndereco(){
        return endereco;
    }

    public void setEndereco(String endereco){
        this.endereco = endereco;
    }

    public String getTelefone(){
        return telefone;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
}