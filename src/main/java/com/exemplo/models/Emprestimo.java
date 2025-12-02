package com.exemplo.models;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.exemplo.repositories.EmprestimoRepository;
import com.exemplo.repositories.LivrosRepository;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.DynamoUtils;
import com.exemplo.utils.EmprestimoUtils;
import com.exemplo.utils.LivroUtils;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

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

    public Emprestimo(Livro livro, Membro membro, Date dataEmprestimo, Date dataDevolucaoPrevista,
            Date dataDevolucaoReal, boolean devolvido) {
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

    public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) {
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

    public void verEmprestimo() {
        System.out.println("Livro: " + livro.getTitulo());
        System.out.println("Membro: " + membro.getNome());
        System.out.println("Data do Empréstimo: " + dataEmprestimo);
        System.out.println("Data da devolução prevista: " + dataDevolucaoPrevista);
        System.out.println("Data da devolução real: "
                + (dataDevolucaoReal != null ? dataDevolucaoReal : "Pendente"));
        System.out.println("Status: " + (devolvido ? "Devolvido" : "Em Aberto"));
    }

    public void registrarDevolucao() {
        if (this.devolvido) {
            System.out.println("Este empréstimo já foi devolvido.");
            return;
        }

        this.devolvido = true;
        this.dataDevolucaoReal = new Date();

        if (this.livro != null) {
            this.livro.setDisponiveis(this.livro.getDisponiveis() + 1);
            DynamoUtils.enviarElementoBancoDeDados(LivroUtils.toMap(this.getLivro()), LivrosRepository.TABLE_NAME);
        }
        

    }

    public float calcularMulta() {
        // Verifica se houve atraso na devolução
        if (dataDevolucaoReal == null || !dataDevolucaoReal.after(dataDevolucaoPrevista)) {
            return 0f;
        }
        // calcula a diferença em milissegundos
        long diffMillis = dataDevolucaoReal.getTime() - dataDevolucaoPrevista.getTime();
        // converte a diferença de milissegundos para dias
        long diasAtraso = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);

        return (float) diasAtraso;
    }

    public static Emprestimo criarEmprestimo(Livro livro, Membro membro) {
        if (livro == null || membro == null) {
            throw new IllegalArgumentException("Livro e Membro são obrigatórios.");

        }

        if (livro.getDisponiveis() <= 0) {
            throw new IllegalArgumentException(
                    "O livro '" + livro.getTitulo() + "' não está disponível no momento.");

        }

        Date hoje = new Date();
        Date devolucaoPrevista = new Date(hoje.getTime() + (7L * 24 * 60 * 60 * 1000));

        livro.removerCopias(1);
        DynamoUtils.enviarElementoBancoDeDados(LivroUtils.toMap(livro), "LivrosPOO");
        Emprestimo emprestimo = new Emprestimo(livro, membro, hoje, devolucaoPrevista);

        ConsoleUI.clear();
        System.out.println("Empréstimo realizado com sucesso: " + livro.getTitulo());
        ConsoleUI.pause();

        return emprestimo;
    }

    public static void editarEmprestimo(Emprestimo e, Date dataEmprestimo,
            Date dataDevolucaoPrevista, Date dataDevolucaoReal, boolean devolvido) {
        if (e == null) {
            return;
        }
        e.setDataEmprestimo(dataEmprestimo);
        e.setDataDevolucaoPrevista(dataDevolucaoPrevista);
        e.setDataDevolucaoReal(dataDevolucaoReal);
        e.setDevolvido(devolvido);

        System.out.println("Dados do empréstimo atualizados.");
    }

    public static void excluirEmprestimo(Emprestimo emprestimo) {
        if (emprestimo != null) {
            System.out.println(
                    "Empréstimo removido do histórico: " + emprestimo.getLivro().getTitulo());
        }
    }

    public static List<Emprestimo> listarEmprestimos() {
        try {
            List<Map<String, AttributeValue>> listaEmprestimos =
                    EmprestimoRepository.buscarTodosEmprestimos();
            if (listaEmprestimos.isEmpty()) {
                return null;
            }
            return EmprestimoUtils.criarListaEmprestimosBancoDados(listaEmprestimos);
        } catch (Exception e) {
            System.out.println("Erro ao listar empréstimos: " + e.getMessage());
            return null;
        }
    }

    public static List<Emprestimo> listarEmprestimosAtrasados() {
        try {
            List<Emprestimo> listaEmprestimosAtrasados = EmprestimoUtils
                    .criarListaEmprestimosBancoDados(EmprestimoRepository.buscarTodosEmprestimos());
            if (!listaEmprestimosAtrasados.isEmpty()) {
                listaEmprestimosAtrasados.removeIf(emprestimo -> emprestimo.calcularMulta() == 0);
            }
            if (listaEmprestimosAtrasados.isEmpty()) {
                return null;
            }
            return listaEmprestimosAtrasados;

        } catch (Exception e) {
            System.out.println("Erro ao listar empréstimos: " + e.getMessage());
            return null;
        }
    }

    public static List<Emprestimo> listarEmprestimosAtrasadosPorMembro(Membro membro) {
        try {
            List<Map<String, AttributeValue>> dadosEmprestimos =
                    EmprestimoRepository.buscarPorMembro(membro.getCpf());

            List<Emprestimo> todosEmprestimosAtrasados =
                    EmprestimoUtils.criarListaEmprestimosBancoDados(dadosEmprestimos);

            if (!todosEmprestimosAtrasados.isEmpty()) {
                todosEmprestimosAtrasados.removeIf(emprestimo -> emprestimo.calcularMulta() == 0);
            }

            if (todosEmprestimosAtrasados.isEmpty()) {
                return null;
            }
            return todosEmprestimosAtrasados;

        } catch (Exception e) {
            System.out.println("Erro ao listar empréstimos ativos por membro: " + e.getMessage());
            return null;
        }
    }

    public static List<Emprestimo> listarEmprestimosAtivos() {
        try {
            List<Emprestimo> listaEmprestimosAtivos = EmprestimoUtils
                    .criarListaEmprestimosBancoDados(EmprestimoRepository.buscarTodosEmprestimos());
            if (!listaEmprestimosAtivos.isEmpty()) {
                listaEmprestimosAtivos.removeIf(emprestimo -> emprestimo.isDevolvido());
            }
            if (listaEmprestimosAtivos.isEmpty()) {
                return null;
            }

            return listaEmprestimosAtivos;
        } catch (Exception e) {
            System.out.println("Erro ao listar empréstimos: " + e.getMessage());
            return null;
        }
    }

    public static List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro) {
        try {
            List<Map<String, AttributeValue>> dadosEmprestimos =
                    EmprestimoRepository.buscarPorMembro(membro.getCpf());
            List<Emprestimo> todosEmprestimosAtivos =
                    EmprestimoUtils.criarListaEmprestimosBancoDados(dadosEmprestimos);

            if (!todosEmprestimosAtivos.isEmpty()) {
                todosEmprestimosAtivos.removeIf(emprestimo -> emprestimo.isDevolvido());
            }
            if (todosEmprestimosAtivos.isEmpty()) {
                return null;
            }
            return todosEmprestimosAtivos;
        } catch (Exception e) {
            System.out.println("Erro ao listar empréstimos ativos por membro: " + e.getMessage());
            return null;
        }
    }

}
