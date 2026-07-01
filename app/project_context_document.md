# Documento de Contexto de Projeto: CogniLink

## 1. VISÃO GERAL DO SISTEMA

O **CogniLink** é uma plataforma de aprendizado móvel projetada para otimizar a retenção de conhecimento através de técnicas de memorização ativa. O sistema centraliza a criação e o estudo de *flashcards*, potencializado por Inteligência Artificial (IA) para automação de conteúdo e geolocalização para contextualização do estudo.

- **Principais Funcionalidades:**
    - **Geração de Flashcards com IA:** Criação automática de cartões a partir de temas, tópicos ou upload de documentos.
    - **Algoritmo de Repetição Espaçada (SRS):** Gestão de revisões baseada no desempenho do usuário para maximizar a memorização de longo prazo.
    - **Sugestões Baseadas em Localização:** Uso de *geofencing* para notificar o usuário sobre decks de estudo específicos ao entrar em locais pré-definidos (ex: biblioteca, faculdade).
    - **Gamificação e Estatísticas:** Monitoramento de progresso, ranking de usuários e métricas de domínio.

- **Atores Envolvidos:**
    - **Usuário (Estudante):** Cria, organiza e estuda flashcards, interage com a IA para geração de conteúdo e configura contextos geográficos de estudo.

---

## 2. ARQUITETURA DE SOFTWARE

O projeto adota os princípios da **Clean Architecture** combinados com o padrão de interface **MVVM (Model-View-ViewModel)**. A separação em camadas garante testabilidade, manutenibilidade e independência de frameworks.

### Camadas do Sistema:
1.  **Camada de UI (Apresentação):** Implementada com Jetpack Compose. Utiliza `ViewModels` para gerenciar o estado da UI e interagir com a camada de domínio.
    - *Exemplos:* `StudySessionViewModel`, `HomeViewModel`, `StudySessionScreen`.
2.  **Camada de Domínio (Negócio):** Contém as regras de negócio puras, modelos de domínio e `Use Cases` (Interactors). É a camada central e independente de tecnologias externas.
    - *Exemplos:* `CalculateSM2UseCase`, `ValidateBasicAnswerUseCase`, `FlashcardType` (Enum).
3.  **Camada de Dados:** Responsável pela persistência e comunicação externa. Implementa as interfaces de repositório definidas no domínio.
    - *Exemplos:* `FlashcardRepositoryImpl`, `KtorAIService`, `CogniLinkDatabase` (Room).

### Navegação e Gerenciamento de Estado:
- **Navegação:** Utiliza a biblioteca `navigation-compose`, com rotas definidas de forma declarativa.
- **Estado da UI:** O gerenciamento é feito através de `StateFlow` e `SharedFlow` do Kotlin Coroutines. Cada tela possui um `UiState` (data class) que representa o estado atômico da visualização em qualquer momento.

---

## 3. PADRÕES DE PROJETO (DESIGN PATTERNS)

O projeto aplica diversos padrões consolidados para resolver problemas recorrentes de desenvolvimento:

- **Repository Pattern:** Centraliza o acesso aos dados (local e remoto) e abstrai a origem para as camadas superiores. Implementado em classes como `FlashcardRepository`.
- **Dependency Injection (DI):** Utiliza o framework **Koin** para desacoplar a criação de objetos e gerenciar o ciclo de vida das dependências. Localizado em `KoinModules.kt`.
- **Singleton:** Garantido pelo Koin para instâncias únicas de bases de dados e serviços de rede (ex: `HttpClient`, `CogniLinkDatabase`).
- **Observer Pattern:** Implementado nativamente através de `Flow` e `StateFlow`, permitindo que a UI reaja automaticamente a mudanças nos dados.
- **Factory:** Utilizado pelo Koin (`factoryOf`) para criar novas instâncias de Use Cases sempre que necessário.
- **Mapper:** Conversão de entidades de banco de dados (`Entities`) para modelos de domínio (`Models`) e vice-versa, mantendo a integridade das camadas. Localizado em `Mappers.kt`.

