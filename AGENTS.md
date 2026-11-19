# Regras do projeto

## Objetivo do serviço

Este serviço é o backend da plataforma de comunicação em tempo real descrita na especificação de arquitetura. Deve fornecer APIs seguras para os módulos do produto, começando pelos fluxos de servidores, permissões e chat. A especificação de referência é `/Users/lvyni/Downloads/Arquitetura_Plataforma_Comunicacao.md`; leia-a antes de planejar ou implementar mudanças relacionadas ao produto.

## Stack e comandos

- Use Java 21 e Spring Boot, respeitando as versões já configuradas no projeto.
- Execute comandos Maven pelo wrapper do repositório (`./mvnw`), nunca por uma instalação global do Maven. Por exemplo: `./mvnw test` e `./mvnw verify`.
- Não adicione dependências sem pedir autorização primeiro.
- Não use Lombok.
- Represente valores monetários com `BigDecimal`; não use `float` ou `double` para dinheiro.

## Arquitetura

- Organize o backend como monólito modular, agrupado por funcionalidade.
- Use arquitetura hexagonal nos fluxos de negócio e nas integrações externas: adaptadores de entrada chamam casos de uso; a aplicação depende de portas; adaptadores de saída implementam essas portas.
- Mantenha as dependências apontadas para dentro. O domínio e a aplicação não podem depender de Spring, JPA, HTTP ou detalhes de infraestrutura.
- Mantenha domínio e aplicação sem anotações ou dependências de Spring e JPA. Regras de negócio e transições de estado pertencem ao domínio; coordenação de casos de uso e portas pertencem à aplicação.
- Preserve a propriedade dos dados por módulo e integre módulos através de casos de uso ou eventos publicados.

## Fluxo de trabalho obrigatório

Antes de cada mudança:

1. Leia a especificação indicada para a tarefa e as instruções aplicáveis ao diretório.
2. Inspecione o código relacionado e apresente um plano conciso, com os arquivos e comportamentos previstos.
3. Aguarde o OK explícito do usuário antes de editar.

Depois da aprovação:

4. Implemente somente o escopo aprovado e não faça alterações fora dele.
5. Execute os testes e verificações apropriados usando `./mvnw`.
6. Mostre um resumo do que mudou, os comandos executados e o diff final.

Não faça commit sem pedido explícito.
