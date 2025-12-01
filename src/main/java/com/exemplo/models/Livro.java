package com.exemplo.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.exemplo.repositories.LivrosRepository;
import com.exemplo.repositories.UsuarioRepository;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.DynamoUtils;
import com.exemplo.utils.LivroUtils;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class Livro {
    private String isbn;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private int numeroCopias;
    private int disponiveis;

    public Livro(String isbn, String titulo, String autor, int anoPublicacao, int numeroCopias,
            int disponiveis) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.numeroCopias = numeroCopias;
        this.disponiveis = disponiveis;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public int getNumeroCopias() {
        return numeroCopias;
    }

    public void setNumeroCopias(int numeroCopias) {
        this.numeroCopias = numeroCopias;
    }

    public int getDisponiveis() {
        return disponiveis;
    }

    public void setDisponiveis(int disponiveis) {
        this.disponiveis = disponiveis;
    }

    public void verLivro() {
        System.out.println("ISBN: " + this.isbn);
        System.out.println("Título: " + this.titulo);
        System.out.println("Autor: " + this.autor);
        System.out.println("Ano de Publicação: " + this.anoPublicacao);
        System.out.println("Número de Cópias: " + this.numeroCopias);
        System.out.println("Disponíveis: " + this.disponiveis);
    }

    public void adicionarCopias(int quantidade) {
        if (quantidade > 0) {
            this.numeroCopias += quantidade;
            this.disponiveis += quantidade;
        }
    }

    public void removerCopias(int quantidade) {
        if (quantidade > 0 && this.disponiveis >= quantidade) {
            this.disponiveis -= quantidade;
        }
    }

    public static Livro criarLivro(String isbn, String titulo, String autor, int anoPublicacao,
            int numeroCopias) {
        if (isbn == null || isbn.isEmpty()) {
            System.out.println("ISBN é obrigatório.");
            return null;
        }

        if (titulo == null || titulo.isEmpty()) {
            System.out.println("Título é obrigatório.");
            return null;
        }

        if (autor == null || autor.isEmpty()) {
            System.out.println("Autor é obrigatório.");
            return null;
        }
        if (numeroCopias < 0) {
            System.out.println("Número de cópias não pode ser negativo.");
            return null;
        }

        Livro livro = new Livro(isbn, titulo, autor, anoPublicacao, numeroCopias, numeroCopias);
        System.out.println("Livro criado com sucesso: " + titulo);

        return livro;
    }

    public static void editarLivro(Livro livro, String isbn, String titulo, String autor,
            int anoPublicacao, int numeroCopias, int disponiveis) {
        if (livro == null) {
            throw new Error("Livro Inexistente!");
        }
        if (numeroCopias < 1) {
            throw new Error("Erro: O número total de cópias deve ser no mínimo 1.");
        }

        if (disponiveis < 0) {
            throw new Error("Erro: Disponíveis não pode ser negativo.");
        }

        if (disponiveis > numeroCopias) {
            throw new Error("Erro: Disponíveis não pode ser maior que o número de cópias.");
        }

        if (anoPublicacao < 1400 || anoPublicacao > 2100) {
            throw new Error("Erro: Ano de publicação inválido!");
        }
        livro.setIsbn(isbn);
        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setAnoPublicacao(anoPublicacao);
        livro.setNumeroCopias(numeroCopias);
        livro.setDisponiveis(disponiveis);


    }

    public static void excluirLivro(Livro livro) {
        if (livro != null) {
            System.out.println("Livro removido: " + livro.getTitulo());
        }
        LivrosRepository.excluir(livro.getIsbn());

        System.out.println("Livro excluído com sucesso do banco de dados.");
        ConsoleUI.pause();
        return;

    }

    public static List<Livro> listarLivros() {
        List<Map<String, AttributeValue>> listaLivrosDynamo = LivrosRepository.buscarTodosLivros();

        List<Livro> listaLivros = new ArrayList<Livro>();

        for (Map<String, AttributeValue> item : listaLivrosDynamo) {
            String isbn = item.get("isbn").s();
            String titulo = item.get("titulo").s();
            String autor = item.get("autor").s();
            int anoPublicacao = Integer.parseInt(item.get("anoPublicacao").n());
            int numeroCopias = Integer.parseInt(item.get("numeroCopias").n());
            int diponiveis = Integer.parseInt(item.get("disponiveis").n());
            Livro novoLivro =
                    new Livro(isbn, titulo, autor, anoPublicacao, numeroCopias, diponiveis);

            listaLivros.add(novoLivro);

        }
        return listaLivros;
    }
}
