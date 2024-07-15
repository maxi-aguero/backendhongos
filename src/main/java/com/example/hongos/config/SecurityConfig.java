package com.example.hongos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.hongos.service.CustomUserDetailsService;
/**
 *
 * Define reglas de acceso y configuraciones específicas de Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	/**
     * Configura el encoder de contraseñas utilizado para almacenar contraseñas de usuarios.     *
     * @return BCryptPasswordEncoder  utilizado para codificar contraseñas
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Configura el servicio personalizado para cargar detalles de usuario desde la base de datos.     *
     * @return Instancia de CustomUserDetailsService para gestionar los detalles de usuario.
     */
    @Bean
    public CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    /**
     * Configura el AuthenticationManager para autenticar usuarios utilizando el servicio
     * CustomUserDetailsService y el passwordEncoder.
     *
     * @param auth AuthenticationManagerBuilder utilizado para configurar la autenticación.
     * @throws Exception si hay un problema durante la configuración de la autenticación.
     */    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
    
    /**
     * Configura las reglas de seguridad HTTP para las solicitudes entrantes.
     *
     * @param http HttpSecurity utilizado para configurar la seguridad HTTP.
     * @throws Exception si hay un problema durante la configuración de la seguridad HTTP.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        		.antMatchers("/login**", "/newuser**","/create**").anonymous() // Permitir solo para usuarios no autenticados
                .antMatchers("/js/**", "/css/**", "/img/**").permitAll() // Permitir recursos estÃƒÂ¡ticos para todos
                .anyRequest().authenticated() // Todas las demÃƒÂ¡s rutas requieren autenticaciÃƒÂ³n
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // URL para cerrar sesiÃ³n

                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")  
                .deleteCookies("JSESSIONID") // Eliminar cookies despuÃ©s de cerrar sesiÃ³n
                .permitAll();
    }
}
