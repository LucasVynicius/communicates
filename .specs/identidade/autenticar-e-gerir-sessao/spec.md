# Autenticar e gerir sessão

## Contexto

A plataforma usa um provedor OIDC para autenticação e sessões web em cookies. O catálogo inicial inclui entrada, recuperação de conta e MFA; SSO empresarial fica como extensão.

## Tarefa

Definir e implementar os fluxos de autenticação e gestão de sessão necessários para acesso à API, incluindo login, encerramento de sessão, MFA e recuperação de conta.

## Regras

- Usar o provedor OIDC configurado para login.
- Cookies de sessão devem usar `HttpOnly`, `Secure` e `SameSite`.
- Aplicar proteção CSRF às operações que alteram dados.
- Não incluir SSO empresarial no escopo inicial.
- Definir contrato, expiração e revogação da sessão antes da implementação.

## Definição de pronto

- Os fluxos de login, logout, MFA e recuperação têm contratos e estados de erro documentados.
- Operações autenticadas rejeitam sessões inválidas ou expiradas.
- Cookies e proteção CSRF seguem as regras acima.
- Testes cobrem os fluxos principais e recusas de autenticação.
