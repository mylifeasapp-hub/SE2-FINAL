package org.se2final.gui.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.se2final.control.ImageUploader;
import org.se2final.gui.ui.gui.components.CarAddWindow;
import org.se2final.gui.ui.gui.components.MenuBandUser;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Views;
import org.se2final.model.objects.dao.CarsDAO;
import org.se2final.model.objects.dto.Cars;
import org.se2final.model.objects.dto.User;


import java.sql.SQLException;
import java.util.List;

public class MyCars extends VerticalLayout implements View {
    User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);


    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //Wenn eingeloggt -> Landing Page, sonst -> Startseite
        if(user == null){
            UI.getCurrent().getNavigator().navigateTo(Views.START);
        }else {
            this.setUp();
        }
    }

    public void setUp(){
        final MenuBandUser menuBandUser = new MenuBandUser();

        Button addCar = new Button("Auto hinzufügen");

        Grid<Cars> carsGrid = new Grid<>();
        carsGrid.setSizeUndefined();
        List<Cars> listedCars = null;
        try {
            listedCars = CarsDAO.getInstance().carsByUser(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        carsGrid.setCaptionAsHtml(true);
        carsGrid.setWidth("80%");
        carsGrid.setCaption("Meine Autos:");
        carsGrid.setItems(listedCars);
        carsGrid.setBodyRowHeight(320);
        carsGrid.setHeightByRows(!listedCars.isEmpty() ? listedCars.size() : 1);

        carsGrid.addComponentColumn(c -> {
            Image image = new Image("", ImageUploader.carPicture(c.getCarPicture()));
            image.setWidth(300, Unit.PIXELS);
            return image;
        }).setCaption("Bild:").setWidth(300);
        carsGrid.addColumn(Cars::getCarBrand).setCaption("Marke:");
        carsGrid.addColumn(Cars::getCarYear).setCaption("Erstzulassung:");
        carsGrid.addColumn(Cars::getCarDescription, new HtmlRenderer()).setCaption("Beschreibung:");

        this.addComponents(menuBandUser, addCar, carsGrid);
        this.setComponentAlignment(addCar, Alignment.TOP_RIGHT);
        this.setComponentAlignment(carsGrid, Alignment.TOP_CENTER);

        addCar.addClickListener(event -> {
            CarAddWindow addCarWindow = new CarAddWindow();
            UI.getCurrent().addWindow(addCarWindow);
        });
    }
}
