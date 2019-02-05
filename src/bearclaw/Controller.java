package bearclaw;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

import static javafx.collections.FXCollections.observableArrayList;

public class Controller {

//    ArrayList<String> searchTerms = new ArrayList<>();    // do it this way so we can serialize this
//    ObservableList<String> searchTermsObservable = observableArrayList(searchTerms);
    KeywordList kwords = new KeywordList();
    private GUI gui;
    private Model model;

    // Main Controller functions here

    public Controller(Model setModel) {
        this.model = setModel;
        if (hasDefaultKeywords()) {
            model.addToDebug("Found default keywords! Loading.");
            loadKeywords();
        }
        if (hasDefaultFolder()) {
            model.addToDebug("Found default save folder! Loading.");
            getDefaultFolder();
            model.addToDebug("Set default save folder to "+model.getSaveDir().toString());
        }
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    void dumpKwords() {
        model.addToDebug("Dumping keywords");
        for (String s: model.getCurrentKwords()) {
            model.addToDebug(s);
        }
    }

    void doExit() {
        // to do here:
        // clean up and save any data, etc as necessary
        Platform.exit();
        System.exit(0);
    }

    // Report generating functions here

    void batchGenerate() {
        // todo:
        // crawl through the current save directory and generate reports for ALL keywords
        // lists that are in that folder
    }

    public boolean generateReport() {
        model.addToDebug("Generating report...");
        // first, open our excel sheet
        if (model.getSaveDir() == null) {
            model.addToDebug("No output folder selected");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("UH OH");
            alert.setHeaderText(null);
            alert.setContentText("Choose a directory to save output to");

            alert.showAndWait();
            return false;
        }
        model.addToDebug("Writing to " + model.getSaveDir().toString());

        String excelFileName = "/out.xls";
        String excelFileLocation = model.getSaveDir().getPath() + excelFileName;
        // todo:
        // check if this file already exists and confirm overwrite if it does
        // where do we get file name from? generate?


        // create excel workbook
        WritableWorkbook excelOutput = null;

        try {
            excelOutput = Workbook.createWorkbook(new File(excelFileLocation));
        } catch (IOException e){
            e.printStackTrace();
        }

        // create excel sheet
        int sheet = 0;
        int words = 0;

        // hello, this is hack, please delete later::
//        for (String keyword : gui.searchTermDisplay.getItems()) {
        for (String keyword : kwords.getKeywords()) {

            model.addToDebug("generating report for " + keyword +" on sheet "+sheet);
            // pass the excel sheet to this function
            generateReportEntry(keyword, excelOutput, sheet);
            sheet++;
            words++;
        }
        if (words == 0) {
            model.addToDebug("No keywords found, aborting repott");
            return false;
        }
         // now, write everything
        model.addToDebug("Writing to report...");
        try {
            excelOutput.write();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        model.addToDebug("Closing report...");
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
        model.addToDebug("Success");
        return true;
    }

    public boolean generateReportEntry(String keyword, WritableWorkbook excelOutput, int sheet) {
        // this will eventually be replaced with custom searches!

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
            } catch (Exception e) {
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
                } catch (Exception e) {
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
                    model.addToDebug("Bad ack from eBay");
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



                model.addToDebug("found " + totalResults + " items on page " + (pageNum-1));

            } else {
                model.addToDebug("GET NOT WORKED");
                return false;
            }
        } while (totalResults == maxResultsPerPage);

        return true;
    }

    // Keyword management functions here

    KeywordList getKwords() {
        return kwords;
    }

    public boolean addItem(String toAdd) {
        toAdd = toAdd.trim();
        if ("".compareTo(toAdd) != 0 && "Add tags here...".compareTo(toAdd) != 0) {
            if (!kwords.getKeywords().contains(toAdd)) {
                this.addDebugLog("calling kword to add keyword");
                kwords.addKeyword(toAdd);
                return true;
            }
            model.addToDebug("Attempting to add duplicate key words");
        }
        return false;
    }

    public boolean removeItem(int toRemove){
        kwords.removeKeyword(toRemove);
        return true;
    }

    public boolean removeItems(ArrayList<String> toRemove){
        kwords.removeKeywords(toRemove);
        return true;
    }

    // Keyword management functions

    boolean hasDefaultKeywords() {
        File tmpDir = new File("default.bc");
        return tmpDir.exists();
    }

    void fileLoad() {
        // do saving here
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Keywords");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BearClaw Keywords", "*.bc"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(gui.getStage());
        if (selectedFile != null) {
            loadKeywords(selectedFile.toString());
        }
    }

    void fileSave() {
        if (!hasDefaultKeywords()) {
            boolean saveDefault = saveDefaultDialog("keywords");
            if (saveDefault) saveKeywords();
            else saveChooser();
        } else {
            saveChooser();

        }
    }

    void saveChooser() {
        // do saving here
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Keywords");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BearClaw Keywords", "*.bc"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(gui.getStage());
        if (selectedFile != null) {
            saveKeywords(selectedFile.toString());
        }
    }

    void saveKeywords() {
        saveKeywords("default");
    }

    Model getModel() {
        return model;
    }

    void saveKeywords(String fileName) {
        if (!fileName.contains(".")) {
            fileName += ".bc";
        }
        try {
            FileOutputStream fileOutputStream
                    = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(kwords);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            addDebugLog("Can't write default keywords");
        }
    }

    void loadKeywords() {
        loadKeywords("default");
    }

