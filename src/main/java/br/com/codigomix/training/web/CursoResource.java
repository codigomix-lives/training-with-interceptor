package br.com.codigomix.training.web;

import br.com.codigomix.training.data.CursoRepository;
import br.com.codigomix.training.model.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CursoResource {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/cursos")
    public ResponseEntity<List<Curso>> getCursos(){

        var cursos = cursoRepository.findAll();

        return ResponseEntity.of(Optional.of(cursos));

    }

    @PostMapping("/cursos")
    public ResponseEntity<Curso> createCurso(@RequestBody Curso curso){

        cursoRepository.save(curso);
        return ResponseEntity.ok(curso);

    }

}
