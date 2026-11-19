# Autorizar reprodução de som

## Contexto

O endpoint inicial é `POST /v1/rooms/{id}/sounds/{clipId}/play`. A reprodução é distribuída como evento autorizado aos clientes presentes.

## Tarefa

Validar se o membro pode reproduzir um clip na sala e emitir um evento temporário para os participantes autorizados.

## Regras

- Verificar cargo, sala, visibilidade do clip e permissões atuais.
- Aplicar limites por utilizador e por sala; valores e janela dos limites precisam ser definidos.
- O evento deve incluir identificador e validade curta; clientes devem descartar eventos repetidos ou expirados.
- Permitir silenciar efeitos independentemente do áudio de voz no cliente.

## Definição de pronto

- O contrato documenta autorização, limites e formato do evento.
- Clips invisíveis ou sem autorização não são reproduzidos.
- Eventos repetidos ou expirados podem ser identificados e descartados.
- Testes cobrem permissões e limites.
