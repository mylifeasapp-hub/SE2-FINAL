package org.se2final;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import org.se2final.service.konstanten.Roles;
import org.se2final.service.konstanten.Views;
import org.se2final.views.Landing;
import org.se2final.views.Main;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    private Navigator navi;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(900);

        navi = new Navigator( this , this);
        VaadinSession.getCurrent().setAttribute(Roles.CURRENT, "");

        navi.addView(Views.START, Main.class );
        navi.addView( Views.LANDING, Landing.class);



        if(VaadinSession.getCurrent().getAttribute(Roles.CURRENT) != "") UI.getCurrent().getNavigator().navigateTo( Views.LANDING );
        else UI.getCurrent().getNavigator().navigateTo( Views.START );
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
