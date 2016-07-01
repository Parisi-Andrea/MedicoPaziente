package com.example.andre.medicopaziente;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andre on 24/06/2016.
 */
public class Paziente implements Parcelable{
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
    protected Paziente() {}
    protected Paziente(Parcel in) {
        codiceFiscale = in.readString();
        nome = in.readString();
        cognome = in.readString();
        dataNascita = in.readString();
        luogoNascita = in.readString();
        residenza = in.readString();
        email = in.readString();
        nTel = in.readString();
        password = in.readString();
        medico = in.readString();
    }

    public static final Creator<Paziente> CREATOR = new Creator<Paziente>() {
        @Override
        public Paziente createFromParcel(Parcel in) {
            return new Paziente(in);
        }

        @Override
        public Paziente[] newArray(int size) {
            return new Paziente[size];
        }
    };

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
    public String getResidenza()
    {
        return this.residenza;
    }
    public void setResidenza(String residenza)
    {
        this.residenza = residenza;
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
    public String getPassword()
    {
        return this.password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codiceFiscale);
        dest.writeString(nome);
        dest.writeString(cognome);
        dest.writeString(dataNascita);
        dest.writeString(luogoNascita);
        dest.writeString(residenza);
        dest.writeString(email);
        dest.writeString(nTel);
        dest.writeString(password);
        dest.writeString(medico);
    }
}
