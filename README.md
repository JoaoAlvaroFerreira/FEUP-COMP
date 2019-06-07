# PROJECT TITLE: Compiler of the Java-- language to Java Bytecodes

## Grupo: 26

Nome: João Álvaro Ferreira, Número de Estudante: up201605592, Avaliação: 16, Contribuição: 25%

Nome: Fábio Azevedo, Número de Estudante: up201606540, Avaliação: 16, Contribuição: 25%

Nome: Mariana Dias, Número de Estudante: up201606486, Avaliação: 16 Contribuição: 25%

Nome: Tiago Ribeiro, Número de Estudante: up201605619, Avaliação: 16, Contribuição: 25%

## ÍNDICE:

- [Sumário](#sumário)
- [Execução](#execução)
- [Lidar com Erros Sintáticos](#lidar-com-erros-sintáticos)
- [Análise Semântica](#análise-semântica)
- [Geração de Código](#geração-de-código)
- [Representações Intermédias](#representações-intermédias)
- [Overview](#overview)
- [Distribuição de Tarefas](#testsuite-and-test-infrastructure)
- [Pros](#pros)
- [Cons](#cons)

Avaliação GLOBAL do projeto: 16

### SUMÁRIO: 
A ferramenta implementada analisa um ficheiro de código na linguagem Java--. 
Faz a respetiva análise sintática, verificando a existência de vocábulos errados. 
Verifica também a semântica, encontrando linhas ou comandos que não sejam válidos.
Por último, gera o código correspondente em Java Bytecodes, de modo a que este possa ser compilado e executado na máquina.

### EXECUÇÃO:
Para correr a ferramenta, é necessário executar o seguinte comando dentro da pasta da mesma:
./batchComp.sh <NOME_DA_CLASSE>

(Pode ser necessário dar permissões de execução ao script)

### LIDAR COM ERROS SINTÁTICOS: (Describe how the syntactic error recovery of your tool does work. Does it exit after the first error?)


### ANÁLISE SEMÂNTICA:



### REPRESENTAÇÕES INTERMÉDIAS (IRs): (for example, when applicable, briefly describe the HLIR (high-level IR) and the LLIR (low-level IR) used, if your tool includes an LLIR with structure different from the HLIR)



### GERAÇÃO DE CÓDIGO:

A geração de código é feita com base na árvore sintática. Começando na raíz, esta é percorrida, sendo avaliado o tipo de cada nó (através de um switch no ID do SimpleNode). A cada tipo corresponde uma função diferente que gera o código adequado, chamando recursivamente esta função de avaliação para os nós inferiores. O código gerado vai sendo concatenado para uma string,sendo esta depois retornada e escrita no ficheiro .j.
A geração de código é apoiada pela symbol table, servindo-se desta para encontrar o tipo das variáveis e funções, convertendo-se para os seus equivalentes em Jasmin. Utiliza-a também para verificar se se trata de uma classe ou função externa, invocando o comando de jasmin adequado.
A gestão do tamanho da stack é feita a cada instrução com um contador, sendo que se for necessário fazer push de algo, este é incrementado e em situações de pull, decrementado. No fim guarda-se o valor máximo a que o contador chegou e é esse valor que é utilizado para definir o limite da stack.

O maior problema da ferramenta é a complexidade do código. De modo a cobrir todos os casos particulares dentro da linguagem, acabou-se por adicionar muitas condições, tornando o código complicado e mais suceptível a alguma falha.




### OVERVIEW: (refer the approach used in your tool, the main algorithms, the third-party tools and/or packages, etc.)


### DISTRIBUIÇÃO DE TAREFAS:
Análise Sintática -> Fábio Azevedo, Mariana Dias, Álvaro Ferreira e Tiago Ribeiro;
Análise Semantica -> Fábio Azevedo, Mariana Dias;
Geração de código -> Tiago Ribeiro e Álvaro Ferreira;


### PROS:
A nossa ferramenta cumpre a maioria dos requisitos, não tendo optimizações. (...)


### CONS: (Identify the most negative aspects of your tool)

