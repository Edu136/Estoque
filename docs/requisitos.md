# Levantamento e Análise de Requisitos

## Metodologia de Elicitação

O levantamento de requisitos foi realizado por meio de **entrevistas semiestruturadas** com o proprietário de um pequeno comércio (mercearia de bairro) e observação direta do processo de controle de estoque utilizado atualmente.

### Técnicas Utilizadas

| Técnica | Objetivo |
|---------|----------|
| Entrevista com stakeholder | Compreender dificuldades, necessidades e expectativas |
| Observação direta | Entender o fluxo real do processo de reposição e venda |
| Análise documental | Examinar planilhas e cadernos utilizados atualmente |

---

## Atores Envolvidos

| Ator | Descrição |
|------|-----------|
| **Proprietário** | Responsável por compras, cadastro de produtos e decisões de reposição |
| **Funcionário (estoquista)** | Registra entradas (recebimento de mercadorias) e saídas (vendas) |
| **Sistema** | Processa movimentações, calcula valores e emite alertas automáticos |

---

## Abordagem Ágil — User Stories e Backlog

### Backlog Priorizado

| Prioridade | User Story | Status |
|------------|-----------|--------|
| Alta | US01 — Cadastro de produtos | ✅ Implementada |
| Alta | US02 — Registro de entrada de mercadoria | ✅ Implementada |
| Alta | US03 — Registro de saída (venda) | ✅ Implementada |
| Alta | US04 — Alerta de estoque crítico | ✅ Implementada |
| Média | US05 — Consulta de histórico de movimentações | ✅ Implementada |
| Média | US06 — Cálculo do valor total do estoque | ✅ Implementada |
| Média | US07 — Busca de produtos por nome | ✅ Implementada |
| Média | US08 — Adição direta de quantidade ao estoque | ✅ Implementada |
| Baixa | US09 — Listagem de produtos abaixo do mínimo | ✅ Implementada |
| Baixa | US10 — Atualização de dados do produto | ✅ Implementada |
| Baixa | US11 — Remoção de produto do catálogo | ✅ Implementada |
| Baixa | US12 — Listagem de produtos recentes | ✅ Implementada |

---

### Detalhamento das User Stories

#### US01 — Cadastro de Produtos

**Como** proprietário do comércio,  
**quero** cadastrar produtos com nome, descrição, quantidade atual, estoque mínimo e preço unitário,  
**para que** eu tenha um registro digital de todos os itens disponíveis na loja.

**Critérios de Aceitação:**
- Produto deve ter nome obrigatório (não pode ser vazio)
- Quantidade atual deve ser >= 0
- Estoque mínimo deve ser >= 0
- Preço unitário deve ser > 0
- Produto pode ser associado a uma categoria (opcional)

**Endpoint:** `POST /api/produtos`

---

#### US02 — Registro de Entrada de Mercadoria

**Como** funcionário do estoque,  
**quero** registrar a entrada de mercadorias informando produto, quantidade e fornecedor,  
**para que** o sistema atualize automaticamente a quantidade disponível.

**Critérios de Aceitação:**
- Quantidade de entrada deve ser > 0
- Fornecedor é obrigatório para entradas
- O sistema deve atualizar a quantidade atual do produto
- A movimentação deve ser registrada com data/hora automática
- Deve ser possível incluir uma observação opcional

**Endpoint:** `POST /api/estoque/entrada`

---

#### US03 — Registro de Saída (Venda/Consumo)

**Como** funcionário do estoque,  
**quero** registrar a saída de produtos vendidos ou consumidos,  
**para que** o estoque reflita a quantidade real disponível.

**Critérios de Aceitação:**
- Quantidade de saída deve ser > 0
- Não é permitido registrar saída maior que o estoque disponível
- O sistema deve atualizar a quantidade atual do produto
- A movimentação deve ser registrada com data/hora automática

**Endpoint:** `POST /api/estoque/saida`

---

#### US04 — Alerta de Estoque Crítico

**Como** proprietário do comércio,  
**quero** ser notificado automaticamente quando um produto atingir o estoque mínimo,  
**para que** eu possa providenciar a reposição antes da ruptura.

**Critérios de Aceitação:**
- Após cada movimentação, o sistema verifica se o produto está abaixo do mínimo
- Se estiver, o sistema notifica via log e e-mail (simulado)
- A notificação deve conter: nome do produto, quantidade atual e estoque mínimo
- O sistema deve suportar múltiplos canais de notificação sem alterar a lógica principal

**Implementação:** Padrão Observer — `LogNotificador` e `EmailNotificador`

---

#### US05 — Consulta de Histórico de Movimentações

**Como** proprietário do comércio,  
**quero** consultar o histórico de entradas e saídas de um produto,  
**para que** eu possa analisar o fluxo de mercadorias ao longo do tempo.

