# Relatório: Pipeline de Evolução Segura (CI/CD)

Rascunho do relatório em PDF a ser entregue no D2L. Os itens marcados com `PREENCHER` dependem da primeira
execução real da esteira.

## 1. Repositório GitHub

- Código-fonte: https://github.com/antoniolibreno/atividade-reengenharia
- Aba Actions: https://github.com/antoniolibreno/atividade-reengenharia/actions
- Workflow: `.github/workflows/ci-cd.yml`

## 2. Imagem no Docker Hub

- Repositório: https://hub.docker.com/r/antoniolibreno/sistema-reengenheirado
- Tag publicada automaticamente: `v1.0.0` (e `latest`), digest
  `sha256:8d707c435e8f2335443bfda3eff8bd346a02f04147bf9214322b5ddd118c4a18`
- Execução que publicou: https://github.com/antoniolibreno/atividade-reengenharia/actions/runs/42720274367
- Teste de quem for avaliar: `docker run --rm antoniolibreno/sistema-reengenheirado:v1.0.0` (mostra 6 linhas `[OK]`)

## 3. Como a esteira funciona

A esteira só é disparada pelo push de uma tag de versão (`v1.0.0`). Ela tem três jobs:

1. **qualidade**: compila, roda os 10 testes JUnit, gera cobertura com JaCoCo e envia tudo para o SonarCloud.
   O scanner espera o resultado do Quality Gate; se for reprovado, o job falha.
2. **seguranca**: roda o Trivy no repositório (dependências do `pom.xml`, Dockerfile e segredos), constrói a
   imagem multi-stage (JDK 21 para compilar, JRE 21 Alpine para rodar) e roda o Trivy de novo na imagem.
   Qualquer achado `CRITICAL` ou `HIGH` com correção disponível faz o job falhar.
3. **publicacao**: só executa se os dois jobs anteriores passarem. Publica no Docker Hub exatamente a imagem
   que o Trivy escaneou (o ID da imagem aparece no log dos dois jobs).

## 4. Evidência do Sonar

`PREENCHER`: screenshot do painel do SonarCloud (Measures > Complexity) mostrando a Complexidade Ciclomática,
os Code Smells, as Duplicações e o status do Quality Gate.

Legenda sugerida: "Análise da versão v1.0.0 no SonarCloud. A classe `legado/x_Mgr_F`, mantida de propósito como
referência, concentra os Code Smells; o código reengenheirado aparece com baixa complexidade ciclomática."

## 5. Evidência do Trivy

`PREENCHER`: screenshot do log do job "Trivy Security Gate" com as tabelas de vulnerabilidades do repositório
e da imagem.

## 6. Prova de que a esteira bloqueia

Execução: https://github.com/antoniolibreno/atividade-reengenharia/actions/runs/42721792147

Em uma branch descartável foi adicionada ao `pom.xml` a dependência `log4j-core` 2.14.1 e enviada a tag de teste
`v0.9.0`. Resultado:

- **Trivy Security Gate: falhou** já no scan do repositório, com 3 vulnerabilidades (2 `CRITICAL`, 1 `HIGH`):
  CVE-2021-44228 (Log4Shell), CVE-2021-45046 e CVE-2021-45105.
- **SonarCloud Quality Gate: passou**, porque o código continuou igual; o problema estava só na dependência.
- **Build e Push para Docker Hub: skipped**. A tag `v0.9.0` não aparece no Docker Hub, que continua só com
  `v1.0.0` e `latest`.

Depois do teste, a tag `v0.9.0` e a branch foram apagadas; a execução continua no histórico do Actions.

`PREENCHER`: screenshot dessa execução mostrando o job de segurança vermelho, a tabela de CVEs e o job de
publicação como "skipped".

## 7. Ensaio Crítico Final

O semestre começou com um diagnóstico. A classe `x_Mgr_F` reunia nomes sem significado, regras de cobrança
misturadas com log e um único método concentrando todas as decisões. Era a Entropia de Software em forma
concreta, aquilo que Lehman descreve nas leis da Mudança Contínua e da Complexidade Crescente: um sistema em
uso precisa mudar, e cada mudança aumenta sua complexidade, a menos que exista um trabalho ativo para
reduzi-la. Cada atalho que levou o código até aquele estado era Débito Técnico, uma dívida que cobra juros
sempre que alguém precisa entender ou alterar o programa. A reengenharia com SRP, Strategy e Factory, apoiada
pelos testes JUnit, pagou essa dívida uma vez. As próprias leis de Lehman, porém, mostram o limite dessa
solução: a refatoração é um evento pontual, enquanto a entropia é um processo contínuo. Sem um mecanismo que
acompanhe cada entrega, a próxima mudança feita às pressas voltaria a acumular juros.

A esteira de CI/CD é esse mecanismo. Ela transforma um cuidado que antes dependia da disciplina de cada pessoa
em regra automática, aplicada a toda versão, porque nenhuma tag chega ao Docker Hub sem passar por dois
portões. O SonarCloud torna o débito visível e mensurável: a cada versão calcula complexidade ciclomática,
code smells, duplicação e cobertura, e o Quality Gate, avaliado sobre o código novo, reprova a versão que piora
esses indicadores. A dívida antiga continua registrada (a `x_Mgr_F` foi mantida de propósito como referência),
mas deixa de poder crescer. O Trivy cuida de uma entropia que avança mesmo quando ninguém mexe no código: novas
CVEs são publicadas o tempo todo para as bibliotecas e para a imagem base, então uma versão segura hoje pode
não ser amanhã. Ao varrer dependências, Dockerfile e a própria imagem, e ao bloquear achados críticos ou altos
que já têm correção, a esteira impede que essa dívida de segurança seja embarcada. A esteira não elimina a
entropia, que é inerente a qualquer software em evolução, mas muda sua natureza: a degradação deixa de ser
silenciosa e gradual e passa a ser uma falha explícita, com causa identificada, que precisa ser resolvida antes
do lançamento. É o trabalho de redução de complexidade que Lehman descreve, feito de forma contínua, igual para
todos e sem exceção.
