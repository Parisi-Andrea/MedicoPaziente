package com.example.andre.medicopaziente;

/**
 * Created by andre on 24/06/2016.
 */
public class Medico {
    public String codiceFiscale;
    public String nome;
    public String cognome;
    public String email;
    public String nTel;
    public String password;
    public String ambulatorio;
    public String orario;

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

    public String getPassword()
    {
        return this.password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getAmbulatorio()
    {
        return this.ambulatorio;
    }
    public void setAmbulatorio(String ambulatorio)
    {
        this.ambulatorio = ambulatorio;
    }

    public String getOrario()
    {
        return this.orario;
    }
    public void setOrario(String orario)
    {
        this.orario  = orario;
    }
}