    void loadKeywords(String fileName) {
        if (!fileName.contains(".")) {
            fileName += ".bc";
        }
        try {
            FileInputStream fileInputStream
                    = new FileInputStream(fileName);
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(fileInputStream);
//            ArrayList<String> list = (ArrayList<String>) objectInputStream.readObject();

            kwords = (KeywordList) objectInputStream.readObject();
//            searchTermsObservable.clear();
//            searchTermsObservable.addAll(list);
            objectInputStream.close();
        } catch (IOException e) {
            addDebugLog("Cannot load default keywords");
            addDebugLog(e.getMessage());
        } catch (ClassNotFoundException e) {
            addDebugLog("Default keywords file corrupt");
        }
    }

    // Save folder functions

    boolean hasDefaultFolder() {
        // check if we have a default folder saved
        File tmpDir = new File("prefs.bc");
        return tmpDir.exists();
    }

    void chooseDir() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("select directory");
        File desktop = new File(System.getProperty("user.home") + File.separator + "Desktop");
        if (desktop != null) dc.setInitialDirectory(desktop);
        File selectedFile = dc.showDialog(gui.getStage());
        model.setSaveDir(selectedFile);
        if (!hasDefaultFolder()) {
            boolean doSetDefaultFolder = saveDefaultDialog("save folder");
            if (doSetDefaultFolder) setDefaultFolder();
        }
    }

    void setDefaultFolder() {
        // save default saving folder here
        // to do: serialize the file that points to our default folder for saving
        try {
            FileOutputStream fileOutputStream
                    = new FileOutputStream("prefs.bc");
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(model.getSaveDir());
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            addDebugLog("Can't write default keywords");
        }
    }

    void getDefaultFolder() {
        // load default saving folder here
        // to do: deserialize the file and set default folder
        try {
            FileInputStream fileInputStream
                    = new FileInputStream("prefs.bc");
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(fileInputStream);
            File setDir = (File) objectInputStream.readObject();
            model.setSaveDir(setDir);
            objectInputStream.close();
        } catch (IOException e) {
            addDebugLog("Cannot load default keywords");
        } catch (ClassNotFoundException e) {
            addDebugLog("Default keywords file corrupt");
        }
    }

    // Debug Log functions

    void openLog() {
        // show debug log information here, this only persists per session
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Debug Log");
        alert.setHeaderText("Debug info for this session");

        Label label = new Label("Log:");

        String disp = model.getDebugLog().toString();
        disp = disp.substring(1, disp.length() - 1);
        disp = disp.replace(",","\n");
        TextArea textArea = new TextArea(disp);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait();
    }

    void addDebugLog(String msg) {
        model.addToDebug(msg);
    }

    // Other functions here

    boolean saveDefaultDialog(String which) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save default");
        alert.setHeaderText("Looks like you have no "+which+" default yet. Save this as default?");
        alert.setContentText("Choose wisely");

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonYes){
            return true;
        } else if (result.get() == buttonNo) {
            return false;
        }
        return false;
    }

    void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("This is where the about message will go, I guess.");

        alert.showAndWait();
    }

}


/*
how we will make calls:

HTTPS Headers or URL Parameters—Each Finding API call requires certain HTTPS headers or URL parameters.
For example, you must specify your AppID in the X-EBAY-SOA-SECURITY-APPNAME header or SECURITY-APPNAME URL
parameter. Similarly, you must always specify the call name in the X-EBAY-SOA-OPERATION-NAME header or
OPERATION-NAME URL parameter. Other headers are optional or conditionally required. (Is that what including
our dev id in the call does?)

http://svcs.ebay.com/services/search/FindingService/v1?
   OPERATION-NAME=findCompletedItems&
   SERVICE-VERSION=1.7.0&
   SECURITY-APPNAME=TylerWes-BearClaw-PRD-7bddb6120-21fb4e35&
   RESPONSE-DATA-FORMAT=XML&
   REST-PAYLOAD&
   keywords=OUR KEYWORDS GO HERE!&
   categoryId=64482&
   itemFilter(0).name=SoldItemsOnly&
   itemFilter(0).value=true&
   sortOrder=PricePlusShippingLowest&
   paginationInput.entriesPerPage=2



 */

// app key:
// TylerWes-BearClaw-PRD-7bddb6120-21fb4e35

// dev id:
// d35e0ed9-cedb-4720-a258-ab8109363518

// cert id:
// PRD-bddb612064b3-6428-46f8-b6ca-da45

// fields to check / grab:
// searchResult.item.sellingStatus.sellingState
//        EndedWithSales
//        The listing has been ended with sales.

// searchResult.item.title
//      name of item as listed

// searchResult.item.location
//      physical location of the item

// searchResult.item.sellingStatus.convertedCurrentPrice --> return double
// The listing's current price converted to the currency of the site specified in the find request (globalId).

// categories: Sports Mem, Cards & Fan Shop	-> 64482

//example:
/*
http://svcs.ebay.com/services/search/FindingService/v1?
   OPERATION-NAME=findCompletedItems&
   SERVICE-VERSION=1.7.0&
   SECURITY-APPNAME=YourAppID&
   RESPONSE-DATA-FORMAT=XML&
   REST-PAYLOAD&
   keywords=Garmin+nuvi+1300+Automotive+GPS+Receiver&
   categoryId=156955&
   itemFilter(0).name=Condition&
   itemFilter(0).value=3000&
   itemFilter(1).name=FreeShippingOnly&
   itemFilter(1).value=true&
   itemFilter(2).name=SoldItemsOnly&
   itemFilter(2).value=true&
   sortOrder=PricePlusShippingLowest&
   paginationInput.entriesPerPage=2
 */
