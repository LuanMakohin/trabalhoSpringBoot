package mensalidades.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mensalidades.app.model.Jogador;
import mensalidades.app.model.Mensalidade;

public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long>{
    List<Mensalidade> findByAno(int ano);
    List<Mensalidade> findByMes(int mes);
    List<Mensalidade> findByJogador(Jogador jogador);
}
