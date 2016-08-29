package com.example.andre.medicopaziente;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        String stato = item_richiesta.getStato();
        String data_richiesta = item_richiesta.getData_richiesta();
        String note_richiesta = item_richiesta.getNote_richiesta();

        if (tipo.equals("Prescrizione")) {
            setContentView(R.layout.details_richiesta_prescrizione);

            TextView txttipo = (TextView) findViewById(R.id.titolo_dett);
            TextView txtdescrizione = (TextView) findViewById(R.id.descrizione_dett);
            TextView txtdata = (TextView) findViewById(R.id.data_ora_dett);
            TextView txtstato = (TextView) findViewById(R.id.stato_richiesta_dett);
            TextView txtnote = (TextView) findViewById(R.id.note_richiesta_dett);
            ImageView imageView = (ImageView) findViewById(R.id.immagine_dett);

            String nome_farmaco = item_richiesta.getNome_farmaco();
            int quantita = item_richiesta.getQuantita_farmaco();


            imageView.setImageResource(R.drawable.pill_icon);
            TextView txtfarmaco = (TextView) findViewById(R.id.farmaco_dett);
            TextView txtquant = (TextView) findViewById(R.id.quant_farmaco_dett);

            txttipo.setText(tipo);
            txtdescrizione.setText("Al medico è stato richiesto il farmaco:" + nome_farmaco);
            txtdata.setText(data_richiesta);
            if (stato.equals("A"))
                txtstato.setText("In Attesa");
            else if (stato.equals("C"))
                txtstato.setText("Completata");
            else if (stato.equals("R"))
                txtstato.setText("Rifiutata");
            txtfarmaco.setText(nome_farmaco);
            txtquant.setText("" + quantita);
            if (note_richiesta == "" | note_richiesta == null)
                txtnote.setText("- -");
            else
                txtnote.setText(note_richiesta);

        } else {
            setContentView(R.layout.details_richiesta_visita);

            TextView txttipo = (TextView) findViewById(R.id.titolo_dett);
            TextView txtdescrizione = (TextView) findViewById(R.id.descrizione_dett);
            TextView txtdata = (TextView) findViewById(R.id.data_ora_dett);
            TextView txtstato = (TextView) findViewById(R.id.stato_richiesta_dett);
            TextView txtnote = (TextView) findViewById(R.id.note_richiesta_dett);
            ImageView imageView = (ImageView) findViewById(R.id.immagine_dett);

            imageView.setImageResource(R.drawable.calendar);

            txttipo.setText(tipo);
            txtdescrizione.setText("Al medico è stata richiesta una visita");
            txtdata.setText(data_richiesta);
            if (stato.equals("C")) {
                txtstato.setText("Completata");
            }else if (stato.equals("R")) {
                txtstato.setText("Rifiutata");
            }
            if (note_richiesta.equals("") | note_richiesta == null)
                txtnote.setText("- -");
            else
                txtnote.setText(note_richiesta);

        }
        if (stato.equals("C") | stato.equals("R")){
            ViewGroup content = (ViewGroup) findViewById(R.id.riepilogo_risposta);
            getLayoutInflater().inflate(R.layout.risposta, content, true);
            String data_risposta = item_richiesta.getData_risposta();
            String note_risposta = item_richiesta.getNote_risposta();


            TextView txtnote_risp = (TextView) findViewById(R.id.note_risposta_dett);
            TextView txtdata_risp = (TextView) findViewById(R.id.data_ora_rispdett);
            txtdata_risp.setText(data_risposta);
            if (note_risposta == "" | note_risposta == null)
                txtnote_risp.setText("- -");
            else
                txtnote_risp.setText(note_risposta);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }

            });

        }
    }
}
