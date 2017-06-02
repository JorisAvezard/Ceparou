package main;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ClientRest {
	
	public static void main(String[] args) {
		
		String nom = "Joris";
		String mdp = "LeCon";
		String heure = "11h09";
		Client client = Client.create(new DefaultClientConfig());
        //ClientResponse response = client.resource(getBaseURI()).path("insertUser").path(nom).path(mdp).get(ClientResponse.class);
		ClientResponse response = client.resource(getBaseURI()).path("coucou").path(nom).path(heure).get(ClientResponse.class);
		String corps_Reponse = response.getEntity(String.class);
		System.out.println(corps_Reponse);
		
		ClientResponse rep2 = client.resource(getBaseURI()).path("insertUser").path(nom).path(mdp).get(ClientResponse.class);
	}
	
	private static URI getBaseURI() {

        //return UriBuilder.fromUri("http://192.168.137.1:8080/Ceparou/service").build();
        return UriBuilder.fromUri("http://127.0.0.1:8080/Ceparou/service").build();

    }

}
