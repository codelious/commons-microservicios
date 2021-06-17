package com.codelious.microservicios.commons.commonsmicroservicios.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.codelious.microservicios.commons.commonsmicroservicios.services.CommonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class CommonController<E, S extends CommonService<E>> {
    
    @Autowired
    protected S service;

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/pageable")
    public ResponseEntity<?> list(Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<E> o = service.findById(id);
        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(o.get());
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody E entity, BindingResult result) {

        if (result.hasErrors()) {
            return this.validar(result);
        }
        E entitySaved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entitySaved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    protected ResponseEntity<?> validar(BindingResult result) {
        Map<String, Object> errores = new HashMap<>();

        result.getFieldErrors().forEach(err -> errores.put(err.getField(), "El campo " + err.getField() + " contiene errores: " + err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }

}
