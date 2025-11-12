import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GerenciadorDeEstoque {

    // --- 4.1. Cadastro e Consulta ---

    /**
     * Insere um novo produto no banco de dados.
     */
    public void cadastrarProduto(Produto produto) {
        String sql = "INSERT INTO PRODUTOS (codigo, nome, descricao, caracteristicas, quantidadeEstoque, estoqueMinimo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getDescricao());
            stmt.setString(4, produto.getCaracteristicas());
            stmt.setInt(5, produto.getQuantidadeEstoque());
            stmt.setInt(6, produto.getEstoqueMinimo());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Produto cadastrado: " + produto.getNome());
            }

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    /**
     * Retorna um objeto Produto com base no código.
     */
    public Produto buscarProdutoPorCodigo(String codigo) {
        String sql = "SELECT codigo, nome, descricao, caracteristicas, quantidadeEstoque, estoqueMinimo FROM PRODUTOS WHERE codigo = ?";
        Produto produto = null;

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Mapeia o ResultSet para o objeto Produto
                    produto = new Produto(
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            rs.getString("caracteristicas"),
                            rs.getInt("quantidadeEstoque")
                    );
                    // O estoqueMinimo já é fixo em 3 na classe, mas poderíamos setar:
                    // produto.setEstoqueMinimo(rs.getInt("estoqueMinimo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }
        return produto;
    }

    // --- 4.2. Controle de Estoque (Baixa) ---

    /**
     * Realiza a baixa de estoque e chama a notificação, se necessário.
     */
    public void darBaixaEmEstoque(String codigo, int quantidadeBaixa) {

        // 1. Busca o produto atual no BD
        Produto produto = buscarProdutoPorCodigo(codigo);

        if (produto == null) {
            System.out.println("ERRO: Produto com código " + codigo + " não encontrado.");
            return;
        }

        int estoqueAtual = produto.getQuantidadeEstoque();

        if (quantidadeBaixa > estoqueAtual) {
            System.out.println("ERRO: Baixa de " + quantidadeBaixa + " unidades é maior que o estoque atual de " + estoqueAtual + ".");
            return;
        }

        // 2. Subtrai a quantidade
        int novoEstoque = estoqueAtual - quantidadeBaixa;

        // 3. Atualiza o registro no banco de dados
        String sqlUpdate = "UPDATE PRODUTOS SET quantidadeEstoque = ? WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

            stmt.setInt(1, novoEstoque);
            stmt.setString(2, codigo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.printf("Baixa efetuada: %d unidades do produto %s. Novo estoque: %d.\n",
                        quantidadeBaixa, produto.getNome(), novoEstoque);

                // 4. Implementa a notificação
                produto.setQuantidadeEstoque(novoEstoque); // Atualiza o objeto para a verificação
                verificarEstoqueMinimo(produto);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao dar baixa no estoque: " + e.getMessage());
        }
    }
    /**
     * Adiciona a quantidade reposta ao estoque atual do produto.
     */
    public void reporEstoque(String codigo, int quantidadeReposta) {

        // 1. Busca o produto atual no BD
        Produto produto = buscarProdutoPorCodigo(codigo);

        if (produto == null) {
            System.out.println("ERRO: Produto com código " + codigo + " não encontrado.");
            return;
        }

        // 2. Calcula o novo estoque
        int novoEstoque = produto.getQuantidadeEstoque() + quantidadeReposta;

        // 3. Atualiza o registro no banco de dados
        String sqlUpdate = "UPDATE PRODUTOS SET quantidadeEstoque = ? WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

            stmt.setInt(1, novoEstoque);
            stmt.setString(2, codigo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.printf("⬆Reposição efetuada: Adicionadas %d unidades do produto %s. Novo estoque: %d.\n",
                        quantidadeReposta, produto.getNome(), novoEstoque);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao repor o estoque: " + e.getMessage());
        }
    }
    // --- 4.3. Notificação de Estoque Mínimo ---

    /**
     * Verifica se o estoque atingiu ou ficou abaixo do mínimo e imprime alerta.
     */
    public void verificarEstoqueMinimo(Produto produto) {
        // Se a nova quantidade for menor ou igual ao estoque mínimo (que é 3)
        if (produto.getQuantidadeEstoque() <= produto.getEstoqueMinimo()) {
            System.out.println("\n" +
                    "ALERTA DE ESTOQUE MÍNIMO: O produto " + produto.getNome() +
                    " (Cód: " + produto.getCodigo() + ") atingiu " +
                    produto.getQuantidadeEstoque() + " unidades. Reposição Urgente!" +
                    "\n");
        }
    }
}
