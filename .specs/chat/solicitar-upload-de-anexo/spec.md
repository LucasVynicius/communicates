# Solicitar upload de anexo

## Contexto

O endpoint inicial é `POST /v1/attachments/uploads`. O arquivo é enviado para quarentena por URL curta previamente assinada.

## Tarefa

Autorizar um upload e criar uma referência ao anexo em quarentena, retornando os dados necessários para o envio direto ao armazenamento.

## Regras

- Autorizar o upload segundo as permissões atuais do recurso associado.
- Definir limites por formato, tamanho e servidor antes de disponibilizar o fluxo.
- Não publicar nem disponibilizar para download um anexo antes da validação e análise.
- URLs assinadas devem ter duração limitada; detalhes do armazenamento permanecem configuráveis.

## Definição de pronto

- O contrato documenta autorização, resposta, expiração e erros do upload.
- A referência inicia em estado de quarentena.
- Uploads inválidos ou não autorizados são recusados.
- Testes cobrem autorização e transições de estado do anexo.
