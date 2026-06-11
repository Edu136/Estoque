# Decisões Arquiteturais e Padrões de Projeto

## Arquitetura Geral

O sistema segue uma **arquitetura em camadas** (Layered Architecture), separando responsabilidades em:

```
Controller (HTTP) → Service (Lógica de Negócio) → Repository (Acesso a Dados)
```

Essa separação foi escolhida por:
- Facilitar manutenção e evolução independente de cada camada
- Permitir testes unitários isolados (mocking da camada inferior)
- Seguir convenções do ecossistema Spring Boot

---

## Padrões de Projeto Aplicados

### 1. Builder (Criacional)

**Classe:** `MovimentacaoBuilder`

** Problema resolvido:**  
A criação de um objeto `Movimentacao` envolve múltiplos campos, validações cruzadas (ex: entrada exige fornecedor, quantidade > 0) e campos automáticos (dataHora). Usar construtores com muitos parâmetros ou setters públicos tornaria o código confuso e propenso a erros.

**Por que Builder:**
- Permite construção fluente e legível
- Encapsula regras de validação no momento da construção
- Garante que objetos inválidos nunca sejam criados
- Separa a lógica de construção da entidade em si

**Alternativas consideradas:**
- Factory Method: descartado pois não resolveria o problema de múltiplas combinações de parâmetros
- Construtor telescópico: descartado pela baixa legibilidade

---

### 2. Observer (Comportamental)

**Interface:** `EstoqueObserver`  
**Implementações:** `LogNotificador`, `EmailNotificador`

**Problema resolvido:**  
Quando o estoque de um produto atinge nível crítico, o sistema precisa notificar diferentes canais (log, e-mail, futuramente SMS, push notification). Sem o Observer, a classe `EstoqueService` precisaria conhecer todos os mecanismos de notificação, violando SRP e OCP.

**Por que Observer:**
- Desacopla o emissor (EstoqueService) dos receptores (notificadores)
- Permite adicionar novos canais de notificação sem alterar o serviço
- Cada notificador tem responsabilidade única (SRP)
- Extensível para futuros canais (WhatsApp, Telegram, etc.)

**Alternativas consideradas:**
- Event-driven com Spring Events: viável, mas o Observer explícito demonstra melhor o padrão para fins acadêmicos
- Chamada direta aos notificadores: descartado por violar OCP e SRP

---

### 3. Strategy (Comportamental)

**Interface:** `CalculoEstoqueStrategy`  
**Implementações:** `CalculoPrecoAtualStrategy`, `CalculoPrecoMedioStrategy`

**Problema resolvido:**  
O cálculo do valor total do estoque pode ser feito de diferentes formas dependendo do contexto contábil (preço atual, preço médio ponderado, custo de reposição). O algoritmo precisa ser intercambiável sem alterar a lógica do serviço.

**Por que Strategy:**
- Encapsula cada algoritmo de cálculo em sua própria classe
- Permite trocar a estratégia via configuração (Spring `@Primary`)
- Facilita adição de novas fórmulas sem modificar código existente
- Torna os algoritmos testáveis isoladamente

**Alternativas consideradas:**
- If/else ou switch no serviço: descartado por violar OCP
- Template Method: descartado pois não há estrutura algoritmica comum entre as estratégias

---

### 4. Repository (Estrutural)

**Interfaces:** `ProdutoRepository`, `MovimentacaoRepository`, `FornecedorRepository`

**Problema resolvido:**  
A camada de serviço precisa acessar dados persistidos sem se acoplar a uma tecnologia específica de banco de dados. O padrão Repository abstrai o mecanismo de persistência por trás de uma interface de domínio.

**Por que Repository:**
- Abstrai completamente a tecnologia de persistência (JPA, JDBC, NoSQL)
- Permite trocar o banco de dados sem alterar a lógica de negócio
- Facilita testes com mocks (DIP)
- Métodos com nomes expressivos do domínio (ex: `findAbaixoDoEstoqueMinimo`)

**Alternativas consideradas:**
- DAO (Data Access Object): similar, mas Repository é mais alinhado com DDD
- Acesso direto ao EntityManager: descartado por acoplar serviços ao JPA

---

## Princípios SOLID na Prática

### Single Responsibility Principle (SRP)

| Classe | Responsabilidade Única |
|--------|----------------------|
| `ProdutoController` | Mapear requisições HTTP para operações de produto |
| `EstoqueService` | Coordenar fluxo de movimentações de estoque |
| `MovimentacaoBuilder` | Construir e validar objetos Movimentacao |
| `LogNotificador` | Registrar alerta em log |
| `EmailNotificador` | Enviar alerta por e-mail |
| `CalculoPrecoAtualStrategy` | Calcular valor por preço atual × quantidade |

### Open/Closed Principle (OCP)

- **Observer:** Novos notificadores podem ser adicionados implementando `EstoqueObserver` — nenhuma alteração no `EstoqueService`
- **Strategy:** Novas fórmulas de cálculo implementam `CalculoEstoqueStrategy` — o serviço não muda

### Liskov Substitution Principle (LSP)

- `LogNotificador` e `EmailNotificador` são substituíveis onde `EstoqueObserver` é esperado — ambos notificam sem efeitos colaterais inesperados
- `CalculoPrecoAtualStrategy` e `CalculoPrecoMedioStrategy` retornam `BigDecimal` válido em todos os cenários

### Interface Segregation Principle (ISP)

- `EstoqueObserver` define apenas `onEstoqueBaixo(Produto)` — clientes não são forçados a implementar métodos desnecessários
- `CalculoEstoqueStrategy` define apenas `calcularValorEstoque(List<Produto>)` — interface mínima e coesa
- Cada Repository expõe apenas métodos relevantes para sua entidade

### Dependency Inversion Principle (DIP)

- `EstoqueService` depende de interfaces (`ProdutoRepository`, `CalculoEstoqueStrategy`, `EstoqueObserver`), não de implementações concretas
- `ProdutoService` depende de `ProdutoRepository` (interface), não do Spring Data JPA diretamente
- Inversão realizada via injeção de dependência no construtor (sem `@Autowired` em campo)

---

## Decisões Tecnológicas

| Decisão | Justificativa |
|---------|---------------|
| Spring Boot 3.2 | Framework maduro, produtivo, com amplo suporte a testes |
| Java 17 | LTS com recursos modernos (records, text blocks, pattern matching) |
| H2 Database | Banco em memória para desenvolvimento/testes sem infraestrutura externa |
| JUnit 5 + Mockito | Stack padrão para testes unitários com mocking |
| Maven | Gerenciador de build consolidado no ecossistema Java |
| Bean Validation | Validação declarativa integrada ao Spring MVC |

---

## Estrutura de Testes

A estratégia de testes foca em **testes unitários** com isolamento completo via mocks:

| Classe de Teste | O que valida |
|----------------|-------------|
| `ProdutoTest` | Regras de domínio da entidade (estoque mínimo, atualização de quantidade) |
| `MovimentacaoBuilderTest` | Validações do Builder (campos obrigatórios, regras de negócio) |
| `EstoqueServiceTest` | Fluxos de entrada/saída, disparo de Observer, uso de Strategy |

**Princípio:** Cada teste valida um único comportamento (Arrange-Act-Assert), garantindo rastreabilidade entre requisito e teste.
