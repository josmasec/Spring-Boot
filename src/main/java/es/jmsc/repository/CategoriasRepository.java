package es.jmsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.jmsc.model.Categoria;

// <Modelo y tipo de clase primaria>
// public interface CategoriasRepository extends CrudRepository<Categoria, Integer>
public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {

}
