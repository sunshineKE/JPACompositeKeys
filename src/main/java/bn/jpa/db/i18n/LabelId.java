package bn.jpa.db.i18n;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public final class LabelId implements Serializable {
    public String lableId;
    public String lang;

    public LabelId(String labelId, String lang) {
        this.lableId = labelId;
        this.lang = lang;
    }

    protected LabelId() {
    }
}
