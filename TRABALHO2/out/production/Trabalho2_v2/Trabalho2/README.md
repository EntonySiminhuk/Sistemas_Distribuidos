# Chat Multithread TCP em Java

Este projeto consiste em uma aplicação de chat em tempo real baseada na arquitetura **Cliente-Servidor**, utilizando sockets TCP para comunicação confiável e threads para suportar múltiplos usuários simultâneos. A aplicação permite o envio de mensagens globais (broadcast), mensagens privadas direcionadas e comandos interativos de sistema, operando tanto via terminal (CLI) quanto por uma interface gráfica (GUI).

O sistema foi desenvolvido para a disciplina de **Sistemas Distribuídos / Programação Paralela**, utilizando conceitos de concorrência, serialização de objetos e comunicação assíncrona em rede.

---

# Arquitetura do Sistema

O sistema segue uma arquitetura cliente-servidor centralizada.

## Fluxo de Comunicação

```text
Cliente → Servidor TCP → Distribuição para Clientes
```

Cada cliente conectado possui:

- Uma thread própria no servidor (`ThreadCliente`)
- Uma thread assíncrona de escuta no cliente (`ThreadEscuta`)

O servidor mantém uma estrutura global compartilhada contendo todos os clientes conectados, permitindo o roteamento eficiente de mensagens públicas e privadas.

---

# Inicialização Simplificada da Aplicação

A aplicação possui uma interface central de gerenciamento chamada `interfacePrincipal`, responsável por controlar todo o sistema distribuído de forma visual.

Para iniciar o projeto completo:

```bash
java Trabalho2.interfacePrincipal
```

Através desta interface é possível:

- Iniciar o servidor TCP
- Finalizar o servidor
- Criar múltiplos clientes gráficos
- Monitorar logs do sistema em tempo real

Essa abordagem facilita os testes e demonstrações do sistema distribuído, centralizando o controle da aplicação em uma única janela.

---
# Compilação Manual do Projeto

Caso deseje executar os módulos individualmente via terminal, primeiro compile todos os arquivos:

```bash
javac Trabalho2/*.java
```
Ou
```bash
javac -d . *.java
```

---

# Execução Manual dos Componentes

## 1. Executar o Servidor

```bash
java Trabalho2.servidor_TCP
```

O servidor escutará conexões TCP na porta:

```text
1234
```

---

## 2. Executar Cliente Gráfico

```bash
java Trabalho2.telaInterface
```

---

## 3. Executar Cliente via Terminal (CLI)

```bash
java Trabalho2.Cliente_TCP
```

---

# Comandos Disponíveis no Chat

Os comandos abaixo podem ser utilizados tanto no cliente de terminal quanto na interface gráfica.

---

## Listar Usuários Conectados

```text
/usuarios
```

Retorna uma lista contendo:

- usuários conectados
- total de conexões ativas

---

## Mensagem Privada

```text
/privado [nome_do_usuario] [mensagem]
```

Exemplo:

```text
/privado Maria Olá Maria
```

Apenas o remetente e o destinatário conseguem visualizar a mensagem.

---

## Mensagem Global (Broadcast)

Qualquer mensagem que não utilize comandos especiais será enviada para todos os usuários conectados na sala.

---

# Recursos da Interface Gráfica

A interface gráfica fornece recursos adicionais para facilitar a utilização do sistema.

---

## Botão Help

Exibe uma janela com:

- comandos disponíveis
- instruções de uso
- funcionalidades do sistema

---

## Botão Usuários Online

Solicita automaticamente ao servidor a lista de clientes conectados utilizando internamente o comando:

```text
/usuarios
```

---

## Botão Desconectar

Realiza:

- encerramento seguro do socket
- remoção automática do usuário
- liberação de recursos de rede
- atualização da lista global de usuários online

---

# Funcionalidades Implementadas

- Comunicação TCP em tempo real
- Suporte a múltiplos clientes simultâneos
- Broadcast de mensagens
- Mensagens privadas
- Interface gráfica Swing
- Interface administrativa central
- Lista de usuários online
- Sistema de comandos
- Desconexão segura
- Validação de usernames duplicados
- Atualização assíncrona de mensagens
- Logs em tempo real
- Controle de múltiplos clientes
- Encerramento controlado do servidor

---

# Explicação Detalhada dos Arquivos

A aplicação está dividida em módulos bem definidos, onde o servidor gerencia as conexões de forma concorrente e os clientes tratam o envio e o recebimento de mensagens de forma assíncrona.

---

## 1. `servidor_TCP.java`

É o ponto de entrada do servidor. Responsável por inicializar o `ServerSocket` na porta especificada (`1234`) e escutar requisições de novas conexões em um loop contínuo.

Ao aceitar uma conexão através do método `accept()`, o servidor delega o canal de comunicação gerado para uma nova instância de `ThreadCliente`, garantindo que o servidor continue livre para receber novos usuários simultaneamente.

