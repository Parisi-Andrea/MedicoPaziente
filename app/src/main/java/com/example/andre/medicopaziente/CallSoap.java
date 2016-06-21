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



    public String PazienteRegistration(String nome,String cognome,String email,String password,String indirizzo,String codFiscalePaziente,String codFiscaleMedico, String dataNascita,String luogoNascita, String telefono ) {
        String SOAP_ACTION = "http://tempuri.org/PazienteRegistration";
        String OPERATION_NAME = "PazienteRegistration";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.137.1:80/test/WebService1.asmx";

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



    public ArrayList<Richiesta> Profilo(String idUtente)
    {
        ArrayList<Richiesta> richieste = new ArrayList<Richiesta>();

        String SOAP_ACTION = "http://tempuri.org/Profilo";
        String OPERATION_NAME = "Profilo";
        String WSDL_TAREGET_NAMESPACE ="http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.137.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE,OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("idUtente");
        PI.setValue(idUtente);
        PI.setType(Integer.class);
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
            for(int i = 0;i<cols;i++)
            {
                Object objectResponse = (Object) result.getProperty(i);
                SoapObject r = (SoapObject) objectResponse;

                Richiesta test = new Richiesta();
                test.idRichiesta = r.getProperty("idRichiesta").toString();
                test.image = r.getProperty("image").toString();
                test.nome = r.getProperty("Nome").toString();
                test.cognome = r.getProperty("Cognome").toString();
                test.richiesta = r.getProperty("Richiesta").toString();
                test.descrizione = r.getProperty("Descrizione").toString();

                richieste.add(i,test);

            }
        } catch (Exception ex) {
            response = ex.getMessage().toString();
        }
        return richieste;
    }
    public String Login(String username, String password,String tipoUtente)
    {
        String SOAP_ACTION = "http://tempuri.org/Autenticazione";
        String OPERATION_NAME = "Autenticazione";
        String WSDL_TAREGET_NAMESPACE ="http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.137.1:80/test/WebService1.asmx";

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
            httpTransport.call(SOAP_ACTION,envelope);
            //response = httpTransport.responseDump;
            SoapPrimitive tmp = (SoapPrimitive) envelope.getResponse();

            response = tmp.toString();
        }
        catch (Exception ex)
        {
            response = ex.getMessage().toString();
        }
        return response;
    }

    public String MedicoRegistration(String nome,String cognome,String email,String password,String codFiscale, String telefono ) {
        String SOAP_ACTION = "http://tempuri.org/MedicoRegistration";
        String OPERATION_NAME = "MedicoRegistration";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.137.1:80/test/WebService1.asmx";

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
