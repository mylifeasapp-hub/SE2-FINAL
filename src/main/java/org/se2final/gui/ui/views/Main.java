package org.se2final.gui.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import org.se2final.gui.ui.gui.components.Footer;
import org.se2final.gui.ui.gui.components.MenuBandDefault;

import java.io.File;

public class Main extends VerticalLayout implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.setUp();
    }

    public void setUp(){
        final VerticalLayout layout = new VerticalLayout();


        final MenuBandDefault menuBandDefault = new MenuBandDefault();

        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/carpool.png"));
        Image image = new Image("", resource);
        image.setWidth(800, Unit.PIXELS);

        Footer footerBar = new Footer();

        layout.addComponents(menuBandDefault, image, footerBar);
        layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(footerBar, Alignment.BOTTOM_CENTER);

        this.addComponent(layout);
    }
}
