package com.example.hongos.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hongos.model.Hongo;
import com.example.hongos.model.Rol;
import com.example.hongos.model.User;
import com.example.hongos.service.HongoService;
import com.example.hongos.service.RolService;
import com.example.hongos.service.usuarioService;

@Controller
public class micontroller {
    
	@Autowired
    private usuarioService usuarioService;
	@Autowired
    private RolService rolservice;
    @Autowired
    private HongoService hongoService;
    private Hongo modifDatosHongos=new Hongo();
    
    /**
     * Método que devuelve la página de inicio de sesión cuando se accede a la ruta "/login".
     *
     * @return Cadena que representa el nombre de la página "login".
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    /**
     * 
     * Se maneja la logica cuando hace submit/click con el boton sign in del formulario login
     * Se envia los datos del formulario mediante POST a /login
     * 
     * @param username Nombre de usuario ingresado en el formulario.
     * @param password Contraseña ingresada.
     * @return Redirige al usuario a la página de home después de procesar el inicio de sesión.
     */
    @PostMapping("/login")
    public String loginPost(@RequestParam String username, @RequestParam String password) {
        // Logica de login (Spring Security lo hace automaticamente)
        return "redirect:/";
    }
    
    /**
     * Devuelve la página principal cuando se accede a "/home".
     * 
     * @return Nombre de la vista de la página principal "home".
     */
    @GetMapping("/home")
    public String home() {
        return "home"; // Va a home
    }
    
    /**
     * Prepara la página para crear un nuevo usuario cuando se accede a "/newuser".
     * 
     * @param model Modelo utilizado para mandar un objeto User a la vista el cual va a setease los valores con los datos del formulario de la pagina newuser
     * @return La vista "newuser" que tiene un formulario para crear un nuevo usuario.
     */    
    @GetMapping("/newuser")
    public String newuser(Model model) {
        model.addAttribute("usuario", new User());
        return "newuser";
    }
    
    /**
     * Gestiona la creación de un nuevo usuario cuando se envía el formulario mediante POST a "/create".
     * 
     * @param usuario Objeto de usuario que contiene los datos del formulario.
     * @param model Modelo utilizado para cargar datos desde el formulario al objeto User.
     * @param redirectAttributes Atributo para enviar mensajes al redirigir.(mensaje de exito)
     * @return Redirige al inicio de sesión después de un registro exitoso, o muestra errores si falla.
     */    
    @PostMapping("/create")
    public String createUsuario(@ModelAttribute User usuario, Model model, RedirectAttributes redirectAttributes) {
        try {
        	      	
        	Rol elem=rolservice.buscarRolUSER("USER");        	
        	usuario.setRoles(elem);//usuario viene cargada el formulario html de newuser
                    	
            usuarioService.createUser(usuario);
            redirectAttributes.addFlashAttribute("atributosuccess", "Registro exitoso. Complete con sus credenciales para iniciar.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage()); 
            model.addAttribute("usuario", usuario);
            return "newuser"; 
        }
    }
    
    /**
     * Carga la página de inicio ("/" o "/home") y muestra todos los hongos disponibles.
     * 
     * @param model Modelo utilizado para enviar datos a la vista.
     * @return La vista "home" con la lista de hongos.
     */
    @GetMapping({"/", "/home"})
    public String getAllHongos(Model model) {
        List<Hongo> hongos = hongoService.getAllHongos();
        model.addAttribute("atributename", "de hongos");//prueba de envio de datos hacia el formulario
        model.addAttribute("hongos", hongos);
        return "home";
    }
    
    /**
     * @param model Model usado para guardar un objeto Hongo el cual se seteara los valores con los datos del formulario de /posteo
     * @return La vista "posteo" cuando el usuario hace click en Agregrar un nuevo Hongo.
     * **/
    @GetMapping("/posteo")
    public String posteoNewHongo(Model model) {
        model.addAttribute("hongo", new Hongo());    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();    
        String miusername = authentication.getName();   //dice el email del usuario que esta en sesion    
        model.addAttribute("miusername", miusername);    //usado para enviar el email al formulario de /posteo
        return "posteo"; 
    }
    
    
    /**
     * Se gestiona la logica de boton submit(boton Agregar nuevo Hongo) del formulario de /posteo 
     * @param hongo El objeto Hongo que se va a agregar, se vincula automáticamente con los datos del formulario.
     * @param model Se usa para agregar datos a la vista.
     * @param request HttpServletRequest Tiene acceso para leer los datos del formulario actual.(en nuestro caso leemos la opcion seleccionada del formulario)
     * @param atributo Lo utilizamos para enviar mensaje a la pagina home cuando se ha agrgado exitosamente un hongo.
     * @return se redirige a la pagina home si un hongo se posteo con exito, en caso contratio queda en /posteo esperando a que el usuario cargue los datos del hongo que desea postear
     * **/
    @PostMapping("/addhongo")
    public String createHongo(@ModelAttribute Hongo hongo, Model model, HttpServletRequest request,RedirectAttributes atributo) {
        String opcionSeleccionada = request.getParameter("opcion"); //lee la opcio cargada del formulario      
        hongo.setEscomestible(opcionSeleccionada);//los demas datos ya estan cargados en hongo
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String miusername = authentication.getName();     
        hongo.setEm(miusername);

        try {
            hongoService.createHongo(hongo);
			atributo.addFlashAttribute("msg", "Hongo agregado exitosamente.");
            return "redirect:/"; 
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());//no habria mensaje porque se cargan todos los hongos que se postean
            model.addAttribute("hongo", hongo);
            return "posteo"; 
        }
    }
    
