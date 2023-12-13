package es.jmsc.service;

import java.util.List;

import es.jmsc.model.Usuario;

public interface IUsuariosService {
	void guardar(Usuario usuario);

	void eliminar(Integer idUsuario);

	List<Usuario> buscarTodos();

	Usuario buscarPorUsername(String username);
}
