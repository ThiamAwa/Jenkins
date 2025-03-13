cette Cette application permet de gérer un système de 
gestion d’etablissement superieur avec le module 8
en utilisant Spring Boot, Spring Data JPAElle prend 
en charge l'enregistrement et la gestion des voitures, avec un CRUD complet.

Technologies utilisées
•	Java 17+
•	Spring Boot 3+
•	Spring Data JPA
•	Postgres 
•	Lombok (facultatif, pour simplifier le code)
 Configuration de la base de données
Créez une base de données MySQL :
CREATE DATABASE gestionEtablissement;
Mettez à jour application.properties dans src/main/resources :

spring.application.name=GestionEtablissement
spring.datasource.url=jdbc:postgresql://localhost:5432/gestionEtablissement?serverTimezone=UTC
spring.datasource.username=postgres
spring.datasource.password=passer
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.thymeleaf.cache=false
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
#spring.security.user.name=root
#spring.security.user.password=123
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
📂 Structure du Projet
location-voiture/
│── src/
│   ├── main/
│   │   ├── java/sn/dev/gestionetablissement/
│   │   │   ├── model/
│   │   │   ├── doa/
│   │   │   ├── service/
│   │   │   ├── controller/
│   │   ├── resources/
│   │   │   ├── application.properties
│── pom.xml
│── README.md
Construire et exécuter l'application
mvn clean install
mvn spring-boot:run
•	📌 formulaire les inscrit
curl -X GET "http://localhost:8080/inscire"
Améliorations possibles
✅ si le temps etait augment je terminer le module.
