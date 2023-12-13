package es.jmsc.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.jmsc.model.Solicitud;

public interface ISolicitudesService {

	void guardar(Solicitud solicitud);

	void eliminar(Integer idSolicitud);

	List<Solicitud> buscarTodas();

	Solicitud buscarPorId(Integer idSolicitud);

	// Paginación.
	Page<Solicitud> buscarTodas(Pageable page);
}
