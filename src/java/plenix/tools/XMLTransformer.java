package plenix.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * XMLTransformer.
 */
public class XMLTransformer {
    private Templates templates;
    
    public XMLTransformer(InputStream in) throws TransformerConfigurationException {
        templates = TransformerFactory.newInstance().newTemplates(new StreamSource(in));
    }
    
    public InputStream transform(InputStream in) throws TransformerException {
        Transformer transformer = templates.newTransformer();
        Source source = new StreamSource(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Result result = new StreamResult(out);
        transformer.transform(source, result);
        return new ByteArrayInputStream(out.toByteArray());
    }
}
