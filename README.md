# Reengenharia: Calculadora de Atendimento

Reengenharia da classe legada `x_Mgr_F` do sistema financeiro da clínica, aplicando Clean Code,
Princípio da Responsabilidade Única (SRP) e os padrões Strategy e Factory.

## Requisitos

- Java 21 ou superior (JDK)
- Maven 3.8 ou superior (o IntelliJ e o VS Code já trazem o Maven embutido)
- Ou apenas Docker, para compilar e rodar sem instalar Java nem Maven (ver seção Docker)

A única dependência externa é o JUnit 5, usado apenas nos testes. O Maven baixa sozinho na primeira execução.
O JaCoCo gera o relatório de cobertura em `target/site/jacoco/` ao rodar `mvn verify`.

## Estrutura

```
src/main/java/br/com/clinica/financeiro/
├── Main.java                              Compara o código novo com o legado em 6 cenários
├── atendimento/
│   ├── CalculadoraDeAtendimento.java      Coordena o cálculo
│   └── SomadorDeProcedimentos.java        Soma os procedimentos realizados
├── cobranca/
│   ├── TipoConsulta.java                  Convênio, Particular ou Outro
│   ├── RegraDeCobranca.java               Interface (Strategy)
│   ├── RegraDeCobrancaConvenio.java       12% de desconto + taxa opcional de R$ 15,50
│   ├── RegraDeCobrancaParticular.java     5% de desconto acima de R$ 500
│   ├── RegraDeCobrancaOutro.java          Nenhuma regra aplicada
│   └── FabricaDeRegrasDeCobranca.java     Escolhe a regra (Factory)
└── legado/
    └── x_Mgr_F.java                       Código original, sem alterações

src/test/java/br/com/clinica/financeiro/
├── CalculadoraDeAtendimentoTest.java      Prova que o código novo é idêntico ao legado
└── cobranca/RegrasDeCobrancaTest.java     Testa cada regra isoladamente

docs/Relatorio_Reengenharia.pdf            Relatório da atividade
```

## Como executar

### IntelliJ IDEA
1. File > Open e selecione a pasta `reengenharia-clinica` (o IntelliJ reconhece o `pom.xml`).
2. Aguarde o Maven carregar as dependências.
3. Para a simulação: abra `Main.java` e clique no botão de play ao lado do `main`.
4. Para os testes: clique com o botão direito na pasta `src/test/java` > Run 'All Tests'.

### VS Code
1. Instale a extensão "Extension Pack for Java".
2. File > Open Folder e selecione a pasta `reengenharia-clinica`.
3. Abra `Main.java` e clique em "Run" acima do método `main`.
4. Os testes aparecem na aba Testing (ícone de frasco).

### Terminal (com Maven instalado)
```
mvn test              # roda os 10 testes JUnit
mvn compile exec:java # roda a Main com a simulação
```

## Resultado esperado da Main

```
[OK] Convênio com taxa (cenário da atividade) | novo: 279.50 | legado: 279.50 | esperado: 279.50
[OK] Convênio sem taxa | novo: 264.00 | legado: 264.00 | esperado: 264.00
[OK] Particular acima de 500 | novo: 570.00 | legado: 570.00 | esperado: 570.00
[OK] Particular com 500 exatos | novo: 500.00 | legado: 500.00 | esperado: 500.00
[OK] Tipo não identificado | novo: 300.00 | legado: 300.00 | esperado: 300.00
[OK] Convênio sem procedimentos | novo: 15.50 | legado: 15.50 | esperado: 15.50
```

As linhas `LOG:` intercaladas vêm da classe legada, que ainda tem o `System.out.println`.

## Docker

O `Dockerfile` usa build multi-stage:

1. Estágio `build` (`maven:3.9-eclipse-temurin-21`, JDK 21 + Maven): roda `mvn clean package`, incluindo os testes.
   Se algum teste falhar, o build da imagem falha.
2. Estágio final (`eclipse-temurin:21-jre-alpine`, só JRE 21): recebe apenas o `app.jar`, roda com o usuário
   sem privilégio `app` e executa `java -jar app.jar`.

```
docker build -t sistema-reengenheirado:local .
docker run --rm sistema-reengenheirado:local
```

A saída é a mesma da seção anterior. O `EXPOSE 8080` é apenas declarativo: a aplicação atual é de console,
imprime os cenários e encerra, então nada escuta na porta.

## CI/CD (GitHub Actions)

O workflow `.github/workflows/ci-cd.yml` é o "guarda-costas" do projeto. Ele **só roda quando uma tag de versão
é enviada** (`v1.0.0`, `v1.1.2`). Commits comuns, pull requests e tags fora desse padrão não disparam nada.

```
tag vX.Y.Z
   ├── qualidade   mvn verify (testes + JaCoCo) e SonarCloud, esperando o Quality Gate
   ├── seguranca   Trivy no repositório; docker build; Trivy na imagem; guarda a imagem escaneada
   └── publicacao  (só se os dois acima passarem) push da mesma imagem para o Docker Hub
```

### Como lançar uma versão

```
git switch main && git pull
git tag v1.0.0
git push origin v1.0.0
```

A imagem fica disponível em `<DOCKER_USERNAME>/sistema-reengenheirado:v1.0.0` e `:latest`.

### O que quebra a esteira (e impede o push)

- Erro de compilação ou qualquer teste JUnit falhando.
- Quality Gate do SonarCloud reprovado (ou sem resposta em 5 minutos).
- Trivy encontrando vulnerabilidade, má configuração ou segredo de severidade `CRITICAL` ou `HIGH` com correção
  disponível, no repositório ou na imagem Docker.
- Secret ou Variable obrigatório não configurado.

### Configuração (Settings > Secrets and variables > Actions)

| Nome | Tipo | Conteúdo |
|---|---|---|
| `SONAR_TOKEN` | Secret | Token de análise do SonarCloud |
| `DOCKER_USERNAME` | Secret | Usuário do Docker Hub |
| `DOCKER_PASSWORD` | Secret | Access token do Docker Hub (Read & Write), não a senha da conta |
| `SONAR_ORGANIZATION` | Variable | Chave da organização no SonarCloud |
| `SONAR_PROJECT_KEY` | Variable | Chave do projeto no SonarCloud |

No SonarCloud, a **Automatic Analysis** precisa estar desligada (a análise é feita pela esteira) e a definição de
New Code deve ser "Previous version", para cada tag ser comparada com a anterior.
