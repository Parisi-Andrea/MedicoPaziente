package com.example.andre.medicopaziente;

/**
 * Created by andre on 06/05/2016.
 */
public class Richiesta {

    public Integer idRichiesta;
    public String stato;
    public String tipo;
    public String data_richiesta;
    public String note_richiesta;
    public String nome_farmaco;
    public int quantita_farmaco;
    public String data_risposta;
    public String note_risposta;
    public String cf_paziente;
    public String cf_medico;

    public Integer getIdRichiesta()
    {
        return idRichiesta;
    }
    public void setIdRichiesta(Integer idRichiesta)
    {
        this.idRichiesta = idRichiesta;
    }
    public String getStato()
    {
        return stato;
    }
    public void setStato(String stato)
    {
        this.stato = stato;
    }
    public String getTipo()
    {
        return tipo;
    }
    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }
    public String getData_richiesta()
    {
        return data_richiesta;
    }
    public void setData_richiesta(String data_richiesta)
    {
        this.data_richiesta = data_richiesta;
    }
    public String getNote_richiesta()
    {
        return note_richiesta;
    }
    public void setNote_richiesta(String note_richiesta)
    {
        this.note_richiesta = note_richiesta;
    }
    public String getNome_farmaco()
    {
        return nome_farmaco;
    }
    public void setNome_farmaco(String nome_farmaco)
    {
        this.nome_farmaco = nome_farmaco;
    }
    public int getQuantita_farmaco()
    {
        return quantita_farmaco;
    }
    public void setQuantita_farmaco(int quantita_farmaco) {
        this.quantita_farmaco = quantita_farmaco;
    }
    public String getData_risposta()
    {
        return data_risposta;
    }
    public void setData_risposta(String data_risposta)
    {
        this.data_risposta = data_risposta;
    }
    public String getNote_risposta()
    {
        return note_risposta;
    }
    public void setNote_risposta(String note_risposta)
    {
        this.note_risposta = note_risposta;
    }
    public String getCf_paziente()
    {
        return cf_paziente;
    }
    public void setCf_paziente(String cf_paziente)
    {
        this.cf_paziente = cf_paziente;
    }
    public String getCf_medico()
    {
        return cf_medico;
    }
    public void setCf_medico(String cf_medico)
    {
        this.cf_medico = cf_medico;
    }

//    @Override
//    public String toString()
//    {
//
//    }


}
