package com.example.aismarinetracker;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AisMarineTrackerApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(AisMarineTrackerApplication.class, args);
    }

}
