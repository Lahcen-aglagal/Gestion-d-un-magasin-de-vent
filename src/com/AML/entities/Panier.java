package com.AML.entities;
import com.AML.entities.Produits;
import java.util.ArrayList;
import java.util.List;
public class Panier {
    private List<LignePanier> items=new ArrayList();

    public List<LignePanier> getItems() {
        return items;
    }

    public void setItems(List<LignePanier> items) {
        this.items = items;
    }
    
}