---

## 4. STACK TECNOLÓGICA E FERRAMENTAS

- **Linguagem:** Kotlin (foco em segurança de tipos e concorrência com Coroutines).
- **Interface Gráfica:** Jetpack Compose (UI declarativa moderna).
- **Injeção de Dependência:** Koin (leve e idiomático para Kotlin).
- **Persistência Local:** Room Database (abstração sobre SQLite).
- **Comunicação de Rede:** Ktor Client (consumo de APIs REST de forma assíncrona).
- **Backend/Cloud:** Firebase Auth (autenticação) e Firestore (armazenamento secundário/sincronização).
- **Serviços do Google:** Play Services Location (Geofencing API).
- **Serialização:** kotlinx.serialization (processamento de JSON).

---

## 5. ALGORITMOS E LÓGICAS PRINCIPAIS

### Geração de Flashcards com IA
A integração ocorre via `KtorAIService`, que comunica com um backend especializado. O fluxo permite:
1.  Envio de temas ou documentos (*Multipart/form-data*).
2.  Recebimento de estruturas JSON contendo perguntas, respostas corretas, alternativas e dicas.
3.  O sistema processa diferentes tipos de cartões: `BASIC` (pergunta/resposta), `MULTIPLE_CHOICE` e `TRUE_OR_FALSE`.

### Repetição Espaçada (SM-2 Algorithm)
A lógica principal reside no `CalculateSM2UseCase`. O algoritmo ajusta o intervalo de revisão com base na "qualidade" (0-5) da resposta do usuário:
- **Cálculo de Intervalo:** Se o usuário acerta, o intervalo cresce exponencialmente baseado no `Ease Factor` (EF).
- **Ease Factor:** Um multiplicador que define a dificuldade do cartão. Se o usuário tem dificuldade, o EF diminui, tornando as revisões mais frequentes.
- **Persistência:** A data da próxima revisão é calculada e armazenada em `FlashcardStats`.

### Geolocalização e Geofencing
Implementado via `AndroidGeofenceManager`:
- O usuário define uma região circular (latitude, longitude, raio) associada a um `StudyContext`.
- O sistema utiliza a API de `Geofencing` do Google para monitorar a transição `DWELL` (quando o usuário permanece no local por um tempo determinado).
- Ao disparar o gatilho, um `GeofenceBroadcastReceiver` processa o evento em segundo plano e envia uma notificação push sugerindo os decks vinculados àquele local.

---

## 6. ESTRUTURA DE DIRETÓRIOS E COMPONENTES

```text
com.lucasdpm.cognilink/
├── data/
│   ├── database/    # Configuração Room, DAOs e Entidades
│   ├── mappers/     # Conversores entre camadas
│   ├── model/       # Modelos de dados de persistência/API
│   ├── repository/  # Implementações dos repositórios
│   └── service/     # Serviços externos (IA, Localização, Notificação)
├── di/              # Módulos de Injeção de Dependência (Koin)
├── domain/
│   ├── model/       # Modelos de negócio puros
│   ├── repository/  # Interfaces de repositórios (Contratos)
│   ├── service/     # Interfaces de serviços
│   └── usecase/     # Regras de negócio (Use Cases)
├── ui/
│   ├── components/  # Componentes reutilizáveis do Compose
│   ├── screens/     # Telas principais do aplicativo
│   ├── states/      # Definições de UiState
│   ├── theme/       # Temas, cores e tipografia
│   └── viewmodels/  # Lógica de apresentação e gestão de estado
└── CogniLinkApplication.kt # Inicialização do app e Koin
```

- **Módulo `app` (Raiz):** Contém todo o código fonte seguindo a estrutura acima, organizado para suportar uma aplicação modular no futuro.
- **Mocking:** Atualmente, a integração com o backend de IA em `KtorAIService` depende de um endpoint configurado em `BuildConfig.BASE_BACKEND_URL`. Em ambientes de desenvolvimento, dados de exemplo são providos por `PreviewDataProvider`.
