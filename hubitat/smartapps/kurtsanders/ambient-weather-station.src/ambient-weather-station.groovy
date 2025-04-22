/*
*  Copyright 2025 SanderSoft™
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
*  Ambient Weather Station
*
*  Author: Kurt Sanders, SanderSoft™
*
*  Dates: 2018,2019,2020,2021,2022,2023,2024,2025
*/

#include kurtsanders.AWSLibrary
@Field static String PARENT_DEVICE_NAME            = "Ambient Weather Station"
@Field static final String VERSION                 = "6.4.0"

//************************************ Version Specific ***********************************
String appModified()			{ return "Apr-21-2025" }
//*************************************** Constants ***************************************

String appNameVersion() 		{ return "Ambient Weather Station " + VERSION }
String appShortName() 			{ return "STAmbientWeather " + VERSION }

String DTHName() 				{ return PARENT_DEVICE_NAME  }
String DTHRemoteSensorName() 	{ return PARENT_DEVICE_NAME + " Remote Sensor"}
String DTHPMSensorName() 		{ return "Ambient Particulate Monitor"}
String DTHDNI() 				{ return "${app.id}:MyAmbientWeatherStation" }
String DTHDNIRemoteSensorName() { return "${app.id}:MyAmbientRemoteSensor"}
String DTHDNIPMName() 			{ return "${app.id}:MyAmbientParticulateMonitor"}
String DTHDNIActionTiles() 		{ return "${app.id}:MyAmbientSmartWeatherStationTile" }
Integer MaxNumRemoteSensors()	{ return 8 }

String DTHnamespace()			{ return NAMESPACE }
String appAuthor()	 			{ return AUTHOR_NAME }
String AppImg(imgName) 			{ return GITHUB_IMAGES_LINK + "${imgName}" }
String wikiURL(pageName)		{ return "https://github.com/KurtSanders/STAmbientWeather/wiki/$pageName"}
Integer wm2lux(value)			{ return (value * 126.7).toInteger() }
Integer wm2fc(value)			{ return (wm2lux(value) * 0.0929).toInteger() }

// ============================================================================================================
// This APP key is ONLY for this application - Do not copy or use elsewhere
@Field static final String APPKEY                  = "33054086b3d745779f5ac35e147baa76f13e75d44ea245388ba598911905fb50"
// ============================================================================================================

definition(
    name              : PARENT_DEVICE_NAME,
    namespace         : NAMESPACE,
    author            : AUTHOR_NAME,
    description       : "Integrate your Ambient™ Weather Station and remote weather/soil/particle monitor sensors to Hubitat™",
    category          : "",
    iconUrl           : "",
    iconX2Url         : "",
    documentationLink : COMM_LINK,
	singleInstance    : false,
	pausable          :	false
)
preferences {
    page(name: "apiPage")
    page(name: "mainPage")
    page(name: "optionsPage")
    page(name: "unitsPage")
    page(name: "remoteSensorPage")
    page(name: "notifyPage")
    page(name: "DataPage")
}

def apiPage() {
    if (apiKey == null) {
        log.info "${app.name}: First Run: Initializing default 'Units of Measure' values to Imperial system"
        app.updateSetting("tempUnits", [type: "enum", value: "°F"])
        app.updateSetting("windUnits", [type: "enum", value: "mph"])
        app.updateSetting("measureUnits", [type: "enum", value: "in"])
        app.updateSetting("baroUnits", [type: "enum", value: "inHg"])
        app.updateSetting("solarRadiationTileDisplayUnits", [type: "enum", value: "W/m²"])
    }
    if (keepDataKeysAllOption == null) {
        log.info "${app.name}: First Run: Initializing default keepDataKeysAllOption = True"
        app.updateSetting("keepDataKeysAllOption", [type: "bool", value: true])
    }

    dynamicPage(name: "apiPage", submitOnChange: true, nextPage: "mainPage", uninstall: true, install: false ) {
        section(sectionHeader("Ambient Weather Station API Key")) {
            input ( name: "apiKey", type: "text",
                   title: fmtTitle("Enter your Ambient Weather Station API Key below (Required)"),
                   required: true
                  )
            paragraph ""
            href(name: "APIKeyLink",
                 title: fmtTitle("Here is a WebLink to display your Ambient Weather Station Account Page with API key (scroll to the bottom of the page to copy your API string)"),
                 required: false,
                 style: "external",
                 url: "https://ambientweather.net/account",
                 description: fmtTitle("tap to view your weather station account page and copy your API key")
                )
        }
    }
}

def mainPage() {
    def apiappSetupCompleteBool = false
    if (apiKey == state.apiKey && state.ambientMap) {
        apiappSetupCompleteBool = true
    } else {
        state.apiKey = apiKey
        apiappSetupCompleteBool = AmbientStationData(0)
    }
    def setupMessage = ""
    def setupTitle = "${appNameVersion()} API Settings Check"
    def nextPageName = getAllChildDevices().count{it}>0?"optionsPage":"remoteSensorPage"
    state.retry = 0
    def AmbientStationDataRC = (state.ambientMap)?true:false
    if (!state.ambientMap) {
        AmbientStationDataRC = AmbientStationData(0)
    }
    if (apiappSetupCompleteBool && AmbientStationDataRC) {
        setupMessage = "SUCCESS! You have completed entering a valid Ambient API Key for ${appNameVersion()}. "
        setupMessage += (weatherStationMac)?"Please Press 'Next' for additional configuration choices.":"I found ${state.ambientMap.size()} reporting weather station(s)."
        setupTitle = "<span style=\"color:red\">Please confirm the Ambient Weather Station Information below and if correct, Tap 'NEXT' to continue to the 'Settings' page'</span>"
    } else {
        setupMessage = "<span style=\"color:red\">Ambient API Setup INCOMPLETE or MISSING!\n\nPlease check and/or complete the REQUIRED Ambient Weather API key setup for ${appNameVersion()}.\n\nAPI Error message: ${state.httpError}</span>"
        nextPageName = null
    }
    dynamicPage(name: "mainPage", title: setupTitle, submitOnChange: true, nextPage: nextPageName, uninstall:true, install:false) {
        section(hideable: apiappSetupCompleteBool, hidden: apiappSetupCompleteBool, sectionHeader(setupMessage) ) {
            paragraph "<span style=\"color:blue\">The API string key is used to securely connect your weather station to ${appNameVersion()}.</span>"
            paragraph image: AppImg("blue-ball-100.jpg"),
                title: "<span style=\"color:red\">Required API Key</span>",
                required: false,
                informationList("apiHelp")
                href(name: "hrefReadme",
                     title: fmtTitle("${appNameVersion()} Setup/Read Me Page"),
                     required: false,
                     style: "external",
                     url: "https://github.com/KurtSanders/STAmbientWeather#hubitat-installation",
                     description: "tap to view the Setup/Read Me page")
            href(name: "hrefAmbient",
                 title: fmtTitle("Ambient Weather Dashboard Account Page for API Key"),
                 required: false,
                 style: "external",
                 url: "https://dashboard.ambientweather.net/account",
                 description: "tap to login and view your Ambient Weather's dashboard")
        }
        if (apiappSetupCompleteBool && AmbientStationDataRC) {
            if (weatherStationMac) {
                setStateWeatherStationData()
                state.weatherStationMac = weatherStationMac
                countRemoteTempHumiditySensors()
                section (sectionHeader("Ambient Weather Station Information")) {
                    paragraph image: AppImg("blue-ball-100.jpg"),
                        title: fmtTitle("${state.weatherStationName}"),
                        required: false,
                        "Name: ${state.weatherStationName}" +
                        "\nLocation: ${state.weatherStationLocation?:'Not Provided'}" +
                        "\nMac Address: ${state.ambientMap[state.weatherStationDataIndex].macAddress}" +
                        "\nRemote Temp/Hydro/Moisture Sensors: ${state.countRemoteTempHumiditySensors}" +
                        "\nAQIN Particulate Monitor Sensor: ${state.countParticulateMonitors}"
                    href(name: "<span style=\"color:blue\">Weather Station Options</span>",
                         page: nextPageName,
                         description: "<span style=\"color:red\"Next: Label your weather device sensor locations -></span>")
                }

            } else {
                def weatherStationList = [:]
                def stationlocation
                state.ambientMap.each {
                    stationlocation = it.info.location?:it.info.containsKey("coords")?it.info.coords.location:''
                    weatherStationList << [[ "${it.macAddress}" : "${it.info.name}${stationlocation?' @ ':''}${stationlocation}" ]]
                }
                section ("Ambient Weather Station Information") {
                    input (name: "weatherStationMac", submitOnChange: true, type: "enum",
                           title: fmtTitle("Select the Weather Station to Install"),
                           options: weatherStationList,
                           multiple: false,
                           required: true
                          )
                }
            }
        }
        section ("STAmbientWeather™ - ${appAuthor()}") {
            href(name: "hrefVersions",
                 image: AppImg("readme.png"),
                 title: fmtTitle("Release Notes for ${VERSION} : ${appModified()}"),
                 required: false,
                 style:"embedded",
                 url: wikiURL("Features-by-Version")
                )
        }
    }
}

def unitsPage() {
    dynamicPage(name: "unitsPage", title: "Ambient Units of Measure Settings for: '${state.weatherStationName}'",
                uninstall : false,
                install   : false ) {
        section("Weather Station Unit of Measure Options") {
            input ( name: "tempUnits", type: "enum",
                   title: fmtTitle("Select Temperature Units of Measure"),
                   options: ['°F':'Fahrenheit °F','°C':'Celsius °C'],
                   defaultValue: "°F",
                   required: true
                  )
            input ( name: "windUnits", type: "enum",
                   title: fmtTitle("Select Wind Speed Units of Measure"),
                   options: ['mph':'Miles per Hour','fps':'Feet per Second','mps':'Meter per Second','kph':'Kilometers per Hour','knotts':'Knotts'],
                   defaultValue: "mph",
                   required: true
                  )
            input ( name: "measureUnits", type: "enum",
                   title: fmtTitle("Select Rainfall Units of Measure"),
                   options: ['in':'Inches','mm':'Millimeters','cm':'Centimeters'],
                   defaultValue: "in",
                   required: true
                  )
            input ( name: "baroUnits", type: "enum",
                   title: fmtTitle("Select Barometer Units of Measure"),
                   options: ['inHg':'inHg','mmHg':'mmHg', 'hPa':'hPa'],
                   defaultValue: "inHg",
                   required: true
                  )
            input ( name: "solarRadiationTileDisplayUnits", type: "enum",
                   title: fmtTitle("Select Solar Radiation ('Light') Units of Measure"),
                   options: ['W/m²':'Imperial Units (W/m²)','lux':'Metric Units (lux)', 'fc':'Foot Candles (fc)'],
                   defaultValue: "W/m²",
                   required: true
                  )
        }
    }
}

