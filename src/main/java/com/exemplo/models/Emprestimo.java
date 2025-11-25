package com.exemplo.models;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Emprestimo {
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucaoReal;
    private boolean devolvido;

    private Livro livro;
    private Membro membro;

    public Emprestimo(Livro livro, Membro membro, Date dataEmprestimo, Date dataDevolucaoPrevista) {
        this.livro = livro;
        this.membro = membro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = null;
        this.devolvido = false;
    }

    public Emprestimo(Livro livro, Membro membro, Date dataEmprestimo, Date dataDevolucaoPrevista, Date dataDevolucaoReal, boolean devolvido) {
        this.livro = livro;
        this.membro = membro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.devolvido = devolvido;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Date getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista( Date dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public Date getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(Date dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

    public Livro getLivro() {
        return livro;
    }

    public Membro getMembro() {
        return membro;
    }

    public void verEmprestimo(){
        System.out.println("Livro: " + livro.getTitulo());
        System.out.println("Membro: " + membro.getNome());
        System.out.println("Data Empréstimo: " + dataEmprestimo);
        System.out.println("Data Prevista: " + dataDevolucaoPrevista);
        System.out.println("Data Real: " + (dataDevolucaoReal != null ? dataDevolucaoReal : "Pendente"));
        System.out.println("Status: " + (devolvido ? "Devolvido" : "Em Aberto"));
    }

    public void registrarDevolucao(){
        if(this.devolvido){
            System.out.println("Este empréstimo já foi devolvido.");
            return;
        }

        this.devolvido = true;
        this.dataDevolucaoReal = new Date();

        if(this.livro != null){
            this.livro.adicionarCopias(1);
        }

        System.out.println("Devolução registrada.");
    }

    public float calcularMulta(){
        if(dataDevolucaoReal == null || !dataDevolucaoReal.after(dataDevolucaoPrevista)){
            return 0f;
        }

        long diffMillis = dataDevolucaoReal.getTime() - dataDevolucaoPrevista.getTime();
        long diasAtraso = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);

        return (float) diasAtraso;
    }

    public static Emprestimo criarEmprestimo(Livro livro, Membro membro) {
        if(livro == null && membro == null){
            System.out.println("Livro e Membro são obrigatórios.");
            return null;
        }

        if(livro.getDisponiveis() <= 0){
            System.out.println("O livro '" + livro.getTitulo() + "' não está disponível no momento.");
            return null;
        }

        Date hoje = new Date();
        Date devolucaoPrevista = new Date(hoje.getTime() + (7L * 24 * 60 * 60 * 1000));

        livro.removerCopias(1);

        Emprestimo emprestimo = new Emprestimo(livro, membro, hoje, devolucaoPrevista);

        System.out.println("Empréstimo realizado com sucesso: " + livro.getTitulo());

        return emprestimo;
    }

    public static void editarEmprestimo(Emprestimo e, Date dataEmprestimo, Date dataDevolucaoPrevista, Date dataDevolucaoReal, boolean devolvido){
        if(e == null){
            return;
        }
        e.setDataEmprestimo(dataEmprestimo);
        e.setDataDevolucaoPrevista(dataDevolucaoPrevista);
        e.setDataDevolucaoReal(dataDevolucaoReal);
        e.setDevolvido(devolvido);

        System.out.println("Dados do empréstimo atualizados.");
    }

    public static void excluirEmprestimo(Emprestimo emprestimo){
        if(emprestimo != null){
            System.out.println("Empréstimo removido do histórico: " + emprestimo.getLivro().getTitulo());
        }
    }

    /* public static List<Emprestimo> listarEmprestimos() {
        return new ArrayList<>();
    }

    public static List<Emprestimo> listarEmprestimosAtrasados() {
        return new ArrayList<>();
    }

    public static List<Emprestimo> listarEmprestimosAtrasadosPorMembro(Membro membro) {
        return new ArrayList<>();
    }

    public static List<Emprestimo> listarEmprestimosAtivos() {
        return new ArrayList<>();
    }

    public static List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro) {
        return new ArrayList<>(); */
}
