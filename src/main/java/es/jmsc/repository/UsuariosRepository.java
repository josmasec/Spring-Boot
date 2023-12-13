package es.jmsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.jmsc.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByUsername(String username);

}
