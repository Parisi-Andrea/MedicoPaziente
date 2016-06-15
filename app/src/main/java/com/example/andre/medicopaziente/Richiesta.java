package com.example.andre.medicopaziente;

/**
 * Created by andre on 06/05/2016.
 */
public class Richiesta {

    public String idRichiesta;
    public String image;
    public String idMedico;
    public String idPaziente;
    public String nome;
    public String cognome;
    public String richiesta;
    public String descrizione;

    public String getIdRichiesta()
    {
        return idRichiesta;
    }

    public String getIdMedico()
    {
        return idMedico;
    }

    public  void setIdMedico(String idMedico)
    {
        this.idMedico = idMedico;
    }
    public String idPaziente()
    {
        return idPaziente;
    }
    public  void setIdPaziente(String idPaziente)
    {
        this.idPaziente = idPaziente;
    }
    public String getNome()
    {
        return nome;
    }
    public  void setNome(String nome)
    {
        this.nome = nome;
    }
    public String getCognome()
    {
        return cognome;
    }
    public  void setCognome(String cognome)
    {
        this.cognome = cognome;
    }
    public String getRichiesta()
    {
        return richiesta;
    }
    public  void setRichiesta(String richiesta)
    {
        this.richiesta = richiesta;
    }
    public String getDescrizione()
    {
        return descrizione;
    }
    public  void setDescrizione(String descrizione)
    {
        this.descrizione = descrizione;
    }
    @Override
    public String toString()
    {
        return idRichiesta + ": " + nome + " " + cognome + " " + richiesta + " " + descrizione;
    }


}