Além disso, utiliza uma estrutura global baseada em `ConcurrentHashMap` para armazenar os clientes ativos de forma segura entre múltiplas threads.

Também implementa controle de encerramento seguro do servidor, finalizando conexões ativas e liberando os recursos de rede adequadamente.

---

## 2. `ThreadCliente.java`

Implementa a interface `Runnable` e roda inteiramente no lado do servidor.

Cada cliente conectado possui uma instância própria desta thread rodando em paralelo, responsável por:

- receber mensagens
- interpretar comandos
- enviar respostas
- tratar desconexões

### Principais funcionalidades

### ✔️ Validação de Login

Verifica se o username escolhido já está em uso antes de liberar o acesso ao chat.

---

### ✔️ Broadcast

Replica mensagens públicas para todos os usuários conectados.

---

### ✔️ Mensagens Privadas

Encaminha mensagens diretamente para um destinatário específico.

Também realiza:

- validação de destinatário inexistente
- bloqueio de auto conversa
- confirmação de envio privado

---

### ✔️ Lista de Usuários

Processa o comando:

```text
/usuarios
```

gerando dinamicamente uma lista de usuários online.

---

### ✔️ Desconexão Segura

Ao detectar queda de conexão ou encerramento do socket:

- remove o cliente do mapa global
- fecha streams de entrada e saída
- libera o socket
- notifica os demais usuários

---

## 3. `Mensagem.java`

Classe modelo responsável por encapsular todos os dados trafegados pela rede.

Implementa a interface:

```java
Serializable
```

permitindo a serialização completa dos objetos para transmissão via TCP.

Cada objeto armazena:

- remetente
- destinatário
- conteúdo textual
- data e horário da criação

Além disso, sobrescreve o método:

```java
toString()
```

para fornecer formatação automática das mensagens.

Exemplo:

```text
[26/05/2026 22:59:18] João: Olá pessoal
```

ou:

```text
[26/05/2026 22:59:18] João -> Maria: Mensagem privada
```

---

## 4. `Cliente_TCP.java`

Representa a versão via terminal (CLI) do cliente.

Responsável por:

- abrir conexão TCP
- enviar mensagens digitadas
- criar a thread de escuta
- realizar autenticação do usuário

Após validar o username junto ao servidor, o cliente entra em um loop contínuo de leitura do terminal através do `Scanner`.

---

## 5. `ThreadEscuta.java`

Thread auxiliar responsável exclusivamente pela recepção de mensagens no lado cliente.

Ela permanece continuamente bloqueada no método:

```java
readObject()
```

aguardando mensagens vindas do servidor.

Quando um objeto `Mensagem` é recebido:

- a thread desperta
- processa o conteúdo
- imprime imediatamente na interface ou terminal

Esse comportamento assíncrono evita bloqueios na digitação do usuário.

---

## 6. `telaInterface.java`

Implementa a interface gráfica principal do cliente utilizando `Java Swing`.

A interface possui:

- área de chat rolável (`JTextArea`)
- campo de entrada (`JTextField`)
- botão de envio
- botão de desconexão
- botão Help
- botão Usuários Online

Internamente, utiliza uma thread assíncrona dedicada à recepção de mensagens em tempo real, permitindo atualização contínua da interface sem travamentos.

A classe também realiza:

- envio de comandos
- comunicação via sockets de objetos
- encerramento seguro da conexão TCP

---

## 7. `interfacePrincipal.java`

Implementa uma interface gráfica administrativa responsável pelo gerenciamento central do sistema distribuído.

Seu objetivo é facilitar:

- inicialização do servidor
- encerramento do servidor
- criação dinâmica de clientes
- monitoramento de eventos do sistema

A interface possui:

- botão para iniciar servidor
- botão para finalizar servidor
- campo de criação de usuários
- painel de logs em tempo real

O servidor é executado em uma thread separada da interface gráfica, permitindo controle assíncrono sem bloqueios da GUI principal.

Essa abordagem centraliza completamente o gerenciamento do sistema distribuído em uma única aplicação.

---

# Tecnologias Utilizadas

- Java SE 8+
- Java Swing
- Sockets TCP
- Programação Concorrente
- Multithreading
- Serializable
- ObjectInputStream
- ObjectOutputStream
- ConcurrentHashMap
- Arquitetura Cliente-Servidor

---

# Conceitos Aplicados

- Sistemas Distribuídos
- Comunicação Cliente-Servidor
- Comunicação TCP/IP
- Threads
- Concorrência
- Comunicação Assíncrona
- Serialização de Objetos
- Compartilhamento Seguro de Recursos
- Gerenciamento de Conexões
- Encapsulamento
- Programação Orientada a Objetos

---

# Autores

- Bruno Nunes da Silva
- Entony Cesar Siminhuk

Disciplina: Sistemas Distribuídos / Programação Paralela  
UTFPR — Universidade Tecnológica Federal do Paraná