# Aula 1 - 06/08/2019.

Um compilador é composto por um analisador léxico, um analisador sintático, um analisador semântico e um gerador de código fonte (com otimizações de código internas)

## Análise léxica

* Separa o código em lexemas
* Analisa os lexemas e classifica-os em tokens;
  * Cada lexema possui apenas um token
* Retorna a sequência de tokens identificados na forma de tuplas (lexema, token)
* Pode realizar validação básica: identificadores não podem começar com números, não podem possuir acentos, caracteres especiais, etc

### Exemplo:
*Entrada do analisador léxico:*
```
public class Zirigudim {
    String a;
    String b;

    public static void main(){
        a = 14;
        System.out.println(a);
    }
}
```

*Saída do analisador léxico:*

| LEXEMA    | TOKEN         |
| --------- | ------------- |
| public    | T_PUBLIC      |
| class     | T_CLASS       |
| Zirigudim | T_ID          |
| {         | T_ABRE_CHAVES |
| String    | T_STRING      |
| a         | T_ID          |
| int       | T_INT         |
| b         | T_ID          |

O analisador léxico possui um buffer de lookAhead, ou seja: na prática, é uma variável que verifica o caracter que está à sua frente. Isso serve para saber quando um token finalizou.

### Análise sintática

* Também chamado de parser
* Estrutura gramatical correta... for i in xxx blablabla        deve montar uma árvore
* *"O gato cachorro"*

### Análise semântica
* *"O gato dorme o rato"*


### Gramáticas

* Tipo 3 = automato finito
* Tipo 2 = automato com pilha
* Tipo 1 = máquina de turing
* Tipo 0 = máquina de turing

Segundo a hierarquia de Chomsky, linguagens de programação são gramáticas de tipo 2 (não sensíveis ao contexto) porém com exceção na tipagem, onde aí sim são sensíveis ao contexto.

----------

### **Homework:**
O professor nos deu o código fonte do analisador léxico da linguagem python (incompleto) e solicitou que implementássemos a validação para os tokens das quatro operações (+,-,*,/) e o sinal de :

### **Keywords to study:** *lookahed*

### **Links úteis:** 
  * http://www.inf.ufes.br/~tavares/labcomp2000/intro2.html


### **ToReview:** 
  * O analisador léxico executa um *lookahead* 