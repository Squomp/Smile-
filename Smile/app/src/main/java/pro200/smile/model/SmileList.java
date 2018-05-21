package pro200.smile.model;

import java.util.ArrayList;
import java.util.List;

public class SmileList {

    public List<Smile> smiles;

    public List<Smile> getSmiles() {
        return smiles;
    }

    public void addSmile(Smile s) {
        smiles.add(s);
    }

    public SmileList() {
        smiles = new ArrayList<>();
    }
}
