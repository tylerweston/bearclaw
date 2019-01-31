package bearclaw;

/*
    bearclaw ebay price history amalgamator / spreadsheet generator
    tyler weston, dec 31st 2018
    what it does so far:
    - need to fix these things since they are broken now that the program
      is split into different classes, deal with this
    - can add / remove keywords
    - generate report for multiple items
    to do still:
    - saving the list <----------------------- next big thing to finish core functionality
    - choosing where to save the excel to (!!) fix this entirely
    - add more options (ie what data to write to file !! )
    - add keyword name to top of each sheet
    - better gui functionality
    - REFACTORING
        - written very hackily right now
    dream features:
    - multiple lists?
    - in different categories?

    - make sure to fix basic functionality next
    - take unused menu items out
    - save / load lists
    - allow different category IDs for
    - ??
 */


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {


    private Controller controller;
    @Override

    public void start(Stage primaryStage) throws Exception {

        Model model = new Model();
        controller= new Controller(model);
        GUI gui = new GUI(primaryStage, controller);

        controller.setGUI(gui);
    }

    public static void main(String[] args) {
        launch(args);
        // is this where the main method does things?
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

// to do:
//  - easy gui
//  - saving / loading list of information you want to scrape
//  x how to make calls / receive data back
//  - validate call was a success by checking ACK data
//  - now that we have data, how do we pull out the information we need?
//  - how can we make sure we only get the information we want instead of all of it?
//  - how to parse the JSON that is returned?
//  - how to write this information to a spreadsheet?

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
