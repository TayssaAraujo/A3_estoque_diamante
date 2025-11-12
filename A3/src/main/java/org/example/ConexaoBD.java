import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    // 1. Variável de Conexão: Deve ser private static
    private static Connection conexao = null;

    // 2. Variáveis de Configuração: Devem ser private static final (constantes)
    private static final String URL = "jdbc:mysql://localhost:3306/estoque_diamante";
    private static final String USUARIO = "root";
    private static final String SENHA = "Assyat.123"; // <-- Substitua por sua senha real

    // Método para conectar
    public static Connection conectar() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
                // A linha abaixo é opcional em drivers modernos, mas pode ajudar a resolver o "No suitable driver"
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Agora o código consegue 'encontrar' URL, USUARIO, SENHA e conexao
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão com o BD estabelecida com sucesso.");
            } catch (SQLException e) {
                System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
                throw e;
            } catch (ClassNotFoundException e) {
                System.err.println("Driver JDBC não encontrado. Verifique o pom.xml.");
                throw new SQLException("Driver JDBC não encontrado.");
            }
        }
        return conexao;
    }

    // Método para fechar
    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão com o BD fechada.");
            } catch (SQLException e) {
                System.err.println(" Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}