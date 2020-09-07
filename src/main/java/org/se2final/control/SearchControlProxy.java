package org.se2final.control;

import org.se2final.model.objects.dao.SearchI;
import org.se2final.model.objects.dto.Cars;

import java.sql.SQLException;
import java.util.List;

public class SearchControlProxy implements SearchI {

    private SearchControl searchControl;

    public SearchControlProxy(){
        this.searchControl = SearchControl.getInstance();
    }

    @Override
    public List<Cars> searchCars(String search []) throws SQLException {
        return searchControl.searchCars(search);
    }

}
