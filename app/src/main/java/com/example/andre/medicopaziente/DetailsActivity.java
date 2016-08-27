package com.example.andre.medicopaziente;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.andre.medicopaziente.R;

/**
 * Created by Annalisa on 11/08/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Richiesta item_richiesta = intent.getParcelableExtra("ITEM_CLICKED");
        String tipo = item_richiesta.getTipo();
        if(tipo == "Prescrizione"){
            setContentView(R.layout.activity_details);

            String nome_farmaco = item_richiesta.getNome_farmaco();
            String data_richiesta = item_richiesta.getData_richiesta();
            String note_richiesta = item_richiesta.getNote_richiesta();
            String stato = item_richiesta.getStato();
            int quantita = item_richiesta.getQuantita_farmaco();

            TextView txttipo = (TextView) findViewById(R.id.titolo_dett);
            TextView txtdescrizione = (TextView) findViewById(R.id.descrizione_dett);
            TextView txtdata = (TextView) findViewById(R.id.data_ora_dett);
            TextView txtstato = (TextView) findViewById(R.id.stato_richiesta_dett);
            TextView txtfarmaco = (TextView) findViewById(R.id.farmaco_dett);
            TextView txtquant = (TextView) findViewById(R.id.quant_farmaco_dett);
            TextView txtnote = (TextView) findViewById(R.id.note_richiesta_dett);

            txttipo.setText(tipo);
            txtdescrizione.setText("Al medico è stato richiesto il farmaco:"+nome_farmaco);
            txtdata.setText(data_richiesta);
            if(stato == "A")
                txtstato.setText("In Attesa");
            else if(stato == "A")
                txtstato.setText("Completata");
            else if(stato == "A")
                txtstato.setText("Rifiutata");
            txtfarmaco.setText(nome_farmaco);
            txtquant.setText(""+quantita);
            txtnote.setText(note_richiesta);

        }
        else{
            setContentView(R.layout.activity_details);

            String nome_farmaco = item_richiesta.getNome_farmaco();
            String data_richiesta = item_richiesta.getData_richiesta();
            String note_richiesta = item_richiesta.getNote_richiesta();
            String stato = item_richiesta.getStato();
            int quantita = item_richiesta.getQuantita_farmaco();

            TextView txttipo = (TextView) findViewById(R.id.titolo_dett);
            TextView txtdescrizione = (TextView) findViewById(R.id.descrizione_dett);
            TextView txtdata = (TextView) findViewById(R.id.data_ora_dett);
            TextView txtstato = (TextView) findViewById(R.id.stato_richiesta_dett);
            TextView txtfarmaco = (TextView) findViewById(R.id.farmaco_dett);
            TextView txtquant = (TextView) findViewById(R.id.quant_farmaco_dett);
            TextView txtnote = (TextView) findViewById(R.id.note_richiesta_dett);

            txttipo.setText(tipo);
            txtdescrizione.setText("Al medico è stato richiesto il farmaco:"+nome_farmaco);
            txtdata.setText(data_richiesta);
            if(stato == "A")
                txtstato.setText("In Attesa");
            else if(stato == "A")
                txtstato.setText("Completata");
            else if(stato == "A")
                txtstato.setText("Rifiutata");
            txtfarmaco.setText(nome_farmaco);
            txtquant.setText(""+quantita);
            txtnote.setText(note_richiesta);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
