import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;


public class NMBSFileWriter {

    private static String depotName = "FSD";
    private static String reeksName = "A";
    private static String year = "2019";
    private static String period = "1269";

    public static void writeOutputFile(int[][] assigned, NMBSFactory nmbsFactory, String filename) throws Exception{

        try{
            int amountOfWeeks = nmbsFactory.getAmountOfWeeks();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Depot");
            doc.appendChild(rootElement);

            rootElement.setAttribute("name", depotName);
            rootElement.setAttribute("reeks", reeksName);
            rootElement.setAttribute("year", year);
            rootElement.setAttribute("period", period);

            Element periodSchedule = doc.createElement("PeriodSchedule");
            rootElement.appendChild(periodSchedule);

            periodSchedule.setAttribute("amountOfWeeks", String.valueOf(amountOfWeeks));

            for(int week = 0; week < assigned[0].length; week++){

                Element weekSchedule = doc.createElement("WeekSchedule");
                periodSchedule.appendChild(weekSchedule);

                for(int day = 0; day < 7; day++){
                    int scheduled = assigned[day][week];
                    String description;
                    String beginTime = "0";
                    String endTime = "1440";

                    switch(scheduled){
                        case 1:
                            description = "Repos/Rust";
                            break;
                        case 2:
                            description = "Compentatie";
                            break;
                        case 3:
                            description = "compensatieverlof";
                            break;
                        case 4:
                            description = "Overgang";
                            break;
                        case 5:
                            description = "Reserve";
                            break;
                        default:
                            description = "Prestatie";
                            beginTime = String.valueOf(nmbsFactory.getPrestation(scheduled).getStartTime());
                            endTime = String.valueOf(nmbsFactory.getPrestation(scheduled).getEndTime());
                            break;
                    }

                    Element prestation = doc.createElement("Prestation");
                    prestation.setAttribute("id", String.valueOf(scheduled));
                    prestation.setAttribute("description", description);
                    prestation.setAttribute("startTime", beginTime);
                    prestation.setAttribute("endTime", endTime);

                    weekSchedule.appendChild(prestation);
                }

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));

            transformer.transform(source, result);

        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
