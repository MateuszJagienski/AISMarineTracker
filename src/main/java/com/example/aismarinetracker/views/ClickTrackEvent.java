package com.example.aismarinetracker.views;

import com.vaadin.flow.component.ComponentEvent;

public class ClickTrackEvent extends ComponentEvent<PopupShip> {
    public ClickTrackEvent(PopupShip source, boolean fromClient) {
        super(source, fromClient);
    }
}
