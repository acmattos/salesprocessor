# Aplicação SALESPROCESSOR

A aplicação responsável pelo processamento de arquivos `.DAT` contendo os 
resultados da vendas feitas a clientes pelos vendedores da empresa.

## Requisitos de Software

- JDK 1.8 or later;
- Maven 3 or later.

## Stack

- Lombok
- Java IO
- Java NIO

## Iniciando

1. Assegurar-se de que já possui as ferramentas JDK e Maven instaladas e
funcionando adequadamente.
- Obter fontes da aplicação e copiar para um diretório a sua escolha.
- Pelo prompt de comando, acessar o diretório do projeto:

  > cd c:\<RAIZ>\salesprocessor

- Dentro do diretório do projeto ('c:\<RAIZ>\salesprocessor'), digitar:

  > mvn clean compile package

- Este processo baixará todas as dependências do projeto, compilará o
código e criará o executável.

- Pelo prompt de comando, acessar o diretório alvo:

  > cd c:\<RAIZ>\salesprocessor\target
  
- A seguir, digite o seguinte comando para rodar a aplicação.
  
  > java -jar salesprocessor-1.0.0-SNAPSHOT.jar

- Os arquivos '.dat' devem ser colocados no diretorio: 
  
  > c:\\%HOMEPATH%\data\in

- Todos os arquivos processados serão armazenados em:

  > c:\\%HOMEPATH%\data\out

## Arquitetura

A aplicação foi concebida visando uma codificação limpa (métodos curtos,
significativos) que podem ser observada nas classes (cada qual com sua
lógica de trabalho e devida separação de conceitos) que a compõe.

A aplicação pode ter suas funcionalidades expandidas de forma fácil e
pouco traumática. Com a devida especialização de seus componentes,
torna-se fácil criar novos parsers para coletar novos dados dos arquivos.

O uso de recursos da API NIO, a otimização no uso e descarte de recursos
consumidos pela aplicação tornam ela performática, com baixo consumo de
memória (fora de regime de trabalho) e picos apenas durante o
processamento dos arquivos. Ainda assim, todos os recursos são liberados,
tão logo não forem mais necessários.

A seguir uma descrição dos pacotes principais da aplicação e de suas classes
componentes:

### br.com.acmattos.salesprocessor

Pacote raiz da aplicação. Nele há a classe responsável por preparar e executar
todas as funcionalidades da aplicação.

`Application` é a classe que orquestra todo o processo de execução.

### br.com.acmattos.salesprocessor.file

Pacote responsável pelo trabalho de preparação do sistema de arquivos,
monitoração de diretórios e processamento dos arquivos `.DAT` que contém as
informações de processamento.
 
A classe `Environment` é quem cuida da configuração dos diretórios de 
entrada, saída e descarte, preparando espaço para que a classe
`InputDirectoryListener` possa ficar escutando por mudanças no diretório de 
entrada. 

Ao detectar a entrada de novos arquivos no diretório, a classe recorre às 
funcionalidades disponíveis em `FileDigester`, que é responsável por:
 
1. Ler cada arquivo novo no diretório de entrada.
2. Verificar se pode ou não processar um dado arquivo.
3. Obter o conteúdo dos arquivos (passando o trabalho de parsing para
 `EntityParser`, comentada mais a frente).
4. Criar o arquivo de saída, referente ao de entrada recém processado.
5. Descartar o arquivo processado, em diretório específico.


### br.com.acmattos.salesprocessor.parser

Pacote transforma as linhas recebidas pelo `FileDigester` em entidades
que serão manipuladas e produzirão a resposta esperada.
 
A classe `EntityParser` recebe, uma linha por vez, do arquivo de entrada 
obtido pelo `FileDigester`, faz o pré-processamento da linha, identificando
qual a entidade é responsável por terminar o trabalho de conversão da
informação, transformando linha de arquivo em entidade de negócio. 

Ao terminar o processamento de um arquivo, `EntityParser` é capaz de gerar a
entidade contendo os dados de saída, que irão ser gravados em diretório 
próprio por `FileDigester`.

A classe `Customer` converte o pré-processamento feito por `EntityParser` em
entidades deste tipo. As linhas contidas no arquivo de entrada tem esta lei 
de formação:

 > 002çCNPJçNameçBusinessArea
 
A classe `Sale` converte o pré-processamento feito por `EntityParser` em
entidades deste tipo. As linhas contidas no arquivo de entrada tem esta lei
de formação:
 
 > 003çSale IDç[Item ID-Item Quantity-Item Price]çSalesmanName               

A classe `Saleman` converte o pré-processamento feito por `EntityParser` em
entidades deste tipo. As linhas contidas no arquivo de entrada tem esta lei
de formação:

 > 001çCPFçNameçSalary

A classe `Output` guarda a resposta ao processamento das informações
contidas no arquivo de entrada. Após um objeto desta classe ser criado, ele é
entregue ao `FileDigester`, para que este possa cuidar da geração do arquivo 
de saída (contendo o resultado do processamento de entrada).
 
## Arquivos de entrada

Este é um exemplo de entrada que esta aplicação é capaz de processar:

```
001ç1234567891234çDiegoç50000
001ç3245678865434çRenatoç40000.99
002ç2345675434544345çJose da SilvaçRural
002ç2345675433444345çEduardo PereiraçRural
003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego
003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çRenato
```
 
O diretório `c:\<RAIZ>\salesprocessor\home\data\in` contém os seguintes 
arquivos para teste da aplicação:

* 2_2_8_Diego.dat
* 2_2_10_Renato.dat
* 3_3_9_Diego.dat

Cujos nome denotam as respostas encontradas em seus respectivos arquivos de
saída.

* descartado.txt

Cujo nome indica que o arquivo não será tratado pelo pela aplicação por não 
possuir a extensão permitida.

* sem_resposta.dat

Cujo nome denota que o arquivo, embora lido e tratado pela aplicação, não 
produzirá saída alguma por não conter dados.



