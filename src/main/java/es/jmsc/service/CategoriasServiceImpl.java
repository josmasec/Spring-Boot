package es.jmsc.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.jmsc.model.Categoria;

@Service
public class CategoriasServiceImpl implements ICategoriasService {

	private List<Categoria> lista = null;

	public CategoriasServiceImpl() {
		lista = new LinkedList<Categoria>();
		Categoria categoria1 = new Categoria();
		categoria1.setId(1);
		categoria1.setNombre("Ventas");
		categoria1.setDescripcion("Categoria relacionada con las ventas");
		Categoria categoria2 = new Categoria();
		categoria2.setId(2);
		categoria2.setNombre("Contabilidad");
		categoria2.setDescripcion("Categoria relacionada con la contabilidad");
		Categoria categoria3 = new Categoria();
		categoria3.setId(3);
		categoria3.setNombre("Transporte");
		categoria3.setDescripcion("Categoria relacionada con el transporte");
		Categoria categoria4 = new Categoria();
		categoria4.setId(4);
		categoria4.setNombre("Informática");
		categoria4.setDescripcion("Categoria relacionada con la informática");
		Categoria categoria5 = new Categoria();
		categoria5.setId(5);
		categoria5.setNombre("Construcción");
		categoria5.setDescripcion("Categoria relacionada con la construcción");
		Categoria categoria6 = new Categoria();
		categoria6.setId(6);
		categoria6.setNombre("Desarrollo de software");
		categoria6.setDescripcion("Trabajo para programadores");
		lista.add(categoria1);
		lista.add(categoria2);
		lista.add(categoria3);
		lista.add(categoria4);
		lista.add(categoria5);
		lista.add(categoria6);
	}

	@Override
	public void guardar(Categoria categoria) {
		// TODO Auto-generated method stub
		lista.add(categoria);
	}

	@Override
	public List<Categoria> buscarTodas() {
		// TODO Auto-generated method stub
		return lista;
	}

	@Override
	public Categoria buscarPorId(Integer idCategoria) {
		// TODO Auto-generated method stub
		for (Categoria categoria : lista) {
			if (categoria.getId() == idCategoria) {
				return categoria;
			}
		}
		return null;
	}

	@Override
	public void eliminar(Integer idCategoria) {
		// TODO Auto-generated method stub

	}

	@Override
	public Page<Categoria> buscarTodas(Pageable page) {
		// TODO Auto-generated method stub
		return null;
	}

}
