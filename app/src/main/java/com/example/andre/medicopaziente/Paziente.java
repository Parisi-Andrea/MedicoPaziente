package com.example.andre.medicopaziente;

/**
 * Created by andre on 24/06/2016.
 */
public class Paziente {
    public String codiceFiscale;
    public String nome;
    public String cognome;
    public String dataNascita;
    public String luogoNascita;
    public String residenza;
    public String email;
    public String nTel;
    public String password;
    public String medico;

    public String getCodiceFiscale()
    {
        return this.codiceFiscale;
    }
    public void setCodiceFiscale(String codiceFiscale)
    {
        this.codiceFiscale = codiceFiscale;
    }
    public String getNome()
    {
        return this.nome;
    }
    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getCognome()
    {
        return this.cognome;
    }
    public void setCognome(String cognome)
    {
        this.cognome = cognome;
    }
    public String getDataNascita()
    {
        return this.dataNascita;
    }
    public void setDataNascita(String dataNascita)
    {
        this.dataNascita = dataNascita;
    }
    public String getLuogoNascita()
    {
        return this.luogoNascita;
    }
    public void setLuogoNascita(String luogoNascita)
    {
        this.luogoNascita = luogoNascita;
    }

    public String getEmail()
    {
        return this.email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getNTel()
    {
        return this.nTel;
    }
    public void setNTel(String nTel)
    {
        this.nTel = nTel;
    }
    public String getMedico()
    {
        return this.medico;
    }
    public void setMedico(String medico)
    {
        this.medico = medico;
    }

}
