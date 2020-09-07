package org.se2final.gui.ui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.se2final.control.ImageUploader;
import org.se2final.gui.ui.gui.components.CarAddEditViewWindow;
import org.se2final.gui.ui.gui.components.MenuBandUser;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Views;
import org.se2final.model.objects.dao.CarsDAO;
import org.se2final.model.objects.dto.Cars;
import org.se2final.model.objects.dto.User;


import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyCars extends VerticalLayout implements View {
    User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
    Cars currentCar = null;


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
        addCar.setWidth("250px");
        Grid<Cars> carsGrid = new Grid<>();
        carsGrid.setSizeUndefined();
        List<Cars> listedCars = getItems();


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
        final ButtonRenderer<Cars> buttonEdit = new ButtonRenderer<>(clickEvent -> {
            try {
                currentCar = CarsDAO.getInstance().getCarByID(clickEvent.getItem().getCarID());
                CarAddEditViewWindow addCarWindow = new CarAddEditViewWindow(currentCar);
                UI.getCurrent().addWindow(addCarWindow);
                currentCar = null;
            } catch (Exception ex) {
                Logger.getLogger(Landing.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        buttonEdit.setHtmlContentAllowed(true);
        carsGrid.addColumn(cars -> VaadinIcons.EDIT.getHtml(), buttonEdit);

        final ButtonRenderer<Cars> buttonDelete = new ButtonRenderer<>(clickEvent -> {
            try {
                CarsDAO.getInstance().deleteCar(clickEvent.getItem().getCarID());
                Notification.show("Erfolg:", "Auto wurde erfolgreich gelöscht!", Notification.Type.HUMANIZED_MESSAGE);
                UI.getCurrent().getNavigator().navigateTo(Views.CARS);
            } catch (Exception ex) {
                Logger.getLogger(Landing.class.getName()).log(Level.SEVERE, null, ex);
                Notification.show("Fehler:", "Löschen konnte nicht durchgeführt werden!", Notification.Type.ERROR_MESSAGE);
            }
        });
        buttonDelete.setHtmlContentAllowed(true);
        carsGrid.addColumn(cars -> VaadinIcons.TRASH.getHtml(), buttonDelete);


        this.addComponents(menuBandUser, addCar, carsGrid);
        this.setComponentAlignment(addCar, Alignment.TOP_RIGHT);
        this.setComponentAlignment(carsGrid, Alignment.TOP_CENTER);

        addCar.addClickListener(event -> {
            CarAddEditViewWindow addCarWindow = new CarAddEditViewWindow(currentCar);
            UI.getCurrent().addWindow(addCarWindow);
        });

    }
    public List<Cars> getItems(){
        List<Cars> listedCars = null;
        try {
            listedCars = CarsDAO.getInstance().carsByUser(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listedCars;
    }
}
