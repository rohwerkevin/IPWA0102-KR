package rohwerkevin.ghotsnetfishing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class GeisternetzBean implements Serializable {

    @Inject
    private GeisternetzDAO geisternetzDAO;

    private List<Geisternetz> allGeisternetze;
    private List<Geisternetz> filteredGeisternetze;
    private Geisternetz selectedGeisternetz;
    private String personName;
    private boolean anonymous;
    private double longitude;
    private double latitude;
    private double size;
    private String description;

    private String statusFilter = "ALL"; // Standardfilter: Alle anzeigen

    @PostConstruct
    public void init() {
        allGeisternetze = geisternetzDAO.getAllGeisternetze();
        applyFilter();
    }

    // Methode zum Anwenden des Filters
    private void applyFilter() {
        if ("ALL".equalsIgnoreCase(statusFilter)) {
            filteredGeisternetze = allGeisternetze;
        } else {
            filteredGeisternetze = allGeisternetze.stream()
                    .filter(netz -> netz.getStatus().name().equalsIgnoreCase(statusFilter))
                    .collect(Collectors.toList());
        }
    }

    // Methode zum Setzen des Statusfilters
    public void setStatusFilter(String status) {
        this.statusFilter = status;
        applyFilter();
    }

    // Methode zum Speichern eines neuen Geisternetzes
    public void saveGeisternetz() {
        try {
            Geisternetz geisternetz = new Geisternetz();
            geisternetz.setStandortLatitude(this.latitude);
            geisternetz.setStandortLongitude(this.longitude);
            geisternetz.setGeschaetzteGroesse(this.size);
            geisternetz.setBeschreibung(this.description);
            // Standardstatus setzen
            geisternetz.setStatus(Status.GEMELDET);
            // Optional: Verarbeitung der Person
            if (!anonymous && personName != null && !personName.isEmpty()) {
                Person meldendePerson = new Person();
                meldendePerson.setName(personName);
                geisternetz.setMeldendePerson(meldendePerson);
            }

            // Persistieren des Geisternetzes
            geisternetzDAO.persist(geisternetz);

            // Aktualisiere die Liste nach dem Hinzufügen eines neuen Geisternetzes
            allGeisternetze = geisternetzDAO.getAllGeisternetze();
            applyFilter(); // Wende den aktuellen Filter erneut an

            // Erfolgsmeldung an den Benutzer
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Danke, deine Meldung wurde erfolgreich entgegengenommen", "Geisternetz erfolgreich gemeldet."));
        } catch (Exception e) {
            // Fehlerbehandlung
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Das hat leider nicht geklappt", "Fehler beim Speichern des Geisternetzes."));
        }
    }

    // Methode zum Markieren eines Geisternetzes als Bergung bevorstehend
    public void markAsPendingRecovery() {
        if (selectedGeisternetz == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warnung", "Kein Geisternetz ausgewählt"));
            return;
        }
        geisternetzDAO.updateStatus(selectedGeisternetz.getId(), Status.BERGUNG_BEVORSTEHEND);
        // Aktualisiere die Liste
        allGeisternetze = geisternetzDAO.getAllGeisternetze();
        applyFilter(); // Wende den aktuellen Filter erneut an
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Status auf 'Bergung bevorstehend' gesetzt", "Status auf 'Bergung bevorstehend' gesetzt"));
    }

    // Methode zum Markieren eines Geisternetzes als verschollen
    public void markAsLost() {
        if (selectedGeisternetz == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warnung", "Kein Geisternetz ausgewählt"));
            return;
        }
        geisternetzDAO.updateStatus(selectedGeisternetz.getId(), Status.VERSCHOLLEN);
        // Aktualisiere die Liste
        allGeisternetze = geisternetzDAO.getAllGeisternetze();
        applyFilter(); // Wende den aktuellen Filter erneut an
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Status auf 'Verschollen' gesetzt", "Status auf 'Verschollen' gesetzt"));
    }

    // Methode zum Markieren eines Geisternetzes als Bergung bevorstehend
    public void markAsRecovered() {
        if (selectedGeisternetz == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warnung", "Kein Geisternetz ausgewählt"));
            return;
        }
        geisternetzDAO.updateStatus(selectedGeisternetz.getId(), Status.GEBORGEN);
        allGeisternetze = geisternetzDAO.getAllGeisternetze();
        applyFilter();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Status auf 'Geborgen' gesetzt", "Status auf 'Geborgen' gesetzt"));
    }

    // Methode, die aufgerufen wird, wenn eine Zeile in der DataTable ausgewählt wird
    public void onRowSelect() {
        if (selectedGeisternetz != null) {
            System.out.println("Geisternetz ausgewählt - ID: " + selectedGeisternetz.getId());
            // Weitere Logik, z.B. Anzeige von Details
        }
    }

    public List<Geisternetz> getGeisternetzeForMap() {
        return allGeisternetze.stream()
                .filter(netz -> netz.getStatus() != Status.GEBORGEN)
                .collect(Collectors.toList());
    }

    public String getGeisternetzeForMapJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(getGeisternetzeForMap());
    }




    // Getter und Setter
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public List<Geisternetz> getAllGeisternetze() {
        return allGeisternetze;
    }

    public void setAllGeisternetze(List<Geisternetz> allGeisternetze) {
        this.allGeisternetze = allGeisternetze;
    }

    public List<Geisternetz> getFilteredGeisternetze() {
        return filteredGeisternetze;
    }

    public void setFilteredGeisternetze(List<Geisternetz> filteredGeisternetze) {
        this.filteredGeisternetze = filteredGeisternetze;
    }

    public Geisternetz getSelectedGeisternetz() {
        return selectedGeisternetz;
    }

    public void setSelectedGeisternetz(Geisternetz selectedGeisternetz) {
        this.selectedGeisternetz = selectedGeisternetz;
    }

    public String getStatusFilter() {
        return statusFilter;
    }

    // Optional: Base64-kodierter JSON-String (falls notwendig)
    public String getGeisternetzDataJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(allGeisternetze);
    }

    public String getGeisternetzDataJsonBase64() {
        String json = getGeisternetzDataJson();
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }
}
