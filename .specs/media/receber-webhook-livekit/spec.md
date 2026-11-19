# Receber webhook do LiveKit

## Contexto

O endpoint inicial é `POST /v1/webhooks/livekit`. Eventos técnicos de media podem alimentar presença e, no futuro, reconciliação de jornada.

## Tarefa

Validar, deduplicar e persistir webhooks recebidos do LiveKit para processamento pelo backend.

## Regras

- Validar a assinatura antes de aceitar o evento.
- Deduplicar eventos para evitar aplicar a mesma transição mais de uma vez.
- Persistir o evento antes de confirmar o recebimento, segundo uma política de retentativas a definir.
- Não confiar em dados do webhook para contornar a autorização de recursos.

## Definição de pronto

- Eventos com assinatura inválida são recusados.
- Reentregas não duplicam efeitos persistidos.
- O processamento e a resposta para eventos desconhecidos estão documentados.
- Testes cobrem assinatura e deduplicação.
