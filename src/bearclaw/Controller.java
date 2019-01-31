package bearclaw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.ListView;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;

public class Controller {

    ArrayList<String> searchTerms = new ArrayList<String>();    // do it this way so we can serialize this
    ObservableList<String> searchTermsObservable = observableArrayList(searchTerms);
    GUI gui;

    public Controller(Model setModel) {
//        this.model = setModel;
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }


    public boolean generateReport() {
        GUI.setDebugText("Generating report...");
        // first, open our excel sheet
        String EXCEL_FILE_LOCATION = "/Users/tylerweston/Desktop/out.xls";

        // create excel workbook
        WritableWorkbook excelOutput = null;

        try {
            excelOutput = Workbook.createWorkbook(new File(EXCEL_FILE_LOCATION));
        } catch (IOException e){
            e.printStackTrace();
        }

        // create excel sheet
//        WritableSheet outSheet = excelOutput.createSheet("Sheet 1", 0);
        int sheet = 0;
        for (String keyword : searchTermsObservable) {
            gui.setDebugText("generating report for " + keyword +" on sheet "+sheet);
            // pass the excel sheet to this function
            generateReportEntry(keyword, excelOutput, sheet);
            sheet++;
        }
        // now, write everything
        gui.setDebugText("Writing to report...");
        try {
            excelOutput.write();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        gui.setDebugText("Closing report...");
        // close book
        if (excelOutput != null) {
            try {
                excelOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
        gui.setDebugText("Success");
        return true;
    }

    public boolean addItem(String toAdd) {
        searchTermsObservable.add(toAdd);
        return true;
    }

    public boolean removeItem(int toRemove){
        searchTermsObservable.remove(toRemove);
        return true;
    }

    public boolean generateReportEntry(String keyword, WritableWorkbook excelOutput, int sheet) {
        // this will eventually be replaced with custom searches!
//        String keywords = "tie%20domi";

        String keywords = keyword.replace(" ", "%20");
        int pageNum = 1;    // current page to search
        int maxResultsPerPage = 100;

        // create excel sheet
        WritableSheet outSheet = excelOutput.createSheet(keyword, sheet);

        int totalResults = 0;

        // make this a string-builder and format the line we want properly
        // can we do this with a string instead of stringbuilder?
        StringBuilder https_url_sb = new StringBuilder("http://svcs.ebay.com/services/search/FindingService/v1?");
        https_url_sb.append("OPERATION-NAME=findCompletedItems&");
        https_url_sb.append("SERVICE-VERSION=1.7.0&");
        https_url_sb.append("SECURITY-APPNAME=TylerWes-BearClaw-PRD-7bddb6120-21fb4e35&");
        https_url_sb.append("RESPONSE-DATA-FORMAT=XML&");
        https_url_sb.append("GLOBAL-ID=EBAY-ENCA&");               // remove this for us site
        https_url_sb.append("REST-PAYLOAD&");
        https_url_sb.append("keywords=");
        https_url_sb.append(keywords);
        https_url_sb.append("&");
        https_url_sb.append("categoryId=64482&");
        https_url_sb.append("itemFilter(0).name=SoldItemsOnly&");
        https_url_sb.append("itemFilter(0).value=true&");
        https_url_sb.append("sortOrder=PricePlusShippingHighest&");
//        https_url_sb.append("paginationInput.entriesPerPage=2");    // remove this to get all results
        https_url_sb.append("paginationInput.pageNumber=");
        //https_url_sb.append(pageNum);
        // how to specify which specific fields we want to receive?

        // okay main loop starts here,
        // grab every page number until total results < 100
//        System.out.println("generated url " + https_url_sb);
        do {
            // after we've built our string using our stringsbuilder, convert it to a string
            String https_url = https_url_sb.toString() + pageNum;

            pageNum++;  // next loop lets look at next page

            // this will be our url
            URL url;
            try {
                url = new URL(https_url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            }
            String readLine;
            HttpURLConnection connection;
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            //DocumentBuilderFactory factory =
            //        DocumentBuilderFactory.newInstance();
            int responseCode;
            try {
                connection.setRequestMethod("GET");
                responseCode = connection.getResponseCode();
            } catch (ProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuffer response;
                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    response = new StringBuffer();
                    while ((readLine = in.readLine()) != null) {
                        response.append(readLine);
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                // print result
                Document doc;
                try {
                    doc = DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .parse(new InputSource(new StringReader(response.toString())));
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } catch (SAXException e) {
                    e.printStackTrace();
                    return false;
                }

                Element rroot = doc.getDocumentElement();
                doc.getDocumentElement().normalize();   // what does this do/why do we do it?
                String got_results = rroot.getElementsByTagName("ack").item(0)
                        .getTextContent();  // want this to be Success, otherwise, exit or throw
                // error message or something smart like that
//                System.out.println(rroot.getTagName()); // should be findCompleted..something
//                System.out.println(got_results); // this should be SUCCESS to make sure it is good
                if (got_results.compareTo("Success") != 0) {
                    gui.setDebugText("Bad ack from eBay");
                    return false;
                }

                NodeList nList = doc.getElementsByTagName("item");
                Node curItem;
                Element parseItem;

                String itemName;
                String itemPrice;
                String itemSoldDate;

                totalResults = nList.getLength();

                jxl.write.Label labelItemName;
                jxl.write.Label labelItemPrice;
                jxl.write.Label labelSoldDate;

                labelItemName = new jxl.write.Label(0, 0, "Name");
                labelItemPrice = new jxl.write.Label(1, 0, "Selling Price");
                labelSoldDate = new jxl.write.Label(2, 0, "Selling Date");
                try {
                    outSheet.addCell(labelItemName);
                    outSheet.addCell(labelItemPrice);
                    outSheet.addCell(labelSoldDate);
                } catch (WriteException e) {
                    e.printStackTrace();
                    return false;
                }

                for (int i = 0; i < totalResults; i++) {
                    curItem = nList.item(i);

                    if (curItem.getNodeType() == Node.ELEMENT_NODE) {
                        parseItem = (Element) curItem;

                        // under here is where we will decide what we are going to output to the XL file:
                        // in other words, what extra information do we want to add?

                        itemName = parseItem.getElementsByTagName("title").item(0).getTextContent();
                        itemPrice = parseItem.getElementsByTagName("convertedCurrentPrice").item(0).getTextContent();
                        itemSoldDate = parseItem.getElementsByTagName("endTime").item(0).getTextContent();

//                        System.out.println("name:" + itemName + " price: " + itemPrice);
                        // this is to create a listing item if we end up wanting to do this:
                        int writeIndex = ((pageNum - 2) * maxResultsPerPage) + i + 1;
                        labelItemName = new jxl.write.Label(0, writeIndex, itemName);
                        labelItemPrice = new jxl.write.Label(1, writeIndex, itemPrice);
                        labelSoldDate = new jxl.write.Label(2, writeIndex, itemSoldDate.substring(0, 10));
                        try {
                            outSheet.addCell(labelItemName);
                            outSheet.addCell(labelItemPrice);
                            outSheet.addCell(labelSoldDate);
                        } catch (WriteException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }



                gui.setDebugText("found " + totalResults + " items on page " + (pageNum-1));

            } else {
                gui.setDebugText("GET NOT WORKED");
                return false;
            }
        } while (totalResults == maxResultsPerPage);

        return true;
    }
}