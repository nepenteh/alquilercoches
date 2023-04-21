package com.ejerciciosmesa.coches.appdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.springframework.stereotype.Component;


@Component
public class AppDataImpl  implements AppData {

	private String name; 
	private String author; 
	private int year; 
	private String web; 
	private String webURL; 
	private TreeMap<String,GeneralOption> generalOptions; 
	private ArrayList<GeneralOption> sortedGeneralOptions;
			
	public AppDataImpl() {
		name = "Alquiler de Coches";
		author = "José Manuel Rosado";
		year = 2023;
		web = "ejerciciosmesa.com";
		webURL = "https://ejerciciosmesa.com";
		
		generalOptions = new TreeMap<>();
		sortedGeneralOptions = new ArrayList<>();
		int order=0;
		
		GeneralOption opCoche = new GeneralOption(order,"COCHE","Coche","/coche/list","LIST");

opCoche.addScreen("LIST","Coche (List)");
opCoche.addScreen("CREATE","Coche (Create)");
opCoche.addScreen("UPDATE","Coche (Update)");
opCoche.addScreen("VIEW","Coche (View)");
opCoche.addScreen("VIEWIMG","Coche - View Image");

opCoche.setEmptyMessage("No data");

generalOptions.put("COCHE",opCoche);
sortedGeneralOptions.add(opCoche);

order++;

GeneralOption opCliente = new GeneralOption(order,"CLIENTE","Cliente","/cliente/list","LIST");

opCliente.addScreen("LIST","Cliente (List)");
opCliente.addScreen("CREATE","Cliente (Create)");
opCliente.addScreen("UPDATE","Cliente (Update)");
opCliente.addScreen("VIEW","Cliente (View)");
opCliente.addScreen("VIEWIMG","Cliente - View Image");

opCliente.setEmptyMessage("No data");

generalOptions.put("CLIENTE",opCliente);
sortedGeneralOptions.add(opCliente);

order++;

GeneralOption opAlquiler = new GeneralOption(order,"ALQUILER","Alquiler","/alquiler/list","LIST");

opAlquiler.addScreen("LIST","Alquiler (List)");
opAlquiler.addScreen("CREATE","Alquiler (Create)");
opAlquiler.addScreen("UPDATE","Alquiler (Update)");
opAlquiler.addScreen("VIEW","Alquiler (View)");
opAlquiler.addScreen("VIEWIMG","Alquiler - View Image");

opAlquiler.setEmptyMessage("No data");

generalOptions.put("ALQUILER",opAlquiler);
sortedGeneralOptions.add(opAlquiler);

order++;


		
		Collections.sort(sortedGeneralOptions);
		
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
	public String getAuthor() {
		return author;
	}

	
	@Override
	public int getYear() {
		return year;
	}

	
	@Override
	public String getWeb() {
		return web;
	}
	
	
	@Override
	public String getWebUrl() {
		return webURL;
	}

	
	@Override
	public String getAuthorship() {
		String authorship = "";
		if(!author.isBlank() || !web.isBlank()) {
			authorship += author+" "+year;
			if(!web.isBlank()) authorship += " - "+web;
		}		
		return authorship.trim();
	}
	
	
	@Override
	public TreeMap<String, GeneralOption> getGeneralOptions() {
		return generalOptions;
	}
	
	@Override
	public ArrayList<GeneralOption> getSortedGeneralOptions() {
		return sortedGeneralOptions;
	}
	
	@Override
	public boolean isMainScreen(String optionCode, String screenCode) {
		return generalOptions.get(optionCode).getMainScreenCode().equals(screenCode);
	}
	
	@Override
	public String getMainScreenName(String optionCode) {
		return generalOptions.get(optionCode).getMainScreenName();
	}
	
	@Override
	public String getMainScreenLink(String optionCode) {
		return generalOptions.get(optionCode).getLink();
	}
	
	@Override
	public String getScreenName(String optionCode, String screenCode) {
		return generalOptions.get(optionCode).getScreen(screenCode);
	}

	@Override
	public String getEmptyMessage(String optionCode) {
		return generalOptions.get(optionCode).getEmptyMessage();
	}
		
}