def DataPage() {
    dynamicPage(name: "DataPage", title: getFormat("title", myText="Ambient Weather Station Data Key Import Selector"), submitOnChange: true,
                uninstall : false,
                install   : false ) {
        section() {
            input ( name: "keepDataKeysAllOption", type: "bool",
                   title: "<span style=\"color:red\">Select ALL Data Keys?' Toggle OFF to ONLY import partial weather data keys into Hubitat for '${state.weatherStationName} Weather Station'</span>",
                   required: true,
                   submitOnChange: true,
                   defaultValue:true
                  )
        }
        if(!keepDataKeysAllOption) {
            section () {
                def dateKeystoKeep = ["date","tz","dateutc"]
                def dataValues = new JsonSlurper().parseText(JsonOutput.toJson(state.respdata))
                dataValues = dataValues[state.weatherStationDataIndex].lastData.keySet()
                dataValues.removeAll(dateKeystoKeep)
                section (getFormat("header-blue","Partial Data Keys Selector")) {
                    def wikiURL = "https://github.com/ambient-weather/api-docs/wiki/Device-Data-Specs"
                    paragraph("<span style=\"color:blue\">This Wiki lists all the data key parameters that a AMbient Weather Station device might send.</span> <span style=\"color:red\">Note: Not all devices send all the data key parameters.</span>\n\n<span style=\"color:green\">Weather Data Name Wiki Documentation Wiki</span> <span style=\"color:red\"><i><b>(opens in a new browser tab/window, close tab/window to return): </i></b></span><a href=\"${wikiURL}\" target=\"_blank\">Click Here</a>")
                    input ( name: "keepDataKeys", type: "enum",
                           multiple: true,
                           title: "<span style=\"color:red\">Select the Data Keys of your weather station to <b><u>ONLY</b></u> import into Hubitat for '${state.weatherStationName}.'</span>",
                           options: dataValues.sort(),
                           offerAll: false,
                           submitOnChange: true,
                           required: true
                          )
                }
            }
        }
        selectWeatherKeys(state.respdata)
    }
}

def selectWeatherKeys(respdata) {
    // Keep these time/date related data keys in the filtered dataset
    def dateKeystoKeep = ["date","tz","dateutc"]
    logDebug "==> Start state.ambientMap= ${state.ambientMap}"

    if (keepDataKeysAllOption) {
        logDebug "Keeping ALL Data Keys..."
        state.ambientMap = respdata
    } else if (keepDataKeys) {
        if (debugVerbose) {
            logDebug "Partial Data Keys Selected"
            logDebug "Keeping only '${keepDataKeys}' keys"
        }
        keepDataKeys.addAll(dateKeystoKeep) // Add back the date related data keys to the keepDataKeys array
        AWSmap = new JsonSlurper().parseText(JsonOutput.toJson(state.respdata))
        AWSmap[state.weatherStationDataIndex].lastData.keySet().retainAll(keepDataKeys)
        state.ambientMap = AWSmap
        if (debugVerbose) {
            logDebug "==> End state.ambientMap keys = ${state.ambientMap[state.weatherStationDataIndex].lastData.size()}"
        }
    } else {
        logWarn "keepDataKeys LIST is Null => ${keepDataKeys}..  Changing 'keepDataKeysAllOption' to 'True' in AWS Preferences"
        app.updateSetting("keepDataKeysAllOption", true)
        state.ambientMap = respdata
    }
    if (debugVerbose) {
        logDebug "==> End state.ambientMap= ${state.ambientMap}"
        logDebug "==> End state.ambientMap keys = ${state.ambientMap[state.weatherStationDataIndex].lastData.size()}"
    }
}

def optionsPage () {
    logInfo "Ambient Weather Station: Mac: ${weatherStationMac}, Name/Loc: ${state.weatherStationName}/${state.weatherStationLocation}"
        if (app.label.contains('<span')) {
            app.updateLabel(app.label.substring(0, app.label.indexOf('<span')))
        }
    dynamicPage(name: "optionsPage", uninstall:true, install : true ) {
        section () {
            input name: "helpInfo", type: "hidden", title: fmtHelpInfo("Hubitat Community <u>WebLink</u> to ${app.name}")
            paragraph ("")
        }
        section(sectionHeader("Ambient Weather Station (AWS) Preference Settings for: <i>'${state.weatherStationName}'</i>")) {
            input ( name: "schedulerFreq", type: "enum",
                   title: fmtTitle("Poll the Ambient Weather Station Server every"),
                   options: ['0':'Off','1':'1 min','2':'2 mins','3':'3 mins','4':'4 mins','5':'5 mins','10':'10 mins','15':'15 mins','30':'Every ½ Hour','60':'Every Hour','180':'Every 3 Hours'],
                   required: true,
                   defaultValue: '15 mins'
                  )
            input (name: "showBattery", type: "bool",
                   title: fmtTitle("Show battery level from sensor(s)? (Ambient devices only report 0% and 100%)"),
                   defaultValue: true,
                   required: true
                  )
            href(name: "Weather Units of Measure",
                 title: fmtTitle("Select Weather Units of Measure"),
                 required: false,
                 defaultValue: "<span style=\"color:red\">Tap to Select Units</span>",
                 description:  tempUnits?"<span style=\"color:green\">${unitsSet()}</span>":"<span style=\"color:red\">Tap to Select Units</span>",
                 page: "unitsPage")
            href(name: "Weather Station Filtered Data Key Import Selector",
                 title: fmtTitle("Select/DeSelect the weather station data keys to import (Required)"),
                 required: true,
                 defaultValue: "<span style=\"color:red\">Tap to Select Partial Data Keys to Import</span>",
                 description:  keepDataKeysAllOption?"<span style=\"color:red\">All ${state?.ambientMap[state.weatherStationDataIndex].lastData.size()-3} Weather Data Keys are selected. </span><span style=\"color:blue\">Tap to Select Partial Weather Data Keys</span>":"<span style=\"color:red\">${keepDataKeys?.size()} of ${state.respdata[state?.weatherStationDataIndex].lastData.size()-3} Data Keys Selected. </span><span style=\"color:blue\">Tap to Modify Selection or Select ALL Weather Data Keys</span>",
                 page: "DataPage")
            href(name: "Activate Weather Alerts/Notification",
                 title: fmtTitle("Weather Alerts/Notification (Optional)"),
                 required: false,
                 defaultValue: (checkRequired([pushoverEnabled,sendSMSEnabled,sendPushEnabled]))?"<span style=\"color:green\">Alerts Activated</span> ":"<span style=\"color:blue\">Tap to Activate Alerts</span>",
                 description: (checkRequired([pushoverEnabled,sendSMSEnabled,sendPushEnabled]))?"<span style=\"color:green\">Alerts Activated</span> ":"<span style=\"color:blue\">Tap to Activate Alerts</span>",
                 page: "notifyPage")
            href(name: "Weather Sensor Device Label Management",
                 title: fmtTitle("Weather Sensor Names"),
                 description: "<span style=\"color:blue\">Tap to Change the Device Label of ${state.countRemoteTempHumiditySensors + state.countParticulateMonitors + 1} Remote Weather Sensors</span>",
                 required: false,
                 page: "remoteSensorPage")
        }
        section (sectionHeader('Name this instance of Ambient Weather Station')) {
            label ( name: "name",
                   title: fmtTitle("Assign a name to this SmartApp"),
                   state: (name ? "complete" : null),
                   defaultValue: state.weatherStationName,
                   required: false,
                   submitOnChange: true
                  )
        }
        section(sectionHeader("AWS Logging Options")) {
            if (logLevel == null && logLevelTime == null) {
                log.info "${app.name}: Setting Innital logLevel and LogLevelTime defaults"
                app.updateSetting(logLevel, [type: "enum", value: [5]])
                app.updateSetting(logLevelTime, [type: "enum", value: [30]])
            }
            //Logging Options
            input name: "logLevel", type: "enum", title: fmtTitle("Logging Level"), submitOnChange: true,
                description: fmtDesc("Logs selected level and above"), defaultValue: 3, options: LOG_LEVELS
            input name: "logLevelTime", type: "enum", title: fmtTitle("Logging Level Time"), submitOnChange: true,
                description: fmtDesc("Time to enable Debug/Trace logging"),defaultValue: 10, options: LOG_TIMES
            input name: "SyncLogOptions", type: "button", title: "Sync Log Options in all Devices"
        }
    }
}

def remoteSensorPage() {
    getAllChildDevices().each {
        logDebug "Device: ${it.deviceNetworkId} = ${it.label}"
        app.updateSetting(it.deviceNetworkId, [type: "string", value: it.label])
    }

    dynamicPage(name: "remoteSensorPage", nextPage: "optionsPage", uninstall:false, install : false ) {
//        if ( (!state.deviceId) && (state.ambientMap[state.weatherStationDataIndex].lastData?.tempinf) ) {
        if (state.ambientMap[state.weatherStationDataIndex].lastData?.tempinf) {
            section (sectionHeader("Enter a location name for your AWS Weather console located inside the house?")) {
                input (name: "${DTHDNIRemoteSensorName()}0",
                       type: "text",
                       title: "",
                       required: true,
                       defaultValue: 'Kitchen',
                       submitOnChange: true
                      )
            }
        }
        def i = 1
        def lastData = state.ambientMap[state.weatherStationDataIndex].lastData
        if ( state?.countRemoteTempHumiditySensors > 0) {
            section(sectionHeader("Provide Location names for your ${state?.countRemoteTempHumiditySensors} remote temperature/hydro sensors")) {
                paragraph getFormat("text-red",
                    "You MUST create short descriptive names for each remote sensor or accept the default provided. Do not use ANY special characters in the device names.\n\n" +
                    "Please note that remote sensors are numbered based in the bit switch on the Ambient Weather sensor (1-8) and reported on Ambient Network API as 'tempNf' or 'soiltempN' where N is an integer 1-8.  " +
                    "If a remote weather/soil sensor is deleted from your AWS network or non responsive from your group of Ambient remote sensors, you may have to re-verify and/or rename the remainder of the remote sensors in this app and manually delete that device sensor from the Hubitat 'Devices' page.")
                for (i; i <= MaxNumRemoteSensors(); i++) {
                    if (lastData["temp${i}f"]) {
                        input (
                            name: "${DTHDNIRemoteSensorName()}${i}",
                            type: "text",
                            title: getImage("checkMarkGreen") + fmtTitle("Ambient Weather Station Remote Device #${i} → Temp Sensor (Current: ${lastData["temp${i}f"]}°)"),
                            defaultValue: "Temp Sensor #${i}",
                            required: true
                        )
                    }
                    if (lastData["soiltemp${i}"]) {
                        input (
                            name: "${DTHDNIRemoteSensorName()}${i}",
                            type: "text",
                            title: getImage("checkMarkGreen") + fmtTitle("Ambient Weather Station Remote Device #${i} → Soil Temp Sensor (Current: ${lastData["soiltemp${i}"]}°)"),
                            defaultValue: "Soil Temp Sensor #${i}",
                            required: true
                        )
                    }
                    if (lastData["soilhum${i}"]) {
                        input (
                            name: "${DTHDNIRemoteSensorName()}${i}",
                            type: "text",
                            title: getImage("checkMarkGreen") + fmtTitle("Ambient Weather Station Remote Device #${i} → Soil Moisture Sensor (Current: ${lastData["soilhum${i}"]})"),
                            defaultValue: "Soil Moisture Sensor #${i}",
                            required: true
                        )
                    }
                }
            }
        }
        if (lastData.findAll { it.key.startsWith("pm") }.size() > 0) {
            section() {
                paragraph "Ambient Weather Station Particulate Monitor Location Name"
                paragraph image: AppImg("ambient-weather-pm25.jpg"),
                    title: fmtTitle("Provide a friendly short name for your Ambient Particulate Monitor PM10/PM25/AQIN/CO2"),
                    required: false,
                    null
                input (
                    name: "${DTHDNIPMName()}",
                    type: "text",
                    title: fmtTitle("Ambient Weather Station Particulate Monitor PM10/PM25/AQIN/CO2"),
                    defaultValue: "AWS Particle Monitor",
                    required: true
                )
            }
    }
}
}

