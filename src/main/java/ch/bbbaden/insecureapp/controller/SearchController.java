/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.insecureapp.controller;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Michael Schneider <michael.schneider@bbbaden.ch>
 */
@Named(value = "searchController")
@RequestScoped
public class SearchController implements Serializable {
    private String searchString;
    
    public String search() {
      return "searchResult";  
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    
    
}
