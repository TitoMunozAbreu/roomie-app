package es.roomie.user.controller;

import es.roomie.user.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") String userId) {
        //TODO: Obtener detalles del perfil de usuario.
        return null;
    }
}
