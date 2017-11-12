package com.example.vogel.testlist.util;


/**
 * Created by vogel on 23/10/17.
 */
public class Entry{
    private Integer id;
    private String date;
    private String titre;
    private String texte;
    private String image;

    public Entry(){}
    public Entry(String date, String titre, String texte) {
        this.date = date;
        this.titre = titre;
        this.texte = texte;
    }

    public Entry(String date, String titre, String texte, String image) {
        this.date = date;
        this.titre = titre;
        this.texte = texte;
        this.image = image;
    }

    public Entry(Integer id, String date, String titre, String texte) {
        this(date, titre, texte);
        this.id = id;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getImage() {return image; }

    public void setImage(String image) { this.image = image;}

    @Override
    public boolean equals(Object o){
        com.example.vogel.testlist.util.Entry e = (com.example.vogel.testlist.util.Entry)o;
        if(!e.getId().equals(id))
            return false;
        if(!e.getDate().equals(date))
            return false;
        if(!e.getTexte().equals(texte))
            return false;
        if(!e.getTitre().equals(titre))
            return false;
        return true;
    }
}
