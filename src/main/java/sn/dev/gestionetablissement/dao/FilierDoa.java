package sn.dev.gestionetablissement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.dev.gestionetablissement.model.Filiere;
import sn.dev.gestionetablissement.model.Inscrire;

import java.util.Date;
import java.util.List;

public interface FilierDoa extends JpaRepository<Filiere, Long> {

}
