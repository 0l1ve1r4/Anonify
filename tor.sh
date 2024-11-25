#!/bin/bash

if command -v pacman &>/dev/null; then
    echo "System detected: Arch Linux"
    INSTALL_CMD="sudo pacman -Syu --noconfirm tor"
elif command -v apt &>/dev/null; then
    echo "System detected: Debian/Ubuntu"
    INSTALL_CMD="sudo apt update && sudo apt install -y tor"
else
    echo "Error: Unsupported package manager."
    exit 1
fi

echo "Installing Tor..."
eval $INSTALL_CMD

read -p "Do you want to configure hidden services? (y/n): " CONFIGURE

if [[ $CONFIGURE =~ ^[Yy]$ ]]; then
    echo "Configuring hidden service..."
    sudo bash -c 'cat >> /etc/tor/torrc <<EOF

# Hidden Service Configuration
HiddenServiceDir /var/lib/tor/hidden_service/
HiddenServicePort 12345 127.0.0.1:12345

EOF'

    echo "Restarting Tor..."
    sudo systemctl restart tor

    echo "The .onion address for the hidden service is:"
    sudo cat /var/lib/tor/hidden_service/hostname
else
    echo "Hidden service configuration skipped."
fi
