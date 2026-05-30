# Contexto do Problema

## Descrição do Problema

Pequenos comércios de bairro — como mercearias, padarias e lojas de conveniência — frequentemente enfrentam dificuldades no controle de estoque. A gestão é realizada de forma manual, utilizando cadernos de anotação ou planilhas simples, o que gera uma série de problemas operacionais e financeiros.

---

## Cenário Atual (Processo Manual)

O proprietário de uma mercearia de bairro localizada em Belo Horizonte/MG gerencia aproximadamente 200 produtos diferentes. O processo atual funciona da seguinte forma:

### Fluxo de Entrada (Recebimento de Mercadoria)
1. O fornecedor entrega os produtos na loja
2. O funcionário confere manualmente os itens recebidos
3. A quantidade é anotada em um caderno ou planilha Excel
4. Não há registro do fornecedor vinculado à entrada
5. Não há data/hora precisa do recebimento

### Fluxo de Saída (Venda)
1. O produto é vendido ao cliente
2. **Não há registro da saída no controle de estoque** (apenas no caixa)
3. A atualização do estoque é feita "de cabeça" ou em contagens periódicas
4. Discrepâncias entre estoque real e registrado são comuns

### Fluxo de Reposição (Compras)
1. O proprietário percebe visualmente que um produto está acabando
2. Decide a quantidade de reposição baseado em intuição
3. Não há histórico para embasar a decisão
4. Frequentemente compra demais (capital parado) ou de menos (ruptura)

---

## Problemas Identificados

| # | Problema | Consequência |
|---|----------|-------------|
| 1 | **Falta de registro de saídas** | Estoque registrado diverge do real |
| 2 | **Sem alertas de estoque baixo** | Rupturas de produto (perda de vendas) |
| 3 | **Compras por intuição** | Excesso de estoque (capital parado) ou falta |
| 4 | **Sem histórico de movimentações** | Impossível identificar padrões de consumo |
| 5 | **Sem visão financeira do estoque** | Proprietário não sabe quanto tem investido em mercadorias |
| 6 | **Processo dependente de uma pessoa** | Se o responsável falta, ninguém sabe o que repor |
| 7 | **Contagens manuais frequentes** | Tempo desperdiçado em inventários |

---

## Usuários Envolvidos

### Proprietário (Stakeholder Principal)
- **Perfil:** Dono da mercearia, 45 anos, pouca familiaridade com tecnologia
- **Necessidades:** Saber o que precisa comprar, quanto vale o estoque, evitar rupturas
- **Dor principal:** Perder vendas por falta de produto ou empatar dinheiro em excesso de estoque

### Funcionário / Estoquista
- **Perfil:** Atendente que também cuida do recebimento de mercadorias
- **Necessidades:** Registrar entradas e saídas de forma rápida e simples
- **Dor principal:** Processo manual é demorado e sujeito a erros

---

## Solução Proposta

Desenvolver uma **API REST** que permita:

1. **Cadastrar e gerenciar produtos** com informações de estoque mínimo
2. **Registrar entradas e saídas** de forma digital, criando um histórico confiável
3. **Alertar automaticamente** quando um produto atinge nível crítico
4. **Calcular o valor total do estoque** para controle financeiro
5. **Consultar histórico** de movimentações para embasar decisões de compra

### Como a solução melhora o processo:

| Processo Atual | Processo com o Sistema |
|----------------|----------------------|
| Anotação manual em caderno | Registro digital via API |
| Sem controle de saídas | Saídas registradas com rastreabilidade |
| Percepção visual de falta | Alerta automático ao atingir mínimo |
| Sem histórico | Histórico completo com data/hora |
| Valor do estoque desconhecido | Cálculo automático em tempo real |
| Dependência de uma pessoa | Sistema acessível por qualquer funcionário |

---

## Escopo Delimitado

O objetivo **não** é desenvolver um sistema completo de gestão comercial. O escopo implementado foca na **camada de controle de estoque**, que é o ponto de maior dor identificado.

### Incluído no escopo:
- CRUD de produtos
- Registro de movimentações (entrada/saída)
- Alertas de estoque mínimo
- Cálculo de valor do estoque
- Histórico de movimentações

### Fora do escopo (possíveis evoluções futuras):
- Controle de caixa / vendas
- Integração com sistema de PDV
- Relatórios gerenciais avançados
- Interface gráfica (front-end)
- Autenticação e controle de acesso
- Integração com fornecedores (pedidos automáticos)
