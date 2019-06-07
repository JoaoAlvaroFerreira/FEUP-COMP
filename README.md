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

### LIDAR COM ERROS SINTÁTICOS: 
A análise sintática é feita pelo módulo NewJava.java que constrói a AST e deteta qualquer tipo de erro sintático, de acordo com os parâmetros da linguagem Java--. Quando um erro é detetado, este é reportado. O programa lida com até dez erros, terminando a execução aquando a declaração do décimo.


### ANÁLISE SEMÂNTICA:

A nossa análise semântica é capaz de detetar, como indicado no enunciado, erros semânticos existentes em expressões. Isto inclui:

* Brackets abertos sem o fecho, ou brackets fechados sem a abertura;

* Expressões matemáticas sem um dos elementos;

* Declarações de novos arrays incompletas;

* Abertura de parêntesis sem fecho, ou parentesis fechado sem uma abertura anterior correspondente;

* Uso incorreto de símbolos em expressões;

* Símbolos não reconhecidos pelo Java--;

* Declarações duplicadas de variáveis;

* Declarações duplicadas de funções;

* Valores de return incompatíveis;

* Falta de declaração de return numa função;

* Verificação do número de argumentos chamados;

* Redefinição de variáveis globais;

* Variáveis não definidas;

* Entre outros.



### REPRESENTAÇÕES INTERMÉDIAS (IRs):
A nossa representação intermédia e feita por meio de Symbol Tables. A estrutura de uma Symbol Table (SymbolTable.java) inclui:

* Um Map entre Integers e ArrayLists de Strings para guardar as variáveis inicializadas;
* Um Map entre Integers e um Map de Strings e Integers para guardar os arrays inicializados;
* Um ArrayList de SymbolTableEntry para representar todas as entradas da SymbolTable;

Estas SymbolTableEntries (SymbolTableEntry.java) são representativas de funções, tendo os parâmetros e nódulos necessários para tal representação.

* Um ArrayList de SymbolType para representar as variáveis globais;

Estes SymbolTypes (SymbolType.java) são representativos de variáveis.

* Strings para guardar o nome da class e do filepath. 

Estas SymbolTables são geradas ao percorrer a AST gerada anteriormente.

### GERAÇÃO DE CÓDIGO:

A geração de código é feita com base na árvore sintática. Começando na raíz, esta é percorrida, sendo avaliado o tipo de cada nó (através de um switch no ID do SimpleNode). A cada tipo corresponde uma função diferente que gera o código adequado, chamando recursivamente esta função de avaliação para os nós inferiores. O código gerado vai sendo concatenado para uma string,sendo esta depois retornada e escrita no ficheiro .j.
A geração de código é apoiada pela symbol table, servindo-se desta para encontrar o tipo das variáveis e funções, convertendo-se para os seus equivalentes em Jasmin. Utiliza-a também para verificar se se trata de uma classe ou função externa, invocando o comando de jasmin adequado.
A gestão do tamanho da stack é feita a cada instrução com um contador, sendo que se for necessário fazer push de algo, este é incrementado e em situações de pull, decrementado. No fim guarda-se o valor máximo a que o contador chegou e é esse valor que é utilizado para definir o limite da stack.

O maior problema da ferramenta é a complexidade do código. De modo a cobrir todos os casos particulares dentro da linguagem, acabou-se por adicionar muitas condições, tornando o código complicado e mais suceptível a alguma falha.



### OVERVIEW: 

O Java-- que desenvolvemos é uma ferramenta bastante capaz e versátil, tendo sido desenvolvida para ser capaz de lidar com casos bastante abrangentes. O processo de desenvolvimento, embora trabalhoso e com alguns solavancos, foi eficaz a ensinar-nos a construção e funcionamento interno de uma linguagem e o respetivo compilador. Em retrospetiva, embora julgarmos que fizemos um bom trabalho, teríamos organizado o trabalho de uma forma diferente de modo à informação ser mais acessível e fácil de manipular da forma pretendida, já que isso facilitaria em muito o processo de desenvolvimento.

### DISTRIBUIÇÃO DE TAREFAS:
Análise Sintática -> Fábio Azevedo, Mariana Dias, João Álvaro Ferreira e Tiago Ribeiro;
Análise Semantica -> Fábio Azevedo, Mariana Dias;
Geração de código -> Tiago Ribeiro e João Álvaro Ferreira;


### PROS:
A nossa ferramenta cumpre a maioria dos requisitos à exceção das optimizações e a análise semântica engloba todo o Java-- (de salientar que não era requerido no enunciado).


### CONS:
As optimizações não foram implementadas e a ferramenta não interpreta "extends" como é suposto.

