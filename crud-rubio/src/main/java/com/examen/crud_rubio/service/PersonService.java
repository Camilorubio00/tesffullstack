package com.examen.crud_rubio.service;

import com.examen.crud_rubio.model.Person;
import com.examen.crud_rubio.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(Integer id) {
        return personRepository.findById(id);
    }

    public Person create(Person person) {
        person.setId(null);
        return personRepository.save(person);
    }

    public Optional<Person> update(Integer id, Person person) {
        return personRepository.findById(id).map(existing -> {
            existing.setNombre(person.getNombre());
            existing.setApellido(person.getApellido());
            existing.setFechaNacimiento(person.getFechaNacimiento());
            existing.setPuesto(person.getPuesto());
            existing.setSueldo(person.getSueldo());
            return personRepository.save(existing);
        });
    }

    public boolean delete(Integer id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

