# A3_estoque_diamante
# Sistema de Gerenciamento de Estoque "Diamante"

![Java](https://img.shields.io/badge/Java-8+-blue)
![Maven](https://img.shields.io/badge/Maven-3.6.3-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)

Este é um sistema simples de gerenciamento de estoque para o armazém "Diamante", desenvolvido em **Java** utilizando **POO** e **JDBC** para persistência de dados no MySQL.

O sistema opera via **Terminal** e é ideal para o gerenciamento rápido de entradas, saídas, consultas e alertas de estoque mínimo.

---

## Requisitos de Ambiente

Para rodar este projeto localmente, você precisa ter:

1.  **Java Development Kit (JDK):** Versão 8 ou superior.
2.  **MySQL Server:** Versão 8.0 ou superior, rodando na porta padrão (`3306`).
3.  **Maven:** Para gerenciamento de dependências.

---

## Configuração do Banco de Dados

O sistema exige que o *schema* e a tabela de produtos existam antes da execução.

### 1. Criar o Schema e a Tabela

Crie o *schema* (`estoque_diamante`) e execute o seguinte script SQL para criar a tabela `PRODUTOS`:

```sql
CREATE DATABASE IF NOT EXISTS estoque_diamante;

USE estoque_diamante;

CREATE TABLE PRODUTOS (
    codigo VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    caracteristicas VARCHAR(255),
    quantidadeEstoque INT NOT NULL,
    estoqueMinimo INT NOT NULL
);
