package org.se2final.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import org.se2final.components.MenuBandDefault;
import org.se2final.components.MenuBandUser;

public class Landing extends VerticalLayout implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.setUp();
    }

    public void setUp(){
        final MenuBandUser menuBandUser = new MenuBandUser();

        this.addComponent(menuBandUser);

    }
}
