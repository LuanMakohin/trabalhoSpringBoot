package mensalidades.app.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mensalidades.app.model.Jogador;
import mensalidades.app.repository.JogadorRepository;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Content-Type")
@RestController
@RequestMapping("/api")
public class JogadorController {

    @Autowired
    JogadorRepository jogadorRepository;

    @GetMapping("/jogadores")
    public ResponseEntity<List<Jogador>> getAllJogadores(@RequestParam(required = false) String nome, String email) {
        try {
            List<Jogador> jogadores = new ArrayList<Jogador>();
            if (nome != null) {
                jogadorRepository.findByNomeContaining(nome).forEach(jogadores::add);

            }
            else if (email!= null) {
               jogadorRepository.findByEmailContaining(email).forEach(jogadores::add);
            }
            else {
               jogadorRepository.findAll().forEach(jogadores::add);
            }

            if (jogadores.isEmpty()) {
                return new ResponseEntity<List<Jogador>>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<List<Jogador>>(jogadores, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/jogadores")
    public ResponseEntity<Jogador> createJogador(@RequestBody Jogador jogador) {
        try {
            Jogador _jogador = jogadorRepository.save(new Jogador(jogador.getNome(), jogador.getEmail(), jogador.getDataNascimento()));
            return new ResponseEntity<>(_jogador, HttpStatus.CREATED);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/jogadores/{id}")
    public ResponseEntity<Jogador> getJogadorById(@PathVariable("id") long id) {
        try {
            Optional<Jogador> jogador = jogadorRepository.findById(id);
            if (jogador.isPresent()) {
                return new ResponseEntity<>(jogador.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/jogadores/{id}")
    public ResponseEntity<Jogador> updateJogador(@PathVariable("id") long id, @RequestBody Jogador jogador)
    {
        Optional<Jogador> data = jogadorRepository.findById(id);

        if (data.isPresent())
        {
            Jogador _jogador = data.get();
            _jogador.setNome(jogador.getNome());
            _jogador.setEmail(jogador.getEmail());
            _jogador.setDataNascimento(jogador.getDataNascimento());
            
            return new ResponseEntity<Jogador>(jogadorRepository.save(_jogador), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /*
     * DEL /api/artigos/:id : remover artigo dado um id
     */
    @DeleteMapping("/jogadores/{id}")
    public ResponseEntity<HttpStatus> deleteJogador(@PathVariable("id") long id)
    {
        try {
            jogadorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    /*
     * DEL /api/artigos : remover todos os artigos
     */
    @DeleteMapping("/jogadores")
    public ResponseEntity<HttpStatus> deleteAllJogadores()
    {
        try {
            jogadorRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
}
