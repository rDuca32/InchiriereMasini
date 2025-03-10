package domain;

import service.ServiceMasina;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InchiriereConverter extends EntityConverter<Inchiriere> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private ServiceMasina serviceMasina;

    public InchiriereConverter(ServiceMasina serviceMasina) {
        this.serviceMasina = serviceMasina;
    }

    @Override
    public String toString(Inchiriere inchiriere) {
        return inchiriere.getId() + "," +
                inchiriere.getMasina().getId() + "," +
                DATE_FORMAT.format(inchiriere.getDataInceput()) + "," +
                DATE_FORMAT.format(inchiriere.getDataSfarsit());
    }

    @Override
    public Inchiriere fromString(String string) {
        String[] parts = string.split(",");
        int id = Integer.parseInt(parts[0]);
        int masinaId = Integer.parseInt(parts[1]);

        Masina masina = serviceMasina.findMasina(masinaId);
        if (masina == null) {
            throw new RuntimeException("Masina cu ID-ul " + masinaId + " nu a fost gasita.");
        }

        try {
            return new Inchiriere(id, masina, DATE_FORMAT.parse(parts[2]), DATE_FORMAT.parse(parts[3]));
        } catch (ParseException e) {
            throw new RuntimeException("Format invalid pentru datele de inchiriere", e);
        }
    }
}
