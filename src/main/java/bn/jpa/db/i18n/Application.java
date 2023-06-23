package bn.jpa.db.i18n;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Application {
    @Id UUID id;
    String name;

    public Application(UUID id, String name) {
        this.id= Objects.requireNonNullElseGet(id, UUID::randomUUID);
        this.name=name;
    }
    protected Application() { }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
