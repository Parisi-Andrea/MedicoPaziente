package com.example.andre.medicopaziente;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 30/04/2016.
 */
public class CallSoap {

    String ipAddress = "http://192.168.173.1:80";

    /****************************************************************************
     *
     *	Function Prototype:  	public String PazienteRegistration(String nome,String cognome,String email,String password,String indirizzo,String codFiscalePaziente,
     *                                                             String codFiscaleMedico, String dataNascita,String luogoNascita, String telefono )
     *
     *  Purpose: This function is used to call the the WebService (PazienteRegistration function)
     *
     *  Value Passed:        nome   : the name of patient - STRING
     *                       cognome: surname of patient - STRING
     *                       password: password that used to login -STRING
     *                       indirizzo: city of residence -STRING
     *                       codFiscalePaziente: unique identifier of patient - STRING(16)
     *                       codFiscaleMedico : unique identifier of doctor - STRING(16)
     *                       dataNascita : format (YYYY-mm-dd) - STRING(10)
     *                       luogoNascita: birth place -STRING
     *                       telefono : telephone number - STRING(10)

     *
     *  Value Returned:       STRING : 1 -> CORRECT
     *                                 else -> ERROR
     *
     *
     *  Date    	  Programmer  		Change History
     *  ----------- -------------- ----------------------------------------
     *  2016/04/30   ANDREA PARISI        Initial write up
     ****************************************************************************/
    public String PazienteRegistration(String nome,String cognome,String email,String password,String indirizzo,String codFiscalePaziente,String codFiscaleMedico, String dataNascita,String luogoNascita, String telefono ) {
        String SOAP_ACTION = "http://tempuri.org/PazienteRegistration";
        String OPERATION_NAME = "PazienteRegistration";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.173.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("nome");
        PI.setValue(nome);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("cognome");
        PI.setValue(cognome);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("email");
        PI.setValue(email);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("password");
        PI.setValue(password);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("residenza");
        PI.setValue(indirizzo);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("luogoNascita");
        PI.setValue(luogoNascita);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("codiceFiscale");
        PI.setValue(codFiscalePaziente);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("codFiscaleMedico");
        PI.setValue(codFiscaleMedico);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("dataNascita");
        PI.setValue(dataNascita);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("telefono");
        PI.setValue(telefono);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);
            //response = httpTransport.responseDump;
            SoapPrimitive tmp = (SoapPrimitive) envelope.getResponse();

