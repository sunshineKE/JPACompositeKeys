package bn.jpa.db.i18n;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface ApplicationRepo extends CrudRepository<Application, UUID> {

}