**Critérios de Aceitação:**
- A consulta é feita por produto (ID)
- Retorna todas as movimentações ordenadas por data (mais recente primeiro)
- Cada movimentação mostra: tipo (entrada/saída), quantidade, data/hora, observação e fornecedor (se aplicável)

**Endpoint:** `GET /api/estoque/movimentacoes/{produtoId}`

---

#### US06 — Cálculo do Valor Total do Estoque

**Como** proprietário do comércio,  
**quero** saber o valor total do meu estoque em reais,  
**para que** eu tenha uma visão financeira do patrimônio em mercadorias.

**Critérios de Aceitação:**
- O cálculo padrão é: Σ (preço unitário × quantidade atual) de todos os produtos
- O sistema deve permitir trocar a estratégia de cálculo sem alterar o código existente
- O valor retornado deve ser em BigDecimal para precisão monetária

**Endpoint:** `GET /api/estoque/valor-total`  
**Implementação:** Padrão Strategy — `CalculoPrecoAtualStrategy`, `CalculoPrecoMedioStrategy`

---

#### US07 — Busca de Produtos por Nome

**Como** funcionário,  
**quero** buscar produtos pelo nome (parcial, sem diferenciar maiúsculas/minúsculas),  
**para que** eu encontre rapidamente o item desejado ao registrar movimentações.

**Critérios de Aceitação:**
- Busca por substring do nome (case-insensitive)
- Retorna lista de produtos correspondentes
- Retorna lista vazia se nenhum produto for encontrado

**Endpoint:** `GET /api/produtos/buscar?nome={termo}`

---

#### US08 — Adição Direta de Quantidade ao Estoque

**Como** funcionário do estoque,  
**quero** adicionar quantidade a um produto diretamente via operação parcial,  
**para que** eu possa fazer ajustes rápidos sem registrar uma movimentação formal de entrada.

**Critérios de Aceitação:**
- Quantidade a adicionar deve ser >= 1
- O produto deve existir
- Retorna o produto atualizado

**Endpoint:** `PATCH /api/produtos/{id}/adicionar-quantidade?quantidade={n}`

---

#### US09 — Listagem de Produtos Abaixo do Mínimo

**Como** proprietário do comércio,  
**quero** visualizar todos os produtos que estão com estoque abaixo do mínimo,  
**para que** eu possa planejar as compras de reposição.

**Critérios de Aceitação:**
- Retorna apenas produtos cuja quantidade atual <= estoque mínimo
- Inclui informações suficientes para decisão de compra (nome, quantidade atual, mínimo)

**Endpoint:** `GET /api/estoque/abaixo-minimo`

---

#### US10 — Atualização de Dados do Produto

**Como** proprietário do comércio,  
**quero** alterar informações de um produto (nome, descrição, preço, estoque mínimo),  
**para que** os dados estejam sempre corretos e atualizados.

**Critérios de Aceitação:**
- Todos os campos são substituídos pelos novos valores informados
- A quantidade atual não é alterada por esta operação (apenas via movimentações ou PATCH)
- Se o produto não existir, retorna 404

**Endpoint:** `PUT /api/produtos/{id}`

---

#### US11 — Remoção de Produto do Catálogo

**Como** proprietário do comércio,  
**quero** remover produtos descontinuados do sistema,  
**para que** o catálogo reflita apenas itens ativos.

**Critérios de Aceitação:**
- Se o produto não existir, retorna 404
- A remoção é permanente
- Retorna confirmação sem conteúdo (HTTP 204)

**Endpoint:** `DELETE /api/produtos/{id}`

---

#### US12 — Listagem de Produtos Recentes

**Como** funcionário,  
**quero** ver os produtos cadastrados mais recentemente,  
**para que** eu confirme rapidamente os últimos itens adicionados ao sistema.

**Critérios de Aceitação:**
- Retorna os N produtos mais recentes (ordenados por ID decrescente)
- Limite configurável via parâmetro (default: 10)

**Endpoint:** `GET /api/produtos/recentes?limite={n}`

---

## Requisitos Não-Funcionais

| ID | Requisito | Descrição |
|----|-----------|-----------|
| RNF01 | Linguagem | Desenvolvido em Java 17 |
| RNF02 | Framework | Spring Boot 3.2 |
| RNF03 | Banco de dados | H2 em memória (desenvolvimento) |
| RNF04 | Testes | Cobertura com JUnit 5 e Mockito |
| RNF05 | Padrões | Aplicação de padrões de projeto (Builder, Observer, Strategy, Repository) |
| RNF06 | Arquitetura | Camadas separadas (Controller → Service → Repository) |
| RNF07 | Validação | Bean Validation (Jakarta Validation) nos DTOs e entidades |
| RNF08 | Documentação | OpenAPI 3 via springdoc — UI em `/swagger-ui/index.html` |
| RNF09 | Tratamento de Erros | GlobalExceptionHandler com respostas JSON padronizadas |
| RNF10 | CORS | Configurado para aceitar requisições de qualquer origem em `/api/**` |
