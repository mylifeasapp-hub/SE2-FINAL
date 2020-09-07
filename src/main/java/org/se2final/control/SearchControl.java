package org.se2final.control;



import org.se2final.model.objects.dao.CarsDAO;
import org.se2final.model.objects.dao.SearchI;
import org.se2final.model.objects.dto.Cars;

import java.sql.SQLException;
import java.util.List;

public class SearchControl implements SearchI {

   public SearchControl(){
        //SQ satisfyen
    }
    private static SearchControl suche = null;

    public static SearchControl getInstance(){
        if(suche == null){
            suche = new SearchControl();
        }
        return suche;
    }

    public List<Cars> searchCars(String search []) throws SQLException {
        return CarsDAO.getInstance().searchCars(search);
    }

}
