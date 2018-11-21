# STAmbientWeather
*SmartThings Integration for Ambient Weather Stations*

## Description:

A custom SmartThings Device Handler (DTH) which connects to the data generated from an [Ambient weather station](https://www.ambientweather.com/ambientnet.html).  This SmartThings device handler accesses the [Ambientweather.net](https://ambientweather.net/) weather data using the AmbientWeather API which is near realtime.  The user can set the refresh rate of the weather data from 1 min to 180 mins (3 hours).

![STAmbientWeather logo](https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/STMobileClient.PNG)

## Requirements:
1. [Ambient Weather Station](https://www.ambientweather.com/ambientnet.html) which connects to the Ambient Weather Network: (e.g. Model 2902A for example)
2. SmartThings Hub
3. Supported mobile device with ST Client:
	* Apple iOS device or 
	* Android mobile device.  *Know issue: The Android O/S ST client may not be able to display the custom weather tile, but Ambient weather API attributes are accessible from ST WebCore, etc*
4. A working knowledge of the SmartThings IDE
	* Installing a DTH from a GitHub repository (see [WebCore Wiki](https://wiki.webcore.co/) for example instructions and use the repository Owner, Name and Branch from below)
	* Creating a custom device
5. Ambient Weather Station API/APP Keys
	* API Key
	* Application Key
 
	An Ambient Weather Station API Key is used to securely connect this program to your Ambient weather station data. 
	
	You must request an Ambient Weather Station Application Key.
	 
	* Email [support@ambientweather.com](mailto:support@ambientweather.com) and include a brief description of need, ie. "connecting to my weaather station from a local application". You must also include the MAC address of your weather station in the email.

## Installation & Configuration of the SmartThings DTH

**GitHub Repository Integration**

Create a new SmartThings Repository in the SmartThings IDE under 'Settings':

* Owner: 		KurtSanders
* Name:		STAmbientWeather
* Branch: 	Master

1. Create a new device handler by installing the 'STAbientWeather' DTH into the SmartThings IDE "My Device Handlers".  Make sure to 'Save' and 'Publish' the new DTH
2. Create a New Device in the SmartThings IDE 'My Devices'
	*  Name: "My Ambient Weather Station Tile"
	*  Type: "Ambient Weather Station Tile"
	*  Device Network Id:	"Ambient123"
	*  Preferences (you can edit your data here or the Tile Preferences 'Gear Icon')
		* apiString (Your Ambient API Key)
		* appString (Your Ambient App Key)
		* debugVerbose	(Enter 'false')
		* infoVerbose	bool	(Enter 'false')
		* schedulerFreq	(# mins)
		* zipCode	(Your Zipcode for Weather Forecast Info)
3. Display the new SmartThings Tile in your ST Mobile Client
4. Verify the user data in the 'Gear' preferences section of the tile
	* Enter API and APP key into the Device Handler either in the device IDE or Tile Gear.

![STAmbientWeather Mobile Client Settings](https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/STMobileClientSettings.png)



