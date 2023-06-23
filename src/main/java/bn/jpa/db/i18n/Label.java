package bn.jpa.db.i18n;

import com.github.javafaker.App;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQuery;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@NamedQuery(name="APPS_FOR_LANG", query = "SELECT l.usedBy from Label  l WHERE l.id=:id")
@NamedQuery(name="APPS_FOR_LANG_TEXT", query = "SELECT l.usedBy from Label  l WHERE l.id=:id AND l.text like :text")
@NamedQuery(name="FOR_TEXT", query = "SELECT l from Label  l WHERE l.text=:text")
@NamedQuery(name="FOR_TEXT_PREFIX", query = "SELECT l from Label  l WHERE l.text like CONCAT(:text, '%')")
@NamedQuery(name="FOR_TEXT_INFIX", query = "SELECT l from Label  l WHERE l.text like CONCAT('%', :text, '%')")
@NamedQuery(name="FOR_TEXT_SUFFIX", query = "SELECT l from Label  l WHERE l.text like CONCAT('%', :text)")
public class Label {
    @Id
    LabelId id;
    String text;
    @ManyToMany
    List<Application> usedBy;

    public Label(String labelId, String lang, String text) {
        this.id=new LabelId(labelId, lang);
        this.text=text;
        this.usedBy=new ArrayList<>();
    }

    public void registerApplication(Application app) {
        if(!isUsedBy(app)) {
            this.usedBy.add(app);
        }
    }

    public boolean isUsedBy(Application app) {
        return this.usedBy.contains(app);
    }
    protected Label() { }
}
