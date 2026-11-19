# Criar servidor

## Contexto

O endpoint inicial `POST /api/v1/workspaces` cria um servidor e a estrutura mínima para começar a usá-lo. O usuário autenticado passa a ser proprietário. O piloto suporta os modelos Dev, Criador, Comunidade e Empresa.

## Tarefa

Receber o nome e o modelo do servidor, criar o servidor, atribuir o cargo `Admin` ao proprietário e inicializar os cargos `Admin` e `Member` e os canais `general` (texto) e `voice` (voz). Devolver o servidor criado.

## Regras

- O pedido contém `name` não vazio, com no máximo 100 caracteres, e `template` com um dos valores `DEV`, `CREATOR`, `COMMUNITY` ou `COMPANY`.
- Usar o principal autenticado como proprietário; não aceitar o identificador do proprietário fornecido pelo cliente.
- Criar servidor, cargos, canais e associação do proprietário numa única transação.
- Identificadores são UUID.
- Persistir os recursos com escopo explícito do servidor.
- Não criar autenticação OIDC nesta mudança. A rota utiliza a autenticação fornecida pelo Spring Security existente.
- Não há comportamento diferente de canais/cargos entre modelos nesta primeira versão; o modelo escolhido fica registado para evolução posterior.
- Rejeitar pedidos inválidos com `400 Bad Request` e pedidos sem identidade autenticada com `401 Unauthorized`.

## Definição de pronto

- `POST /api/v1/workspaces` retorna `201 Created` com identificador, nome, modelo, cargos e canais criados.
- Um workspace, dois cargos, dois canais e uma associação de proprietário são persistidos atomicamente.
- Todos os quatro modelos são aceitos; nome inválido ou modelo desconhecido não cria dados.
- O proprietário recebe `Admin`; um principal não autenticado não cria servidor.
- Testes cobrem regras de domínio, criação pelo caso de uso e contrato HTTP.
