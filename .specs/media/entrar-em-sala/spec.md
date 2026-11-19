# Entrar em sala de media

## Contexto

O endpoint inicial é `POST /v1/rooms/{id}/join`. O browser conecta-se diretamente ao serviço de media; o backend autoriza e emite tokens curtos.

## Tarefa

Autorizar a entrada de um membro numa sala e emitir um token limitado à sala e às permissões de publicação concedidas.

## Regras

- Verificar associação, permissões e escopo do servidor antes de emitir o token.
- O token deve ter duração limitada e permissões explícitas.
- Microfone e câmara começam desligados por padrão no cliente.
- Provedor, duração do token e permissões exatas precisam ser definidos na implementação.

## Definição de pronto

- O contrato documenta as condições de entrada e permissões do token.
- Membros sem acesso não recebem token válido para a sala.
- Testes cobrem autorização e escopo do token emitido.
