import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class NMBSFileReader {

    // Deze methode leest de prestaties die ingegeven zijn als input,
    // vervolgens worden de attributen van de NMBSFactory klaargezet voor optimalisatie.
    public static NMBSFactory getNMBSFactoryFromInput(String filename) throws Exception {

        int idGenerator = 100;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(filename));
        doc.getDocumentElement().normalize();

        HashMap<Integer, Prestation> idToPrestation = new HashMap<>(); // Mapping id naar prestation object
        HashMap<Integer, ArrayList<Prestation>> dayToPrestations = new HashMap<>(); // Prestaties van die specifieke dag bv. 0 = MAANDAG
        for(int d = 0; d < 7; d++){
            dayToPrestations.put(d, new ArrayList<Prestation>());
        }

        NodeList schedules = doc.getElementsByTagName("Schedule");
        Element s = (Element) schedules.item(0);
        int amountOfWeeks = Integer.parseInt(s.getAttribute("amountOfWeeks"));

        NodeList prestationList = doc.getElementsByTagName("prestation");
        for(int i = 0; i < prestationList.getLength(); i++){
            Element prestation = (Element) prestationList.item(i);
            Prestation p = new Prestation(
                    Integer.parseInt(prestation.getAttribute("id")),
                    Integer.parseInt(prestation.getAttribute("optID")),
                    Integer.parseInt(prestation.getAttribute("startTime")),
                    Integer.parseInt(prestation.getAttribute("endTime")));

            // Het toevoegen van de operaties aan het prestatie-object
            NodeList operationsInPrestation = prestation.getElementsByTagName("operation");
            for(int j = 0; j < operationsInPrestation.getLength(); j++){
                Element operation = (Element) operationsInPrestation.item(j);
                Operation o = new Operation(
                        Integer.parseInt(operation.getAttribute("id")),
                        operation.getAttribute("label"),
                        operation.getAttribute("startStation"),
                        operation.getAttribute("endstation"),
                        Integer.parseInt(operation.getAttribute("startTime")),
                        Integer.parseInt(operation.getAttribute("endTime"))
                );

                p.addOperation(o);
            }

            // Prestatie toevoegen aan elke dag van de week waarin hij ingepland moet worden
            for(int j = 0; j < prestation.getAttribute("days").length(); j++){
                char c = prestation.getAttribute("days").charAt(j);
                dayToPrestations.get(Character.getNumericValue(c)).add(p);
            }

            // Prestatie toevoegen aan id-mapping
            idToPrestation.put(p.getOptimizationID(), p);

        }

        return new NMBSFactory(amountOfWeeks, idToPrestation, dayToPrestations);
    }

}
