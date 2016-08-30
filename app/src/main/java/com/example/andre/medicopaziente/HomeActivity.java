package com.example.andre.medicopaziente;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Annalisa on 24/08/2016.
 */
public class HomeActivity extends BasicDrawerActivity {

    private Medico medico;
    private Paziente paziente;
    Richiesta ultimarichiestavisita_spec;
    Richiesta ultimarichiestavisita_con;
    Richiesta ultimarichiestaprescr;
    private static final String DESCRIZIONE_PRESCR = "Ultima prescrizione richiesta: ";
    private static final String DESCRIZIONE_VISITA = "Ultima visita di controllo richiesta ";
    private static final String DESCRIZIONE_VISITA_spec = "Ultima visita specialistica richiesta in ";

    private static final String DOCDESCRIZIONE_PRESCR = "Ultima richiesta di prescrizione: ";
    private static final String DOCDESCRIZIONE_VISITA = "Ultima richiesta di visita di controllo ";
    private static final String DOCDESCRIZIONE_VISITA_spec = "Ultima richiesta di visita specialistica in ";

    Utils u;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.frag_container);
        getLayoutInflater().inflate(R.layout.activity_home, content, true);

        TextView info_medico = (TextView) findViewById(R.id.info_text);
        ImageView img = (ImageView) findViewById(R.id.img);

        TextView data_prescrizione = (TextView) findViewById(R.id.data_prescrizione);
        TextView info_prescrizione = (TextView) findViewById(R.id.text_prescrizione);

        TextView data_visita = (TextView) findViewById(R.id.data_visita);
        TextView info_visita = (TextView) findViewById(R.id.text_visita);

        TextView data_visita_spec = (TextView) findViewById(R.id.data_spec);
        TextView info_visita_spec = (TextView) findViewById(R.id.text_spec);

        Intent intent = getIntent();
        if (MainActivity.tipoUtente.equals("Paziente")) {

            paziente = intent.getParcelableExtra("Paziente");
            medico = riempiMedico();
            if(photo==null || photo.equals(""))
                img.setImageResource(R.drawable.immagine1);
            else
                u.stringToImageView(img,medico.getImage());
            info_medico.setText(medico.getNome() + " " + medico.getCognome());
            ultimarichiestavisita_spec = getultimaRichiesta(db.getTipoRichieste(paziente.getCodiceFiscale(), "Visita specialistica"));
            ultimarichiestavisita_con = getultimaRichiesta(db.getTipoRichieste(paziente.getCodiceFiscale(), "Visita di controllo"));
            ultimarichiestaprescr = getultimaRichiesta(db.getTipoRichieste(paziente.getCodiceFiscale(), "Prescrizione"));
            if (!(ultimarichiestaprescr == null)) {
                data_prescrizione.setText(ultimarichiestaprescr.getData_richiesta());
                info_prescrizione.setText(DESCRIZIONE_PRESCR + ultimarichiestaprescr.getNome_farmaco());
            } else
                info_prescrizione.setText("Nessuna Prescrizione richiesta.");

            if (!(ultimarichiestavisita_con == null)) {
                data_visita.setText(ultimarichiestavisita_con.getData_richiesta());
                info_visita.setText(DESCRIZIONE_VISITA);
            } else
                info_visita.setText("Nessuna visita di controllo richiesta.");

            if (!(ultimarichiestavisita_spec == null)) {
                data_visita_spec.setText(ultimarichiestavisita_spec.getData_richiesta());
                info_visita_spec.setText(DESCRIZIONE_VISITA_spec + ultimarichiestavisita_spec.getNome_farmaco());
            } else
                info_visita_spec.setText("Nessuna visita specialistica richiesta.");
        } else {
            medico = intent.getParcelableExtra("Medico");
            if(photo==null || photo.equals(""))
                img.setImageResource(R.drawable.immagine1);
            else
                img.setImageBitmap(photo);
            info_medico.setText(medico.getNome() + " " + medico.getCognome());

            ultimarichiestavisita_spec = getultimaRichiesta(db.getTipoRichieste(medico.getCodiceFiscale(), "Visita specialistica"));
            ultimarichiestavisita_con = getultimaRichiesta(db.getTipoRichieste(medico.getCodiceFiscale(), "Visita di controllo"));
            ultimarichiestaprescr = getultimaRichiesta(db.getTipoRichieste(medico.getCodiceFiscale(), "Prescrizione"));
            if (!(ultimarichiestaprescr == null)) {
                data_prescrizione.setText(ultimarichiestaprescr.getData_richiesta());
                info_prescrizione.setText(DOCDESCRIZIONE_PRESCR + ultimarichiestaprescr.getNome_farmaco());
            } else
                info_prescrizione.setText("Nessuna Prescrizione richiesta.");

            if (!(ultimarichiestavisita_con == null)) {
                data_visita.setText(ultimarichiestavisita_con.getData_richiesta());
                info_visita.setText(DOCDESCRIZIONE_VISITA);
            } else
                info_visita.setText("Nessuna visita di controllo richiesta.");

            if (!(ultimarichiestavisita_spec == null)) {
                data_visita_spec.setText(ultimarichiestavisita_spec.getData_richiesta());
                info_visita_spec.setText(DOCDESCRIZIONE_VISITA_spec + ultimarichiestavisita_spec.getNome_farmaco());
            } else
                info_visita_spec.setText("Nessuna visita specialistica richiesta.");
        }


        Button btnshow = (Button) findViewById(R.id.show);
        assert btnshow != null;
        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.tipoUtente.equals("Paziente")) {
                    Intent intentinfo = new Intent(HomeActivity.this, InfoActivity.class);
                    intentinfo.putExtra("Paziente", paziente);
                    startActivity(intentinfo);
                } else {
                    Intent intentinfo = new Intent(HomeActivity.this, DettagliMedico.class);
                    intentinfo.putExtra("Medico", medico);
                    startActivity(intentinfo);
                }
            }
        });

        CardView prescrizione = (CardView) findViewById(R.id.card_view_pres);
        assert prescrizione != null;
        prescrizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ultimarichiestaprescr!=null) {
                    Intent intent1;
                    if (ultimarichiestaprescr.getStato().equals("A")) {
                        if (MainActivity.tipoUtente.equals("Medico")) {
                            intent1 = new Intent(HomeActivity.this, RequestManagerFarmaco.class);
                            intent1.putExtra("richiesta", ultimarichiestaprescr);
                        } else {
                            intent1 = new Intent(HomeActivity.this, DetailsInAttesa.class);
                            intent1.putExtra("ITEM_CLICKED", ultimarichiestaprescr);
                        }
                    } else {
                        intent1 = new Intent(HomeActivity.this, DetailsActivity.class);
                        intent1.putExtra("ITEM_CLICKED", ultimarichiestaprescr);
                    }
                    startActivity(intent1);
                }
            }
        });
        CardView viscontrollo = (CardView) findViewById(R.id.card_view_vis);
        assert viscontrollo != null;
        viscontrollo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ultimarichiestavisita_con!=null) {
                    Intent intent1;
                    if (ultimarichiestavisita_con.getStato().equals("A")) {
                        if (MainActivity.tipoUtente.equals("Medico")) {
                            intent1 = new Intent(HomeActivity.this, RequestManagerVisita.class);
                            intent1.putExtra("richiesta", ultimarichiestavisita_con);
                        } else {
                            intent1 = new Intent(HomeActivity.this, DetailsInAttesa.class);
                            intent1.putExtra("ITEM_CLICKED", ultimarichiestavisita_con);
                        }
                    } else {
                        intent1 = new Intent(HomeActivity.this, DetailsActivity.class);
                        intent1.putExtra("ITEM_CLICKED", ultimarichiestavisita_con);
                    }
                    startActivity(intent1);
                }
            }
        });
        CardView visspec = (CardView) findViewById(R.id.card_view_spec);
        assert visspec != null;
        visspec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ultimarichiestavisita_spec!=null) {
                    Intent intent1;
                    if (ultimarichiestavisita_spec.getStato().equals("A")) {
                        if (MainActivity.tipoUtente.equals("Medico")) {
                            intent1 = new Intent(HomeActivity.this, RequestManagerVisita.class);
                            intent1.putExtra("richiesta", ultimarichiestavisita_spec);
                        } else {
                            intent1 = new Intent(HomeActivity.this, DetailsInAttesa.class);
                            intent1.putExtra("ITEM_CLICKED", ultimarichiestavisita_spec);
                        }
                    } else {
                        intent1 = new Intent(HomeActivity.this, DetailsActivity.class);
                        intent1.putExtra("ITEM_CLICKED", ultimarichiestavisita_spec);
                    }
                    startActivity(intent1);
                }
            }
        });
    }

    public Richiesta getultimaRichiesta(ArrayList<Richiesta> richieste) {
        if (richieste == null)
            return null;
        else
            return richieste.get(0);
    }

    public Medico riempiMedico() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        if (MainActivity.tipoUtente.equals("Paziente")) {
            Medico res = db.getMedico(paziente.getMedico());
            return res;
        } else {
            return medico;
        }
    }
}
