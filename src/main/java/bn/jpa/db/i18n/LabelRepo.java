package bn.jpa.db.i18n;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LabelRepo extends CrudRepository<Label, LabelId> {

}

