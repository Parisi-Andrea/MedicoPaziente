package com.example.andre.medicopaziente;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Annalisa on 29/08/2016.
 */
public class DetailsDocActivity extends AppCompatActivity {
    Utils u = new Utils();
    DatabaseHelper db = new DatabaseHelper(this);

    static final String descrizione_prescrizione = "Richiesta prescrizione farmaco: ";
    static final String descrizione_visita_spec = "Richiesta una visita specialistica in ";
    static final String descrizione_visita = "Richiesta una visita di controllo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Richiesta item_richiesta = intent.getParcelableExtra("ITEM_CLICKED");
        String tipo = item_richiesta.getTipo();
        String stato = item_richiesta.getStato();
        String data_richiesta = item_richiesta.getData_richiesta();
        String note_richiesta = item_richiesta.getNote_richiesta();
        String nome_farmaco = item_richiesta.getNome_farmaco();
        int quantita = item_richiesta.getQuantita_farmaco();

        if (tipo.equals("Prescrizione")) {
            setContentView(R.layout.details_doc_prescrizione);

            TextView txttipo = (TextView) findViewById(R.id.titolo_dett);
            TextView txtdescrizione = (TextView) findViewById(R.id.descrizione_dett);
            TextView txtdata = (TextView) findViewById(R.id.data_ora_dett);
            TextView txtstato = (TextView) findViewById(R.id.stato_richiesta_dett);
            TextView txtnote = (TextView) findViewById(R.id.note_richiesta_dett);
            ImageView imageView = (ImageView) findViewById(R.id.immagine_dett);

            imageView.setImageResource(R.drawable.pill_icon);
            TextView txtfarmaco = (TextView) findViewById(R.id.farmaco_dett);
            TextView txtquant = (TextView) findViewById(R.id.quant_farmaco_dett);

            txttipo.setText(tipo);
            txtdescrizione.setText(descrizione_prescrizione + nome_farmaco);
            txtdata.setText(data_richiesta);
            if (stato.equals("A"))
                txtstato.setText("In Attesa");
            else if (stato.equals("C"))
                txtstato.setText("Completata");
            else if (stato.equals("R"))
                txtstato.setText("Rifiutata");
            txtfarmaco.setText(nome_farmaco);
            txtquant.setText("" + quantita);
            if (note_richiesta == null || note_richiesta.equals("") )
                txtnote.setText("- -");
            else
                txtnote.setText(note_richiesta);

        } else {
            setContentView(R.layout.details_doc_visita);

            TextView txttipo = (TextView) findViewById(R.id.titolo_dett);
            TextView txtdescrizione = (TextView) findViewById(R.id.descrizione_dett);
            TextView txtdata = (TextView) findViewById(R.id.data_ora_dett);
            TextView txtstato = (TextView) findViewById(R.id.stato_richiesta_dett);
            TextView txtnote = (TextView) findViewById(R.id.note_richiesta_dett);
            ImageView imageView = (ImageView) findViewById(R.id.immagine_dett);

            imageView.setImageResource(R.drawable.calendar);

            txttipo.setText(tipo);
            if (tipo.equals("Visita di controllo"))
                txtdescrizione.setText(descrizione_visita);
            else
                txtdescrizione.setText(descrizione_visita_spec+nome_farmaco);
            txtdata.setText(data_richiesta);
            if (stato.equals("C")) {
                txtstato.setText("Completata");
            } else if (stato.equals("R")) {
                txtstato.setText("Rifiutata");
            }
            if (note_richiesta == null || note_richiesta.equals("") )
                txtnote.setText("- -");
            else
                txtnote.setText(note_richiesta);

        }

        TextView txtpaziente = (TextView) findViewById(R.id.paziente);
        ImageView pazienteimg = (ImageView) findViewById(R.id.paziente_img);
        final Paziente paz = getpaziente();
        if(!(paz==null)) {
            txtpaziente.setText(paz.getNome() + paz.getCognome());
            //pazienteimg.setImageBitmap(u.stringToBitmap(paz.getImage()));
        }
        if (stato.equals("C") || stato.equals("R")) {
            ViewGroup content = (ViewGroup) findViewById(R.id.riepilogo_risposta);
            getLayoutInflater().inflate(R.layout.risposta, content, true);
            String data_risposta = item_richiesta.getData_risposta();
            String note_risposta = item_richiesta.getNote_risposta();


            TextView txtnote_risp = (TextView) findViewById(R.id.note_risposta_dett);
            TextView txtdata_risp = (TextView) findViewById(R.id.data_ora_rispdett);
            txtdata_risp.setText(data_risposta);
            if (note_risposta==null || note_risposta.equals("") )
                txtnote_risp.setText("- -");
            else
                txtnote_risp.setText(note_risposta);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }

            });

        }
        ImageButton btninfo = (ImageButton) findViewById(R.id.btninfo);
        assert btninfo != null;
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailsDocActivity.this, InfoDettagliPazActivity.class);
                intent1.putExtra("Paziente", paz);
                startActivity(intent1);
            }
        });
    }
    public Paziente getpaziente() {
        Paziente elemento = new Paziente();
        elemento.setCodiceFiscale("NLSFLP94T45L378G");
        elemento.setNome("Lia");
        elemento.setCognome("Filippi");
        elemento.setDataNascita("05/12/1994");
        elemento.setLuogoNascita("Trento");
        elemento.setResidenza("via paludi, 42");
        elemento.setEmail("annalisa.filippi@mail.it");
        elemento.setNTel("0461 961361");

        return elemento;
    }

}
