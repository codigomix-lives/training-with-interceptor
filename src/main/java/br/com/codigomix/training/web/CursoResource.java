package br.com.codigomix.training.web;

import br.com.codigomix.training.data.CursoRepository;
import br.com.codigomix.training.model.Curso;
import br.com.codigomix.training.service.RateLimitService;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class CursoResource {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    RateLimitService rateLimitService;

    @GetMapping("/cursos")
    public ResponseEntity<List<Curso>> getCursos(){
        ConsumptionProbe probe = rateLimitService.getBucket().tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()){
            var cursos = cursoRepository.findAll();
            return ResponseEntity.ok()
                            .header("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()))
                            .body(cursos);
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                            .header("X-Rate-Limit-Retry-After-Seconds",
                                    String.valueOf(TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill())))
                            .build();
        }

    }

    @PostMapping("/cursos")
    public ResponseEntity<Curso> createCurso(@RequestBody Curso curso){

        cursoRepository.save(curso);
        return ResponseEntity.ok(curso);

    }

}
