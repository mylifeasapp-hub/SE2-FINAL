package org.se2final.components;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

public class MenuBandDefault extends HorizontalLayout {
    public MenuBandDefault(){
        HorizontalLayout buttonPannel = new HorizontalLayout();
        this.setSizeFull();
        this.setHeight("100px");
        this.addStyleName("border_bottom");

        Label pageTitle = new Label("<div style=\"font-size:50px\">CARPOOL by CarLook Ltd.</div>", ContentMode.HTML);

        Button buttonRegister = new Button("Registrieren");
        Button buttonLogin = new Button("Anmelden");

        buttonPannel.addComponents(buttonLogin, buttonRegister);
        this.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        this.addComponents(pageTitle,buttonPannel);
        this.setComponentAlignment(pageTitle, Alignment.MIDDLE_LEFT);

        //Click Listener
        LoginWindow loginPanel = new LoginWindow();
        buttonLogin.addClickListener(event -> UI.getCurrent().addWindow(loginPanel));
        RegisterWindow registerPanel = new RegisterWindow();
        buttonRegister.addClickListener(event -> UI.getCurrent().addWindow(registerPanel));
    }
}
