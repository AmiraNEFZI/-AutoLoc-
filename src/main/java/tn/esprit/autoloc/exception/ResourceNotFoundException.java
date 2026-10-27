package tn.esprit.autoloc.exception;

/**
 * Exception métier levée lorsqu'une ressource demandée n'est pas trouvée en base de données.
 * Utilisée par la couche Service pour traduire l'absence de résultat.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructeur avec le nom de la ressource et son identifiant.
     * 
     * @param ressource Le nom de la ressource (ex: "Contrat", "Client")
     * @param id L'identifiant recherché
     */
    public ResourceNotFoundException(String ressource, Long id) {
        super(ressource + " introuvable avec l'identifiant " + id);
    }
}
