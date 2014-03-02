I'm in progress internationalization,Welcome your to join.

androrat (Fork/Silent Edition)

Remote Administration Tool for Android

Androrat is a client/server application developed in Java Android for the client side and in Java/Swing for the Server.

> The name Androrat is a mix of Android and RAT (Remote Access Tool).

It has been developed in a team of 4 for a university project. It has been realised in one month. The goal of the application is to give the control of the android system remotely and retrieve informations from it.

# Technical matters

* The android application is the client for the server which receive all the connections. 
* The android application run as a service(not an activity) that is started during the boot. So the user does not need to interact with the service (Even though there is a debug activity that allow to configure the IP and the port to connect to). 
* The connection to the server can be triggered by a SMS or a call (this can be configured)

### All the available functionalities are

* Get contacts (and all theirs informations) 
* Get call logs 
* Get all messages 
* Location by GPS/Network 
* Monitoring received messages in live 
* Monitoring phone state in live (call received, call sent, call missed..) 
* Take a picture from the camera 
* Stream sound from microphone (or other sources..) 
* Streaming video (for activity based client only) 
* Do a toast 
* Send a text message 
* Give call 
* Open an URL in the default browser 
* Do vibrate the phone

# Folders

The project contains the following folders:

* doc: Will soonly contain all the documentation about the project
* Experiment: Contain an *experimental* version of the client articulated around an activity wish allow by the way to stream video
* src/Androrat: Contain the source code of the client that should be put on the android plateform
* src/AndroratServer: Contain the sources of the Java/Swing server that can be run on any plateform
* src/api: Contain all the different api used in the project (JMapViewer for the map, forms for swing, and vlcj for video streaming)
* src/InOut: Contain the code of the content common for the client and the server which is basically the protocol implementation

# Screenshots

## Main GUI

This is the main GUI where all the clients connected appears. The list is dynamically updated when a new client connects or is disconnected. Moreover a log of all connections and global informations are showed in the log panel at the bottom of the window. A simple double-click on a client open his window to interact with him.

![Main GUI](https://raw.github.com/wszf/androrat/master/doc/main.png)

## Client Panel

All the actions with client can be made in the client window which is articulated around tabs. The default tab is called Home and provide various functionalities. First as we can see in the left scrollview all the informations about the client like sim infos, battery infos, network infos, sensors infos etc. On the right there is the options which allow remotely to change the configuration of the client like the ip and port to connect to, either or not wait a trigger to intent server connection etc. Finally quick actions can be perfomed in this tab like a toast message, do vibrate the phone or open an URL.

![Client Panel](https://raw.github.com/wszf/androrat/master/doc/homepanel.png)

## Other tabs

The two screenshots below shows two others tabs for two functionalities which are respectively get contacts and geolocation. As you can see on the get contacts panel the list on the left show all contacts the name, the phone number and the picture if available. Morevover on the right three buttons allow to get more information about the selected contact send him a sms or call him. For Geolocation we can choose our provider either GPS either network that use google to locate. Then the streaming can be started and the map will be updated as soon as data has been received.

![Contacts](https://raw.github.com/wszf/androrat/master/doc/contact.png)

![GPS tab](https://raw.github.com/wszf/androrat/master/doc/gps.png)

Use Notes

* You will need the Android SDK and Eclipse to compile this project.
* Command & Control Application is cross platform Java, tested fine on OSX, Linux and Windows
* You need to edit /src/Androrat/src/my/app/client/LauncherActivity.java before compiling the APK.
* This is where you hardcode the listeners IP and Port. You can use an IP, or a domain name like a dyndns.
* On certain mobile devices it drains battery fast. Be aware of this.

Note: I am not the author of this software, it is forked and slightly edited. I may make changes to it to fix bugs as I go along, but I am not a java programmer at heart, and only have this here for my own use in testing.

