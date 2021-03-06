package by.vbalanse.spark.test;

import org.jdom.*;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.EscapeStrategy;
import org.jdom.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Created by Vasilina_Terehova on 12/10/2016.
 */
public class XmlUtilsJDom1 {
    public static void outputDoc(Document document, String fullFileName) throws IOException {
        XMLOutputter xmlOutput = new XMLOutputter() {
            @Override
            public String escapeAttributeEntities(String str) {

                EscapeStrategy strategy = this.currentFormat.getEscapeStrategy();
                StringBuffer buffer = null;

                for (int i = 0; i < str.length(); ++i) {
                    int ch = str.charAt(i);
                    String entity;
                    switch (ch) {
                        case 9:
                            entity = "&#x9;";
                            break;
                        case 10:
                            entity = "&#xA;";
                            break;
                        case 13:
                            entity = "&#xD;";
                            break;
                        case 34:
                            entity = "&quot;";
                            break;
                        case 38:
                            entity = "&amp;";
                            break;
                        default:
                            if (strategy.shouldEscape((char) ch)) {
                                if (Verifier.isHighSurrogate((char) ch)) {
                                    ++i;
                                    if (i >= str.length()) {
                                        throw new IllegalDataException("Surrogate pair 0x" + Integer.toHexString(ch) + " truncated");
                                    }

                                    char low = str.charAt(i);
                                    if (!Verifier.isLowSurrogate(low)) {
                                        throw new IllegalDataException("Could not decode surrogate pair 0x" + Integer.toHexString(ch) + " / 0x" + Integer.toHexString(low));
                                    }

                                    ch = Verifier.decodeSurrogatePair((char) ch, low);
                                }

                                entity = "&#x" + Integer.toHexString(ch) + ";";
                            } else {
                                entity = null;
                            }
                    }

                    if (buffer == null) {
                        if (entity != null) {
                            buffer = new StringBuffer(str.length() + 20);
                            buffer.append(str.substring(0, i));
                            buffer.append(entity);
                        }
                    } else if (entity == null) {
                        buffer.append((char) ch);
                    } else {
                        buffer.append(entity);
                    }
                }

                return buffer == null ? str : buffer.toString();
            }
        };
        // display nice nice
        //xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(document, new FileWriter(Paths.get(fullFileName).toString()));
    }

    public static org.jdom.Document getDocumentFromFile(String fullName) throws JDOMException, IOException {
        SAXBuilder jdomBuilder = new SAXBuilder();
        return jdomBuilder.build(fullName);
    }

}
