package com.carpool.application.views.login;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.carpool.application.views.main.MainView;

@Route(value = "Login", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Login")
@CssImport("styles/views/login/login-view.css")
public class LoginView extends Div {

    public LoginView() {
        setId("login-view");
        add(new Label("Content placeholder"));
    }

}
