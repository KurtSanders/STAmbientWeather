/*
*  Copyright 2019 SanderSoft™
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License. You may obtain a copy of the License at:
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.
*
*  My Ambient Weather Stations
*
*  Author: Kurt Sanders, SanderSoft™
*
*  Date: 2018,2019
*/

import groovy.time.*
import java.text.DecimalFormat
import groovy.time.TimeCategory

//************************************ Version Specific ***********************************
String version()				{ return "V4.0.0" }
String appModified()			{ return "Jan-23-2019"}

//*************************************** Constants ***************************************
String appNameVersion() 		{ return "Ambient Weather Station ${version()}" }
String appShortName() 			{ return "STAmbientWeather ${version()}" }

String DTHDNI() 				{ return "${app.id}:MyAmbientWeatherStation" }
String DTHDNIActionTiles() 		{ return "${app.id}:MyAmbientSmartWeatherStationTile" }
String DTHDNIRemoteSensorName() { return "${app.id}:MyAmbientRemoteSensor"}
String DTHName() 				{ return (noColorTiles)?"Ambient Weather Station V3 No Color Tiles":"Ambient Weather Station" }
String DTHRemoteSensorName() 	{ return "Ambient Weather Station Remote Sensor"}

String DTHNameActionTiles() 	{ return "SmartWeather Station Tile" }
String AWSNameActionTiles()		{ return "SmartWeather" }
String AWSNameActionTilesHide()	{ return false }

String namespace()				{ return "kurtsanders" }
String childAppName()			{ return "My Ambient Weather Stations - Backend" }
String appAuthor()	 			{ return "SanderSoft" }
String getAppImg(imgName) 		{ return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/$imgName" }
String wikiURL(pageName)		{ return "https://github.com/KurtSanders/STAmbientWeather/wiki/$pageName"}
// ============================================================================================================
// This APP key is ONLY for this application - Do not copy or use elsewhere
String appKey() 				{return "33054086b3d745779f5ac35e147baa76f13e75d44ea245388ba598911905fb50"}
// ============================================================================================================

definition(
    name: 			"My Ambient Weather Stations",
    namespace: 		"kurtsanders",
    author: 		"kurt@kurtsanders.com",
    description: 	"My Ambient Weather Stations Install Manager ${version()}",
    category: 		"My Apps",
    iconUrl:   		getAppImg("blue-ball-100.jpg"),
    iconX2Url: 		getAppImg("blue-ball-200.jpg"),
    iconX3Url: 		getAppImg("blue-ball.jpg")
)
{
// The following API Key is to be entered in this SmartApp's settings in the SmartThings IDE App Setting.
    appSetting 		"apiKey"
}
preferences {
    page(name: "mainPage", title: "My Ambient Weather Stations", submitOnChange: true, install: true, uninstall: true) {
        section {
            app(name: "childApps", appName: childAppName(), namespace: namespace(), title: "Add a Weather Station", multiple: true)
        }
    }
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    initialize()
}

def initialize() {
    log.debug "there are ${childApps.size()} child smartapps"
    unsubscribe
    subscribe(app, appTouchHandler)
    childApps.each {child ->
        log.debug "child app: ${child.label}"
    }
}

def appTouchHandler(evt="") {
    def timeStamp = new Date().format("h:mm:ss a", location.timeZone)
    log.info "App Touch: 'Requested at ${timeStamp}"
    log.debug "There are ${childApps.size()} child smartapps defined:"
    childApps.eachWithIndex {child, i ->
        log.info "${i}) Child App Label: ${child.label}"
        log.debug "Sending appTouch() to ${child.label}"
    }
}

def getMyAmbientKey(childID) {
    log.debug "Request for myAmbientAPIkey from '${childID}'"
    return appSettings.apiKey
}