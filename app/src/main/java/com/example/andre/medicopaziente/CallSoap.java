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


    public String Registration(String nome,String cognome,String email,String password,String indirizzo,String codFiscale, int medico) {
        String SOAP_ACTION = "http://tempuri.org/Registration";
        String OPERATION_NAME = "Registration";
        String WSDL_TAREGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.137.1:80/test/WebService1.asmx";

        SoapObject request = new SoapObject(WSDL_TAREGET_NAMESPACE, OPERATION_NAME);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("Nome");
        PI.setValue(nome);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("Cognome");
        PI.setValue(cognome);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("Email");
        PI.setValue(email);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("Password");
        PI.setValue(password);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("Indirizzo");
        PI.setValue(indirizzo);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("CodiceFiscale");
        PI.setValue(codFiscale);
        PI.setType(String.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("Medico");
        PI.setValue(medico);
        PI.setType(Integer.class);
        request.addProperty(PI);

        PI = new PropertyInfo();
        PI.setName("Registrato");
        PI.setValue(1);
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
}
