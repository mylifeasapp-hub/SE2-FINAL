package org.se2final.gui.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Views;
import org.se2final.gui.ui.views.*;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Title("CARPOOL by CarLook Ltd. //dprost2s")
@PreserveOnRefresh
public class MyUI extends UI {
    private Navigator navi;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(900);

        navi = new Navigator( this , this);
        VaadinSession.getCurrent().setAttribute(Roles.EXTERN, 0);

        navi.addView(Views.START, Main.class );
        navi.addView( Views.LANDING, Landing.class);
        navi.addView( Views.PROFILE, Profile.class);
        navi.addView( Views.RESERVATIONS, Reservations.class);
        navi.addView( Views.CARS, MyCars.class);



        if(VaadinSession.getCurrent().getAttribute(Roles.CURRENT) != null) UI.getCurrent().getNavigator().navigateTo( Views.LANDING );
        else UI.getCurrent().getNavigator().navigateTo( Views.START );
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}


