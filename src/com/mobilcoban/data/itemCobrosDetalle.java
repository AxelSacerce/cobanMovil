package com.mobilcoban.data;

public class itemCobrosDetalle {
	String sID_Cliente;
    String sNombre;   
    String sNombre_Negocio;
    String sDireccion;
    String sOrden;
    //int iPosicion;
    
    
    
    // Getters    
    public String get_orden()
    {
    	return sOrden;
    }
    
    public String getid_Cliente()
    {
        return sID_Cliente;
    }
     
    
    public String getNombre()
    {
        return sNombre;
    } 
    
    public String getNegocio()
    {
        return sNombre_Negocio;
    }
    
    public String getDireccion()
    {
        return sDireccion;
    }
    
    /*public int getNumber()
    {
    	return iPosicion;
    }*/
    
    
    
    
    // Setters    
    
    public void set_Orden(String data)
    {
    	this.sOrden = data;
    }
    
    public void setid_Cliente(String data)
    {
        this.sID_Cliente = data;
    }
       
    public void setNombre(String data)
    {
    	this.sNombre = data;
    }
    
    public void setNegocio(String data)
    {
    	this.sNombre_Negocio = data;
    }   
        
    public void setDireccion(String data)
    {
        this.sDireccion = data;
    }
    
    /*public void setNumber(int data)
    {
    	this.iPosicion = data;
    }*/
    
   
   
}
