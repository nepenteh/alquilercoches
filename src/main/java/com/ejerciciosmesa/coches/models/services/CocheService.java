package com.ejerciciosmesa.coches.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import com.ejerciciosmesa.coches.models.entities.Coche;

public interface CocheService {
	
	public List<Coche> findAll();
	public Page<Coche> findAll(Pageable pageable);
	public Coche findOne(Long id);
	public void save(Coche coche);
	public void remove(Long id);
	public Long count();
	
	
	
}
