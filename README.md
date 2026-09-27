# Assistente de Navegação Segura para Idosos

## Visão Geral

Aplicativo Android que monitora links em segundo plano e exibe alertas visuais acessíveis quando detecta ameaças, antes que o usuário acesse o site. Desenvolvido para o público idoso, com foco em linguagem simples, botões grandes e integração com familiar de confiança.

A proteção é silenciosa para links seguros - o usuário só é interrompido quando há perigo real.

## Como Executar

```bash
# Clonar e abrir no Android Studio
git clone <url-do-repositorio>
```

### Visualizar as telas com Compose Preview
1. Abra o projeto na IDE Android Studio
2. Aguarde a sincronização dos arquivos do Gradle
3. Navegue até os arquivos das telas (especificado na seção de estrutura de arquivos desse README)
4. Abra o arquivo com final "Screen"
5. No canto superior direito do painel de código do Android Studio, mude o modo de exibição de Editor para Editor e Preview ou Preview

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