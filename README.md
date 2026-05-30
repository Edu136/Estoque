# 📦 Controle de Estoque — Engenharia de Software

Sistema back-end em Java para controle de estoque de pequenos comércios,
desenvolvido como trabalho prático da UC de Engenharia de Software.

---

## 👥 Problema Identificado

Pequenos comércios gerenciam estoque manualmente (cadernos, planilhas),
causando rupturas de produto, compras desnecessárias e falta de histórico.

**Solução:** API REST para registrar entradas e saídas, alertar automaticamente
sobre estoque crítico e calcular o valor total do estoque.

---

## 🧩 Padrões de Projeto Utilizados

| Padrão | Tipo | Aplicação |
|---|---|---|
| **Builder** | Criacional | `MovimentacaoBuilder` — constrói movimentações com validações |
| **Observer** | Comportamental | `EstoqueObserver` — notifica quando produto atinge estoque mínimo |
| **Strategy** | Comportamental | `CalculoEstoqueStrategy` — algoritmos intercambiáveis de cálculo de valor |
| **Repository** | Estrutural | Abstração da camada de dados via interfaces Spring Data |

---

## ✅ Princípios SOLID

- **SRP:** Cada classe tem uma única responsabilidade (service, controller, builder separados)
- **OCP:** Novos observers e strategies sem alterar código existente
- **LSP:** `LogNotificador` e `EmailNotificador` substituem `EstoqueObserver` sem quebrar comportamento
- **ISP:** Interfaces de repositório específicas por entidade
- **DIP:** Services dependem de interfaces, não de implementações concretas

---

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Executar a aplicação
```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`

Console H2 disponível em `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:estoquedb`
- Usuário: `sa` | Senha: (vazio)

### Executar os testes
```bash
mvn test
```

---

## 📡 Endpoints da API

### Produtos
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/produtos` | Cadastrar produto |
| GET | `/api/produtos` | Listar todos |
| GET | `/api/produtos/{id}` | Buscar por ID |
| GET | `/api/produtos/buscar?nome=X` | Buscar por nome |
| PUT | `/api/produtos/{id}` | Atualizar produto |
| DELETE | `/api/produtos/{id}` | Remover produto |

### Estoque
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/estoque/entrada` | Registrar entrada de mercadoria |
| POST | `/api/estoque/saida` | Registrar saída (venda/consumo) |
| GET | `/api/estoque/abaixo-minimo` | Listar produtos em estoque crítico |
| GET | `/api/estoque/movimentacoes/{produtoId}` | Histórico de movimentações |
| GET | `/api/estoque/valor-total` | Valor total do estoque |

---

## 🗂️ Estrutura do Projeto

```
src/
├── main/java/com/estoque/
│   ├── domain/
│   │   ├── model/          # Produto, Movimentacao, Categoria, Fornecedor
│   │   └── enums/          # TipoMovimentacao
│   ├── builder/            # MovimentacaoBuilder (padrão Builder)
│   ├── repository/         # Interfaces JPA (padrão Repository)
│   ├── service/
│   │   ├── observer/       # EstoqueObserver, LogNotificador, EmailNotificador
│   │   └── strategy/       # CalculoEstoqueStrategy e implementações
│   ├── controller/         # ProdutoController, EstoqueController
│   └── dto/                # EntradaDTO, SaidaDTO
└── test/java/com/estoque/
    └── service/            # Testes unitários com JUnit 5 + Mockito
```

---

## 🧪 Cobertura de Testes

- `ProdutoTest` — regras de domínio (estoque mínimo, atualização de quantidade)
- `MovimentacaoBuilderTest` — validações do Builder
- `EstoqueServiceTest` — fluxos de entrada/saída, Observer e Strategy

---

## 📄 Documentação

| Documento | Descrição |
|-----------|-----------|
| [docs/contexto-problema.md](docs/contexto-problema.md) | Descrição detalhada do problema, usuários envolvidos e escopo |
| [docs/requisitos.md](docs/requisitos.md) | Levantamento de requisitos, User Stories e backlog priorizado |
| [docs/arquitetura.md](docs/arquitetura.md) | Justificativa dos padrões de projeto e decisões arquiteturais |
| [diagrama-classes-estoque.mermaid](diagrama-classes-estoque.mermaid) | Diagrama de classes UML |