    /**
     * Se carga la pagina delete/{id} cuando el usuario quiere eliminar un hongo.
     * @param id Long el id del hongo que el usuario desea eliminar.
     * @param atributo es usado para llevar el mensaje si se logro con exito eliminar o no.
     * @return se redirige a la vista home ya si se logoro o no eliminar un hongo.
     * **/
    
    @GetMapping({"/delete", "/delete/{id}"})
    public String delhongo(@PathVariable Long id, RedirectAttributes atributo) {
           
        Hongo hongo = hongoService.getHongoById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String miusername = authentication.getName();
        
    	User miuser=usuarioService.BuscarUser(miusername);
		 if (miuser.getRoles().getName().equals("ADMIN") || hongo.getEm().equals(miusername)) 

            {hongoService.deleteHongo(id);
        	atributo.addFlashAttribute("msg", "Hongo eliminado exitosamente.");} 
            
        else 
           	atributo.addFlashAttribute("msg", "Error al eliminar un hongo. Al ser usuario comun no puede eliminar un hongo que posteo otro usuario. Si lo desea cambiese a usuario Pro.");
        		
       
        
        return "redirect:/";
    }

    
    
    /**
     * Se carga la pagina /update{id} cuando el usuario quiere modificar un hongo.
     * @param id Long es el id del hongo que se desea modificar.
     * @param model Model es usado para llevar al formulario los datos del hongo.
     * @param atributo es el encargado de llevar un mensaje error a la pagina home. 
     * @return la vista "update" si el usuario cumple las condiciones para modificar un hongo, en caso contrario se le informa con un mensaje de error.
     * ***/    
    @GetMapping({"/update", "/update{id}"})
    public String actualiza(@PathVariable Long id, Model model,RedirectAttributes atributo) {

         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String miusername = authentication.getName();
         
     	 User miuser=usuarioService.BuscarUser(miusername);
		 Hongo hongo = hongoService.getHongoById(id);
     	 
		 if (miuser.getRoles().getName().equals("ADMIN") || hongo.getEm().equals(miusername)) 
     	 	{
  	         model.addAttribute("miusername", miusername);
    		 model.addAttribute("hongo", hongo);//cargar el formulario con los datos del hongo seleccionado
    		 model.addAttribute("iddelhongo", id);
    		 
    		 boolean opcionnueva = hongo.getEscomestible().equals("SI") ? true : false;
    		 model.addAttribute("opcionnueva", opcionnueva);
    		 model.addAttribute("escomestiblehongo", hongo.getEscomestible());

    		 setmodifDatosHongos(id,hongo.getEm());
  	        
     	 	}
     	 else 
     	 {
     		atributo.addFlashAttribute("msg", "Error al modificar un hongo. Al ser usuario comun no puede modiicar un hongo que posteo otro usuario. Si lo desea cambiese a usuario Pro.");
     	    return "redirect:/";
    
         }
         
         
         
        return "update"; 
    }
   /**setter y getter de un objeto Hongo con el id del hongo y el email del usuario cuyo valores no se modifican. 
    * **/
    private Hongo getmodifDatosHongos() {
    		return modifDatosHongos;
    }
    private void setmodifDatosHongos(Long id,String email) {
    	modifDatosHongos.setId(id);
    	modifDatosHongos.setEm(email);
    }