def notifyPage() {
    dynamicPage(name: "notifyPage", title: "Weather Alerts/Notifications", uninstall: false, install: false) {
            section(sectionHeader("Enable Pushover™ and/or Twilio™ service(s). (Must install virtual device(s) and have an active service account):")) {
                input ("pushoverEnabled", "bool", title: "Use Pushover™ and/or Twilio™ Service(s) for Alert Notifications", required: false, submitOnChange: true)
                if (pushoverEnabled) {
                    input(name: "pushoverDevices", type: "capability.notification", title: "", required: false, multiple: true,
                          description: "Select notification device(s)", submitOnChange: true)
                    paragraph ""
                }
            }

        if (checkRequired([pushoverEnabled,sendSMSEnabled,sendPushEnabled])) {
            section (sectionHeader("Weather Station Notify Options")) {
                input ( name: "notifyAlertFreq", type: "enum",
                       required: checkRequired([pushoverEnabled,sendSMSEnabled,sendPushEnabled]),
                       title: "Restrict notification(s) per event type to once every NUMBER of hours (Default is 24, Once/day)",
                       options: [0,1,2,4,6,12,24],
                       defaultValue: 24,
                       submitOnChange: true,
                       multiple: false
                      )
                if (state.ambientMap[state.weatherStationDataIndex].lastData.keySet().grep(~/^temp1?f$/))  {
                    input ( name: "notifyAlertLowTemp", type: "number", required: false,
                           title: "Notify when a temperature value is EQUAL OR BELOW this value. Leave field blank to cancel notification."
                          )
                    input ( name: "notifyAlertHighTemp", type: "number", required: false,
                           title: "Notify when a temperature value is EQUAL OR ABOVE this value.  Leave field blank to cancel notification."
                          )
                }
                if (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('hourlyrainin'))  {
                    input ( name: "notifyRain", type: "bool", required: false,
                           title: "Notify when RAIN is detected"
                          )
                }
            }
        }
        section (hideable: true, hidden: true, sectionHeader("Last Notification Times")) {
            paragraph image: "",
                required: false,
                SMSNotifcationHistory()
        }
    }
}

private static boolean checkRequired(vars) {
    def rc = false
    vars.each {
        if ((it) || it==true) {
            rc = true
        }
    }
    return rc
}

def appButtonHandler(String buttonName) {
    logDebug "appButtonHandler: buttonName: ${buttonName}"
    if (buttonName == "SyncLogOptions") {
        logDebug "logLevel: ${settings.logLevel} logLevelTime: ${settings.logLevelTime}"
        Integer level = settings.logLevel as Integer ?: 0
        Integer time = settings.logLevelTime as Integer ?: 0
        logInfo "level: ${LOG_LEVELS[level]} and time: ${LOG_TIMES[time]}"
        logInfo "${app.name}: Current LogLevel is ${getLogLevelInfo()}"
        logInfo "Synchronizing all AWS devices to 'level: ${LOG_LEVELS[level]}' and time: '${LOG_TIMES[time]}'"
        getAllChildDevices().each {
            logInfo "Before Sync → Current LogLevel of '${it.label}' is ${it.getLogLevelInfo()}"
            it.syncLogLevelApp2Children(level, time)
            logInfo "After Sync → Current LogLevel of '${it.label}' is ${it.getLogLevelInfo()}"
        }
    }
}

def initialize() {
    logInfo "initialize()"
    def now = now()
    // Initialize/Reset Alert Warnings DateTime values
    state.notifyAlertLowTempDT 		= 0
    state.notifyAlertHighTempDT 	= 0
    state.notifyRainDT 				= 0
    state.notifySevereAlertDT 		= 0
    state.notifyAlertFreq 			= notifyAlertFreq?:24
    state.tempUnitsDisplay 			= tempUnits
    state.windUnitsDisplay 			= windUnits
    state.measureUnitsDisplay 		= measureUnits
    state.baroUnitsDisplay 			= baroUnits

    // Check for all devices needed to run this app
    addAmbientChildDevice()
    // Set user defined refresh rate
    if(state.schedulerFreq!=schedulerFreq) {
        logInfo "Updating your Cron REFRESH schedule from ${state.schedulerFreq?:0} mins to ${schedulerFreq} mins"
        state.schedulerFreq = schedulerFreq
        if(debugVerbose){logDebug "state.schedulerFreq->${state.schedulerFreq}"}
        setScheduler(schedulerFreq)
        def d = getChildDevice(state.deviceId)
        d.sendEvent(name: "scheduleFreqMin", value: state.schedulerFreq)
    }
    checkLogLevel()
}

def installed() {
    logInfo "installed()"
    state.deviceId = DTHDNI()
    initialize()
    runIn(10, refresh)
}

def updated() {
    logInfo "updated()"
    initialize()
    getAllChildDevices().each {
        logInfo "Clearing current states of device '${it.label}' at '${it.deviceNetworkId}'"
        it.deleteDeviceData()
    }
    refresh()
}

def scheduleCheckReset(quiet=false) {
    if (schedulerFreq!='0'){
        setScheduler(schedulerFreq)
        if (!quiet) {
        Date start = new Date()
        Date end = new Date()
        use( TimeCategory ) {
            end = start + schedulerFreq.toInteger().minutes
        }
        logInfo "Reset the next CRON Refresh to ~${schedulerFreq} mins from now (${end.format("h:mm:ss a", location.timeZone)}) to avoid excessive HTTP requests"
        }
    }
}

def refresh() {
    updateMyLabel('refreshing')
    logInfo "Device: 'Refresh ALL'"
    def runID = new Random().nextInt(10000)
    main(runID)
}

def autoScheduleHandler() {
    def runID = new Random().nextInt(10000)
    logInfo "Executing Cron Schedule runID: ${runID} every ${schedulerFreq} min(s)"
    main(runID)
    }

def main(runID=null) {
    runID = (runID)?:new Random().nextInt(10000)
    logInfo "Main(#${runID}) Section: Executing Ambient Weather Station API's for: '${state.weatherStationName}'"

    // Ambient Weather Station API
    ambientWeatherStation(runID)

    // Notify Events Check
    notifyEvents()
    updateMyLabel('updated')
}

def retryQuick(data) {
    logInfo "retryQuick #${state.retry} RunID: ${data.runID}"
    // Ambient Weather Station API
    updateMyLabel('retry')
    ambientWeatherStation(data.runID)

    // Notify Events Check
    notifyEvents()
}

