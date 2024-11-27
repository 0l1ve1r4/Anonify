package com.anonify.main;

import com.anonify.ui.UI;
import com.anonify.services.Services;;

public class Main {
    public static void main(String[] args) {
        Services services = new Services();
        UI userInterface = new UI(services);
        userInterface.startUI("Anonify", 580, 800);
    }
}
