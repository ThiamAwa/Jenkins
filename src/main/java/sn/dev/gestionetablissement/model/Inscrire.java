package sn.dev.gestionetablissement.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Inscrire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateInscription;
    private String statut;
    private String niveau;
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate AnneAcad;
private String AnneAcad = "2026-2025";
    @OneToOne
    private Paiement paiement;

    @OneToOne
    private Etudiant etudiant;



}
