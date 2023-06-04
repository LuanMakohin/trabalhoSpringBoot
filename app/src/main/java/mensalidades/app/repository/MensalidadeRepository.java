package mensalidades.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mensalidades.app.model.Mensalidade;

@Repository
public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long>{
    List<Mensalidade> findByAno(int ano);
    List<Mensalidade> findByMes(int mes);
    List<Mensalidade> findByJogadorNome(String nome);

}
