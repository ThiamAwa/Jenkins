package sn.dev.gestionetablissement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sn.dev.gestionetablissement.Service.EtudiantService;
import sn.dev.gestionetablissement.Service.FilierService;
import sn.dev.gestionetablissement.model.Etudiant;
import sn.dev.gestionetablissement.model.Filiere;

@SpringBootApplication
public class GestionEtablissementApplication implements CommandLineRunner {

    @Autowired
//    private EtudiantService etudiantService;
    private FilierService filierService;

    public static void main(String[] args) {
        SpringApplication.run(GestionEtablissementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Ajouter des étudiants
//        Etudiant etudiant1 = new Etudiant();
//        etudiant1.setNom("DIOP");
//        etudiant1.setPrenom("Fatou");
//        etudiant1.setEmail("fatou@example.com");
//        etudiant1.setMatricule("E001");
//        etudiant1.setAdresse("Dakar");
//
//        Etudiant etudiant2 = new Etudiant();
//        etudiant2.setNom("NDIAYE");
//        etudiant2.setPrenom("AMADOU");
//        etudiant2.setEmail("ndiaye@example.com");
//        etudiant2.setMatricule("E002");
//        etudiant2.setAdresse("Dakar");
//
//        // Sauvegarder dans la base de données
//        etudiantService.createEtudiant(etudiant1);
//        etudiantService.createEtudiant(etudiant2);
//
//        System.out.println("Les étudiants ont été ajoutés avec succès !");

        // Créer  filières
        Filiere filiere1 = new Filiere();
        filiere1.setName("Informatique");
        filiere1.setDescription("Filière spécialisée dans l'informatique.");

        Filiere filiere2 = new Filiere();
        filiere1.setName("Economie");
        filiere1.setDescription("Filière spécialisée dans l'ecoomie.");

        Filiere filiere3 = new Filiere();
        filiere1.setName("Management");
        filiere1.setDescription("Filière spécialisée dans le Management.");

        filierService.createFilier(filiere1);
        filierService.createFilier(filiere2);
        filierService.createFilier(filiere3);

        System.out.println("Les étudiants ont été ajoutés avec succès !");

    }
}
