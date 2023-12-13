package es.jmsc.security;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {

	// Le dice al framework de spirng que en tiempo de ejecución se debe de crear una
	// estancia de este tipo y que va a estar siempre en memoria listo para ser inyectado en otros componentes.
	@Bean
	UserDetailsManager users(DataSource dataSource) {
		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		users.setUsersByUsernameQuery("select username,password,estatus from Usuarios u where username=?");
		users.setAuthoritiesByUsernameQuery(
				"select u.username,p.perfil from UsuarioPerfil up " + "inner join Usuarios u on u.id = up.idUsuario "
						+ "inner join Perfiles p on p.id = up.idPerfil " + "where u.username=?");
		return users;
	}

	/*
	 * Tablas por defecto de Spring.
	 * 
	 * @Bean 
	 * UserDetailsManager users(DataSource dataSource) {
	 * JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource); 
	 * return users; }
	 * */

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				// Los recursos estáticos no requieren autenticación
				.requestMatchers("/bootstrap/**", "/images/**", "/tinymce/**", "/logos/**").permitAll()
				// Las vistas públicas no requieren autenticación
				.requestMatchers("/", "/signup", "/search", "/vacantes/view/**", "/bcrypt/**").permitAll()

				// Asignar permisos a URLs por ROLES
				.requestMatchers("/solicitudes/create/**", "/solicitudes/save/**").hasAnyAuthority("USUARIO")
				.requestMatchers("/vacantes/**").hasAnyAuthority("SUPERVISOR", "ADMINISTRADOR")
				.requestMatchers("/categorias/**").hasAnyAuthority("SUPERVISOR", "ADMINISTRADOR")
				.requestMatchers("/usuarios/**").hasAnyAuthority("ADMINISTRADOR")

				// Todas las demás URLs de la Aplicación requieren autenticación
				.anyRequest().authenticated());
		// El formulario de Login no requiere autenticacion
		http.formLogin(form -> form.loginPage("/login").permitAll());
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
