package com.barbercita;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//configuracion del login y de los permisos segun el rol
@Configuration
public class SecurityConfig {

    //Las contrasenas se comparan usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            LoginExitosoHandler loginExitosoHandler) throws Exception {
        http
                //Permisos de cada ruta segun el rol
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/cliente/**").hasRole("CLIENTE")
                        .requestMatchers("/barbero/**").hasRole("BARBERO")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //Las demas paginas (inicio, nosotros, servicios, contacto, login...) son publicas
                        .anyRequest().permitAll()
                )
                //Pantalla de login propia, se inicia sesion con el correo
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("correo")
                        .passwordParameter("password")
                        //Despues de iniciar sesion se envia a cada usuario al area de su rol
                        .successHandler(loginExitosoHandler)
                        //Si el usuario esta inactivo se muestra otro mensaje
                        .failureHandler((request, response, exception) -> {
                            if (exception instanceof DisabledException) {
                                response.sendRedirect(request.getContextPath() + "/login?inactivo");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/login?error");
                            }
                        })
                        .permitAll()
                )
                //Cerrar sesion
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                //Si no tiene el rol correcto se muestra la pagina de acceso denegado
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acceso_denegado")
                );
        return http.build();
    }
}
