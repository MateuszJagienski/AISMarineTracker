package pl.ais.aismarinetracker.views;

import com.vaadin.flow.component.ComponentEvent;

public class CloseEvent extends ComponentEvent<PopupShip> {
    public CloseEvent(PopupShip source, boolean fromClient) {
        super(source, fromClient);
    }
}
