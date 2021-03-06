package plenix.tools.dbcopier;

import java.util.Map;

import plenix.components.copying.Copier;
import plenix.components.copying.CopyingException;

/**
 * DBCopier.
 */
public class DBCopier {
    private Copier[] copiers;
    
    public void copy(Map parameters) throws CopyingException {
        for (int i = 0; i < getCopiers().length; i++) {
            getCopiers()[i].copy(parameters);
        }
    }

    public void setCopiers(Copier[] copiers) {
        this.copiers = copiers;
    }

    public Copier[] getCopiers() {
        return copiers;
    }
}