def ambientWeatherStation(runID="missing runID") {
    // Ambient Weather Station
    logInfo "Executing full ambientWeatherStation routine runID: ${runID}"
    def d = getChildDevice(state.deviceId)
    def okTOSendEvent = true
    def remoteSensorDNI = ""
    def now = new Date().format('EEE MMM d, h:mm:ss a',location.timeZone)
    def nowTime = new Date().format('h:mm a',location.timeZone).toLowerCase()
    def currentDT = new Date()
    def sendEventOptions = ""
    if (AmbientStationData(runID)) {
        logDebug "httpget resp status = ${state.respStatus}"
        logInfo "Processing Ambient Weather data returned from AmbientStationData)"
        setStateWeatherStationData()
        convertStateWeatherStationData()
//        d.sendEvent(name:"unitsOfMeasure",
//                    value: "Temperature: ${state.tempUnitsDisplay}\nHeight: ${state.measureUnitsDisplay}\nWind: ${state.windUnitsDisplay}\nBarometric: ${state.baroUnitsDisplay}",
//                    displayed: false)
        if (settings.logLevel > 4) {
            state.ambientMap[state.weatherStationDataIndex].each{ k, v ->
                logTrace "${k} = ${v}"
                if (k instanceof Map) {
                    k.each { x, y ->
                        logTrace "${x} = ${y}"
                    }
                }
                if (v instanceof Map) {
                    v.each { x, y ->
                        logTrace "${x} = ${y}"
                    }
                }
            }
        }
        logDebug "Checking Weather Station data array for 'Last Rain Date' information..."
        if (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('lastRain')) {
            logDebug "Weather Station has 'Last Rain Date' information...Processing"
            def dateRain = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", state.ambientMap[state.weatherStationDataIndex].lastData.lastRain)
            use (groovy.time.TimeCategory) {
                def lastRainDuration = ((currentDT - dateRain) =~ /(.+)\b,/)[0][1]
                logDebug ("lastRainDuration -> ${lastRainDuration}")
                if (lastRainDuration) {
                    d.sendEvent(name:"lastRainDuration", value: lastRainDuration, displayed: false)
                }
            }
//        } else {
//            logDebug "Weather Station does NOT provide 'Last Rain Date' information...Skipping"
//            d.sendEvent(name:"lastRainDuration", value: "N/A", displayed: false)
        }
//        if ((state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('totalrainin')==false) || (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('yearlyrainin')==false)) {
//            d.sendEvent(name:"totalrainin", value: "N/A", unit: state.measureUnitsDisplay, displayed: false)
//        }
        d.sendEvent(name:"lastSTupdate", value: tileLastUpdated(), displayed: false)
        d.sendEvent(name:"macAddress", value: state.ambientMap[state.weatherStationDataIndex].macAddress, displayed: false)

        // Update Main Weather Device with Remote Sensor 1 values if tempf does not exist, same with humidity
        if (!state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('tempf')) {
            if (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('temp1f')) {
                d.sendEvent(name:"temperature", value: state.ambientMap[state.weatherStationDataIndex].lastData.temp1f, units: state.tempUnitsDisplay)
            }
            if (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('humidity1')) {
                d.sendEvent(name:"humidity", value: state.ambientMap[state.weatherStationDataIndex].lastData.humidity1, units: "%", displayed: false)
                d.sendEvent(name:"humidity_display", value: "${state.ambientMap[state.weatherStationDataIndex].lastData.humidity1}%")
            }
        }
        // Update Main Weather Device with Remote Sensor 1 values if tempinf does not exist, same with humidityin
        if (!state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('tempinf')) {
            logDebug "Fixing Main Station for inside temp"
            if (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('temp1f')) {
                d.sendEvent(name:"tempinf", value: state.ambientMap[state.weatherStationDataIndex].lastData.temp1f, units: state.tempUnitsDisplay, displayed: false)
                d.sendEvent(name:"tempinf_display", value: "${state.ambientMap[state.weatherStationDataIndex].lastData.temp1f}${state.tempUnitsDisplay}")
            }
        }
        if (!state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('humidityin')) {
            logDebug "Fixing Main Station for inside humidity"
            if (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('humidity1')) {
                d.sendEvent(name:"humidityin", value: state.ambientMap[state.weatherStationDataIndex].lastData.humidity1, units: "%", displayed: false)
                d.sendEvent(name:"humidityin_display", value: "${state.ambientMap[state.weatherStationDataIndex].lastData.humidity1}%")
            }
        }

        state.ambientServerDate=convertToCurrentTimeZone(state.respdata[state.weatherStationDataIndex].lastData.date)

        state.ambientMap[state.weatherStationDataIndex].info.each{ k, v ->
            if(k=='name'){k='pwsName'}
            if (v) {
                logDebug "sendEvent(name: ${k}, value: ${v})"
                d.sendEvent(name: k, value: v, displayed: false)
            }
        }

        state.ambientMap[state.weatherStationDataIndex].lastData.each{ k, v ->
            logDebug "Posted ${k.toUpperCase()}: ${v}"
            switch(k) {
                case ~/.*rain.*/:
                d.sendEvent(name: "${k}_display", value: "${v} ${state.measureUnitsDisplay}")
                break
                case ~/^barom.*/:
                d.sendEvent(name: "${k}_display", value: "${v}${state.baroUnitsDisplay}")
                break
                case ~/^tempi?n?f$|^dewPoint$|^feelsLikein$|^feelsLike$/:
                d.sendEvent(name: "${k}_display", value: "${v}${state.tempUnitsDisplay}")
                break
                case ~/^wind.*|^maxdailygust$/:
                d.sendEvent(name: "${k}_display", value: "${v} ${state.windUnitsDisplay}")
                break
                case ~/^humidity($|1|in)/:
                d.sendEvent(name: "${k}_display", value: "${v}%")
                break
                case ~/^batt.*/:
                // Change device battery level to 100% if the User preferences showBattery value has been defined and false
                if ( (showBattery != null) && (!showBattery) ) {
                    v = 1
                    state.ambientMap[state.weatherStationDataIndex].lastData["${k}"] = v
                }
                break

                default:
                    break
            }
            okTOSendEvent = true
            switch (k) {
                case 'dateutc':
                okTOSendEvent = false
                break
                case 'date':
                v = state.ambientServerDate
                break
                case 'battin':
                k='battery'
                d.sendEvent(name: k, value: v.toInteger()*100, units:'%', displayed: false)
                break
                case 'battout':
                k='battery'
                v=v.toInteger()*100
                break
                case ~/^batt[0-9].*/:
                okTOSendEvent = false
                break
                case 'lastRain':
                v=convertToCurrentTimeZone(v)
                break
                case 'lightning_time':
                def lightning_datetime = new Date(v).toString()
                v=lightning_datetime
                break
                case 'tempf':
                k='temperature'
                break
                case ~/^feelsLike$|^feelsLikein$/:
                break
                case 'windspeedmph':
                // Send windSpeed as wind for Hubitat™
                d.sendEvent(name: "wind"   , value: v , displayed: false)
                d.sendEvent(name: "windSpeed"   , value: v , displayed: false)
                break
                case 'winddir':
                def winddirectionState = degToCompass(state.ambientMap[state.weatherStationDataIndex].lastData?.winddir, true)
                logDebug "Wind Direction -> ${winddirectionState}"
                d.sendEvent(name:'winddirection', value: winddirectionState, displayed: false)
                d.sendEvent(name:'wind_cardinal', value: degToCompass(state.ambientMap[state.weatherStationDataIndex].lastData?.winddir, false), displayed: false)
                d.sendEvent(name:'winddir2', value: winddirectionState + " (" + state.ambientMap[state.weatherStationDataIndex].lastData.winddir + "º)")
                // Send winddir as windVector for Hubitat™
                d.sendEvent(name:'windVector', value: state.ambientMap[state.weatherStationDataIndex].lastData?.winddir, displayed: false)
                d.sendEvent(name:'windDirection', value: state.ambientMap[state.weatherStationDataIndex].lastData?.winddir, displayed: false)
                break
                case 'uv':
                def UVInumRange
                switch (v) {
                    case {it < 3}:
                    UVInumRange="Low (${v})"
                    break
                    case {it < 6}:
                    UVInumRange="Medium (${v})"
                    break
                    case {it < 8}:
                    UVInumRange="High (${v})"
                    break
                    case {it < 11}:
                    UVInumRange="Very High (${v})"
                    break
                    default:
                    UVInumRange="Extreme (${v})"
                    break
                }
                d.sendEvent(name: 'ultravioletIndexDisplay', value: UVInumRange )
                k='ultravioletIndex'
                break
                case 'yearlyrainin':
                k='totalrainin'
                break
                case 'solarradiation':
                	v = v.toInteger()
                    switch(solarRadiationTileDisplayUnits) {
                        case ('lux'):
                    		v = wm2lux(v)
                        	break
                        case ('fc'):
                    		v = wm2fc(v)
                        break
                        default:
                            break
                    }
                d.sendEvent(name: k, value: sprintf("%,7d %s",v,solarRadiationTileDisplayUnits?:'W/m²'), units: solarRadiationTileDisplayUnits?:'W/m²')
                k='illuminance'
                break

                // Weather Console Sensors
                case 'tempinf':
                remoteSensorDNI = getChildDevice("${DTHDNIRemoteSensorName()}0")
                if (remoteSensorDNI) {
                    logDebug "Posted temperature with value ${v} -> ${remoteSensorDNI}"
                    remoteSensorDNI.sendEvent(name: "temperature", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDNI.sendEvent(name:"date", value: state.ambientServerDate, displayed: false)
                    remoteSensorDNI.sendEvent(name:"lastSTupdate", value: tileLastUpdated(), displayed: false)
                    if (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('battout') ) {
                        remoteSensorDNI.sendEvent(name:"battery", value: state.ambientMap[state.weatherStationDataIndex].lastData.battout.toInteger()*100, displayed: false)
                    }
                } else {
                    logErr "Missing ${DTHDNIRemoteSensorName()}0"
                }
                break
                case 'humidityin':
                remoteSensorDNI = getChildDevice("${DTHDNIRemoteSensorName()}0")
                if (remoteSensorDNI) {
                    logDebug "Posted humidity with value ${v} -> ${remoteSensorDNI}"
                    remoteSensorDNI.sendEvent(name: "humidity", value: v, units: "%", displayed: false)
                    remoteSensorDNI.sendEvent(name: "humidity_display", value: "${v}%")
                } else {
                    logErr "Missing ${DTHDNIRemoteSensorName()}0"
                }
                break
                // Post values for remote temperature & humidity sensors
                case ~/^temp[0-9][0-9]?f$|^soiltemp[0-9][0-9]?$/:
                def remoteIndexNumber = k.findAll( /\d+/ )[0]
                remoteSensorDNI = getChildDevice("${DTHDNIRemoteSensorName()}${remoteIndexNumber}")
                logDebug "${k} = ${remoteSensorDNI}"
                if (remoteSensorDNI) {
                    logDebug "Posted temperature with value ${v} -> ${remoteSensorDNI}"
                    remoteSensorDNI.sendEvent(name: "temperature", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDNI.sendEvent(name:"lastSTupdate", value: tileLastUpdated(), displayed: false)
                    remoteSensorDNI.sendEvent(name:"date", value: state.ambientServerDate, displayed: false)

                    String batteryFieldName = "batt" + remoteIndexNumber.toString()
                    logDebug "batteryFieldName for '${k}' = ${batteryFieldName}"
                    if (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey(batteryFieldName)) {
                        def battValue = state.ambientMap[state.weatherStationDataIndex].lastData."${batteryFieldName}".toInteger()*100
                        logDebug "batteryFieldName = ${batteryFieldName} = ${battValue}%"
                        remoteSensorDNI.sendEvent(name:"battery", value: battValue, displayed: false)
                    }

                } else {
                    logErr "Missing ST Device ${DTHDNIRemoteSensorName()}${k.findAll( /\d+/ )[0]} for ${k}"
                }
                okTOSendEvent = false
                break
                case ~/^dewPoint\d/:
                remoteSensorDNI = getChildDevice("${DTHDNIRemoteSensorName()}${k.findAll( /\d+/ )[0]}")
                logDebug "${k} = ${remoteSensorDNI}"
                if (remoteSensorDNI) {
                    logDebug "Posted ${k.toUpperCase()}: ${v} -> ${remoteSensorDNI}"
                    remoteSensorDNI.sendEvent(name: "dewpoint", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDNI.sendEvent(name: "dewPoint", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDNI.sendEvent(name: "dewPoint_display", value: "${v}${state.tempUnitsDisplay}", units: state.tempUnitsDisplay)
                } else {
                    logErr "Missing HE Device ${DTHDNIRemoteSensorName()}${k.findAll( /\d+/ )[0]} for ${k}"
                }
                okTOSendEvent = false
                break
                case ~/^feelsLike\d/:
                remoteSensorDNI = getChildDevice("${DTHDNIRemoteSensorName()}${k.findAll( /\d+/ )[0]}")
                logDebug "Posted ${k.toUpperCase()} = ${remoteSensorDNI}"
                if (remoteSensorDNI) {
                    logDebug "Posted ${k.toUpperCase()}: ${v} -> ${remoteSensorDNI}"
                    remoteSensorDNI.sendEvent(name: "feelsLike", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDNI.sendEvent(name: "feelsLike_display", value: "${v}${state.tempUnitsDisplay}", units: state.tempUnitsDisplay)
                } else {
                    logErr "Missing HE Device ${DTHDNIRemoteSensorName()}${k.findAll( /\d+/ )[0]} for ${k}"
                }
                okTOSendEvent = false
                break
                case ~/^humidity[0-9][0-9]?$|^soilhum[0-9][0-9]?$/:
                remoteSensorDNI = getChildDevice("${DTHDNIRemoteSensorName()}${k.findAll( /\d+/ )[0]}")
                logDebug "Posted ${k.toUpperCase()} = ${remoteSensorDNI}"
                if (remoteSensorDNI) {
                    logDebug "Posted humidity with value ${v} -> ${remoteSensorDNI}"
                    remoteSensorDNI.sendEvent(name: "humidity", value: v, units: "%", displayed: false)
                    remoteSensorDNI.sendEvent(name: "humidity_display", value: "${v}%")
                } else {
                    logErr "Missing ST Device ${DTHDNIRemoteSensorName()}${k.findAll( /\d+/ )[0]} for ${k}"
                }
                okTOSendEvent = false
                break
                // Post values for Particle Monitor which report PM10/PM25/AQI/CO2
                case ~/(^pm.*)|(^co2.*)|(^aqi.*)/:
                remoteSensorDNI = getChildDevice("${DTHDNIPMName()}")
                logDebug "${k} = ${remoteSensorDNI}"
                if (remoteSensorDNI) {
                    def sensorUnits = ''
                    switch (k) {
                        case ~/^aqi.*/:
                            sensorUnits = ''
                        	break
                        case ~/^co2.*/:
                            sensorUnits = 'ppm'
                        	break
                        case ~/^pm\d\d.*/:
                            sensorUnits = 'µg/m3'
                        	break
                        case ~/.*temp.*/:
                            sensorUnits = '°F'
                        	break
                        case ~/.*humidity.*/:
                            sensorUnits = '%'
                        	break
                        default:
                            sensorUnits = ''
                        	break
                    }
                    logDebug "Posted ${k.toUpperCase()}: ${v} ${sensorUnits} -> ${remoteSensorDNI}"
                    remoteSensorDNI.sendEvent(name: k, value: v, units: sensorUnits)
                    remoteSensorDNI.sendEvent(name:"lastSTupdate", value: tileLastUpdated(), displayed: false)
                    remoteSensorDNI.sendEvent(name:"date", value: state.ambientServerDate, displayed: false)
                    if (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey("batt_25")) {
                        remoteSensorDNI.sendEvent(name:"battery", value: state.ambientMap[state.weatherStationDataIndex].lastData.batt_25.toInteger()*100, displayed: false)
                    } else if (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey("batt_co2")) {
                        remoteSensorDNI.sendEvent(name:"battery", value: state.ambientMap[state.weatherStationDataIndex].lastData.batt_co2.toInteger()*100, displayed: false)
                    }
                } else {
                    logErr "Missing HE Device ${DTHDNIPMName()} for ${k}"
                }
                okTOSendEvent = false
                break
                default:
                    break
            }
            if (okTOSendEvent){
                // , unit: "%"
                switch (k) {
                    case ('battery'):
                    case ('date'):
                    sendEventOptions = [displayed : false]
                    break
                    case ~/^temp.*/:
                    sendEventOptions = [units : state.tempUnitsDisplay, displayed : false]
                    break
                    case ~/^feelsLike$|^feelsLikein$/:
                    sendEventOptions = [units : state.tempUnitsDisplay, displayed : false]
                    break
                    case ('dewPointin'):
                    case ('dewPoint'):
                    sendEventOptions = [units : state.tempUnitsDisplay, displayed : false]
                    d.sendEvent(name: k.toLowerCase(), value: v, units : state.tempUnitsDisplay, displayed: false )
                    break
                    case ('illuminance'):
                    sendEventOptions = [units: solarRadiationTileDisplayUnits?:'W/m²']
                    break
                    case ~/^humidity.*/:
                    sendEventOptions = [units : '%', displayed : false]
                    break
                    case ~/.*rain.*/:
                    sendEventOptions = [units : state.measureUnitsDisplay, displayed : false]
                    break
                    case ('windir'):
                    sendEventOptions = [units : 'º']
                    break
                    case ~/^wind.*/:
                    case ('maxdailygust'):
                    sendEventOptions = [units : state.windUnitsDisplay, displayed : false]
                    break
                    case ~/^barom.*/:
                    sendEventOptions = [units : state.baroUnitsDisplay, displayed : false]
                    break
                    default:
                        sendEventOptions = []
                    break
                }
                sendEventOptions << [name: k, value: v]
                d.sendEvent(sendEventOptions)
                logDebug "${d}.sendEvent(${sendEventOptions})"
                sendEventOptions = []
            }
        }
    } else {
        logDebug "AmbientStationData did not return any weather data"
    }
}

def AmbientStationData(runID="????") {
    def df = new java.text.SimpleDateFormat("hh:mm:ss a")
    df.setTimeZone(location.timeZone)
	def currentGETAmbientStationData = now()
    state.lastGETAmbientStationData = state.lastGETAmbientStationData?:now()
    logInfo "Start: AmbientStationData runID: ${runID} at ${df.format(new Date())}"
    def timeSecsLastRun = (((currentGETAmbientStationData - state.lastGETAmbientStationData)/1000).toInteger())
    logInfo "AmbientStationData Time difference is ${timeSecsLastRun} secs between last execution"
    if (runID!=0 && timeSecsLastRun < 2) {
        logWarn "Aborting AmbientStationData run ${runID}:  Too Short for API Limits"
        return
    }
    state.lastGETAmbientStationData = currentGETAmbientStationData
    if(!state.apiKey){
        logErr "Severe Error: The API key is UNDEFINED in ${app.name}'s IDE 'App Settings' field, fatal error now exiting"
        return false
    }
    state.retry = state.retry?:0
    scheduleCheckReset(true)
    if (state.retry.toInteger()>0) {
        logInfo "Executing Retry AmbientStationData re-attempt #${state.retry} for RunID: ${runID}"
    }
    def params = [
        uri				: "https://api.ambientweather.net",
        path			: "/v1/devices",
        contentType		: 'application/json',
        query			: [
            "applicationKey" : APPKEY,
            "apiKey"		 : state.apiKey
        ]
    ]
    try {
        httpGet(params) { resp ->
            // get the data from the response body
            state.respdata = resp.data
            selectWeatherKeys(resp.data)
            state.respStatus = resp.status
            state.remove("httpError")
            if (resp.status != 200) {
                logErr "AmbientWeather.Net: response status code: ${resp.status}: response: ${resp.data}"
                return false
            }
            if (state.weatherStationDataIndex) {
                countRemoteTempHumiditySensors()
            }
            if (state.retry.toInteger()>0) {
                logInfo "SUCCESS: Retry AmbientStationData re-attempt #${state.retry} for runID: ${runID}"
                state.retry = 0
                updateMyLabel('updated')
            }
        }
    } catch (e) {
        logDebug "Ambient Weather Station API Data runID ${runID}: ${e}"
        resp?.headers.each {
            logTrace "${it.name}: ${it.value}"
        }
        state.httpError = e.toString().toLowerCase()
        if (e.toString().contains("unauthorized")) {
            updateMyLabel('unauthorized')
            return false
        }
        state.retry = state.retry.toInteger() + 1
        if (state.retry.toInteger()<4) {
            logInfo "Waiting 10 seconds to Try HttpGet Again runID ${runID}: Attempt #${state.retry}"
            updateMyLabel('retry')
            runIn(10, 'retryQuick', [overwrite: true, data: [runID: "${runID}"]])
        }
        return false
    }
    logInfo "SUCCESS: AmbientStationData successfully updated for runID: ${runID}"
    updateMyLabel('updated')
    return true
}

def addAmbientChildDevice() {
    // add Ambient Weather Reporter Station devices
    // Derive a Short Name for the Weather Station and Remote Sensors
    // Create/Validate Weather Console Device
    def AWSName  = "${state.weatherStationName}-Console"
    def AWSLabel = "AWS-Console"
    def AWSDNI = getChildDevice(state.deviceId)
    if (!AWSDNI) {
        logInfo "NEW: Adding Ambient Device: ${AWSName} with DNI: ${state.deviceId}"
        try {
            addChildDevice(DTHnamespace(), DTHName(), DTHDNI(), null, ["name": AWSName, "label": AWSLabel, completedSetup: true])
        } catch(ex) {
            logErr "The Ambient Weather Device Handler '${DTHName()}' was not found in your 'My Device Handlers', Error-> '${ex}'.  Please run HPM Repair option for Ambient Weather Station"
            return false
        }
        logInfo "Success: Added ${AWSName} with DNI: ${DTHDNI()}"
    } else {
        logInfo "Verified Weather Station '${getChildDevice(state.deviceId).label}' = DNI: '${DTHDNI()}'"
        /*
        if (!AWSDNI.name.equalsIgnoreCase(AWSName)) {
            logInfo "Device NAME MIS-MATCH: Device Name: ${AWSDNI.name} <> ${AWSName}"
            AWSDNI.name = AWSName
        }
        if (!AWSDNI.label.equalsIgnoreCase(AWSName)) {
            logInfo "Device LABEL MIS-MATCH: Device Label: ${AWSDNI.label} <> ${AWSName}"
            deleteChildDevice(AWSDNI)
            addChildDevice(DTHnamespace(), DTHName(), DTHDNI(), null, ["name": AWSName, "label": AWSName, completedSetup: true])
        }
        */
    }


    // add Ambient Weather Remote Sensor Device(s)
    def remoteSensorNamePref
    def remoteSensorLabelPref
    def remoteSensorNameDNI
    def remoteSensorNumber
    settings.each { key, value ->
        if ( key.startsWith(DTHDNIRemoteSensorName()) ) {
            remoteSensorNamePref = "${state.weatherStationName}-${value}"
            remoteSensorLabelPref = "AWS-${value}"
            remoteSensorNameDNI = getChildDevice(key)
            remoteSensorNumber = key.reverse()[0..0]
            if (remoteSensorNumber.toInteger() <= MaxNumRemoteSensors()) {
                if (!remoteSensorNameDNI) {
                    logInfo "NEW: Adding Remote Sensor #${remoteSensorNumber}: ${remoteSensorLabelPref}"
                    try {
                        addChildDevice(DTHnamespace(), DTHRemoteSensorName(), "${key}", null, ["name": remoteSensorNamePref, "label": remoteSensorLabelPref, completedSetup: true])
                    } catch(ex) {
                        logErr "The Ambient Weather Device Handler '${DTHRemoteSensorName()}' was not found in your 'My Device Handlers', Error-> '${ex}'.  Please run HPM Repair option for Ambient Weather Station."
                        return false
                    }
                    logInfo "Success Added Ambient Remote Sensor: ${remoteSensorLabelPref} with DNI: ${key}"
                } else {
                    logInfo "Verified Remote Sensor #${remoteSensorNumber} of ${state.countRemoteTempHumiditySensors} Exists: ${getChildDevice(key).label} = DNI: ${key}"
                    // Update Device Label Values
                    if (remoteSensorNameDNI.label != value) {
                        logInfo "Renaming Device ${remoteSensorNameDNI.deviceNetworkId}: Old Device Label: ${remoteSensorNameDNI.label} → New Device Label: ${value}"
                        remoteSensorNameDNI.label = value
                    }
                    /*
                    if (!remoteSensorNameDNI.name.equalsIgnoreCase(remoteSensorNamePref)) {
                        logInfo "Device NAME MIS-MATCH: Device Name: ${remoteSensorNameDNI.name} <> ${remoteSensorNamePref}"
                        remoteSensorNameDNI.name  = remoteSensorNamePref
                    }
                    if (!remoteSensorNameDNI.label.equalsIgnoreCase(remoteSensorNamePref)) {
                        logInfo "Device LABEL MIS-MATCH: Device Label: ${remoteSensorNameDNI.label} <> ${remoteSensorNamePref}"
                        logDebug "remoteSensorNameDNI.deviceNetworkId = ${remoteSensorNameDNI.deviceNetworkId}"
                        deleteChildDevice(remoteSensorNameDNI.deviceNetworkId)
                        addChildDevice(DTHnamespace(), DTHRemoteSensorName(), "${key}", null, ["name": remoteSensorNamePref, "label": remoteSensorNamePref, completedSetup: true])
                    }
                    */
                }
            } else {
                logWarn "Device ${remoteSensorNumber} DNI: ${key} '${remoteSensorNameDNI.name}' exceeds # of remote sensors (${state.countRemoteTempHumiditySensors}) reporting from Ambient -> ACTION REQUIRED"
                logWarn "Please verify that all Ambient Remote Sensors are online and reporting to Ambient Network.  If so, please manually delete the device in the 'Devices' view"
            }
        }
    }

    // add Ambient Weather Particulate Monitor Device(s)
    def PMkey = "${DTHDNIPMName()}"
    def PMvalue = settings.find{ it.key == "${DTHDNIPMName()}" }?.value

    if(PMvalue) {
        remoteSensorNamePref = "${state.weatherStationName}${PMvalue?'-'+PMvalue:''}"
        remoteSensorLabelPref = "AWS-${PMvalue}"
        remoteSensorNameDNI = getChildDevice(PMkey)
        if (!remoteSensorNameDNI) {
            logInfo "NEW: Adding Particulate Monitor device: ${remoteSensorLabelPref}"
            try {
                addChildDevice(DTHnamespace(), DTHPMSensorName(), "${PMkey}", null, ["name": remoteSensorNamePref, "label": remoteSensorLabelPref, completedSetup: true])
            } catch(ex) {
                logErr "The Ambient Weather Device Handler '${DTHPMSensorName()}' was not found in your 'My Device Handlers', Error-> '${ex}'.  Please install this in the IDE's 'My Device Handlers'"
                return false
            }
            logInfo "Success Added Ambient Particulate Monitor: ${remoteSensorLabelPref} with DNI: ${PMkey}"
        } else {
            if(infoVerbose){logInfo "Verified Particulate Monitor ${state.countParticulateMonitors} Exists: ${remoteSensorLabelPref} = DNI: ${PMkey}"}
            // Update Device Label Values
            if (remoteSensorNameDNI.label != PMvalue) {
                logInfo "Renaming Device ${remoteSensorNameDNI.deviceNetworkId}: Old Device Label: ${remoteSensorNameDNI.label} → New Device Label: ${PMvalue}"
                remoteSensorNameDNI.label = PMvalue
            }
            /*
            if ( (remoteSensorNameDNI.label == remoteSensorNamePref) && (remoteSensorNameDNI.name == remoteSensorNamePref) ) {
                if(infoVerbose){
                    logInfo "Device Label/Name Pref Match: Name: ${remoteSensorNameDNI.name} = Label: ${remoteSensorNameDNI.label} == Pref Label: ${remoteSensorNamePref} -> NO CHANGE"
                }
            } else {
                logWarn "Device Label/Name Pref Mis-Match: Name: ${remoteSensorNameDNI.name} <> Label: ${remoteSensorNameDNI.label} <> Pref Label: ${remoteSensorNamePref} -> RENAMING"
                remoteSensorNameDNI.name  = remoteSensorNamePref
                remoteSensorNameDNI.label = remoteSensorNamePref
                logWarn "Successfully Renamed Device Label and Names for: ${remoteSensorNameDNI}"
            }
            */
        }
    }
}

def degToCompass(num,longTitles=true) {
    if (num) {
        def val = Math.floor((num.toFloat() / 22.5) + 0.5).toInteger()
        def arr = []
        if (longTitles) {
            arr = ["N", "North NE", "NE", "East NE", "E", "East SE", "SE", "South SE", "S", "South SW", "SW", "West SW", "W", "West NW", "NW", "North NW"]
        } else {
            arr = ["N", "N NE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"]
        }
        return arr[(val % 16)]
    }
    return "N/A"
}


def setScheduler(schedulerFreq) {
    def scheduleHandler = 'autoScheduleHandler'
    unschedule(scheduleHandler)
    if(infoVerbose){logInfo "Auto Schedule Refresh Rate is now -> ${schedulerFreq} mins"}
    switch(schedulerFreq) {
        case '0':
        logInfo "Auto Schedule Refresh Rate is now: OFF"
        break
        case '1':
        runEvery1Minute(scheduleHandler)
        break
        case '2':
        schedule("20 0/2 * * * ?",scheduleHandler)
        break
        case '3':
        schedule("20 0/3 * * * ?",scheduleHandler)
        break
        case '4':
        schedule("20 0/4 * * * ?",scheduleHandler)
        break
        case '5':
        runEvery5Minutes(scheduleHandler)
        break
        case '10':
        runEvery10Minutes(scheduleHandler)
        break
        case '15':
        runEvery15Minutes(scheduleHandler)
        break
        case '30':
        runEvery30Minutes(scheduleHandler)
        break
        case '60':
        runEvery1Hour(scheduleHandler)
        break
        case '180':
        runEvery3Hours(scheduleHandler)
        break
        default :
        unschedule()
        break
    }
}

def tileLastUpdated() {
    def now = new Date().format('EEE MMM dd, hh:mm:ss a',location.timeZone)
    return now
}
def informationList(variable) {
    switch(variable) {
        case ("apiHelp") :
        // Help Text for API Key
        variable =  [
            "You MUST enter your Ambient Weather API key in the ${appNameVersion()}.",
            "Visit your Ambient Weather Dashboards's Account page.",
            "Create/Copy your API key from the bottom of the page",
            "Return to your Hubitat App.",
            "Exit the SmartApp and Start ${appNameVersion()} Setup again."
        ]
        break
        default:
            break
    }
    if (variable instanceof List) {
        def numberedText = ""
        variable.eachWithIndex { item, index ->
            numberedText += "${index+1}. ${item}"
            numberedText += (index<variable.size()-1)?"\n":''
        }
        return numberedText
    }
    return variable
}

def parseAlertTime(s) {
    def dtf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    def s2 = s.replaceAll(/([0-9][0-9]):([0-9][0-9])$/,'$1$2')
    dtf.parse(s2)
}

def SMSNotifcationHistory() {
    def date = now()
    def dateToday = date
    def today = new Date(date).format("MMM-DD-YYYY", location.timeZone)
    def msg = ""
    def notifyVars = ["Severe Weather"	: state.notifySevereAlertDT ]
    if ( (state.deviceId) && (state.ambientMap[state.weatherStationDataIndex].lastData.keySet().grep(~/^temp1?f$/)) ) {
        notifyVars << ["Low Temp" : state.notifyAlertLowTempDT, "High Temp" : state.notifyAlertHighTempDT]
    }
    if ( (state.deviceId) && (state.ambientMap[state.weatherStationDataIndex].lastData.containsKey('hourlyrainin')) ) {
        notifyVars << ["Rain Detected" : state.notifyRainDT]
    }

    notifyVars.eachWithIndex { k, index ->
        if (k.value) {
            date = new Date(k.value).format("MMM-DD-YYYY h:mm a", location.timeZone)
            dateToday = new Date(k.value).format("MMM-DD-YYYY", location.timeZone)
            if (today==dateToday) {
                date = "Today @ " + new Date(k.value).format("h:mm a", location.timeZone)
            }
            msg += "${index+1}) ${k.key} : ${date}\n"
        } else {
            msg += "${index+1}) ${k.key} : --\n"
        }
    }
    return msg
}

def notifyEvents() {
    if (checkRequired([pushoverEnabled,sendSMSEnabled,sendPushEnabled])){
        def now = now()
        //        state.notifyAlertLowTempDT = now-3600000*2
        def msg
        def tempCheck = state.ambientMap[state.weatherStationDataIndex].lastData.tempf?:state.ambientMap[state.weatherStationDataIndex].lastData.temp1f
        def ambientWeatherStationName = "${DTHName()} - '${state.weatherStationName}'"
        if ( (notifyAlertLowTemp) && (tempCheck) && (tempCheck.toInteger()<=notifyAlertLowTemp) ) {
            msg = "${ambientWeatherStationName}: LOW TEMP ALERT:  Current temperature of ${tempCheck}${state.tempUnitsDisplay} <= ${notifyAlertLowTemp}${state.tempUnitsDisplay}"
            if (lastNotifyDT(state.notifyAlertLowTempDT, "Low Temp")) {
                send_message(msg)
                state.notifyAlertLowTempDT = now
            }
        }
        if ( (notifyAlertHighTemp) && (tempCheck) && (tempCheck.toInteger()>=notifyAlertHighTemp) ) {
            msg = "${ambientWeatherStationName}: HIGH TEMP ALERT:  Current temperature of ${tempCheck}${state.tempUnitsDisplay} >= ${notifyAlertHighTemp}${state.tempUnitsDisplay}"
            if (lastNotifyDT(state.notifyAlertHighTempDT, "High Temp")) {
                state.notifyAlertHighTempDT = now
                send_message(msg)
            }
        }
        if ( (notifyRain) && (state.ambientMap[state.weatherStationDataIndex].lastData.hourlyrainin) && (state.ambientMap[state.weatherStationDataIndex].lastData.hourlyrainin.toFloat()>0) ){
            msg = "${ambientWeatherStationName}: RAIN DETECTED ALERT: Current hourly rain sensor reading of ${state.ambientMap[state.weatherStationDataIndex].lastData.hourlyrainin} ${state.measureUnitsDisplay}/hr"
            if (lastNotifyDT(state.notifyRainDT, "Rain")) {
                state.notifyRainDT = now
                send_message(msg)
            }
        }
    }
}

def lastNotifyDT(lastDT, eventName) {
    if (!lastDT) { return true }
    def now = now()/1000
    def date = new Date(lastDT).format("MMM-DD-YYYY h:mm:ss a", location.timeZone)
    def hours = ((now-(lastDT/1000))/3600).toFloat().round(1)
    def days = (hours/24).toFloat().round(1)
    def rc = hours>=state.notifyAlertFreq.toInteger()
    logInfo "This '${eventName}' event was last sent on ${date}: ${days} days, ${hours} hours ago"
    logInfo "${eventName} Alert Every ${notifyAlertFreq} hours: ${rc?'OK to SMS':'TOO EARLY TO SEND'}"
    return rc
}

def convertStateWeatherStationData() {
    // Check to see if Units of Measure have been defined in the preferences section, otherwise default to Hub's location for imperial or metric
    if (tempUnits == null) {
        def tempUnitsSmartThingsScale = getTemperatureScale()
        logWarn "Missing 'Units of Measure' App Preference Setting Values:  ALL Default Units of Measure will be based on your hub's location temperature preference of '${tempUnitsSmartThingsScale}'"
        logWarn "Please run '${state.weatherStationName}' SmartAPP install to select your default Units of Measure for display"
        state.tempUnitsDisplay = "°${tempUnitsSmartThingsScale}"
        state.windUnitsDisplay = (tempUnitsSmartThingsScale == "F") ? "mph" : "kph"
        state.measureUnitsDisplay = (tempUnitsSmartThingsScale == "F") ? "in" : "cm"
        state.baroUnitsDisplay = (tempUnitsSmartThingsScale == "F") ? "inHg" : "mmHg"
    }
    logDebug "tempUnitsDisplay = ${state.tempUnitsDisplay}, windUnitsDisplay = ${state.windUnitsDisplay}, measureUnitsDisplay = ${state.measureUnitsDisplay}, baroUnitsDisplay 	= ${state.baroUnitsDisplay}"
    def tempVar = null
    def newAmbientMap = [:]
    newAmbientMap = state.ambientMap
    newAmbientMap[state.weatherStationDataIndex].lastData.each{ k, v ->
        tempVar = null
        switch (k) {
            case ~/^temp.*/:
            case ~/^feelsLike.*/:
            case 'dewPoint':
            if (state.tempUnitsDisplay == '°C') {
                tempVar = String.format("%.01f",(v-32)*5/9)
            }
            break
            case ~/.*rain.*/:
            try {
            if (state.measureUnitsDisplay == 'cm') {
                tempVar = String.format("%.02f",v*2.54)
            } else if (state.measureUnitsDisplay == 'mm') {
                tempVar = String.format("%.02f",v*25.4)
            }
            } catch(Exception ex) {
                tempVar = 0
            }
            break
            case ~/^winddir.*/:
            break
            case ~/^wind.*/:
            case ('maxdailygust'):
            if (state.windUnitsDisplay == 'kph') {
                tempVar = String.format("%.02f",v*1.609344)
            } else if (state.windUnitsDisplay == 'fps') {
                tempVar = String.format("%.02f",v*2/3)
            } else if (state.windUnitsDisplay == 'mps') {
                tempVar = String.format("%.02f",v*0.44704)
            } else if (state.windUnitsDisplay == 'knotts') {
                tempVar = String.format("%.02f",v*0.86898)
            }
            break
            case ~/^barom.*/:
            if (state.baroUnitsDisplay == 'mmHg') {
                tempVar = String.format("%.02f",v*25.4)
            } else if (state.baroUnitsDisplay == 'hpa') {
                tempVar = String.format("%.02f",v*33.86389)
            }
            break
            default:
                break
        }
        if(tempVar != null) {
            logDebug "tempVar k=${k}, v=${v} tempVar=${tempVar}"
            newAmbientMap[state.weatherStationDataIndex].lastData."${k}" = tempVar.toFloat()
        }
    }
    state.ambientMap = [:]
    state.ambientMap = newAmbientMap
}

def setStateWeatherStationData() {
    if (weatherStationMac) {
        state.weatherStationDataIndex = state.ambientMap.findIndexOf {
            it.macAddress in [weatherStationMac]
        }
    }
    state.weatherStationDataIndex  = state.weatherStationDataIndex?:0
    state.weatherStationMac        = state.weatherStationMac?:state.ambientMap[state.weatherStationDataIndex].macAddress
    state.weatherStationName       = state.ambientMap[state.weatherStationDataIndex].info.name
    state.weatherStationLocation   = state.ambientMap[state.weatherStationDataIndex].info.location?:state.ambientMap[state.weatherStationDataIndex].info.containsKey("coords")?state.ambientMap[state.weatherStationDataIndex].info.coords.location:''
    countRemoteTempHumiditySensors()
    countParticulateMonitors()
}

def countRemoteTempHumiditySensors() {
    state.countRemoteTempHumiditySensors =  state.ambientMap[state.weatherStationDataIndex].lastData.keySet().count { it.matches('^temp[0-9][0-9]?f|^soiltemp[0-9]?[0-9]?|^soilhum[0-9]?[0-9]?') }
    return state.countRemoteTempHumiditySensors
}

def countParticulateMonitors() {
    def pmDevice =  state.ambientMap[state.weatherStationDataIndex].lastData?.keySet().count { it.matches('^pm.*') }
    if (pmDevice == 0) state.countParticulateMonitors = 0
    else state.countParticulateMonitors = 1
    return state.countParticulateMonitors
}

def unitsSet() {
    if ([tempUnits, windUnits, measureUnits, baroUnits, solarRadiationTileDisplayUnits].findAll({it != null}).join()=='') return "Tap to Select"
    return sprintf("%s, %s, %s, %s, %s", tempUnits, windUnits, measureUnits, baroUnits, solarRadiationTileDisplayUnits)
}

def alertFilterList() {
    def x = [
        "ABV":"ABV - Rawinsonde Data Above 100 Millibars",
        "ADA":"ADA - Alarm/Alert Administrative Msg",
        "ADM":"ADM - Alert Administrative Message",
        "ADR":"ADR - NWS Administrative Message",
        "ADV":"ADV - Generic Space Environment Advisory",
        "AFD":"AFD - Area Forecast Discussion",
        "AFM":"AFM - Area Forecast Matrices",
        "AFP":"AFP - Area Forecast Product",
        "AFW":"AFW - Fire Weather Matrix",
        "AGF":"AGF - Agricultural Forecast",
        "AGO":"AGO - Agricultural Observations",
        "ALT":"ALT - Space Environment Alert",
        "AQA":"AQA - Air Quality Alert",
        "AQI":"AQI - Air Quality Index Statement",
        "ASA":"ASA - Air Stagnation Advisory",
        "AVA":"AVA - Avalanche Watch",
        "AVW":"AVW - Avalanche Warning",
        "AWO":"AWO - Area Weather Outlook",
        "AWS":"AWS - Area Weather Summary",
        "AWU":"AWU - Area Weather Update",
        "AWW":"AWW - Airport Weather Warning",
        "BOY":"BOY - Buoy Report",
        "BRG":"BRG - Coast Guard Observations",
        "BRT":"BRT - Hourly Roundup for Weather Radio",
        "CAE":"CAE - Child Abduction Emergency",
        "CCF":"CCF - Coded City Forecast",
        "CDW":"CDW - Civil Danger Warning",
        "CEM":"CEM - Civil Emergency Message",
        "CF6":"CF6 - WFO Monthly/Daily Climate Data",
        "CFP":"CFP - Convective Forecast Product",
        "CFW":"CFW - Coastal Flood Warnings/Watches/Statements",
        "CGR":"CGR - Coast Guard Surface Report",
        "CHG":"CHG - Computer Hurricane Guidance",
        "CLA":"CLA - Climatological Report (Annual)",
        "CLI":"CLI - Climatological Report (Daily)",
        "CLM":"CLM - Climatological Report (Monthly)",
        "CLQ":"CLQ - Climatological Report (Quarterly)",
        "CLS":"CLS - Climatological Report (Seasonal)",
        "CLT":"CLT - Climate Report",
        "CMM":"CMM - Coded Climatological Monthly Means",
        "COD":"COD - Coded Analysis and Forecasts",
        "CPF":"CPF - Great Lakes Port Forecast",
        "CUR":"CUR - Routine Space Environment Products",
        "CWA":"CWA - Center (CWSU) Weather Advisory",
        "CWF":"CWF - Coastal Waters Forecast",
        "CWS":"CWS - Center (CWSU) Weather Statement",
        "DAY":"DAY - Routine Space Environment Product (Daily)",
        "DDO":"DDO - Daily Dispersion Outlook",
        "DGT":"DGT - Drought Information Statement",
        "DSA":"DSA - Unnumbered Depression / Suspicious Area Advisory",
        "DSM":"DSM - ASOS Daily Summary",
        "DSW":"DSW - Dust Storm Warning and Dust Advisory",
        "EFP":"EFP - 3 To 5 Day Extended Forecast",
        "EOL":"EOL - Average 6 To 10 Day Weather Outlook (Local)",
        "EQI":"EQI - Tsunami Bulletin",
        "EQR":"EQR - Earthquake Report",
        "EQW":"EQW - Earthquake Warning",
        "ESF":"ESF - Flood Potential Outlook",
        "ESG":"ESG - Extended Streamflow Guidance",
        "ESP":"ESP - Extended Streamflow Prediction",
        "ESS":"ESS - Water Supply Outlook",
        "EVI":"EVI - Evacuation Immediate",
        "EWW":"EWW - Extreme Wind Warning",
        "FA0":"FA0 - Aviation Area Forecasts (Pacific)",
        "FA1":"FA1 - Aviation Area Forecasts (Northeast)",
        "FA2":"FA2 - Aviation Area Forecasts (Southeast)",
        "FA3":"FA3 - Aviation Area Forecasts (North Central)",
        "FA4":"FA4 - Aviation Area Forecasts (South Central)",
        "FA5":"FA5 - Aviation Area Forecasts (Rocky Mountains)",
        "FA6":"FA6 - Aviation Area Forecasts (West Coast)",
        "FA7":"FA7 - Aviation Area Forecasts (Juneau, AK)",
        "FA8":"FA8 - Aviation Area Forecasts (Anchorage, AK)",
        "FA9":"FA9 - Aviation Area Forecasts (Fairbanks, AK)",
        "FD0":"FD0 - 24 Hr Fd Winds Aloft Fcst (45,000 and 53,000 Ft)",
        "FD1":"FD1 - 6 Hour Winds Aloft Forecast",
        "FD2":"FD2 - 12 Hour Winds Aloft Forecast",
        "FD3":"FD3 - 24 Hour Winds Aloft Forecast",
        "FD4":"FD4 - Winds Aloft Forecast",
        "FD5":"FD5 - Winds Aloft Forecast",
        "FD6":"FD6 - Winds Aloft Forecast",
        "FD7":"FD7 - Winds Aloft Forecast",
        "FD8":"FD8 - 6 Hour Fd Winds Aloft Fcst (45,000 and 53,000 Ft)",
        "FD9":"FD9 - 12 Hr Fd Winds Aloft Fcst (45,000 and 53,000 Ft)",
        "FDI":"FDI - Fire Danger Indices",
        "FFA":"FFA - Flash Flood Watch",
        "FFG":"FFG - Flash Flood Guidance",
        "FFH":"FFH - Headwater Guidance",
        "FFS":"FFS - Flash Flood Statement",
        "FFW":"FFW - Flash Flood Warning",
        "FLN":"FLN - National Flood Summary",
        "FLS":"FLS - Flood Statement",
        "FLW":"FLW - Flood Warning",
        "FOF":"FOF - Upper Wind Fallout Forecast",
        "FRW":"FRW - Fire Warning",
        "FSH":"FSH - Natl Marine Fisheries Administrative Service Message",
        "FTM":"FTM - WSR-88D Radar Outage Notification / Free Text Message",
        "FTP":"FTP - FOUS Prog Max/Min Temp/Pop Guidance",
        "FWA":"FWA - Fire Weather Administrative Message",
        "FWD":"FWD - Fire Weather Outlook Discussion",
        "FWF":"FWF - Routine Fire Wx Fcst (With/Without 6-10 Day Outlook)",
        "FWL":"FWL - Land Management Forecasts",
        "FWM":"FWM - Miscellaneous Fire Weather Product",
        "FWN":"FWN - Fire Weather Notification",
        "FWO":"FWO - Fire Weather Observation",
        "FWS":"FWS - Suppression Forecast",
        "FZL":"FZL - Freezing Level Data (RADAT)",
        "GLF":"GLF - Great Lakes Forecast",
        "GLS":"GLS - Great Lakes Storm Summary",
        "GRE":"GRE - GREEN",
        "HD1":"HD1 - RFC Derived QPF Data Product",
        "HD2":"HD2 - RFC Derived QPF Data Product",
        "HD3":"HD3 - RFC Derived QPF Data Product",
        "HD4":"HD4 - RFC Derived QPF Data Product",
        "HD7":"HD7 - RFC Derived QPF Data Product",
        "HD8":"HD8 - RFC Derived QPF Data Product",
        "HD9":"HD9 - RFC Derived QPF Data Product",
        "HLS":"HLS - Hurricane Local Statement",
        "HMD":"HMD - Hydrometeorological Discussion",
        "HML":"HML - AHPS XML",
        "HMW":"HMW - Hazardous Materials Warning",
        "HP1":"HP1 - RFC QPF Verification Product",
        "HP2":"HP2 - RFC QPF Verification Product",
        "HP3":"HP3 - RFC QPF Verification Product",
        "HP4":"HP4 - RFC QPF Verification Product",
        "HP5":"HP5 - RFC QPF Verification Product",
        "HP6":"HP6 - RFC QPF Verification Product",
        "HP7":"HP7 - RFC QPF Verification Product",
        "HP8":"HP8 - RFC QPF Verification Product",
        "HRR":"HRR - Weather Roundup",
        "HSF":"HSF - High Seas Forecast",
        "HWO":"HWO - Hazardous Weather Outlook",
        "HWR":"HWR - Hourly Weather Roundup",
        "HYD":"HYD - Daily Hydrometeorological Products",
        "HYM":"HYM - Monthly Hydrometeorological Plain Language Product",
        "ICE":"ICE - Ice Forecast",
        "IDM":"IDM - Ice Drift Vectors",
        "INI":"INI - ADMINISTR [NOUS51 KWBC]",
        "IOB":"IOB - Ice Observation",
        "KPA":"KPA - Keep Alive Message",
        "LAE":"LAE - Local Area Emergency",
        "LCD":"LCD - Preliminary Local Climatological Data",
        "LCO":"LCO - Local Cooperative Observation",
        "LEW":"LEW - Law Enforcement Warning",
        "LFP":"LFP - Local Forecast",
        "LKE":"LKE - Lake Stages",
        "LLS":"LLS - Low-Level Sounding",
        "LOW":"LOW - Low Temperatures",
        "LSR":"LSR - Local Storm Report",
        "LTG":"LTG - Lightning Data",
        "MAN":"MAN - Rawinsonde Observation Mandatory Levels",
        "MAP":"MAP - Mean Areal Precipitation",
        "MAW":"MAW - Amended Marine Forecast",
        "MFM":"MFM - Marine Forecast Matrix",
        "MIM":"MIM - Marine Interpretation Message",
        "MIS":"MIS - Miscellaneous Local Product",
        "MOB":"MOB - MOB Observations",
        "MON":"MON - Routine Space Environment Product Issued Monthly",
        "MRP":"MRP - Techniques Development Laboratory Marine Product",
        "MSM":"MSM - ASOS Monthly Summary Message",
        "MTR":"MTR - METAR Formatted Surface Weather Observation",
        "MTT":"MTT - METAR Test Message",
        "MVF":"MVF - Marine Verification Coded Message",
        "MWS":"MWS - Marine Weather Statement",
        "MWW":"MWW - Marine Weather Message",
        "NOU":"NOU - Weather Reconnaisance Flights",
        "NOW":"NOW - Short Term Forecast",
        "NOX":"NOX - Data Mgt Message",
        "NPW":"NPW - Non-Precipitation Warnings / Watches / Advisories",
        "NSH":"NSH - Nearshore Marine Forecast",
        "NUW":"NUW - Nuclear Power Plant Warning",
        "NWR":"NWR - NOAA Weather Radio Forecast",
        "OAV":"OAV - Other Aviation Products",
        "OBS":"OBS - Observations",
        "OFA":"OFA - Offshore Aviation Area Forecast",
        "OFF":"OFF - Offshore Forecast",
        "OMR":"OMR - Other Marine Products",
        "OPU":"OPU - Other Public Products",
        "OSO":"OSO - Other Surface Observations",
        "OSW":"OSW - Ocean Surface Winds",
        "OUA":"OUA - Other Upper Air Data",
        "OZF":"OZF - Zone Forecast",
        "PFM":"PFM - Point Forecast Matrices",
        "PFW":"PFW - Fire Weather Point Forecast Matrices",
        "PLS":"PLS - Plain Language Ship Report",
        "PMD":"PMD - Prognostic Meteorological Discussion",
        "PNS":"PNS - Public Information Statement",
        "POE":"POE - Probability of Exceed",
        "PRB":"PRB - Heat Index Forecast Tables",
        "PRC":"PRC - State Pilot Report Collective",
        "PRE":"PRE - Preliminary Forecasts",
        "PSH":"PSH - Post Storm Hurricane Report",
        "PTS":"PTS - Probabilistic Outlook Points",
        "PWO":"PWO - Public Severe Weather Outlook",
        "PWS":"PWS - Tropical Cyclone Probabilities",
        "QPF":"QPF - Quantitative Precipitation Forecast",
        "QPS":"QPS - Quantitative Precipitation Statement",
        "RDF":"RDF - Revised Digital Forecast",
        "REC":"REC - Recreational Report",
        "RER":"RER - Record Report",
        "RET":"RET - EAS Activation Request",
        "RFD":"RFD - Rangeland Fire Danger Forecast",
        "RFI":"RFI - RFI Observation",
        "RFR":"RFR - Route Forecast",
        "RFW":"RFW - Red Flag Warning",
        "RHW":"RHW - Radiological Hazard Warning",
        "RNS":"RNS - Rain Information Statement",
        "RR1":"RR1 - Hydro-Met Data Report Part 1",
        "RR2":"RR2 - Hydro-Met Data Report Part 2",
        "RR3":"RR3 - Hydro-Met Data Report Part 3",
        "RR4":"RR4 - Hydro-Met Data Report Part 4",
        "RR5":"RR5 - Hydro-Met Data Report Part 5",
        "RR6":"RR6 - Hydro-Met Data Report Part 6",
        "RR7":"RR7 - Hydro-Met Data Report Part 7",
        "RR8":"RR8 - Hydro-Met Data Report Part 8",
        "RR9":"RR9 - Hydro-Met Data Report Part 9",
        "RRA":"RRA - Automated Hydrologic Observation Sta Report (AHOS)",
        "RRM":"RRM - Miscellaneous Hydrologic Data",
        "RRS":"RRS - HADS Data",
        "RRY":"RRY - ASOS SHEF Hourly Routine Test Message",
        "RSD":"RSD - Daily Snotel Data",
        "RSM":"RSM - Monthly Snotel Data",
        "RTP":"RTP - Regional Max/Min Temp and Precipitation Table",
        "RVA":"RVA - River Summary",
        "RVD":"RVD - Daily River Forecasts",
        "RVF":"RVF - River Forecast",
        "RVI":"RVI - River Ice Statement",
        "RVM":"RVM - Miscellaneous River Product",
        "RVR":"RVR - River Recreation Statement",
        "RVS":"RVS - River Statement",
        "RWR":"RWR - Regional Weather Roundup",
        "RWS":"RWS - Regional Weather Summary",
        "SAB":"SAB - Special Avalanche Bulletin",
        "SAF":"SAF - Speci Agri Wx Fcst / Advisory / Flying Farmer Fcst Outlook",
        "SAG":"SAG - Snow Avalanche Guidance",
        "SAT":"SAT - APT Prediction",
        "SAW":"SAW - Prelim Notice of Watch & Cancellation Msg (Aviation)",
        "SCC":"SCC - Storm Summary",
        "SCD":"SCD - Supplementary Climatological Data (ASOS)",
        "SCN":"SCN - Soil Climate Analysis Network Data",
        "SCP":"SCP - Satellite Cloud Product",
        "SCS":"SCS - Selected Cities Summary",
        "SDO":"SDO - Supplementary Data Observation (ASOS)",
        "SDS":"SDS - Special Dispersion Statement",
        "SEL":"SEL - Severe Local Storm Watch and Watch Cancellation Msg",
        "SEV":"SEV - SPC Watch Point Information Message",
        "SFP":"SFP - State Forecast",
        "SFT":"SFT - Tabular State Forecast",
        "SGL":"SGL - Rawinsonde Observation Significant Levels",
        "SHP":"SHP - Surface Ship Report at Synoptic Time",
        "SIG":"SIG - International Sigmet / Convective Sigmet",
        "SIM":"SIM - Satellite Interpretation Message",
        "SLS":"SLS - Severe Local Storm Watch and Areal Outline",
        "SMF":"SMF - Smoke Management Weather Forecast",
        "SMW":"SMW - Special Marine Warning",
        "SOO":"SOO - SOO Product",
        "SPE":"SPE - Satellite Precipitation Estimates (TXUS20 KWBC)",
        "SPF":"SPF - Storm Strike Probability Bulletin (TPC)",
        "SPS":"SPS - Special Weather Statement",
        "SPW":"SPW - Shelter in Place Warning",
        "SQW":"SQW - Snow Squall Warning",
        "SRD":"SRD - Surf Discussion",
        "SRF":"SRF - Surf Forecast",
        "SRG":"SRG - Soaring Guidance",
        "SSM":"SSM - Main Synoptic Hour Surface Observation",
        "STA":"STA - Network and Severe Weather Statistical Summaries",
        "STD":"STD - Satellite Tropical Disturbance Summary",
        "STO":"STO - Road Condition Reports (State Agencies)",
        "STP":"STP - State Max/Min Temperature and Precipitation Table",
        "STQ":"STQ - Spot Forecast Request",
        "SUM":"SUM - Space Weather Message",
        "SVR":"SVR - Severe Thunderstorm Warning",
        "SVS":"SVS - Severe Weather Statement",
        "SWO":"SWO - Severe Storm Outlook Narrative (AC)",
        "SWS":"SWS - State Weather Summary",
        "SYN":"SYN - Regional Weather Synopsis",
        "TAF":"TAF - Terminal Aerodrome Forecast",
        "TAP":"TAP - Terminal Alerting Products",
        "TAV":"TAV - Travelers Forecast Table",
        "TCA":"TCA - Aviation Tropical Cyclone Advisory",
        "TCD":"TCD - Tropical Cyclone Discussion",
        "TCE":"TCE - Tropical Cyclone Position Estimate",
        "TCM":"TCM - Marine/Aviation Tropical Cyclone Advisory",
        "TCP":"TCP - Public Tropical Cyclone Advisory",
        "TCS":"TCS - Satellite Tropical Cyclone Summary",
        "TCU":"TCU - Tropical Cyclone Update",
        "TCV":"TCV - Tropical Cyclone Watch/Warning Break Points",
        "TIB":"TIB - Tsunami Bulletin",
        "TID":"TID - Tide Report",
        "TMA":"TMA - Tsunami Tide/Seismic Message Acknowledgement",
        "TOE":"TOE - 911 Telephone Outage Emergency",
        "TOR":"TOR - Tornado Warning",
        "TPT":"TPT - Temperature Precipitation Table (Natl and Intnl)",
        "TSU":"TSU - Tsunami Watch/Warning",
        "TUV":"TUV - Weather Bulletin",
        "TVL":"TVL - Travelers Forecast",
        "TWB":"TWB - Transcribed Weather Broadcast",
        "TWD":"TWD - Tropical Weather Discussion",
        "TWO":"TWO - Tropical Weather Outlook and Summary",
        "TWS":"TWS - Tropical Weather Summary",
        "URN":"URN - Aircraft Reconnaissance",
        "UVI":"UVI - Ultraviolet Index",
        "VAA":"VAA - Volcanic Activity Advisory",
        "VER":"VER - Forecast Verification Statistics",
        "VFT":"VFT - Terminal Aerodrome Forecast (TAF) Verification",
        "VOW":"VOW - Volcano Warning",
        "WA0":"WA0 - Airmet (Pacific)",
        "WA1":"WA1 - Airmet (Northeast)",
        "WA2":"WA2 - Airmet (Southeast)",
        "WA3":"WA3 - Airmet (North Central)",
        "WA4":"WA4 - Airmet (South Central)",
        "WA5":"WA5 - Airmet (Rocky Mountains)",
        "WA6":"WA6 - Airmet (West Coast)",
        "WA7":"WA7 - Airmet (Juneau, AK)",
        "WA8":"WA8 - Airmet (Anchorage, AK)",
        "WA9":"WA9 - Airmet (Fairbanks, AK)",
        "WAR":"WAR - Space Environment Warning",
        "WAT":"WAT - Space Environment Watch",
        "WCN":"WCN - Weather Watch Clearance Notification",
        "WCR":"WCR - Weekly Weather and Crop Report",
        "WDA":"WDA - Weekly Data for Agriculture",
        "WDU":"WDU - Warning Decision Update",
        "WEK":"WEK - Routine Space Environment Product Issued Weekly",
        "WOU":"WOU - Tornado/Severe Thunderstorm Watch",
        "WS1":"WS1 - Sigmet (Northeast)",
        "WS2":"WS2 - Sigmet (Southeast)",
        "WS3":"WS3 - Sigmet (North Central)",
        "WS4":"WS4 - Sigmet (South Central)",
        "WS5":"WS5 - Sigmet (Rocky Mountains)",
        "WS6":"WS6 - Sigmet (West Coast)",
        "WST":"WST - Tropical Cyclone Sigmet",
        "WSV":"WSV - Volcanic Activity Sigmet",
        "WSW":"WSW - Winter Weather Warnings / Watches / Advisories",
        "WWA":"WWA - Watch Status Report",
        "WWP":"WWP - Severe Thunderstorm / Tornado Watch Probabilities",
        "ZFP":"ZFP - Zone Forecast Product"
    ]
    return x
}

// ======= Pushover Routines ============

def send_message(msgData) {
    if (sendPushEnabled) 	{sendPush(msgData)}
    if (sendSMSEnabled) 	{sendSms(mobilePhone, msgData)}
    if (pushoverEnabled) 	{sendPushoverMessage(msgData)}
}

def sendPushoverMessage(msgData) {
        if (settings.pushoverDevices != null) {
            settings.pushoverDevices.each {							// Use notification devices on Hubitat
                it.deviceNotification(msgData)
            }
        }
    }

def findMyPushoverDevices() {
    Boolean validated = false
    List pushoverDevices = []
    Map params = [
        uri: "https://api.pushover.net",
        path: "/1/users/validate.json",
        contentType: "application/json",
        requestContentType: "application/json",
        body: [token: pushoverToken.trim() as String, user: pushoverUser.trim() as String] as Map
    ]
    try {
        httpPostJson(params) { resp ->
            if(resp?.status != 200) {
                logErr "Received HTTP error ${resp.status}. Check your User and App Pushover keys!"
            } else {
                if(resp?.data) {
                    if(resp?.data?.status && resp?.data?.status == 1) validated = true
                    if(resp?.data?.devices) {
                        logDebug "Found (${resp?.data?.devices?.size()}) Pushover Devices..."
                        pushoverDevices = resp?.data?.devices
                    } else {
                        logErr "Device List is empty"
                        pushoverDevices ['No devices found, Check your User and App Pushover keys!']
                    }
                } else { validated = false }
            }
            logDebug "findMyPushoverDevices | Validated: ${validated} | Resp | status: ${resp?.status} | data: ${resp?.data}"
        }
    } catch (Exception ex) {
        if(ex instanceof groovyx.net.http.HttpResponseException && ex?.response) {
            logErr "findMyPushoverDevices HttpResponseException | Status: (${ex?.response?.status}) | Data: ${ex?.response?.data}"
        } else logErr "An invalid key was probably entered. PushOver Server Returned: ${ex}"
    }
    return pushoverDevices
}

def pushoverResponse(resp, data) {
    try {
        Map headers = resp?.getHeaders()
        def limit = headers["X-Limit-App-Limit"]
        def remain = headers["X-Limit-App-Remaining"]
        def resetDt = headers["X-Limit-App-Reset"]
        if(resp?.status == 200) {
            logDebug "Message Received by Pushover Server ${(remain && limit) ? " | Monthly Messages Remaining (${remain} of ${limit})" : ""}"
        } else if (resp?.status == 429) {
            logWarn "Couldn't Send Pushover Notification... You have reached your (${limit}) notification limit for the month"
        } else {
            if(resp?.hasError()) {
                logErr "pushoverResponse: status: ${resp.status} | errorMessage: ${resp?.getErrorMessage()}"
                logErr "Received HTTP error ${resp?.status}. Check your keys!"
            }
        }
    } catch (ex) {
        if(ex instanceof groovyx.net.http.HttpResponseException && ex?.response) {
            def rData = (ex?.response?.data && ex?.response?.data != "") ? " | Data: ${ex?.response?.data}" : ""
            logErr "pushoverResponse() HttpResponseException | Status: (${ex?.response?.status})${rData}"
        } else { logErr "pushoverResponse() Exception:", ex }
    }
}