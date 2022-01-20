package desafio.vr.miniautorizador.repositories;

import desafio.vr.miniautorizador.models.Cartao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepository extends MongoRepository<Cartao, String>{

}
