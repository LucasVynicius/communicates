# Abrir denúncia

## Contexto

O endpoint inicial é `POST /v1/reports`. O piloto guarda o caso numa fila de revisão humana com prioridade inicial baseada em regras explícitas.

## Tarefa

Registar uma denúncia com contexto mínimo autorizado e criar o caso para revisão humana.

## Regras

- Verificar acesso ao contexto denunciado e recolher somente dados necessários.
- Persistir o caso e o evento outbox na mesma transação.
- Casos urgentes seguem o procedimento de resposta imediata; os limiares e o procedimento precisam ser definidos.
- A triagem automática Python é futura; as decisões de moderação permanecem com a equipa responsável.
- Restringir o acesso às evidências e auditar consultas relevantes.

## Definição de pronto

- O contrato documenta contexto permitido, resposta e erros.
- Uma denúncia válida fica disponível para revisão humana mesmo se um worker falhar.
- O acesso ao caso e às evidências é restrito e auditável.
- Testes cobrem autorização e persistência do caso/outbox.