    /**
     * Se maneja la logica de POST a /updatehongo
     * @param hongo Hongo el hongo que tiene los datos cargados desde el formulario de /update
     * @param request se lo solicita para que traiga la opcion nueva del hongo.
     * @param atributo lleva a car en envio de datos para mensajes a la pagina home
     * @return se redirige a la vista "home" si se logra modificar el hongo.
     * ***/
    
    @PostMapping({"/updatehongo"})
    public String actalizaformulario(@ModelAttribute Hongo hongo,HttpServletRequest request,RedirectAttributes atributo) {
        String opcionSeleccionada = request.getParameter("opcionnueva"); //lee la opcio cargada del formulario      


	 	hongo.setId(getmodifDatosHongos().getId());;
    	hongo.setEm(getmodifDatosHongos().getEm());;
    	hongo.setEscomestible(opcionSeleccionada);
    	
    	
    	hongoService.updateHongo(hongo.getId(), hongo);
		atributo.addFlashAttribute("msg", "Cambios realizados en el hongo "+hongo.getNombre()+ " con Id "+hongo.getId()+" con exito.");


    	return "redirect:/";         
    }    
    
    
   /**
    * Se encarga de la logica de cerrar sesion.
    * **/ 

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
    
    
    /**
     * Carga la pagina /pro
     * @param model para guardar el usuario que va a ser modificado su rol de usuario.
     * @return la vista "pro" 
     **/
    @GetMapping("/pro")
    public String prouser(Model model) {
        model.addAttribute("usuario", new User());
        return "pro";
    }
    
    /**
     * Se encarga de solucionar la logica del boton Ser usuario comun
     * @param atributo el encargado d llevar el mensaje si se logra cambiar a usuario Comun
     * @return la vista home
     * ***/
    @PostMapping("/comun")
    public String comunUsuario(RedirectAttributes atributo) {
      //buscar el id cargado
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String miusername = authentication.getName();        
        
        Rol roluser = rolservice.buscarRolUSER("USER");
    	//buscar el usuario con su email/username
    	
        User miuser=usuarioService.BuscarUser(miusername);
    	
    	//ya tengo el id del usuario, con el id lo busco en la base de datos
        User basededatos_user = usuarioService.buscarusario_en_bd(miuser.getId());
        
        basededatos_user.setRoles(roluser);
	    
        usuarioService.guarrdar_usuario(basededatos_user);
		atributo.addFlashAttribute("msg", "Se cambio a usuario comun con exito.");

        return "redirect:/";

    }
    
    /**
     * Se encarga de solucionar la logica del boton Ser usuario Pro
     * @param atributo el encargado d llevar el mensaje si se logra cambiar a usuario Pro
     * @return la vista home
     * ***/
    @PostMapping("/cambiapro")
    public String proUsuario(RedirectAttributes atributo) {
    	//buscar el id cargado
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String miusername = authentication.getName();        
        
        Rol roluser = rolservice.buscarRolUSER("ADMIN");
    	//buscar el usuario con su email/username
    	
        User miuser=usuarioService.BuscarUser(miusername);
    	
    	//ya tengo el id del usuario, con el id lo busco en la base de datos
        User basededatos_user = usuarioService.buscarusario_en_bd(miuser.getId());
        
        basededatos_user.setRoles(roluser);
	    
        usuarioService.guarrdar_usuario(basededatos_user);
		atributo.addFlashAttribute("msg", "Se cambio a usuario pro con exito.");

        return "redirect:/";

    }
    
    /**
     * Carga la pagina /contactos
     * @return la vista "contacto" 
     **/
    @GetMapping("/contactos")
    public String contactos() {
        return "contactos";
    }
    
    /**
     * Carga la pagina /venenosos-api
     * @return la vista "venenosos-api" 
     **/
    @GetMapping("/venenosos-api")
    public String nocomestibles() {
        return "venenosos-api";
    }
    
    /**
     * Carga la pagina /comestible-api
     * @return la vista "comestible-api" 
     **/
    @GetMapping("/comestible-api")
    public String comestibles() {
        return "comestible-api";
    }

    /**
     * Carga la pagina /detalles
     * @return la vista "detalles" 
     **/
    @GetMapping("/detalles")
    public String detalles() {
        return "detalles";
    }
    
    
    
    
}
