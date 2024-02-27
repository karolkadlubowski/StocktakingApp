# Stocktaking application

The application is an advanced mobile solution leveraging Google's ML Kit for text and barcode recognition to facilitate inventory management. It enables efficient conversion of handwritten inventory numbers into 1D/2D codes, significantly speeding up the inventory process. By utilizing Google ML Kit, the app offers robust text recognition capabilities, allowing for accurate scanning and digitization of inventory numbers. Additionally, the integration with Zebra printers enhances the app's functionality by enabling the immediate printing of barcode labels. This not only streamlines the inventory management process but also ensures accuracy and efficiency in tracking and managing inventory items.

The application is built in MVVM architecture. The main issues of the project are:<br>
-Jetpack Compose<br>
-Room Database<br>
-Coroutines<br>
-Flow<br>
-View Model<br>
-Dependency Injection - Hilt<br>
-Google Machine Learning Kit<br>
-Bluetooth<br>
-Zebra Printer handling<br>
-Unit tests<be>

The main screen of the application, which appears upon launch, displays objects stored in the database. Users can filter the list by name or number. Clicking on an object tile opens its edit screen. From the list screen, users can also print a label by clicking the printer icon on the object tile. The top right corner features three icons for navigating to the scan screen, object modification screen, and configuration screen.

<img src ="https://github.com/karolkadlubowski/StocktakingApp/blob/main/screenshots/Screenshot_1708950906.png" width="200"/>

The scanning screen allows for simultaneous scanning of text and barcodes. Text scanning seeks inventory numbers, matching them against a configured pattern. If a match is found, it directs to the add new object screen. For unmatched patterns, an image cropping function is available, requiring users to photograph, crop, and possibly rotate the image for clarity, leading to the addition of a new object.

<p float="left">
<img src ="https://github.com/karolkadlubowski/StocktakingApp/blob/main/screenshots/Screenshot_1708950105.png" width="200"/>
<img src ="https://github.com/karolkadlubowski/StocktakingApp/blob/main/screenshots/Screenshot_1708951285.png" width="200"/>
<img src ="https://github.com/karolkadlubowski/StocktakingApp/blob/main/screenshots/Screenshot_1708951295.png" width="200"/>
</p>

The object modification screen serves both for adding and editing objects, allowing users to input all characteristics of an object. In edit mode, an icon for deleting the object from the database is available in the top right corner.

<p float="left">
<img src="https://github.com/karolkadlubowski/StocktakingApp/blob/main/screenshots/Screenshot_1708950749.png" width="200"/>
<img src="https://github.com/karolkadlubowski/StocktakingApp/blob/main/screenshots/Screenshot_1708968455.png" width="200"/>
</p>

The configuration screen is essential for setting up the application's functionality related to other screens, requiring configuration upon the first launch. It includes specifying an inventory number scheme, translating into a regular expression for identifying letters, digits, and special characters. Users can select the type of labels to print, choosing between barcodes and QR codes. Lastly, it involves selecting a printer for the smartphone to connect to, with instructions for pairing if the printer is not listed.

<img src="https://github.com/karolkadlubowski/StocktakingApp/blob/main/screenshots/Screenshot_1708962673.png" width="200"/>
