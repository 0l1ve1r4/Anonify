#!/bin/bash

# Detecta o gerenciador de pacotes (pacman para Arch, apt para Debian/Ubuntu)
if command -v pacman &>/dev/null; then
    echo "Sistema detectado: Arch Linux"
    INSTALL_CMD="sudo pacman -Syu --noconfirm tor"
elif command -v apt &>/dev/null; then
    echo "Sistema detectado: Debian/Ubuntu"
    INSTALL_CMD="sudo apt update && sudo apt install -y tor"
else
    echo "Erro: Gerenciador de pacotes não suportado."
    exit 1
fi

# Instala o Tor
echo "Instalando o Tor..."
eval $INSTALL_CMD

# Configura o serviço oculto no arquivo torrc
echo "Configurando serviço oculto..."
sudo bash -c 'cat >> /etc/tor/torrc <<EOF

# Configuração do Hidden Service
HiddenServiceDir /var/lib/tor/hidden_service/
HiddenServicePort 12345 127.0.0.1:12345

EOF'

# Reinicia o serviço Tor para aplicar as alterações
echo "Reiniciando o Tor..."
sudo systemctl restart tor

# Exibe o endereço .onion gerado
echo "O endereço .onion do serviço oculto é:"
sudo cat /var/lib/tor/hidden_service/hostname