            response = tmp.toString();
        } catch (Exception ex) {
            response = ex.getMessage().toString();
        }
        return response;
    }
    public Medico GetMedicoInfo(String codiceFiscale)
    {
        Medico medico = new Medico();
        String SOAP_ACTION = "http://tempuri.org/GetMedicoInfo";
        String OPERATION_NAME = "GetMedicoInfo";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.173.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("codiceFiscale");
        PI.setValue(codiceFiscale);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try
        {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);
            //String tmp = httpTransport.responseDump;
            //SoapObject tmp = (SoapObject) envelope.getResponse();
            SoapObject result = (SoapObject) envelope.getResponse();


            medico.codiceFiscale = result.getProperty("CodiceFiscale").toString();
            medico.nome = result.getProperty("Nome").toString();
            medico.cognome = result.getProperty("Cognome").toString();
            medico.email = result.getProperty("Email").toString();
            medico.nTel = result.getProperty("NTel").toString();
            medico.ambulatorio = result.getProperty("Ambulatorio").toString();
            medico.orario = result.getProperty("Orario").toString();

        } catch (Exception ex) {
        response = ex.getMessage().toString();
    }
        return medico;
    }
    public ArrayList<Richiesta> GetPazienteRequest(String codiceFiscale,String stato) {
        ArrayList<Richiesta> richieste = new ArrayList<Richiesta>();

        String SOAP_ACTION = "http://tempuri.org/GetPazienteRequest";
        String OPERATION_NAME = "GetPazienteRequest";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.173.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("codiceFiscale");
        PI.setValue(codiceFiscale);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("stato");
        PI.setValue(stato);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);
            //String tmp = httpTransport.responseDump;
            //SoapObject tmp = (SoapObject) envelope.getResponse();
            SoapObject result = (SoapObject) envelope.getResponse();
            int cols = result.getPropertyCount();
            for (int i = 0; i < cols; i++) {
                Object objectResponse = (Object) result.getProperty(i);
                SoapObject r = (SoapObject) objectResponse;

                Richiesta test = new Richiesta();
                test.idRichiesta = Integer.parseInt(r.getProperty("idRichiesta").toString());
                test.stato = r.getProperty("stato").toString();
                test.tipo = r.getProperty("tipo").toString();
                test.cf_paziente = r.getProperty("cf_paziente").toString();
                test.note_richiesta = r.getProperty("note_richiesta").toString();
                test.data_richiesta = r.getProperty("data_richiesta").toString();
                test.note_risposta = r.getProperty("note_risposta").toString();
                test.data_risposta = r.getProperty("data_risposta").toString();
                test.nome_farmaco = r.getProperty("nome_farmaco").toString();
                test.quantita_farmaco = Integer.parseInt(r.getProperty("quantita_farmaco").toString());
                test.cf_medico = r.getProperty("cf_medico").toString();
                richieste.add(i, test);

            }
        } catch (Exception ex) {
            response = ex.getMessage().toString();
        }
        return richieste;
    }

    public ArrayList<Richiesta> GetMedicoRequest(String codiceFiscale,String stato) {
        ArrayList<Richiesta> richieste = new ArrayList<Richiesta>();

        String SOAP_ACTION = "http://tempuri.org/GetMedicoRequest";
        String OPERATION_NAME = "GetMedicoRequest";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.173.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("codiceFiscale");
        PI.setValue(codiceFiscale);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("stato");
        PI.setValue(stato);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);
            //String tmp = httpTransport.responseDump;
            //SoapObject tmp = (SoapObject) envelope.getResponse();
            SoapObject result = (SoapObject) envelope.getResponse();
            int cols = result.getPropertyCount();
            for (int i = 0; i < cols; i++) {
                Object objectResponse = (Object) result.getProperty(i);
                SoapObject r = (SoapObject) objectResponse;

                Richiesta test = new Richiesta();
                test.idRichiesta = Integer.parseInt(r.getProperty("idRichiesta").toString());
                test.stato = r.getProperty("stato").toString();
                test.tipo = r.getProperty("tipo").toString();
                test.cf_paziente = r.getProperty("cf_paziente").toString();
                test.note_richiesta = r.getProperty("note_richiesta").toString();
                test.data_richiesta = r.getProperty("data_richiesta").toString();
                test.nome_farmaco = r.getProperty("nome_farmaco").toString();
                test.quantita_farmaco = Integer.parseInt(r.getProperty("quantita_farmaco").toString());
                test.cf_medico = r.getProperty("cf_medico").toString();
                richieste.add(i, test);

            }
        } catch (Exception ex) {
            response = ex.getMessage().toString();
        }
        return richieste;
    }

    public Medico LoginMedico(String username, String password,String tipoUtente)
    {
        Medico medico = new Medico();
        String SOAP_ACTION = "http://tempuri.org/AutenticazioneMedico";
        String OPERATION_NAME = "AutenticazioneMedico";
        String WSDL_TAREGET_NAMESPACE ="http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.173.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE,OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("codiceFiscale");
        PI.setValue(username);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("Password");
        PI.setValue(password);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("TipoUtente");
        PI.setValue(tipoUtente);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet  = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try
        {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);

            SoapObject result = (SoapObject) envelope.getResponse();

            medico.codiceFiscale = result.getProperty("CodiceFiscale").toString();
            medico.nome = result.getProperty("Nome").toString();
            medico.cognome = result.getProperty("Cognome").toString();
            medico.email = result.getProperty("Email").toString();
            medico.nTel = result.getProperty("NTel").toString();
            medico.password = result.getProperty("Password").toString();
            medico.ambulatorio = result.getProperty("Ambulatorio").toString();
            medico.orario = result.getProperty("Orario").toString();

        }
        catch (Exception ex)
        {
            response = ex.getMessage().toString();
            medico = null;
        }
        return medico;
    }

    public Paziente LoginPaziente(String username, String password,String tipoUtente)
    {
        Paziente paziente = new Paziente();
        String SOAP_ACTION = "http://tempuri.org/AutenticazionePaziente";
        String OPERATION_NAME = "AutenticazionePaziente";
        String WSDL_TAREGET_NAMESPACE ="http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.173.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE,OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("codiceFiscale");
        PI.setValue(username);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("Password");
        PI.setValue(password);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("TipoUtente");
        PI.setValue(tipoUtente);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet  = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try
        {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);

            SoapObject result = (SoapObject) envelope.getResponse();

            paziente.codiceFiscale = result.getProperty("CodiceFiscale").toString();
            paziente.nome = result.getProperty("Nome").toString();
            paziente.cognome = result.getProperty("Cognome").toString();
            paziente.dataNascita = result.getProperty("DataNascita").toString();
            paziente.luogoNascita = result.getProperty("LuogoNascita").toString();
            paziente.residenza = result.getProperty("Residenza").toString();
            paziente.email = result.getProperty("Email").toString();
            paziente.nTel = result.getProperty("NTel").toString();
            paziente.medico = result.getProperty("Medico").toString();
            paziente.password = result.getProperty("Password").toString();
            paziente.image = result.getProperty("Image").toString();

        }
        catch (Exception ex)
        {
            response = ex.getMessage().toString();
            paziente = null;
        }
        return paziente;
    }

    public String Richiesta(String data_richiesta,String tipo,String note_richiesta,String nome_farmaco,Integer quantita_farmaco, String cf_paziente, String cf_medico) {
        String SOAP_ACTION = "http://tempuri.org/Richiesta";
        String OPERATION_NAME = "Richiesta";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.173.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("data_richiesta");
        PI.setValue(data_richiesta);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("stato");
        PI.setValue("A");
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("tipo");
        PI.setValue(tipo);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("note_richiesta");
        PI.setValue(note_richiesta);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("nome_farmaco");
        PI.setValue(nome_farmaco);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("quantita_farmaco");
        PI.setValue(quantita_farmaco);
        PI.setType(Integer.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("cf_paziente");
        PI.setValue(cf_paziente);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("cf_medico");
        PI.setValue(cf_medico);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);
            //response = httpTransport.responseDump;
            SoapPrimitive tmp = (SoapPrimitive) envelope.getResponse();

            response = tmp.toString();
        } catch (Exception ex) {
            response = ex.getMessage().toString();
        }
        return response;
    }

    public String getImageFromDB(String codFiscale) {
        String SOAP_ACTION = "http://tempuri.org/GetImage";
        String OPERATION_NAME = "GetImage";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.173.1:80/test/WebService1.asmx";
        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();

        PI.setName("codiceFiscale");
        PI.setValue(codFiscale);
        PI.setType(String.class);
        request.addProperty(PI);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);
            //response = httpTransport.responseDump;
            SoapPrimitive tmp = (SoapPrimitive) envelope.getResponse();

            response = tmp.toString();
        } catch (Exception ex) {
            response = ex.getMessage().toString();
        }
        return response;
    }

    public String InsertImageToDB(String fileEnc, String codFiscale) {
        String SOAP_ACTION = "http://tempuri.org/InsertImage";
        String OPERATION_NAME = "InsertImage";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.173.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();

        PI = new PropertyInfo();
        PI.setName("codiceFiscale");
        PI.setValue(codFiscale);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("image");
        PI.setValue(fileEnc);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);
            //response = httpTransport.responseDump;
            SoapPrimitive tmp = (SoapPrimitive) envelope.getResponse();

            response = tmp.toString();
        } catch (Exception ex) {
            response = ex.getMessage().toString();
        }
        return response;
    }

    public String MedicoRegistration(String nome,String cognome,String email,String password,String codFiscale, String telefono ) {
        String SOAP_ACTION = "http://tempuri.org/MedicoRegistration";
        String OPERATION_NAME = "MedicoRegistration";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.173.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("nome");
        PI.setValue(nome);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("cognome");
        PI.setValue(cognome);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("email");
        PI.setValue(email);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("password");
        PI.setValue(password);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("codiceFiscale");
        PI.setValue(codFiscale);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("telefono");
        PI.setValue(telefono);
        PI.setType(String.class);
        request.addProperty(PI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try {
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope);
            //response = httpTransport.responseDump;
            SoapPrimitive tmp = (SoapPrimitive) envelope.getResponse();

            response = tmp.toString();
        } catch (Exception ex) {
            response = ex.getMessage().toString();
        }
        return response;
    }
}
