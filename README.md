# Reengenharia: Calculadora de Atendimento

Reengenharia da classe legada `x_Mgr_F` do sistema financeiro da clínica, aplicando Clean Code,
Princípio da Responsabilidade Única (SRP) e os padrões Strategy e Factory.

## Requisitos

- Java 17 ou superior (JDK)
- Maven 3.8 ou superior (o IntelliJ e o VS Code já trazem o Maven embutido)

A única dependência externa é o JUnit 5, usado apenas nos testes. O Maven baixa sozinho na primeira execução.

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
