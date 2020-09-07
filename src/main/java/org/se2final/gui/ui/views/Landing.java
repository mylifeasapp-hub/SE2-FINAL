package org.se2final.gui.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import org.se2final.control.ImageUploader;
import org.se2final.control.SearchControlProxy;
import org.se2final.gui.ui.gui.components.MenuBandUser;
import org.se2final.gui.ui.gui.components.ReservationWindow;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Views;
import org.se2final.model.objects.dto.Cars;
import org.se2final.model.objects.dto.User;

import javax.swing.text.html.HTML;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Landing extends VerticalLayout implements View {
    User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
    SearchControlProxy sc = new SearchControlProxy();
    Grid<Cars> carsGrid = new Grid<>();

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

        //Suchfeld
        TextField carSearchBrand = new TextField();
        carSearchBrand.setDescription("Fügen Sie einen Namen zur Suche hinzu!");
        carSearchBrand.setValue("");
        carSearchBrand.setCaption("Suchbegriff eingeben:");
        carSearchBrand.setWidth("80%");






        this.addComponents(menuBandUser, carSearchBrand);
        this.setComponentAlignment(carSearchBrand, Alignment.TOP_CENTER);

        OnTheFlySearch(carSearchBrand.getValue());
        //Change Listener
        carSearchBrand.addValueChangeListener(e-> OnTheFlySearch(carSearchBrand.getValue()));

    }

    public void OnTheFlySearch(String name){
        //Erstellung Tabelle mit Jobangeboten

        carsGrid.setSizeUndefined();
        List<Cars> listedCars = null;

        String search [] = new String[1];

        if(name.isEmpty()){
            search[0] ="";
        }
        else{
            search = name.split(" ");
        }
        try {
            listedCars = sc.searchCars(search);
        } catch (SQLException ex) {
            Logger.getLogger(Landing.class.getName()).log(Level.SEVERE, null, ex);
        }
        carsGrid.removeAllColumns();
        carsGrid.setCaptionAsHtml(true);
        carsGrid.setCaption((search[0].equals("") ? "Alle Autos:" : "Ergebnisse für: " + name) + " </span>");
        carsGrid.setHeightByRows(!listedCars.isEmpty() ? listedCars.size() : 1);
        carsGrid.setBodyRowHeight(320);
        carsGrid.setItems(listedCars);


        //Befüllung des Grids
        carsGrid.addComponentColumn(c -> {
            Image image = new Image("", ImageUploader.carPicture(c.getCarPicture()));
            image.setWidth(300, Unit.PIXELS);
            return image;
        }).setCaption("Bild:").setWidth(300);
        carsGrid.addColumn(Cars::getCarBrand).setCaption("Marke:");
        carsGrid.addColumn(Cars::getCarYear).setCaption("Erstzulassung:");
        carsGrid.addColumn(Cars::getCarDescription, new HtmlRenderer()).setCaption("Beschreibung:");

        if(user.getRolle().equals(Roles.KUNDE)){
            final ButtonRenderer<Cars> buttonView = new ButtonRenderer<>(clickEvent -> {
                try {
                    ReservationWindow resWindow = new ReservationWindow(clickEvent.getItem().getCarID());
                    UI.getCurrent().addWindow(resWindow);
                } catch (Exception ex) {
                    Logger.getLogger(Landing.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            buttonView.setHtmlContentAllowed(true);
            carsGrid.addColumn(person -> "Reservieren", buttonView);
        }

        /*final ButtonRenderer<Cars> buttonView = new ButtonRenderer<>(clickEvent -> {
            try {
                JobAngebotPopUp view = new JobAngebotPopUp(Konstanten.JOB_VIEW);
                view.createJobView(Konstanten.JOB_VIEW, clickEvent.getItem().getStelId());
                view.setModal(true);
                UI.getCurrent().addWindow(view);
            } catch (Exception ex) {
                Logger.getLogger(StellenangebotSuchenView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        buttonView.setHtmlContentAllowed(true);
        jobGrid.addColumn(person -> VaadinIcons.EYE.getHtml(), buttonView).setWidth(80);*/
        carsGrid.setWidth("80%");
        this.addComponents(carsGrid);
        this.setComponentAlignment(carsGrid, Alignment.TOP_CENTER);

    }
}