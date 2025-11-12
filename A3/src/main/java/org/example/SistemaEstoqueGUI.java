import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class SistemaEstoqueGUI extends JFrame {

    private GerenciadorDeEstoque gerente;
    private JPanel painelPrincipal;
    private CardLayout cardLayout;

    // Componentes do Menu Principal
    private JButton btnCadastrar, btnConsultar, btnBaixa, btnRepor, btnSair;

    // Componentes do Cadastro
    private JTextField txtCodigo, txtNome, txtDescricao, txtCaracteristicas, txtQuantidade;

    // Componentes da Consulta
    private JTextField txtCodigoConsulta;
    private JTextArea areaResultado;

    // Componentes da Baixa
    private JTextField txtCodigoBaixa, txtQuantidadeBaixa;

    // Componentes da Reposição
    private JTextField txtCodigoRepor, txtQuantidadeRepor;

    public SistemaEstoqueGUI() {
        gerente = new GerenciadorDeEstoque();

        // Tenta conectar ao banco de dados
        try {
            ConexaoBD.conectar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Falha na conexão com o BD. Verifique o MySQL e o ConexaoBD.",
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        configurarJanela();
        inicializarComponentes();

        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Sistema de Estoque Diamante");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 245, 245));
    }

    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        // Adiciona os painéis
        painelPrincipal.add(criarPainelMenu(), "MENU");
        painelPrincipal.add(criarPainelCadastro(), "CADASTRO");
        painelPrincipal.add(criarPainelConsulta(), "CONSULTA");
        painelPrincipal.add(criarPainelBaixa(), "BAIXA");
        painelPrincipal.add(criarPainelReposicao(), "REPOSICAO");

        add(painelPrincipal);
        cardLayout.show(painelPrincipal, "MENU");
    }

    // ==================== PAINEL MENU ====================
    private JPanel criarPainelMenu() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(245, 245, 245));

        // Título
        JLabel titulo = new JLabel("SISTEMA DE ESTOQUE DIAMANTE", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        titulo.setBorder(new EmptyBorder(40, 0, 40, 0));
        titulo.setForeground(new Color(33, 33, 33));
        painel.add(titulo, BorderLayout.NORTH);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new GridLayout(5, 1, 10, 15));
        painelBotoes.setBorder(new EmptyBorder(20, 150, 100, 150));
        painelBotoes.setBackground(new Color(245, 245, 245));

        btnCadastrar = criarBotaoMenu("Cadastrar Novo Produto", new Color(66, 66, 66));
        btnConsultar = criarBotaoMenu("Buscar Produto por Código", new Color(66, 66, 66));
        btnBaixa = criarBotaoMenu("Dar Baixa em Estoque", new Color(66, 66, 66));
        btnRepor = criarBotaoMenu("Repor Estoque", new Color(66, 66, 66));
        btnSair = criarBotaoMenu("Sair do Sistema", new Color(189, 189, 189));

        // Ações dos botões
        btnCadastrar.addActionListener(e -> cardLayout.show(painelPrincipal, "CADASTRO"));
        btnConsultar.addActionListener(e -> cardLayout.show(painelPrincipal, "CONSULTA"));
        btnBaixa.addActionListener(e -> cardLayout.show(painelPrincipal, "BAIXA"));
        btnRepor.addActionListener(e -> cardLayout.show(painelPrincipal, "REPOSICAO"));
        btnSair.addActionListener(e -> sairDoSistema());

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnConsultar);
        painelBotoes.add(btnBaixa);
        painelBotoes.add(btnRepor);
        painelBotoes.add(btnSair);

        painel.add(painelBotoes, BorderLayout.CENTER);

        return painel;
    }

    private JButton criarBotaoMenu(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
        });

        return botao;
    }

    // ==================== PAINEL CADASTRO ====================
    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(250, 250, 250));

        JLabel titulo = new JLabel("CADASTRO DE PRODUTO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        titulo.setBorder(new EmptyBorder(30, 0, 30, 0));
        titulo.setForeground(new Color(33, 33, 33));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel painelForm = new JPanel(new GridLayout(6, 2, 10, 15));
        painelForm.setBorder(new EmptyBorder(20, 80, 20, 80));
        painelForm.setBackground(new Color(250, 250, 250));

        txtCodigo = new JTextField();
        txtNome = new JTextField();
        txtDescricao = new JTextField();
        txtCaracteristicas = new JTextField();
        txtQuantidade = new JTextField();

        estilizarCampoTexto(txtCodigo);
        estilizarCampoTexto(txtNome);
        estilizarCampoTexto(txtDescricao);
        estilizarCampoTexto(txtCaracteristicas);
        estilizarCampoTexto(txtQuantidade);

        painelForm.add(criarLabel("Código (Ex: CHU005):"));
        painelForm.add(txtCodigo);
        painelForm.add(criarLabel("Nome:"));
        painelForm.add(txtNome);
        painelForm.add(criarLabel("Descrição:"));
        painelForm.add(txtDescricao);
        painelForm.add(criarLabel("Características:"));
        painelForm.add(txtCaracteristicas);
        painelForm.add(criarLabel("Quantidade Inicial:"));
        painelForm.add(txtQuantidade);

        painel.add(painelForm, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.setBackground(new Color(250, 250, 250));

        JButton btnSalvar = criarBotaoAcao("Cadastrar", new Color(66, 66, 66));
        JButton btnVoltar = criarBotaoAcao("Voltar", new Color(158, 158, 158));

        btnSalvar.addActionListener(e -> realizarCadastro());
        btnVoltar.addActionListener(e -> {
            limparCamposCadastro();
            cardLayout.show(painelPrincipal, "MENU");
        });

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnVoltar);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    // ==================== PAINEL CONSULTA ====================
    private JPanel criarPainelConsulta() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(250, 250, 250));

        JLabel titulo = new JLabel("CONSULTA DE PRODUTO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        titulo.setBorder(new EmptyBorder(30, 0, 30, 0));
        titulo.setForeground(new Color(33, 33, 33));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        painelBusca.setBackground(new Color(250, 250, 250));

        JLabel lblCodigo = new JLabel("Código do Produto:");
        lblCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCodigoConsulta = new JTextField(15);
        estilizarCampoTexto(txtCodigoConsulta);

        JButton btnBuscar = criarBotaoAcao("Buscar", new Color(66, 66, 66));
        btnBuscar.addActionListener(e -> realizarConsulta());

        painelBusca.add(lblCodigo);
        painelBusca.add(txtCodigoConsulta);
        painelBusca.add(btnBuscar);

        painel.add(painelBusca, BorderLayout.NORTH);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaResultado.setMargin(new Insets(10, 10, 10, 10));
        areaResultado.setBackground(Color.WHITE);
        areaResultado.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        JScrollPane scrollPane = new JScrollPane(areaResultado);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.setBorder(new EmptyBorder(0, 20, 20, 20));
        painelCentro.setBackground(new Color(250, 250, 250));
        painelCentro.add(scrollPane, BorderLayout.CENTER);
        painel.add(painelCentro, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.setBackground(new Color(250, 250, 250));
        JButton btnVoltar = criarBotaoAcao("Voltar", new Color(158, 158, 158));
        btnVoltar.addActionListener(e -> {
            txtCodigoConsulta.setText("");
            areaResultado.setText("");
            cardLayout.show(painelPrincipal, "MENU");
        });
        painelBotoes.add(btnVoltar);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    // ==================== PAINEL BAIXA ====================
    private JPanel criarPainelBaixa() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(250, 250, 250));

        JLabel titulo = new JLabel("BAIXA DE ESTOQUE", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        titulo.setBorder(new EmptyBorder(30, 0, 30, 0));
        titulo.setForeground(new Color(33, 33, 33));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel painelForm = new JPanel(new GridLayout(2, 2, 10, 20));
        painelForm.setBorder(new EmptyBorder(80, 150, 80, 150));
        painelForm.setBackground(new Color(250, 250, 250));

        txtCodigoBaixa = new JTextField();
        txtQuantidadeBaixa = new JTextField();

        estilizarCampoTexto(txtCodigoBaixa);
        estilizarCampoTexto(txtQuantidadeBaixa);

        painelForm.add(criarLabel("Código do Produto:"));
        painelForm.add(txtCodigoBaixa);
        painelForm.add(criarLabel("Quantidade Vendida:"));
        painelForm.add(txtQuantidadeBaixa);

        painel.add(painelForm, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.setBackground(new Color(250, 250, 250));

        JButton btnConfirmar = criarBotaoAcao("Confirmar Baixa", new Color(66, 66, 66));
        JButton btnVoltar = criarBotaoAcao("Voltar", new Color(158, 158, 158));

        btnConfirmar.addActionListener(e -> realizarBaixa());
        btnVoltar.addActionListener(e -> {
            limparCamposBaixa();
            cardLayout.show(painelPrincipal, "MENU");
        });

        painelBotoes.add(btnConfirmar);
        painelBotoes.add(btnVoltar);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    // ==================== PAINEL REPOSIÇÃO ====================
    private JPanel criarPainelReposicao() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(250, 250, 250));

        JLabel titulo = new JLabel("REPOSIÇÃO DE ESTOQUE", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        titulo.setBorder(new EmptyBorder(30, 0, 30, 0));
        titulo.setForeground(new Color(33, 33, 33));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel painelForm = new JPanel(new GridLayout(2, 2, 10, 20));
        painelForm.setBorder(new EmptyBorder(80, 150, 80, 150));
        painelForm.setBackground(new Color(250, 250, 250));

        txtCodigoRepor = new JTextField();
        txtQuantidadeRepor = new JTextField();

        estilizarCampoTexto(txtCodigoRepor);
        estilizarCampoTexto(txtQuantidadeRepor);

        painelForm.add(criarLabel("Código do Produto:"));
        painelForm.add(txtCodigoRepor);
        painelForm.add(criarLabel("Quantidade Recebida:"));
        painelForm.add(txtQuantidadeRepor);

        painel.add(painelForm, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.setBackground(new Color(250, 250, 250));

        JButton btnConfirmar = criarBotaoAcao("Confirmar Reposição", new Color(66, 66, 66));
        JButton btnVoltar = criarBotaoAcao("Voltar", new Color(158, 158, 158));

        btnConfirmar.addActionListener(e -> realizarReposicao());
        btnVoltar.addActionListener(e -> {
            limparCamposReposicao();
            cardLayout.show(painelPrincipal, "MENU");
        });

        painelBotoes.add(btnConfirmar);
        painelBotoes.add(btnVoltar);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(66, 66, 66));
        return label;
    }

    private JButton criarBotaoAcao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(180, 40));

        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
        });

        return botao;
    }

    private void estilizarCampoTexto(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setBackground(Color.WHITE);
    }

    // ==================== AÇÕES ====================
    private void realizarCadastro() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nome = txtNome.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String caracteristicas = txtCaracteristicas.getText().trim();
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());

            if (codigo.isEmpty() || nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código e Nome são obrigatórios!",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Produto produto = new Produto(codigo, nome, descricao, caracteristicas, quantidade);
            gerente.cadastrarProduto(produto);

            JOptionPane.showMessageDialog(this, "✅ Produto cadastrado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCamposCadastro();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❗ Quantidade deve ser um número inteiro!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarConsulta() {
        String codigo = txtCodigoConsulta.getText().trim();

        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um código para buscar!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Produto produto = gerente.buscarProdutoPorCodigo(codigo);

        if (produto != null) {
            areaResultado.setText(
                    "✅ PRODUTO ENCONTRADO\n\n" +
                            "Código: " + produto.getCodigo() + "\n" +
                            "Nome: " + produto.getNome() + "\n" +
                            "Descrição: " + produto.getDescricao() + "\n" +
                            "Características: " + produto.getCaracteristicas() + "\n" +
                            "Estoque Atual: " + produto.getQuantidadeEstoque() + " unidades\n" +
                            "Estoque Mínimo: " + produto.getEstoqueMinimo() + " unidades"
            );
        } else {
            areaResultado.setText("❗ Produto com código '" + codigo + "' não encontrado.");
        }
    }

    private void realizarBaixa() {
        try {
            String codigo = txtCodigoBaixa.getText().trim();
            int quantidade = Integer.parseInt(txtQuantidadeBaixa.getText().trim());

            if (codigo.isEmpty() || quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Código e quantidade válidos são obrigatórios!",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            gerente.darBaixaEmEstoque(codigo, quantidade);
            limparCamposBaixa();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❗ Quantidade deve ser um número inteiro!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarReposicao() {
        try {
            String codigo = txtCodigoRepor.getText().trim();
            int quantidade = Integer.parseInt(txtQuantidadeRepor.getText().trim());

            if (codigo.isEmpty() || quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Código e quantidade válidos são obrigatórios!",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            gerente.reporEstoque(codigo, quantidade);
            limparCamposReposicao();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❗ Quantidade deve ser um número inteiro!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCamposCadastro() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtDescricao.setText("");
        txtCaracteristicas.setText("");
        txtQuantidade.setText("");
    }

    private void limparCamposBaixa() {
        txtCodigoBaixa.setText("");
        txtQuantidadeBaixa.setText("");
    }

    private void limparCamposReposicao() {
        txtCodigoRepor.setText("");
        txtQuantidadeRepor.setText("");
    }

    private void sairDoSistema() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente sair do sistema?",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            ConexaoBD.fecharConexao();
            System.exit(0);
        }
    }

    // ==================== MAIN ====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaEstoqueGUI());
    }
}