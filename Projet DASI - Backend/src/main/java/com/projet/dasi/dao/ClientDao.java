package com.projet.dasi.dao;

import java.util.List;
import javax.persistence.TypedQuery;
import com.projet.dasi.model.Client;

public class ClientDao {
    
    public ClientDao() {}
    
    /* Chercher tous les clients de la DB */
    public List<Client> chercherTous() {
        String s = "SELECT c FROM Client c ORDER BY c.nom ASC";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(s, Client.class);
        return query.getResultList();
    }
    
    /* Mettre à jour un client dans la DB */
    public Client mettreAJour(Client client) {
        return JpaUtil.obtenirContextePersistance().merge(client);
    }
    
}
