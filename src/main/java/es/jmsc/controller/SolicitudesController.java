package es.jmsc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.jmsc.model.Solicitud;
import es.jmsc.model.Usuario;
import es.jmsc.model.Vacante;
import es.jmsc.service.ISolicitudesService;
import es.jmsc.service.IUsuariosService;
import es.jmsc.service.IVacantesService;
import es.jmsc.util.Utileria;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController {

	@Value("${empleosapp.ruta.cv}")
	private String rutaCv;

	@Autowired
	private IVacantesService serviceVacantes;

	@Autowired
	private IUsuariosService servicesUsuario;

	@Autowired
	private ISolicitudesService solicitudesService;

	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Solicitud> lista = solicitudesService.buscarTodas(page);
		model.addAttribute("solicitudes", lista);
		return "solicitudes/listSolicitudes";
	}

	@GetMapping("/create/{idVacante}")
	public String crear(Solicitud solicitud, @PathVariable("idVacante") Integer idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		System.out.println("idVacante: " + idVacante);
		model.addAttribute("vacante", vacante);
		return "solicitudes/formSolicitud";
	}

	// BindingResult contorla los errores que puedan surgir.
	@PostMapping("/save")
	public String guardar(Solicitud solicitud, BindingResult result, @RequestParam("archivoCV") MultipartFile multipart,
			Authentication auth, RedirectAttributes atributes) {

		String username = auth.getName();

		if (result.hasErrors()) {
			System.out.println("Existen errores");
			return "solicitudes/formSolicitud";
		}
		if (!multipart.isEmpty()) {
			String nombreArchivo = Utileria.guardarArchivo(multipart, rutaCv);
			if (nombreArchivo != null) {
				solicitud.setArchivo(nombreArchivo);
			}
		}

		Usuario usuario = servicesUsuario.buscarPorUsername(username);
		solicitud.setUsuario(usuario);

		solicitudesService.guardar(solicitud);
		atributes.addFlashAttribute("msg", "Gracias por enviar tu CV!");

		System.out.println("Solicitud: " + solicitud);
		return "redirect:/";
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idSolicitud, RedirectAttributes atributes) {
		solicitudesService.eliminar(idSolicitud);
		atributes.addFlashAttribute("msg", "La solicitud fue eliminada!");
		return "redirect:/solicitudes/indexPaginate";
	}

}