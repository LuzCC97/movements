package com.example.movements.movements.controller;
import com.example.movements.movements.dto.MovementListResponse;
import com.example.movements.movements.service.MovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//anotación para indicar que esta api es tipo rest
@RestController
//anotacion para indicar la ruta de mi api
@RequestMapping("/account")
public class MovementController {

    //MovementService: clase del service
    //movementService: variable atributo, variable dentro del controller que guarda la instancia del MovementService(servicio)
    private final MovementService movementService;

    //constructor para usar los metodos del servicio movementservice
    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    //anotación para indicar metodo tipo get, que recibe el accouintid como parametro - es la ruta de mi endpoint
    @GetMapping("/{accountId}/movements")
    public ResponseEntity<MovementListResponse> getMovements(@PathVariable String accountId) {
        MovementListResponse response = movementService.getMovementsByAccount(accountId);
        if(response == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}

