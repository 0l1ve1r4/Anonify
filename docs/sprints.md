### **Sprint 1: Definição do modelo de processo e requisitos com foco em segurança e anonimato**
- **Atividades**:
  1. **Modelo de Processo**:
     - Estabelecer cronograma e tarefas considerando as adaptações para o uso da rede Tor.
     - Definir responsabilidades entre os integrantes.
  2. **Especificação de Requisitos**:
     - **Requisitos Funcionais**:
       - Permitir envio e recebimento de mensagens anônimas através da rede Tor.
       - Criação de canais seguros (equivalente a "salas de chat").
       - Suporte para gerenciamento de chaves para criptografia ponta a ponta.
     - **Requisitos Não-Funcionais**:
       - Privacidade: Garantir que nenhum dado sensível seja acessível por terceiros.
       - Anonimato: Roteamento das mensagens exclusivamente pela rede Tor.
       - Resiliência: Funcionamento estável mesmo em cenários de rede instável.
  3. **Estudo de Ferramentas**:
     - Selecionar bibliotecas Java para integração com a rede Tor, como **jtorctl** ou usar Tor executando como serviço.
     - Escolher protocolo de comunicação seguro, como **OMEMO** ou **Signal Protocol**.
  - **Saída**:
    - Documento de requisitos focado em segurança.
    - Cronograma detalhado.

---

### **Sprint 2: Arquitetura e detalhamento técnico**
- **Atividades**:
  1. **Histórias de Usuário**:
     - "Como usuário, quero enviar mensagens através da rede Tor para manter meu anonimato."
     - "Como usuário, quero que minhas mensagens sejam criptografadas ponta a ponta para evitar interceptações."
  2. **Cenários de Teste**:
     - Testar o envio de mensagens através da rede Tor.
     - Verificar que mensagens são roteadas anonimamente e com criptografia aplicada.
  3. **Projeto Arquitetural**:
     - **Diagrama de Classes**:
       - Classes principais: `TorConnector`, `MessageHandler`, `EncryptionManager`, `User`.
     - **Diagrama de Componentes**:
       - Cliente conecta à rede Tor via `TorConnector`.
       - Servidor Onion como ponto central de recepção.
     - **Diagrama de Sequência**:
       - Fluxo para envio de mensagem:
         1. Cliente conecta ao serviço Onion.
         2. Mensagem é criptografada e enviada.
         3. Destinatário descriptografa a mensagem.
  - **Saída**:
    - Diagramas UML (Classes, Componentes e Sequência).
    - Esboço técnico para integração com a rede Tor.

---

### **Sprint 3: Implementação e testes**
- **Atividades**:
  1. **Integração com Tor**:
     - Configurar e gerenciar comunicação com a rede Tor:
       - Iniciar um cliente Tor embutido ou conectar-se a um Tor em execução.
       - Configurar **Onion Services** para comunicação entre os usuários.
     - Classe `TorConnector`:
       - Interface com Tor via SOCKS5 para roteamento de mensagens.
  2. **Criptografia ponta a ponta**:
     - Implementar o uso de bibliotecas como **BouncyCastle** para criptografia AES e RSA.
     - Gerenciamento de chaves públicas/privadas.
  3. **Implementação Parcial**:
     - Backend:
       - Servidor Onion gerencia conexões dos usuários.
       - Mensagens são roteadas anonimamente e criptografadas.
     - Frontend:
       - Interface gráfica para enviar mensagens e gerenciar contatos.
  4. **Testes Automatizados**:
     - Verificar se mensagens são roteadas pela rede Tor.
     - Testar a criptografia de mensagens com JUnit.
  - **Saída**:
    - Código-fonte funcional (parcial) com suporte à rede Tor.
    - Relatório de testes e cobertura.

---

### **Plano de Implementação do Sistema**

#### **Exemplo: Configuração básica com Tor**
1. **Configuração do Cliente Tor**:
   - Inicie o Tor como processo em segundo plano (ou embutido com torrc minimalista):
     ```shell
     tor --ControlPort 9051 --SocksPort 9050
     ```

2. **Classe TorConnector**:
```java
import org.silvertunnel_ng.netlib.api.NetLayer;
import org.silvertunnel_ng.netlib.layer.tor.TorNetLayer;

public class TorConnector {
    private NetLayer netLayer;

    public TorConnector() {
        try {
            netLayer = new TorNetLayer();
            netLayer.waitUntilReady();
            System.out.println("Conectado à rede Tor.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socket connectToOnion(String onionAddress, int port) throws IOException {
        return netLayer.createNetSocket(null, null, onionAddress, port);
    }
}
```

3. **Envio de Mensagem com Criptografia**:
```java
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class EncryptionManager {
    private KeyPair keyPair;

    public EncryptionManager() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        this.keyPair = keyGen.generateKeyPair();
    }

    public String encryptMessage(String message, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }
}
```

---

### **Resultados Esperados**
Ao final, deve-se entregar:
1. **Documentação de requisitos e arquitetura** com foco em segurança.
2. **Código-fonte** funcional para troca de mensagens seguras e anônimas.
3. **Testes demonstrando privacidade** e anonimato via Tor.

Se precisar de mais detalhes sobre Tor ou implementação, posso aprofundar!