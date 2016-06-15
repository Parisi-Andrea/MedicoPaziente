package com.example.andre.medicopaziente;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by andre on 02/05/2016.
 */
public class Utility {
    public static String GetFieldCSV(String csv, int pos, String separator)
    {
        int at = 0;
        int len = csv.length();
        int start = 0;
        int cont = 0;
        int inizioCampo = -1;
        int fineCampo = -1;

        String campo = "";

        while ((start <= len) && (at > -1) && fineCampo == -1)
        {
            if (pos == 0) inizioCampo = 0;
            else if (cont == pos) inizioCampo = at + 1;

            at = csv.indexOf(separator, start);

            if (inizioCampo >= 0)
            {
                if (at == -1) fineCampo = len;
                else
                    fineCampo = at;

                /*if (pos == 6)
                {
                    Console.WriteLine("Test");
                }*/
                //campo = csv.substring(inizioCampo, fineCampo - inizioCampo);

                campo = csv.substring(inizioCampo, fineCampo);

            }

            if (at != -1)
            {
                cont++;
                start = at + 1;
            }
        }

        return campo;
    }

    public static String getPropertyFromWebserver(SoapObject result, String propertyname)
    {
        String strres = "";
        try
        {
            strres = result.getPropertyAsString(propertyname);
        }
        catch(Exception ex)
        {
            strres = "";
            //ex.printStackTrace();
        }

        return strres;
    }
}
