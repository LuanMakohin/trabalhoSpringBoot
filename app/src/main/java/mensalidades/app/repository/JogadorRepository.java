package mensalidades.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mensalidades.app.model.Jogador;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {
    List<Jogador> findByNome(String nome);
    List<Jogador> findByEmail(String email);

}
