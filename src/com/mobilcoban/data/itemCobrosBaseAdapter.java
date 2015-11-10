package com.mobilcoban.data;

import java.util.ArrayList;
import com.mobilcoban.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams") 
public class itemCobrosBaseAdapter extends BaseAdapter 
{
	
	public static ArrayList<itemCobrosDetalle> itemDetailsArrayList;
	
	
	private LayoutInflater l_Inflater;
	
	// Crea el contexto del adapter
	public itemCobrosBaseAdapter(Context ctx, ArrayList<itemCobrosDetalle> results)
	{
		itemDetailsArrayList = results;
		l_Inflater = LayoutInflater.from(ctx);
		//getFilter();
		
	}	

	public int getCount() 
	{
		
		return itemDetailsArrayList.size();
	}

	public Object getItem(int position) 
	{
		return itemDetailsArrayList.get(position);
	}

	public long getItemId(int position) {
		
		return position;
	}
	
	static class ViewHolder
	{
		TextView cobros_linea0;
		TextView cobros_linea1;
		TextView cobros_linea1_1;
		TextView cobros_linea2;
		TextView cobros_linea3;
		ImageView ImagenCobrosIcono;
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView == null)
		{
			
			convertView = l_Inflater.inflate(R.layout.listado_details,null);
			
			holder = new ViewHolder();
			holder.cobros_linea0 = (TextView) convertView.findViewById(R.id.cobros_linea0);
			holder.cobros_linea1 = (TextView) convertView.findViewById(R.id.cobros_linea1);
			holder.cobros_linea1_1 = (TextView) convertView.findViewById(R.id.cobros_linea1_1);
			holder.cobros_linea2 = (TextView) convertView.findViewById(R.id.cobros_linea2);
			holder.cobros_linea3 = (TextView) convertView.findViewById(R.id.cobros_linea3);
			//holder.ImagenCobrosIcono = (ImageView) convertView.findViewById(R.id.imagenCobro);
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		// Obtengo la numercion de cada posicion + 1, por ser vector el list se suma 1 para que la 
		// numerion con mience en 1 de lo contrario comineza en 0
		//holder.cobros_linea0.setText(itemDetailsArrayList.get(position));
		
		holder.cobros_linea1.setText("Código: " + itemDetailsArrayList.get(position).getid_Contrato());
		holder.cobros_linea1_1.setText("Nombre: " + itemDetailsArrayList.get(position).getNombres());
		holder.cobros_linea2.setText("Negocio: " + itemDetailsArrayList.get(position).getTelefono());
		holder.cobros_linea3.setText("Direccion: " + itemDetailsArrayList.get(position).getDireccion());
		//holder.ImagenCobrosIcono.setImageResource(R.drawable.ic_getpayment_dp);
				
		return convertView;
	}
	
	
		
}
