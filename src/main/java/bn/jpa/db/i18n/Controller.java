package bn.jpa.db.i18n;

import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
@Transactional
@RestController
public class Controller {
    EntityManager em;
    ApplicationRepo applicationRepo;
    LabelRepo labelRepo;
    public final static String[] LANGS={"de", "dk", "en", "fr", "gr", "hu", "it", "no", "pt", "se"};

    @PostMapping("/dummy/{app}/{label}")
    public ResponseEntity<String> fill(@PathVariable String app, @PathVariable String label) {
        Faker faker=new Faker();
        int nlabels=1000;
        int nApps=50;
        List<Application> apps=repeat(nApps, (i)-> new Application(null, app+i));
        applicationRepo.saveAll(apps);
        for(String lang:LANGS) {
            List<Label> labels=repeat(nlabels, (i)-> new Label(label+i, lang, faker.lorem().word()));
            labels.forEach(l -> l.setUsedBy(apps));
            labelRepo.saveAll(labels);
        }
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/applications/{label}/{lang}")
    public ResponseEntity<Object> getApplications(@PathVariable String label, @PathVariable String lang) {
        Query q=em.createNamedQuery("APPS_FOR_LANG");
        q.setParameter("id", new LabelId(label, lang));
        return ResponseEntity.ok(q.getResultList());
    }

    public static enum SearchMode {
        VERBATIM, PREFIX, INFIX, SUFFIX
    }
    @GetMapping("/applications/{label}/{lang}/{text}")
    public ResponseEntity<Object> getApplicationsText(@PathVariable String label,
                                                      @PathVariable String lang,
                                                      @PathVariable String text,
                                                      @RequestParam(name = "mode", required = false, defaultValue = "VERBATIM") SearchMode mode) {
        Query q=em.createNamedQuery("APPS_FOR_LANG_TEXT");
        q.setParameter("id", new LabelId(label, lang));
        switch(mode) {
            case PREFIX: text=(text+"%"); break;
            case SUFFIX: text=("%"+text); break;
            case INFIX: text=("%"+text+"%"); break;
            default: break;
        }
        q.setParameter("text", text);
        return ResponseEntity.ok(q.getResultList());
    }

    @GetMapping("/labels/{text}")
    public ResponseEntity<Object> getApplicationsText(@PathVariable String text,
            @RequestParam(name = "mode", required = false, defaultValue = "VERBATIM") SearchMode mode) {
        Query q=em.createNamedQuery(switch(mode) {
            case VERBATIM -> "FOR_TEXT";
            case PREFIX -> "FOR_TEXT_PREFIX";
            case INFIX -> "FOR_TEXT_INFIX";
            case SUFFIX -> "FOR_TEXT_SUFFIX";
        });
        q.setParameter("text", text);
        return ResponseEntity.ok(q.getResultList());
    }


    public static <T> List<T> repeat(int n, java.util.function.Function<Integer, T> s) {
        // Whoa, shouldn't the stream lib provide a solution for this?
        ArrayList<T> r = new ArrayList<>();
        for (int i = 0; i < n; ++i) r.add(s.apply(i));
        return r;
    }
}
