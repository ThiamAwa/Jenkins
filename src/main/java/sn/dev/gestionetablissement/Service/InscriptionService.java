package sn.dev.gestionetablissement.Service;

import org.springframework.stereotype.Service;
import sn.dev.gestionetablissement.dao.FilierDoa;
import sn.dev.gestionetablissement.dao.InscriptionDao;
import sn.dev.gestionetablissement.model.Inscrire;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InscriptionService {
    private final FilierDoa filierDoa;
    private InscriptionDao inscriptionDao;

    public InscriptionService(InscriptionDao inscriptionDao, FilierDoa filierDoa) {
        this.inscriptionDao = inscriptionDao;
        this.filierDoa = filierDoa;
    }

    public List<Inscrire> findAll(){
        return inscriptionDao.findAll();
    }

//    public List<Inscrire> findAllByAnnAccContaining(String AnneAcad) {
//        return null;
//        inscriptionDao.findAllByAnneAccadContaining(AnneAcad);
//    }

    public Optional<Inscrire> findById(Long id) {
        return inscriptionDao.findById(id);
    }

    public Inscrire create(Inscrire produit) {
        return inscriptionDao.save(produit);
    }
}
