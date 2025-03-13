package sn.dev.gestionetablissement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.dev.gestionetablissement.model.Filiere;
import sn.dev.gestionetablissement.model.Inscrire;

import java.util.Date;
import java.util.List;

@Repository

public interface InscriptionDao extends JpaRepository<Inscrire, Long> {
//    List<Inscrire> findAllByAnneAccadContaining(Date AnneAcad);
//    List<Inscrire> findAllByFilierContaining(Filiere filiere);
//    List<Inscrire> findAllByStatutContaining(String statut);
}
