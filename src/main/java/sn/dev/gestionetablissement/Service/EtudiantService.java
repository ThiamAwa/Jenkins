package sn.dev.gestionetablissement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.dev.gestionetablissement.dao.EtudiantDoa;
import sn.dev.gestionetablissement.model.Etudiant;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {
    @Autowired
    private EtudiantDoa etudiantDoa;

    // Méthode pour créer un nouvel étudiant
    public Etudiant createEtudiant(Etudiant etudiant) {
        return etudiantDoa.save(etudiant);
    }


    // Méthode pour récupérer tous les étudiants
    public List<Etudiant> getAllEtudiants() {
        return etudiantDoa.findAll();
    }

    // Méthode pour récupérer un étudiant par son ID
    public Optional<Etudiant> getEtudiantById(Long id) {
        return etudiantDoa.findById(id);
    }

    // Méthode pour mettre à jour un étudiant
    public Etudiant updateEtudiant(Long id, Etudiant etudiantDetails) {
        if (etudiantDoa.existsById(id)) {
            etudiantDetails.setId(id);
            return etudiantDoa.save(etudiantDetails);
        }
        return null;
    }

    // Méthode pour supprimer un étudiant
    public void deleteEtudiant(Long id) {
        etudiantDoa.deleteById(id);
    }
}
