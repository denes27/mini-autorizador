### Projeto Mini-Autorizador VR ###
* Autor: Denes Leal dos Santos
* Github: denes27
* Gitlab: denes27

###Sobre a Arquitetura ###
O projeto foi executado em uma arquitetura padrão Spring MVC, com as requisições entrando na aplicação pela Controller, as regras de negócio executadas nos Services que
    por sua vez invoca o repository para a integração com a camada de persistência. Este modelo foi escolhido pela simplicidade para fazer projetos desta natureza sem perder
    muito em questões de encapsulamento e isolamento das regras de negócio das tecnologias utilizadas. 

Cogitou-se utilizar uma arquitetura hexagonal, porém a necessidade de criar diversas interfaces entre as camadas acaba aumentando o tempo de desenvolvimento em troca de manutenabilidade, o que para um projeto de exemplo como esse não é tão interessante.

O banco de dados escolhido foi o MongoDb por dois motivos: 

* simplicidade em armazenar um objeto completo ao invés de mapear colunas específicas em um banco relacional; 
* desejo de estudar este banco de dados, até então o único NoSql com que havia tido contato foi o Cassandra;

Além disso, pode-se argumentar que para um projeto deste tipo, com operações simples sobre um único documento que não é composto de diversos objetos, o MongoDb seria até mesmo
    mais apropriado em questão de performance do que um banco relacional, porém para uma decisão válida nesse sentido, precisariamos de mais detalhes do escopo do projeto, como o 
    tamanho do sistema, desejo de escalabilidade etc.

### Sobre os Desafios Opcionais ###
Este projeto foi feito tendo em mente não utilizar nenhuma cláusula 'if', assim como proposto pela especificação como desafio opcional. A parte mais desafiadora foi arrumar alguma
    forma de lançar uma exceção dependendo de alguma condição na camada de service, para que eu pudesse utilizar apenas try/catchs na camada de controller. 

A solução encontrada foi utilizar operadores ternários com um método que jogue uma exceção como um dos resultados:

    Ex:
    boolean cartaoValido = verificador.verificarCartaoDto(novoCartaoDto) ? true : exceptionHandler.throwCartaoInvalidoException();

Esta solução funciona bem, lançando a exceção quando necessário e mantendo o código relativamente enxuto e fácil de entender. Porém não é uma solução que eu utilizaria em um ambiente real, já que
para que funcione precisei tomar a liberdade de usar certos improvisos, como criar um método com assinatura boolean que na prática nunca retorna um boolean, apenas joga a exceção, e também precisei 
declarar variaveis que nunca são utilizadas exceto por algum log (no caso do exemplo a variavel cartaoValido).

Portanto apesar de parece menos elegante, nesse caso eu ainda cogitaria lançar as exceções com algo do tipo:
        
    if(!verificador.verificarCartaoDto(novoCartaoDto)) {
        logger.error("Cartão inválido");
        throw new CartaoInvalidoException();
    }
            
Uma verificação simples do tipo, com um único if e poucas instruções não aumenta a complexidade cognitiva da aplicação o suficiente pra justificar "gambiarras".

Se há outras formas de fazer algo do tipo, eu estou interessado em saber, por favor me mandem sugestões!

Sobre proteções contra transações concorrentes, aparentemente os métodos do JPA para o MongoDb já vem com especificações robustas contra esse tipo de problema, mas para garantir maior segurança, foi feita a configuração
do acesso do banco para o WriteConcern W1:

        Requests acknowledgment that the write operation has propagated to the standalone mongod or the primary in a replica set. 
        Data can be rolled back if the primary steps down before the write operations have replicated to any of the secondaries.

Em que basicamente as operações de update esperam uma confirmação do banco de dados antes de confirmar a transação, com a opção de executar rollback em caso de erro.

### Pontos de Melhoria ###

Como o intuito é que este projeto não se alongasse por muito tempo, algumas melhorias acabaram por ficar de lado, vou listá-las aqui para referência futura caso resolva atualizar o projeto:

* Objeto de Transação com Controllers e Services separadas:

    Apesar da transação alterar o valor do saldo de um cartão, a transação pode ser tratada como uma entidade em si, portanto olhando dessa forma, há uma leve violação do princípio de responsabilidade única ao tratar de transaçãoes dentro
    de objetos responsáveis por manipular cartões, portanto seria interessante criar uma controller e service separada, com seus respectivos testes.


* Testes de Integração e testes de concorrencia:

    Uma aplicação robusta precisa de testes robustos, portanto seria interessante acrescentar testes para a aplicação de ponta a ponta, possivelmente na propria coleção do postman e testar também se o comportamento em caso de transações concorrentes é o esperado.


* Guardar senhas criptografadas:

    Em termos de segurança da informação, é uma má prática enorme guardar no banco de dados a senha como ela é, sendo o correto guardar sua forma criptografadas, e posteriormente comparar o hash gerado
    a partir da senha passada na transação com o hash guardado no momento da criação da senha.


* ExceptionHandler mais customizado:

    As exceções utilizadas no projeto são cascas de sua classe pai, RunTimeException, sem nenhuma mensagem ou método específico. Além disso, poderiamos usar o handler de exceções do spring para não precisar necessáriamente 
    utilizar muitos try/catchs na camada de controller.


### Conclusão ###

Obrigado pela oportunidade de praticar minhas técnicas implementando esta especificação, aprecio qualquer feedback ou sugestão!