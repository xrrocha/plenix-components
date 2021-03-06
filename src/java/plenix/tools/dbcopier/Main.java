package plenix.tools.dbcopier;

import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.xml.XmlBeanFactory;

import plenix.tools.XMLTransformer;

/**
 * Main.
 */
public class Main {
    // FIXME: This belongs to DBCopier!
    private static XMLTransformer transformer;
    static {
        try {
            transformer = new XMLTransformer(Main.class.getResourceAsStream("DBCopier.xsl"));
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating DBCopier transformer", e);
        }
    }

    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream(args[0]);
        XmlBeanFactory factory = new XmlBeanFactory(transformer.transform(in));
        DBCopier copier = (DBCopier) factory.getBean("dbCopier"); 

        // TODO: Read *typed* params from command line

        copier.copy(null);
    }
}
