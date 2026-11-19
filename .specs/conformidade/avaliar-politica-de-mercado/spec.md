# Avaliar política de mercado

## Contexto

As políticas são versionadas por mercado e podem controlar idade, contacto, descoberta, live, retenção e disponibilidade de funcionalidades. A API aplica decisões em ações como registo, convite, consulta, upload e emissão de tokens.

## Tarefa

Determinar a política aplicável a uma ação de produto segundo o mercado, a funcionalidade e o contexto disponível, e devolver uma decisão rastreável.

## Regras

- Cada versão deve identificar mercado e vigência.
- A decisão deve referenciar a versão da política utilizada.
- Quando houver incerteza relevante ou conflito de contexto, restringir o acesso conforme a especificação.
- Requisitos legais e a resolução de conflitos por ação devem ser validados antes da disponibilização em cada mercado.

## Definição de pronto

- A decisão é reproduzível a partir da política versionada e do contexto registado.
- A API aplica a decisão nos pontos de entrada definidos.
- Testes cobrem políticas disponíveis, bloqueadas e contexto insuficiente.
