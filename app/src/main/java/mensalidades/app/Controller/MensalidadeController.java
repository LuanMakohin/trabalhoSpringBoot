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

import mensalidades.app.Requests.MensalidadeRequest;
import mensalidades.app.model.Jogador;
import mensalidades.app.model.Mensalidade;
import mensalidades.app.repository.JogadorRepository;
import mensalidades.app.repository.MensalidadeRepository;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Content-Type")
@RestController
@RequestMapping("/api")
public class MensalidadeController {

    @Autowired
    private MensalidadeRepository mensalidadeRepository;
    @Autowired
    private JogadorRepository jogadorRepository;

    @GetMapping("/mensalidades")
    public ResponseEntity<List<Mensalidade>> getAllMensalidades(@RequestParam(required = false) Integer ano, Integer mes, String nome) {
        try {
            List<Mensalidade> mensalidades = new ArrayList<Mensalidade>();
            if (ano != null) {
                mensalidadeRepository.findByAno(ano).forEach(mensalidades::add);

            }
            else if (mes != null) {
                mensalidadeRepository.findByMes(mes).forEach(mensalidades::add);
            }
            else if (nome != null) {
                mensalidadeRepository.findByJogadorNome(nome).forEach(mensalidades::add);
            }
            else {
               mensalidadeRepository.findAll().forEach(mensalidades::add);
            }

            if (mensalidades.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<List<Mensalidade>>(mensalidades, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/mensalidades/{jogadorId}")
    public ResponseEntity<Mensalidade> createMensalidade(@PathVariable("jogadorId") long jogadorId,@RequestBody MensalidadeRequest request) {
       try {
            Mensalidade mensalidade;
            Optional<Jogador> optionalJogador = jogadorRepository.findById(jogadorId);
            if (optionalJogador.isPresent()) {
                Jogador jogador = optionalJogador.get();
                mensalidade = mensalidadeRepository.save(new Mensalidade(request.getAno(), request.getMes(), request.getValor(), jogador));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Mensalidade>(mensalidade, HttpStatus.CREATED);
       } catch(Exception e){
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @GetMapping("/mensalidades/{id}")
    public ResponseEntity<Mensalidade> getMensalidadeById(@PathVariable("id") long id) {
        try {
            Optional<Mensalidade> mensalidade = mensalidadeRepository.findById(id);
            if (mensalidade.isPresent()) {
                return new ResponseEntity<>(mensalidade.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/mensalidades/{id}")
    public ResponseEntity<Mensalidade> updateMensalidade(@PathVariable("id") long id, @RequestBody Mensalidade mensalidade)
    {
        Optional<Mensalidade> data = mensalidadeRepository.findById(id);

        if (data.isPresent())
        {
            Mensalidade _mensalidade = data.get();
            _mensalidade.setAno(mensalidade.getAno());
            _mensalidade.setMes(mensalidade.getMes());
            _mensalidade.setValor(mensalidade.getValor());
            _mensalidade.setJogador(mensalidade.getJogador());
            
            return new ResponseEntity<Mensalidade>(mensalidadeRepository.save(_mensalidade), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /*
     * DEL /api/artigos/:id : remover artigo dado um id
     */
    @DeleteMapping("/mensalidades/{id}")
    public ResponseEntity<HttpStatus> deleteMensalidade(@PathVariable("id") long id)
    {
        try {
            mensalidadeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    /*
     * DEL /api/artigos : remover todos os artigos
     */
    @DeleteMapping("/mensalidades")
    public ResponseEntity<HttpStatus> deleteAllMensalidades()
    {
        try {
            mensalidadeRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
}
