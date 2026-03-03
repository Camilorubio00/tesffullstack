package com.examen.crud_rubio.controller;

import com.examen.crud_rubio.dto.ApiResponse;
import com.examen.crud_rubio.model.Person;
import com.examen.crud_rubio.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Person>> getAll() {
        List<Person> persons = personService.findAll();
        ApiResponse<Person> response = new ApiResponse<>(true, "Listado de personas", persons);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Person>> getById(@PathVariable Integer id) {
        return personService.findById(id)
                .map(person -> {
                    ApiResponse<Person> response =
                            new ApiResponse<>(true, "Persona encontrada", Collections.singletonList(person));
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponse<Person> response =
                            new ApiResponse<>(false, "Persona no encontrada", Collections.emptyList());
                    return ResponseEntity.status(404).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Person>> create(@RequestBody Person person) {
        Person created = personService.create(person);
        ApiResponse<Person> response =
                new ApiResponse<>(true, "Persona creada correctamente", Collections.singletonList(created));
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Person>> update(@PathVariable Integer id, @RequestBody Person person) {
        return personService.update(id, person)
                .map(updated -> {
                    ApiResponse<Person> response =
                            new ApiResponse<>(true, "Persona actualizada correctamente", Collections.singletonList(updated));
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponse<Person> response =
                            new ApiResponse<>(false, "Persona no encontrada", Collections.emptyList());
                    return ResponseEntity.status(404).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Person>> delete(@PathVariable Integer id) {
        boolean deleted = personService.delete(id);
        if (deleted) {
            ApiResponse<Person> response =
                    new ApiResponse<>(true, "Persona eliminada correctamente", Collections.emptyList());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Person> response =
                    new ApiResponse<>(false, "Persona no encontrada", Collections.emptyList());
            return ResponseEntity.status(404).body(response);
        }
    }
}

