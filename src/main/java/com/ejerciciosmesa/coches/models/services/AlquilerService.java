package com.ejerciciosmesa.coches.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import com.ejerciciosmesa.coches.models.entities.Alquiler;

public interface AlquilerService {
	
	public List<Alquiler> findAll();
	public Page<Alquiler> findAll(Pageable pageable);
	public Alquiler findOne(Long id);
	public void save(Alquiler alquiler);
	public void remove(Long id);
	public Long count();
	
	
	
}
