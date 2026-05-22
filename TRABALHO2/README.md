# Chat Multithread TCP em Java

Este projeto consiste em uma aplicação de chat em tempo real baseada na arquitetura **Cliente-Servidor**, utilizando sockets TCP para comunicação confiável e threads para suportar múltiplos usuários simultâneos. A aplicação permite o envio de mensagens globais (broadcast), mensagens privadas direcionadas e comandos interativos de sistema, operando tanto via terminal (CLI) quanto por uma interface gráfica (GUI).

---

## 🚀 Como Executar a Aplicação

Para compilar e rodar o projeto localmente, certifique-se de ter o **Java Development Kit (JDK)** instalado em sua máquina.

### 1. Compilação dos Arquivos
Navegue até a raiz do diretório onde a pasta `Trabalho2` está localizada e compile todos os arquivos `.java`:
```bash
javac Trabalho2/*.java

```

### 2. Iniciar o Servidor

O servidor precisa estar rodando **antes** de qualquer cliente tentar se conectar. Para iniciá-lo, execute:

```bash
java Trabalho2.servidor_TCP

```

*O servidor reservará e escutará a porta **1234** por padrão.*

### 3. Iniciar o Cliente

Você pode escolher entre rodar o cliente via terminal tradicional ou pela interface gráfica. É possível abrir múltiplos terminais ou janelas para simular diferentes usuários conversando na sala.

* **Opção A: Cliente via Interface Gráfica (Recomendado)**
```bash
java Trabalho2.telaInterface

```


* **Opção B: Cliente via Terminal (CLI)**
```bash
java Trabalho2.Cliente_TCP

```



---

## 💬 Comandos Disponíveis no Chat

Independentemente da interface escolhida, você pode interagir utilizando os comandos abaixo diretamente no campo de texto:

* **Listar Usuários Conectados:**
```text
/usuarios

```


*Retorna uma lista estruturada contendo o nome de todos os usuários online no momento e o total de conexões.*
* **Enviar Mensagem Privada:**
```text
/privado [nome_do_usuario] [mensagem]

```


*Envia uma mensagem reservada que apenas o remetente e o destinatário conseguirão visualizar.*
* **Mensagem Global (Broadcast):**
Qualquer texto digitado que não comece com os comandos acima será transmitido publicamente para todas as pessoas na sala.

---

## 📂 Explicação Detalhada dos Arquivos

A aplicação está dividida em módulos bem definidos, onde o servidor gerencia as conexões de forma concorrente e os clientes tratam o envio e o recebimento de mensagens de forma assíncrona.

### 1. `servidor_TCP.java`

É o ponto de entrada do servidor. Responsável por inicializar o `ServerSocket` na porta especificada (`1234`) e escutar requisições de novas conexões em um loop infinito. Ao aceitar uma conexão através do método `accept()`, o servidor delega o canal de comunicação gerado para uma nova instância de `ThreadCliente` dedicada e a inicia, garantindo que o servidor continue livre para receber novos usuários. Para centralizar o gerenciamento dos usuários ativos de forma segura entre múltiplas threads, utiliza a estrutura thread-safe `ConcurrentHashMap`.

### 2. `ThreadCliente.java`

Implementa a interface `Runnable` e roda inteiramente no lado do servidor. Cada cliente conectado possui uma instância própria desta thread rodando em paralelo, gerenciando os fluxos de entrada e saída de objetos (`ObjectInputStream` e `ObjectOutputStream`). Suas principais funções são:

* **Validação de Login:** Garante que o nickname escolhido pelo usuário não esteja em uso antes de liberá-lo para a sala.
* **Roteamento de Mensagens:** Analisa o conteúdo recebido para determinar se é um comando (`/usuarios`), uma mensagem privada (`mensagemPrivada`) ou uma transmissão global (`broadcast`).
* **Tratamento de Desconexão:** No bloco `finally`, caso o socket seja fechado ou caia, ela remove o usuário do mapa global de forma limpa e avisa os demais participantes que o usuário deixou o chat.

### 3. `Mensagem.java`

Classe modelo que encapsula a estrutura de todos os dados que trafegam pela rede. Por implementar a interface `Serializable`, permite que seus objetos sejam convertidos em fluxos de bytes e transmitidos diretamente via sockets de objetos. Ela armazena o nome do remetente, o destinatário (definido como `null` em caso de broadcast), o conteúdo textual e o carimbo de data/hora (`LocalDateTime`). Além disso, sobrescreve o método `toString()` para entregar a mensagem já formatada com a estampa de tempo correspondente (ex: `[dd/MM/yyyy HH:mm:ss] Remetente: Conteúdo`).

### 4. `Cliente_TCP.java`

Contém o ponto de entrada da versão em terminal (CLI) do cliente. Inicializa a conexão do `Socket` apontando para o servidor (`localhost:1234`), cria as streams de objetos e gerencia o loop inicial de autenticação de nome do usuário. Assim que recebe o sinal de `"OK"` do servidor, o programa dispara a `ThreadEscuta` para receber dados em segundo plano e entra em um loop infinito de leitura de terminal através do `Scanner`, interpretando comandos privados ou envios globais para despachar ao servidor.

### 5. `ThreadEscuta.java`

Uma classe auxiliar criada especificamente para dar suporte ao cliente de terminal (`Cliente_TCP`). Como o ato de ler dados do terminal (`teclado.nextLine()`) bloqueia a execução da thread principal, a `ThreadEscuta` roda em paralelo focada única e exclusivamente em escutar o canal de entrada do socket (`readObject()`). Assim que uma nova mensagem chega do servidor, ela a imprime imediatamente na tela, permitindo uma comunicação fluida e em tempo real.

### 6. `telaInterface.java`

Implementa a interface gráfica do usuário (GUI) utilizando a biblioteca `javax.swing`. Esta classe une de forma visual as capacidades do cliente e da thread de escuta em uma experiência intuitiva baseada em janelas (`JFrame`). Ela conta com uma área de chat rolável não editável (`JTextArea`), campo de texto para digitação (`JTextField`) e botões para enviar mensagens ou desconectar de forma segura. Internamente, inicializa uma thread assíncrona dedicada à escuta que atualiza a interface gráfica em tempo real assim que novos objetos `Mensagem` chegam pelo socket.

---

## 🛠️ Tecnologias Utilizadas

* **Java SE 8+**
* **Sockets de Rede (TCP)**
* **Concorrência e Multithreading** (`Thread`, `Runnable`, `ConcurrentHashMap`)
* **Serialização de Objetos** (`Serializable`, `ObjectInputStream`, `ObjectOutputStream`)
* **Java Swing** (Interface Gráfica de Usuário)
