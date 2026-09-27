# Assistente de Navegação Segura para Idosos

## Visão Geral

Aplicativo Android que monitora links em segundo plano e exibe alertas visuais acessíveis quando detecta ameaças, antes que o usuário acesse o site. Desenvolvido para o público idoso, com foco em linguagem simples, botões grandes e integração com familiar de confiança.

A proteção é silenciosa para links seguros - o usuário só é interrompido quando há perigo real.

## Interface do Usuário

| Tecnologia | Função                                                     |
|---|------------------------------------------------------------|
| **Kotlin** | Linguagem principal - substitui Java em todo o projeto     |
| **Jetpack Compose** | Framework declarativo de UI do Android (sem XML de layout) |
| **Material Design 3** | Sistema de design - componentes, tipografia e cores        |
| **ComponentActivity** | Base das Activities com suporte nativo ao Compose          |

- A UI é descrita por funções Kotlin anotadas com `@Composable`
- O estado da tela é gerenciado com `remember { mutableStateOf(...) }` - quando o estado muda, o Compose redesenha apenas os componentes afetados
- As Activities ficam menores: só configuram o tema e chamam `setContent { }` com o composable raiz

### Estrutura de arquivos

```
ui/
├── MainScreen.kt              # Tela principal
├── AlertaScreen.kt            # Tela de alerta
├── FamiliarCadastroScreen.kt  # Cadastro do familiar
└── theme/
    ├── Theme.kt                 # Cores e MaterialTheme
    └── Type.kt                  # Tipografia (tamanhos em sp)
```