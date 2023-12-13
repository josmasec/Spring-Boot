package es.jmsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.jmsc.model.Solicitud;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer> {

}
