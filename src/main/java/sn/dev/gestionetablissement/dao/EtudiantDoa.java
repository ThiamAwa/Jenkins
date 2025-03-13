package sn.dev.gestionetablissement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.dev.gestionetablissement.model.Etudiant;

public interface EtudiantDoa extends JpaRepository<Etudiant, Long> {
}
