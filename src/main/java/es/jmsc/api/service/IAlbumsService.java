package es.jmsc.api.service;

import java.util.List;

import es.jmsc.api.entity.Album;

public interface IAlbumsService {
	List<Album> buscarTodos();

	void guardar(Album album);

	void eliminar(int idAlbum);
}
