package com.mobilcoban.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;


public class NetworkWs 
{

	// Listado de Cobros pendientes
	
	/*public static String ListadoPendientes(String Id, String Action, String Limit, String Filter)
	{
		String result="";
		ArrayList<NameValuePair> params;
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appID",Id));
		params.add(new BasicNameValuePair("action",Action));
		params.add(new BasicNameValuePair("RESULTROWSLIMIT",Limit));
		params.add(new BasicNameValuePair("MAINLISTFILTER",Filter));		
		result = GETData("http://dev-wagadelta.c9.io/api/contratos", params);
		return result;
	}*/
	
	
	public static String ListadoPendientes(int user)
	{
		String result="";
		ArrayList<NameValuePair> params;
		params = null;
				
		result = GETData("http://dev-wagadelta.c9.io/api/users/"+user,params);
		Log.i("RESULTADO DATA", result);
		return result;
	}
	
	
	// Listado Cobros Realizados
	public static String ListadoCobRealizados(String Id, String Action, String Limit, String Filter)
	{
		String result="";
		ArrayList<NameValuePair> params;
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appID",Id));
		params.add(new BasicNameValuePair("action",Action));
		params.add(new BasicNameValuePair("RESULTROWSLIMIT",Limit));
		params.add(new BasicNameValuePair("MAINLISTFILTER",Filter));		
		result = POSTData("http://crm.t4msports.com/webservice/ws.php",params);
		return result;
	}
	
	
	// Recordatorios
	
			public static String recordatoriosList(String Id, String Action, String Limit, String Filter)
			{
				
				String result="";
				ArrayList<NameValuePair> params;
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("appID",Id));
				params.add(new BasicNameValuePair("action",Action));
				params.add(new BasicNameValuePair("RESULTROWSLIMIT",Limit));
				params.add(new BasicNameValuePair("MAINLISTFILTER",Filter));
				result = POSTData("http://crm.t4msports.com/webservice/ws.php",params);
				return result;
			}	
				
	
		
	
	// Detalle de cobro
		public static String NewContract(String sMonto ,String sNo_quote, String sPeriodo, String sSolicitaPor, String sSolicitaEn,
							String sNombres, String sApellidos, String sIDpi, String sFechaNac, String sDirecc, String sTelefonos)
		{
			String result="";
			ArrayList<NameValuePair> params;
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("monto", sMonto));
			params.add(new BasicNameValuePair("no_cuotas", sNo_quote));
			params.add(new BasicNameValuePair("periodo_cobro", sPeriodo));
			params.add(new BasicNameValuePair("solicitado_por", sSolicitaPor));
			params.add(new BasicNameValuePair("solicitado_en", sSolicitaEn));
			params.add(new BasicNameValuePair("nombres", sNombres));
			params.add(new BasicNameValuePair("apellidos", sApellidos));
			params.add(new BasicNameValuePair("identificacion", sIDpi));
			params.add(new BasicNameValuePair("fecha_nacimiento", sFechaNac));
			params.add(new BasicNameValuePair("domicilio", sDirecc));
			params.add(new BasicNameValuePair("telefonos", sTelefonos));			
			result = POSTData("http://dev-wagadelta.c9.io/api/contratos",params);
			return result;
		}
	
	
	
	// Cobro Confirmacion  Realizado
		public static String CobroPrint(String contratoId ,String userId, String datePay, String quotePay, String numVoucher, String tipo)
		{
			
			String result="";
			ArrayList<NameValuePair> params;
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id_contrato", contratoId));
			params.add(new BasicNameValuePair("id_usuario", userId));
			params.add(new BasicNameValuePair("fecha_pago", datePay));
			params.add(new BasicNameValuePair("cuotas_pagadas", quotePay));
			params.add(new BasicNameValuePair( tipo, numVoucher));
			
			
			result = POSTData("http://dev-wagadelta.c9.io/api/cobros",params);
			return result;
		}
		
		
		
		// Foto
				public static String Photo(String quote ,String Id, String Action)
				{
					String result="";
					ArrayList<NameValuePair> params;
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("quoteID", quote));
					params.add(new BasicNameValuePair("appID", Id));
					params.add(new BasicNameValuePair("action", Action));
					result = POSTData("http://crm.t4msports.com/webservice/ws.php",params);
					return result;
				}
			
	
	
	
		
	// Subir Imagen
		public static String CargarFoto( String quote, String Id, String Action, String Image,  String Nombre)
		{
			
			String result="";
			ArrayList<NameValuePair> params;
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("quoteID", quote));
			params.add(new BasicNameValuePair("appID", Id));
			params.add(new BasicNameValuePair("action", Action));
			params.add(new BasicNameValuePair("filename", Nombre));
			params.add(new BasicNameValuePair("file64", Image));
			result = POSTData("http://crm.t4msports.com/webservice/ws.php",params);
			return result;
		}
		
			
				
		
	
	private static String POSTData(String url, ArrayList<NameValuePair> params) 
	{
		String datos="";
		String linea;
		HttpContext mHttpContext = new BasicHttpContext();
		DefaultHttpClient mHttpClient = new DefaultHttpClient();
		HttpPost mHttpPost = null;
		mHttpPost = new HttpPost(url);
		try {
			if (params!= null) {
				
				mHttpPost.setEntity(new UrlEncodedFormEntity(params));
			}
			BasicHttpResponse response = (BasicHttpResponse) mHttpClient.execute(mHttpPost,mHttpContext);
			InputStream is = response.getEntity().getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while((linea = br.readLine())!=null) {
				datos += linea;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datos;
	}
	
	
	
	/*  Verbo GET HTTP para solicitud de datos */
    @SuppressWarnings("unused")
	private static String GETData(String url, ArrayList<NameValuePair> params) {
        String datos="";
        String linea;
        HttpContext mHttpContext = new BasicHttpContext();
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        HttpGet mHttpGet = null;
        if (params!= null) {
            
            mHttpGet = new HttpGet(url+"&"+ URLEncodedUtils.format(params, "utf-8"));
            
        }else{
            mHttpGet = new HttpGet(url);
        }
        try {
            BasicHttpResponse response = (BasicHttpResponse) mHttpClient.execute(mHttpGet,mHttpContext);
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while((linea = br.readLine())!=null) {
                datos += linea;
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return datos;
    }
	
	
}
