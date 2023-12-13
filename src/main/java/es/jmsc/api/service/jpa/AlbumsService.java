package es.jmsc.api.service.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.jmsc.api.entity.Album;
import es.jmsc.api.repository.AlbumsRepository;
import es.jmsc.api.service.IAlbumsService;

@Service
public class AlbumsService implements IAlbumsService {

	@Autowired
	private AlbumsRepository repoAlbums;

	@Override
	public List<Album> buscarTodos() {
		return repoAlbums.findAll();
	}

	@Override
	public void guardar(Album album) {
		repoAlbums.save(album);
	}

	@Override
	public void eliminar(int idAlbum) {
		repoAlbums.deleteById(idAlbum);
	}

}
