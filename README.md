<div align="center">
  <img src="res/logo-no-bg-centralized.png" alt="AnoNify" style="width: 50%;">

  <p align="center">
    <strong>Open-source onion-secured one-to-one communication platform.</strong>    
  </p>

  <hr>

  ![Commit activity](https://img.shields.io/github/commit-activity/m/iyksh/AnoNify)
  ![GitHub top language](https://img.shields.io/github/languages/top/iyksh/AnoNify?logo=java&label=)
  [![GitHub license](https://img.shields.io/github/license/iyksh/AnoNify)](https://github.com/iyksh/AnoNify/LICENSE)
  [![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](./res/README_PTBR.md)

</div>

A secure and anonymous chat platform designed for private one-to-one communication. with a focus on privacy, AnoNify routes all communications via the Tor network.

- **🔐 End-to-end encryption**: All messages are secured using advanced encryption protocols.
- **🧅 Onion Routing**: Communication is routed through Tor for robust anonymity.
- **📲 Lightweight Interface**: Simple and user-friendly interface for seamless communication.
- **🚀 Open-source**: Fully transparent and community-driven development.

## 📚 Documentation Index

### **Backlog and Project Description**
- [📌 Product Backlog](/docs/backlog/backlog.md)
- [📋 First Sprint Backlog](/docs/backlog/SprintBacklog.md)

### **Diagrams**
- [📐 UML Diagrams](/docs/uml/class_diagram.md)

### **Prototypes**
- [🖌️ UI Prototypes](/docs/diagrams/diagrams.md)

## 🚀 Installation & Usage

### Prerequisites
- **Java JDK 11 or higher**
- **Tor service** (installed and running on your system)
- A **modern IDE** such as IntelliJ IDEA or Eclipse for development.

### Setting Up
1. Clone the repository:
   ```bash
   git clone https://github.com/iyksh/AnoNify.git
   cd AnoNify
   ```
2. Configure Tor:
   - Ensure `tor` is running and accessible through `ControlPort` (default: 9051) and `SOCKSPort` (default: 9050).
   - Optional: Use the `torrc` file provided in the `/res/config` directory for quick setup.

3. Build and Run:
   ```bash
   ./gradlew build
   java -jar build/libs/AnoNify.jar
   ```

4. Access the interface and start chatting anonymously.

## 🌟 Acknowledgements

- This project was inspired by [dontTrust](https://github.com/Alvorada9999/dont_trust).
