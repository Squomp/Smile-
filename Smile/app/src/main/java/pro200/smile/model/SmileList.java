package pro200.smile.model;

import java.util.ArrayList;

public class SmileList {

    public ArrayList<Smile> smiles;

    public ArrayList<Smile> getSmiles() {
        return smiles;
    }

    public void addSmile(Smile s) {
        smiles.add(s);
    }

    public SmileList() {
        smiles = new ArrayList<>();
    }
}
