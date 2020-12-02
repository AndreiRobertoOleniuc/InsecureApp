/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.insecureapp.controller;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author Michael Schneider <michael.schneider@bbbaden.ch>
 */
@Named(value = "searchController")
@RequestScoped
public class SearchController implements Serializable {

    @Size(min = 2, message = "Eingabe zu klein")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Bitte gebe richtige Zeichen ein")
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
