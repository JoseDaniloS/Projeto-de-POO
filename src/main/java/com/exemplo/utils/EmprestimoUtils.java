package com.exemplo.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.exemplo.models.Emprestimo;
import com.exemplo.models.Livro;
import com.exemplo.models.Membro;
import com.exemplo.models.Usuario;
import com.exemplo.repositories.EmprestimoRepository;
import com.exemplo.repositories.LivrosRepository;
import com.exemplo.repositories.UsuarioRepository;
import com.exemplo.ui.ConsoleUI;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class EmprestimoUtils {

        public static void realizarEmprestimo(Membro membro) {
                try {
                        String isbnLivro = InputUtils.readString(
                                        "Informe o ISBN do livro que deseja pegar emprestado:");
                        Livro livro = LivroUtils.criaLivroBancoDados(
                                        LivrosRepository.buscarPorIsbn(isbnLivro));

                        List<Emprestimo> emprestimosMembro =
                                        Emprestimo.listarEmprestimosAtivosPorMembro(membro);
                        if (emprestimosMembro != null) {
                                for (Emprestimo e : emprestimosMembro) {
                                        if (e.getLivro().getIsbn().equals(isbnLivro)) {
                                                System.out.println(
                                                                "Você ja realizou o emprestimo desse livro:"
                                                                                + livro.getTitulo());
                                                ConsoleUI.pause();
                                                return;
                                        }
                                }
                        }

                        Emprestimo novoEmprestimo = Emprestimo.criarEmprestimo(livro, membro);

                        DynamoUtils.enviarElementoBancoDeDados(
                                        EmprestimoUtils.toMap(novoEmprestimo),
                                        EmprestimoRepository.TABLE_NAME);
                } catch (Exception e) {
                        System.out.println("Erro ao realizar empréstimo: " + e.getMessage());
                        ConsoleUI.pause();
                }
        }

        public static void submenuEditarEmprestimo(Emprestimo emprestimo) {
                int opcao;


                ConsoleUI.header("EDITAR EMPRÉSTIMO");

                Date dataEmprestimo =
                                solicitarData("Data do empréstimo", emprestimo.getDataEmprestimo());

                Date dataPrevista = solicitarData("Data de devolução prevista",
                                emprestimo.getDataDevolucaoPrevista());

                Date dataReal = solicitarData("Data da devolução real",
                                emprestimo.getDataDevolucaoReal());

                // === STATUS (DEVOLVIDO?) ===
                System.out.println("Status atual: "
                                + (emprestimo.isDevolvido() ? "Devolvido" : "Em aberto"));
                String novoStatus = InputUtils
                                .readString("Marcar como devolvido? (s/n ou ENTER p/ manter): ");

                boolean devolvido = novoStatus.trim().isEmpty() ? emprestimo.isDevolvido()
                                : novoStatus.equalsIgnoreCase("s");


                Emprestimo.editarEmprestimo(emprestimo, dataEmprestimo, dataPrevista, dataReal,
                                devolvido);



                Map<String, AttributeValue> novoMapa = EmprestimoUtils.toMap(emprestimo);
                String id = EmprestimoRepository.buscarIdporIsbnECpf(
                                emprestimo.getLivro().getIsbn(), emprestimo.getMembro().getCpf());
                if (id == null) {
                        System.out.println(
                                        "ERRO: empréstimo encontrado, mas ID não localizado no banco!");
                        ConsoleUI.pause();
                        return;
                }
                novoMapa.put("id", DynamoUtils.criarAttributeValueDynamoDB(id));
                DynamoUtils.enviarElementoBancoDeDados(novoMapa, EmprestimoRepository.TABLE_NAME);

                ConsoleUI.pause();
        }

        public static void mostrarEmprestimos(List<Emprestimo> lista) {
                if (lista == null || lista.isEmpty()) {
                        System.out.println("Nenhum empréstimo encontrado!");
                } else {
                        lista.forEach(e -> {
                                e.verEmprestimo();
                                System.out.println("--------------------------");
                        });
                }
                ConsoleUI.pause();
        }

        public static List<Emprestimo> criarListaEmprestimosBancoDados(
                        List<Map<String, AttributeValue>> dadosEmprestimos) {
                if (dadosEmprestimos == null || dadosEmprestimos.isEmpty()) {
                        return new ArrayList<>();
                }

                List<Emprestimo> listaEmprestimos = new ArrayList<>();
                for (Map<String, AttributeValue> emprestimo : dadosEmprestimos) {
                        String cpf = emprestimo.get("cpf").s();
                        Date dataDevolucaoPrevista = DynamoUtils.parseDateFromDynamoDB(
                                        emprestimo.get("dataDevolucaoPrevista").s());
                        Date dataDevolucaoReal = DynamoUtils.parseDateFromDynamoDB(
                                        emprestimo.get("dataDevolucaoReal").s());
                        Date dataEmprestimo = DynamoUtils.parseDateFromDynamoDB(
                                        emprestimo.get("dataEmprestimo").s());
                        boolean devolvido = emprestimo.get("devolvido").bool();
                        String isbn = emprestimo.get("isbn").s();

                        // faz o cash do membro
                        Usuario usuario = UsuarioRepository.buscarPorCpf(cpf);
                        if (usuario instanceof Membro == false) {
                                continue;
                        }
                        Membro membro = (Membro) usuario;

                        Emprestimo emp = new Emprestimo(
                                        LivroUtils.criaLivroBancoDados(
                                                        LivrosRepository.buscarPorIsbn(isbn)),
                                        membro, dataEmprestimo, dataDevolucaoPrevista,
                                        dataDevolucaoReal, devolvido);

                        listaEmprestimos.add(emp);
                }
                return listaEmprestimos;
        }

        public static Map<String, AttributeValue> toMap(Emprestimo emprestimo) {
                Map<String, AttributeValue> item = new HashMap<>();
                item.put("id", DynamoUtils
                                .criarAttributeValueDynamoDB(UUID.randomUUID().toString()));
                item.put("isbn", DynamoUtils
                                .criarAttributeValueDynamoDB(emprestimo.getLivro().getIsbn()));
                item.put("cpf", DynamoUtils
                                .criarAttributeValueDynamoDB(emprestimo.getMembro().getCpf()));
                item.put("dataEmprestimo", DynamoUtils.criarAttributeValueDynamoDB(DynamoUtils
                                .formatarDataParaBancoDeDados(emprestimo.getDataEmprestimo())));
                item.put("dataDevolucaoPrevista", DynamoUtils.criarAttributeValueDynamoDB(
                                DynamoUtils.formatarDataParaBancoDeDados(
                                                emprestimo.getDataDevolucaoPrevista())));
                item.put("dataDevolucaoReal", DynamoUtils.criarAttributeValueDynamoDB(DynamoUtils
                                .formatarDataParaBancoDeDados(emprestimo.getDataDevolucaoReal())));
                item.put("devolvido",
                                DynamoUtils.criarAttributeValueDynamoDB(emprestimo.isDevolvido()));

                return item;
        }

        public static Emprestimo criaEmprestimoBancoDeDados(
                        Map<String, AttributeValue> dadosEmprestimo) {
                Usuario usuario = UsuarioRepository.buscarPorCpf(dadosEmprestimo.get("cpf").s());

                if (!UsuarioUtils.isBibliotecario(usuario)) {
                        return null;
                }
                Membro membro = (Membro) usuario;

                return new Emprestimo(
                                LivroUtils.criaLivroBancoDados(LivrosRepository
                                                .buscarPorIsbn(dadosEmprestimo.get("isbn").s())),
                                membro,
                                DynamoUtils.parseDateFromDynamoDB(
                                                dadosEmprestimo.get("dataEmprestimo").s()),
                                DynamoUtils.parseDateFromDynamoDB(
                                                dadosEmprestimo.get("dataDevolucaoPrevista").s()),
                                DynamoUtils.parseDateFromDynamoDB(
                                                dadosEmprestimo.get("dataDevolucaoReal").s()),
                                dadosEmprestimo.get("devolvido").bool());
        }

        private static Date solicitarData(String label, Date valorAtual) {
                System.out.println(label + ": " + valorAtual);

                String entrada = InputUtils.readString("Nova data (ENTER para manter)\n"
                                + "Formato: yyyy-MM-dd'T'HH:mm:ss'Z'\n"
                                + "Exemplo: 2025-01-15T14:30:00Z\n> ");

                if (entrada.trim().isEmpty()) {
                        return valorAtual; // mantém o valor anterior
                }

                return DynamoUtils.parseDateFromDynamoDB(entrada);
        }

}
