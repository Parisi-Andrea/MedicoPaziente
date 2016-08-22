package com.example.andre.medicopaziente;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andre on 24/06/2016.
 */
public class Medico implements Parcelable {
    public String codiceFiscale;
    public String nome;
    public String cognome;
    public String email;
    public String nTel;
    public String password;
    public String ambulatorio;
    public String orario;
    public String image;

    protected Medico() {}
    protected Medico(Parcel in) {
        codiceFiscale = in.readString();
        nome = in.readString();
        cognome = in.readString();
        email = in.readString();
        nTel = in.readString();
        password = in.readString();
        ambulatorio = in.readString();
        orario = in.readString();
        image = in.readString();
    }

    public static final Creator<Medico> CREATOR = new Creator<Medico>() {
        @Override
        public Medico createFromParcel(Parcel in) {
            return new Medico(in);
        }

        @Override
        public Medico[] newArray(int size) {
            return new Medico[size];
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

    public String getImage() {return this.image; }
    public void setImage(String image) {this.image = image;}
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codiceFiscale);
        dest.writeString(nome);
        dest.writeString(cognome);
        dest.writeString(email);
        dest.writeString(nTel);
        dest.writeString(password);
        dest.writeString(ambulatorio);
        dest.writeString(orario);
        dest.writeString(image);
    }
}
