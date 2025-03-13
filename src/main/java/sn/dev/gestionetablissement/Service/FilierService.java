package sn.dev.gestionetablissement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.dev.gestionetablissement.dao.FilierDoa;
import sn.dev.gestionetablissement.model.Filiere;

@Service
public class FilierService {

    @Autowired
    private FilierDoa filierDoa;

    // Méthode pour créer une nouvelle filière
    public Filiere createFilier(Filiere filier) {
        return filierDoa.save(filier);
    }
}
