package es.jmsc.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.jmsc.model.Vacante;

public interface IVacantesService {
	List<Vacante> buscarTodas();

	Vacante buscarPorId(Integer idVacante);

	void guardar(Vacante vacante);

	List<Vacante> buscarDestacadas();

	void eliminar(Integer idVacante);

	/*
	 * Consulta en base de datos Query By Example. Select's donde las diferentes
	 * condiciones del WHERE se forman de manera dinámica en base a la clase de
	 * modelo que pasemos como parametro en este caso la clase Vacante los filtros
	 * que van en la parte del WHERE se formaran dependiendo de los valores de
	 * atributos de la clase de modelo que no sean nulos
	 */
	List<Vacante> buscarByExample(Example<Vacante> example);

	Page<Vacante> buscarTodas(Pageable page);
}
