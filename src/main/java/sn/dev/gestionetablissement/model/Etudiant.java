package sn.dev.gestionetablissement.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.transaction.UserTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.persistence.OneToOne;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "etudiant")

public class Etudiant {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String matricule ;
        private String nom;
        private String prenom;
        private String email;
        private String telephone;
        private String adresse;

//        @OneToOne(mappedBy = "student")
//        private Registration registration; // Lien avec l'inscription
}

