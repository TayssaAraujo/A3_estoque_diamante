public class Produto {
    private String codigo;
    private String nome;
    private String descricao;
    private String caracteristicas;
    private Integer quantidadeEstoque;
    // Atributo final conforme solicitado
    private final Integer estoqueMinimo = 3;

    // Construtor completo
    public Produto(String codigo, String nome, String descricao, String caracteristicas, Integer quantidadeEstoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.caracteristicas = caracteristicas;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // Construtor vazio (útil para consultas do BD)
    public Produto() {
    }

    // Getters e Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(String caracteristicas) { this.caracteristicas = caracteristicas; }

    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public Integer getEstoqueMinimo() { return estoqueMinimo; }

    @Override
    public String toString() {
        return "Produto [Cód: " + codigo + ", Nome: " + nome +
                ", Estoque: " + quantidadeEstoque + " ud]";
    }
}