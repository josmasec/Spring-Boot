package es.jmsc.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.jmsc.model.Perfil;
import es.jmsc.model.Usuario;
import es.jmsc.model.Vacante;
import es.jmsc.service.ICategoriasService;
import es.jmsc.service.IUsuariosService;
import es.jmsc.service.IVacantesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private IVacantesService serviceVacantes;

	@Autowired
	private IUsuariosService serviceUsuarios;

	@Autowired
	private ICategoriasService serviceCategorias;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/tabla")
	public String mostrarTabla(Model model) {
		List<Vacante> lista = serviceVacantes.buscarTodas();
		model.addAttribute("vacantes", lista);
		return "tabla";
	}

	@GetMapping("/detalle")
	public String mostrarDetalle(Model model) {
		Vacante vacante = new Vacante();
		vacante.setNombre("Ingeniero de comunicaciones");
		vacante.setDescripcion("Se solicita ingeniero para dar soporte a intranet");
		vacante.setFecha(new Date());
		vacante.setSalario(9700.0);
		model.addAttribute("vacante", vacante);
		return "detalle";
	}

	@GetMapping("/listado")
	public String mostrarListado(Model model) {
		List<String> lista = new LinkedList<String>();
		lista.add("Ingeniero de sistemas");
		lista.add("Aux. de contabilidad");
		lista.add("Vendedor");
		lista.add("Arquitecto");

		model.addAttribute("empleos", lista);

		return "listado";
	}

	@GetMapping("/")
	public String mostrarHome(Model model) {
		// Representa el nombre de la vista.
		return "home";
	}

	@GetMapping("/index")
	public String mostrarIndex(Authentication auth, HttpSession session) {

		String username = auth.getName();
		System.out.println("Nombre del usuario: " + username);

		for (GrantedAuthority rol : auth.getAuthorities()) {
			System.out.println("ROL: " + rol.getAuthority());
		}
		if (session.getAttribute("usuario") == null) {
			Usuario usuario = serviceUsuarios.buscarPorUsername(username);
			usuario.setPassword(null);
			System.out.println("Usuario: " + usuario);
			session.setAttribute("usuario", usuario);
		}

		return "redirect:/";
	}

	@GetMapping("/signup")
	public String registrarse(Usuario usuario) {
		return "usuarios/formRegistro";
	}

	@PostMapping("/signup")
	public String guardarRegistro(Usuario usuario, RedirectAttributes atributes) {

		String pwdPlano = usuario.getPassword();
		String PwdEncriptado = passwordEncoder.encode(pwdPlano);
		usuario.setPassword(PwdEncriptado);

		usuario.setEstatus(1); // Activado por defecto
		usuario.setFechaRegistro(new Date()); // Fecha de registro: fecha actual del servidor.

		// Se crea el perfil que se le asignará al nuevo usuario.
		Perfil perfil = new Perfil();
		perfil.setId(3); // Perfil: USUARIO
		usuario.agregar(perfil);
		/**
		 * Se guarda el usuario en la base de datos. El Perfil se guarda
		 * automaticamente.
		 */
		serviceUsuarios.guardar(usuario);
		System.out.println("Usuario: " + usuario);
		atributes.addFlashAttribute("msg", "Registro Guardado");
		return "redirect:/usuarios/index";
	}

	/*
	 * Método que regresa una lista de objetos de tipo Vacante
	 * 
	 * /* private List<Vacante> getVacantes(){ SimpleDateFormat sdf = new
	 * SimpleDateFormat("dd-MM-yyyy"); List<Vacante> lista = new
	 * LinkedList<Vacante>(); try { // Creamos la oferta de Trabajo 1. Vacante
	 * vacante1 = new Vacante(); vacante1.setId(1);
	 * vacante1.setNombre("Ingeniero Civil");
	 * vacante1.setDescripcion("Se busca ingeniero civil");
	 * vacante1.setFecha(sdf.parse("08-02-2019")); vacante1.setSalario(8500.0); //
	 * La oferta de trabajo está destacada. vacante1.setDestacada(1);
	 * vacante1.setImagen("empresa1.png");
	 * 
	 * // Creamos la oferta de Trabajo 2. Vacante vacante2 = new Vacante();
	 * vacante2.setId(2); vacante2.setNombre("Contador Informático");
	 * vacante2.setDescripcion("Se busca ingeniero informático");
	 * vacante2.setFecha(sdf.parse("09-02-2019")); vacante2.setSalario(12000.0); //
	 * La oferta de trabajo NO está destacada. vacante2.setDestacada(0);
	 * vacante2.setImagen("empresa2.png");
	 * 
	 * lista.add(vacante1); lista.add(vacante2);
	 * 
	 * } catch (ParseException e) {
	 * e.printStackTrace(); }
	 * 
	 * return lista;
	 * 
	 * }
	 */

	@GetMapping("/search")
	public String buscar(@ModelAttribute("search") Vacante vacante, Model model) {
		System.out.println("Buscando por: " + vacante);
		ExampleMatcher matcher = ExampleMatcher.matching().
		// where descripcion like %?%
				withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains());
		Example<Vacante> example = Example.of(vacante, matcher);
		List<Vacante> lista = serviceVacantes.buscarByExample(example);
		model.addAttribute("vacantes", lista);
		return "home";
	}

	@GetMapping("/login")
	public String mostrarLogin() {
		return "formLogin";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/login";
	}

	@GetMapping("/bcrypt/{texto}")
	@ResponseBody
	public String encriptar(@PathVariable("texto") String texto) {
		return texto + " Encriptado en Bcrypt: " + passwordEncoder.encode(texto);
	}

	/*
	 * Si se detecta vacios en el Data Binding los setea a NULL.
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	// Otra forma de agregar datos al modelo.
	@ModelAttribute
	public void setGenericos(Model model) {
		Vacante vacanteSearch = new Vacante();
		vacanteSearch.reset();
		model.addAttribute("vacantes", serviceVacantes.buscarDestacadas());
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		model.addAttribute("search", vacanteSearch);
	}
}
