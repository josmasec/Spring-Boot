package es.jmsc.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.jmsc.api.entity.Album;

public interface AlbumsRepository extends JpaRepository<Album, Integer> {

}
