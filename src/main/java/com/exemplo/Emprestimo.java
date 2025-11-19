package com.exemplo;

import java.util.Date;

public class Emprestimo {
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucaoReal;
    private boolean devolvido;
    private Livro livro;
    private Membro membro;

    private Emprestimo(Livro livro, Membro membro, Date dataEmprestimo, Date dataDevolucaoPrevista){
        this.livro = livro;
        this.membro = membro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.devolvido = false;
        this.dataDevolucaoReal = null;
    }

    public static Emprestimo criarEmprestimo(Livro livro, Membro membro){
        if(livro == null || membro == null){
            System.out.println("");
            return null;
        }

        Date dataAtual = new Date();

        
    }
    
}
