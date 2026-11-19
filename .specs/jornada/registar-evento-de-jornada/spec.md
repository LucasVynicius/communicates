# Registar evento de jornada

## Contexto

O endpoint inicial é `POST /v1/shifts/events`. O registo inclui ações do trabalhador, hora do servidor e chave idempotente.

## Tarefa

Registar uma ação de jornada, como entrada, pausa, retoma ou saída, para posterior apuramento e validação.

## Regras

- Usar a hora do servidor e uma chave idempotente.
- Preservar histórico auditável de ajustes e alterações após fecho.
- Considerar fuso, virada de dia, pausas, escala e regras vigentes no apuramento.
- Regras laborais, arredondamento e estados de jornada dependem da política de mercado e precisam ser definidos antes do cálculo.

## Definição de pronto

- O contrato documenta ações, validações, idempotência e erros.
- Repetições idempotentes não criam eventos duplicados.
- Eventos inválidos não alteram o estado da jornada.
- Testes cobrem transições válidas, inválidas e idempotência.
