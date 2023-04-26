package com.projet.dasi.serialisations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.projet.dasi.model.Astrologue;
import com.projet.dasi.model.Cartomancien;
import com.projet.dasi.model.Client;
import com.projet.dasi.model.Consultation;
import com.projet.dasi.model.Employe;
import com.projet.dasi.model.Genre;
import com.projet.dasi.model.Medium;
import com.projet.dasi.model.ProfilAstral;
import com.projet.dasi.model.Spirite;
import com.projet.dasi.model.Utilisateur;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConsultationSerialisation extends Serialisation {
    
    @Override
    public void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        // Initialiser le container
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        JsonObject container = new JsonObject();
        
        // Récupérer les attributs de la requête
        Consultation consultation = (Consultation)request.getAttribute("consultation");
        
        // Populer le container
        container.addProperty("consultation", consultation != null);
        
        if (consultation != null) {
        
            // Sérialiser le client de la consultation
            Client client = consultation.getClient();
            ProfilAstral profil = client.getProfilAstral();
            JsonObject clientData = new JsonObject();
            JsonObject profilData = new JsonObject();
            
            // Format phone
            String telephone = client.getTelephone();
            String formattedTelephone = "";
            for (int i = 0; i < telephone.length(); i++) {
                formattedTelephone += telephone.charAt(i) + ((i % 2 == 1) ? " " : "");
            }
            
            profilData.addProperty("signeAstro", profil.getSigneAstro());
            profilData.addProperty("signeChinois", profil.getSigneChinois());
            profilData.addProperty("couleur", profil.getCouleur());
            profilData.addProperty("animalTotem", profil.getAnimalTotem());
            clientData.addProperty("prenom", client.getPrenom());
            clientData.addProperty("nom", client.getNom());
            clientData.addProperty("age", client.getAge());
            clientData.addProperty("telephone", formattedTelephone);
            clientData.add("profil", profilData);
            container.add("client", clientData);

            // Sérialiser le médium de la consultation
            Medium medium = consultation.getMedium();
            //JsonObject mediumData = (JsonObject)gson.toJsonTree(medium);
            JsonObject mediumData = new JsonObject();
            mediumData.addProperty("denomination", medium.getDenomination());
            mediumData.addProperty("genre", medium.getGenre().toString());
            mediumData.addProperty("id", medium.getId());
            mediumData.addProperty("presentation", medium.getPresentation());
            if(medium instanceof Astrologue){
                mediumData.addProperty("formation", ((Astrologue)medium).getFormation());
                mediumData.addProperty("promotion", ((Astrologue)medium).getPromotion());
                mediumData.addProperty("type", "Astrologue");
            }
            else if(medium instanceof Spirite){
                mediumData.addProperty("support", ((Spirite)medium).getSupport());
                mediumData.addProperty("type", "Spirite");
            }
            else if(medium instanceof Cartomancien){
                mediumData.addProperty("type", "Cartomancien");
            }
            container.add("medium", mediumData);
            
            // Sérialiser le commentaire et l'état
            container.addProperty("commentaire", consultation.getCommentaire());
            container.addProperty("etat", consultation.getEtat().toString());
        
        }
        
        // L'écrire sur le flux de sortie
        PrintWriter out = this.getWriter(response);
        gson.toJson(container, out);
        out.close();
        
    }
    
}
