/*
*  Copyright 2026 SanderSoft™
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
*  Dates: 2018,2019,2020,2021,2022,2023,2024,2025,2026
*/

#include kurtsanders.AWSLibrary
@Field static String PARENT_DEVICE_NAME            = "Ambient Weather Station"
@Field static final String VERSION                 = "7.0.0"

//************************************ Version Specific ***********************************
String appModified()			{ return "Nov-23-2026" }
//*************************************** Constants ***************************************

String appNameVersion() 		{ return "Ambient Weather Station " + VERSION }
String appShortName() 			{ return "STAmbientWeather " + VERSION }

String DTHName() 				{ return PARENT_DEVICE_NAME  }
String DTHRemoteSensorName() 	{ return PARENT_DEVICE_NAME + " Remote Sensor"}
String DTHPMSensorName() 		{ return "Ambient Particulate Monitor"}
String DTHDNI() 				{ return "${app.id}:MyAmbientWeatherStation" }
String DTHDNIRemoteSensorName() { return "${app.id}:MyAmbientRemoteSensor"}
String DTHDNIPMName() 			{ return "${app.id}:MyAmbientParticulateMonitor"}
Integer MaxNumRemoteSensors()	{ return MAX_REMOTE_SENSORS }

String DTHnamespace()			{ return NAMESPACE }
String appAuthor()	 			{ return AUTHOR_NAME }
String AppImg(imgName) 			{ return GITHUB_IMAGES_LINK + "${imgName}" }
String wikiURL(pageName)		{ return "https://github.com/KurtSanders/STAmbientWeather/wiki/$pageName"}

String getStationDNI(String mac, Boolean legacy = false) {
    return legacy ? DTHDNI() : "${app.id}:${cleanMac(mac)}:Station"
}
String getRemoteSensorDNI(String mac, Integer idx, Boolean legacy = false) {
    return legacy ? "${DTHDNIRemoteSensorName()}${idx}" : "${app.id}:${cleanMac(mac)}:Remote:${idx}"
}
String getPMDNI(String mac, Boolean legacy = false) {
    return legacy ? DTHDNIPMName() : "${app.id}:${cleanMac(mac)}:PM"
}

def resolveStationDevice(String mac) {
    return getChildDevice(getStationDNI(mac, false)) ?: getChildDevice(getStationDNI(mac, true))
}
def resolveRemoteSensorDevice(String mac, Integer idx) {
    return getChildDevice(getRemoteSensorDNI(mac, idx, false)) ?: getChildDevice(getRemoteSensorDNI(mac, idx, true))
}
def resolvePMDevice(String mac) {
    return getChildDevice(getPMDNI(mac, false)) ?: getChildDevice(getPMDNI(mac, true))
}

List<String> getSelectedMacs() {
    if (!weatherStationMac) return []
    return (weatherStationMac instanceof List) ? weatherStationMac.collect { it.toString() } : [weatherStationMac.toString()]
}

List<String> getChildAppMacs() {
    List<String> macs = []
    getChildApps()?.each { child ->
        try {
            String m = child.getManagedMac()
            if (m) macs << m
        } catch (ignored) {}
    }
    return macs
}

List<String> getEffectiveMacs() {
    Set<String> childMacs = new HashSet(getChildAppMacs())
    return getSelectedMacs().findAll { !childMacs.contains(it) }
}

String getAppKey() { return APPKEY }
String getApiKey() { return settings.apiKey ?: state.apiKey }

// ============================================================================================================
// This APP key is ONLY for this application - Do not copy or use elsewhere
@Field static final String APPKEY                  = "33054086b3d745779f5ac35e147baa76f13e75d44ea245388ba598911905fb50"
// ============================================================================================================

definition(
    name              : PARENT_DEVICE_NAME,
    namespace         : NAMESPACE,
    author            : AUTHOR_NAME,
    description       : "Integrate your Ambient™ Weather Station(s) and remote weather/soil/particle monitor sensors to Hubitat™",
    category          : "",
    iconUrl           : "",
    iconX2Url         : "",
    documentationLink : COMM_LINK,
    singleInstance    : false,
    pausable          : false,
    menu              : "Integrations"
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

    dynamicPage(name: "apiPage", submitOnChange: true, nextPage: "mainPage", uninstall: true, install: false) {
        section(sectionHeader("Ambient Weather Station API Key")) {
            input(name: "apiKey", type: "text",
                  title: fmtTitle("Enter your Ambient Weather Station API Key below (Required)"),
                  required: true)
            paragraph ""
            href(name: "APIKeyLink",
                 title: fmtTitle("Here is a WebLink to display your Ambient Weather Station Account Page with API key (scroll to the bottom of the page to copy your API string)"),
                 required: false,
                 style: "external",
                 url: "https://ambientweather.net/account",
                 description: fmtTitle("tap to view your weather station account page and copy your API key"))
        }
    }
}

def mainPage() {
    cleanAppLabel()
    Boolean apiSetupOk = false
    if (apiKey == state.apiKey && state.stationCatalog) {
        apiSetupOk = true
    } else {
        state.apiKey = apiKey
        apiSetupOk = fetchStationCatalogSync()
    }

    state.retry = 0
    List<String> selectedMacs = getSelectedMacs()
    List<String> childAppMacs = getChildAppMacs()
    List<String> effectiveMacs = getEffectiveMacs()

    String setupMessage = ""
    String setupTitle = "${appNameVersion()} API Settings Check"
    String nextPageName = (getAllChildDevices().size() > 0 || effectiveMacs.isEmpty()) ? "optionsPage" : "remoteSensorPage"

    if (apiSetupOk && state.stationCatalog) {
        setupMessage = "SUCCESS! Valid Ambient API Key for ${appNameVersion()}. Found ${state.stationCatalog.size()} reporting weather station(s)."
        setupTitle = "<span style=\"color:red\">Select Weather Station(s) or add Child Apps below, then tap 'NEXT' for Settings</span>"
    } else {
        setupMessage = "<span style=\"color:red\">Ambient API Setup INCOMPLETE or MISSING!\n\nPlease check and/or complete the REQUIRED Ambient Weather API key setup for ${appNameVersion()}.\n\nAPI Error message: ${state.httpError}</span>"
        nextPageName = null
    }

    dynamicPage(name: "mainPage", title: setupTitle, submitOnChange: true, nextPage: nextPageName, uninstall: true, install: false) {
        section(hideable: apiSetupOk, hidden: apiSetupOk, sectionHeader(setupMessage)) {
            paragraph "<span style=\"color:blue\">The API string key is used to securely connect your weather station(s) to ${appNameVersion()}.</span>"
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

        if (apiSetupOk && state.stationCatalog) {
            Set<String> childSet = new HashSet(childAppMacs ?: [])
            Map weatherStationList = [:]
            state.stationCatalog.each { mac, info ->
                if (!childSet.contains(mac)) {
                    String loc = info.location ? " @ ${info.location}" : ""
                    weatherStationList[mac] = "${info.name}${loc} (${mac})"
                }
            }

            section(sectionHeader("Choose How to Manage Each Weather Station (Pick One Mode Per Station)")) {
                paragraph fmtDesc("Choose <b>either</b> Option A (manage station(s) directly in this app with shared settings) <b>or</b> Option B (create one Child App per station for independent units, data keys, and alerts). Do not assign the same station to both.")
            }

            section(sectionHeader("Option A: Manage Station(s) Directly in This App (Shared Settings)")) {
                if (weatherStationList) {
                    input(name: "weatherStationMac", submitOnChange: true, type: "enum",
                          title: fmtTitle("Select Weather Station(s) to manage directly in this App"),
                          options: weatherStationList,
                          multiple: true,
                          required: false)
                } else {
                    paragraph fmtDesc("All stations on your account are currently managed by Child Apps below.")
                }
                if (childAppMacs) {
                    paragraph fmtDesc("Managed by Child App(s): ${childAppMacs.collect { state.stationCatalog[it]?.name ?: it }.join(', ')}")
                }
                if (effectiveMacs) {
                    effectiveMacs.each { mac ->
                        Map info = state.stationCatalog[mac]
                        if (info) {
                            paragraph image: AppImg("blue-ball-100.jpg"),
                                title: fmtTitle("${info.name}"),
                                required: false,
                                "Location: ${info.location ?: 'Not Provided'}" +
                                "\nMAC Address: ${mac}" +
                                "\nRemote Temp/Hydro/Moisture Sensors: ${info.remoteCount ?: 0}" +
                                "\nAQIN Particulate Monitor Sensor: ${info.pmCount ?: 0}"
                        }
                    }
                    href(name: "WeatherStationOptionsLink",
                         title: fmtTitle("Weather Station Options"),
                         page: nextPageName,
                         description: "<span style=\"color:blue\">Next: Configure sensors and options -></span>")
                }
            }

            section(sectionHeader("Option B: Per-Station Child Apps (Independent Units & Alerts)")) {
                paragraph fmtDesc("Leave Option A unselected for any station you want to manage as an independent Child App. All Child Apps share this Parent App's single API poll.")
                app(name: "childStations", appName: "Ambient Weather Station Child", namespace: NAMESPACE,
                    title: "Add a Per-Station Child App", multiple: true)
            }
        }

        section("STAmbientWeather™ - ${appAuthor()}") {
            href(name: "hrefVersions",
                 image: AppImg("readme.png"),
                 title: fmtTitle("Release Notes for ${VERSION} : ${appModified()}"),
                 required: false,
                 style: "embedded",
                 url: wikiURL("Features-by-Version"))
        }
    }
}

def unitsPage() {
    dynamicPage(name: "unitsPage", title: "Ambient Units of Measure Settings", uninstall: false, install: false) {
        section("Weather Station Unit of Measure Options") {
            input(name: "tempUnits", type: "enum",
                  title: fmtTitle("Select Temperature Units of Measure"),
                  options: ['°F':'Fahrenheit °F','°C':'Celsius °C'],
                  defaultValue: "°F", required: true)
            input(name: "windUnits", type: "enum",
                  title: fmtTitle("Select Wind Speed Units of Measure"),
                  options: ['mph':'Miles per Hour','fps':'Feet per Second','mps':'Meter per Second','kph':'Kilometers per Hour','knotts':'Knotts'],
                  defaultValue: "mph", required: true)
            input(name: "measureUnits", type: "enum",
                  title: fmtTitle("Select Rainfall Units of Measure"),
                  options: ['in':'Inches','mm':'Millimeters','cm':'Centimeters'],
                  defaultValue: "in", required: true)
            input(name: "baroUnits", type: "enum",
                  title: fmtTitle("Select Barometer Units of Measure"),
                  options: ['inHg':'inHg','mmHg':'mmHg', 'hPa':'hPa'],
                  defaultValue: "inHg", required: true)
            input(name: "solarRadiationTileDisplayUnits", type: "enum",
                  title: fmtTitle("Select Solar Radiation ('Light') Units of Measure"),
                  options: ['W/m²':'Imperial Units (W/m²)','lux':'Metric Units (lux)', 'fc':'Foot Candles (fc)'],
                  defaultValue: "W/m²", required: true)
            input(name: "solarRadiationDecimalFormat", type: "enum",
                  title: fmtTitle("Select Solar Radiation ('Light') Decimal Format"),
                  options: [0, 1, 2],
                  defaultValue: 0, required: true)
        }
    }
}

def DataPage() {
    Set<String> allKeys = new LinkedHashSet<>()
    List<String> macs = getEffectiveMacs() ?: (state.stationCatalog?.keySet()?.toList() ?: [])
    macs.each { mac ->
        List keys = state.stationCatalog?.get(mac)?.keys
        if (keys) allKeys.addAll(keys)
    }
    allKeys.removeAll(DATE_KEYS_TO_KEEP)

    dynamicPage(name: "DataPage", title: getFormat("title", "Ambient Weather Station Data Key Import Selector"), submitOnChange: true,
                uninstall: false, install: false) {
        section() {
            input(name: "keepDataKeysAllOption", type: "bool",
                  title: "<span style=\"color:red\">Select ALL Data Keys? Toggle OFF to ONLY import selected weather data keys into Hubitat</span>",
                  required: true, submitOnChange: true, defaultValue: true)
        }
        if (!keepDataKeysAllOption) {
            section(getFormat("header-blue", "Partial Data Keys Selector")) {
                paragraph("<span style=\"color:blue\">This Wiki lists all data key parameters an Ambient Weather Station device might send.</span> <span style=\"color:red\">Note: Not all devices send all parameters.</span>\n\n<a href=\"${DEVICE_DATA_SPECS}\" target=\"_blank\">Weather Data Name Wiki Documentation (opens in new tab)</a>")
                input(name: "keepDataKeys", type: "enum",
                      multiple: true,
                      title: "<span style=\"color:red\">Select the Data Keys to <b><u>ONLY</u></b> import into Hubitat</span>",
                      options: allKeys.sort(),
                      offerAll: false,
                      submitOnChange: true,
                      required: true)
            }
        }
    }
}

def optionsPage() {
    if (app.label?.contains('<span')) {
        app.updateLabel(app.label.substring(0, app.label.indexOf('<span')))
    }
    List<String> effectiveMacs = getEffectiveMacs()
    Integer totalCatalogKeys = 0
    Integer totalRemoteAndPM = 0
    List<String> missingInsideNames = []
    effectiveMacs.each { mac ->
        Map info = state.stationCatalog?.get(mac)
        if (info) {
            totalCatalogKeys = Math.max(totalCatalogKeys, (info.keys?.size() ?: 3) - 3)
            totalRemoteAndPM += (info.remoteCount ?: 0) + (info.pmCount ?: 0) + 1
            String cmac = cleanMac(mac)
            if (info.keys?.contains('tempinf') && !settings["sensor_${cmac}_0"] && !resolveRemoteSensorDevice(mac, 0)) {
                missingInsideNames << (info.name ?: mac)
            }
        }
    }

    dynamicPage(name: "optionsPage", uninstall: true, install: missingInsideNames.isEmpty()) {
        section() {
            input name: "helpInfo", type: "hidden", title: fmtHelpInfo("Hubitat Community <u>WebLink</u> to ${app.name}")
            paragraph("")
        }
        section(sectionHeader("Ambient Weather Station (AWS) Preference Settings (${effectiveMacs.size()} Direct Station(s), ${getChildApps().size()} Child App(s))")) {
            input(name: "schedulerFreq", type: "enum",
                  title: fmtTitle("Poll the Ambient Weather Station Server every"),
                  options: POLLING_OPTIONS_MAP,
                  required: true,
                  defaultValue: '15')
            input(name: "showBattery", type: "bool",
                  title: fmtTitle("Show battery level from sensor(s)? (Ambient devices only report 0% and 100%)"),
                  defaultValue: true,
                  required: true)
            if (effectiveMacs) {
                href(name: "Weather Units of Measure",
                     title: fmtTitle("Select Weather Units of Measure"),
                     required: false,
                     defaultValue: "<span style=\"color:red\">Tap to Select Units</span>",
                     description: tempUnits ? "<span style=\"color:green\">${unitsSet()}</span>" : "<span style=\"color:red\">Tap to Select Units</span>",
                     page: "unitsPage")
                href(name: "Weather Station Filtered Data Key Import Selector",
                     title: fmtTitle("Select/DeSelect the weather station data keys to import (Required)"),
                     required: true,
                     defaultValue: "<span style=\"color:red\">Tap to Select Partial Data Keys to Import</span>",
                     description: keepDataKeysAllOption ? "<span style=\"color:green\">All Weather Data Keys selected.</span> <span style=\"color:blue\">Tap to Select Partial Weather Data Keys</span>" : "<span style=\"color:red\">${keepDataKeys?.size() ?: 0} of ${totalCatalogKeys} Data Keys Selected.</span> <span style=\"color:blue\">Tap to Modify Selection</span>",
                     page: "DataPage")
                href(name: "Activate Weather Alerts/Notification",
                     title: fmtTitle("Weather Alerts/Notification (Optional)"),
                     required: false,
                     defaultValue: pushoverEnabled ? "<span style=\"color:green\">Alerts Activated</span>" : "<span style=\"color:blue\">Tap to Activate Alerts</span>",
                     description: pushoverEnabled ? "<span style=\"color:green\">Alerts Activated</span>" : "<span style=\"color:blue\">Tap to Activate Alerts</span>",
                     page: "notifyPage")
                href(name: "Weather Sensor Device Label Management",
                     title: fmtTitle("Weather Sensor Names" + (missingInsideNames ? " (Required)" : "")),
                     description: missingInsideNames ? "<span style=\"color:red\">REQUIRED: Tap to name inside console sensor(s) for: ${missingInsideNames.join(', ')}</span>" : "<span style=\"color:blue\">Tap to Change Device Labels (${totalRemoteAndPM} sensors across ${effectiveMacs.size()} station(s))</span>",
                     required: false,
                     page: "remoteSensorPage")
            }
        }
        section(sectionHeader('Name this instance of Ambient Weather Station')) {
            label(name: "name",
                  title: fmtTitle("Assign a name to this App"),
                  state: (name ? "complete" : null),
                  defaultValue: state.weatherStationName ?: PARENT_DEVICE_NAME,
                  required: false,
                  submitOnChange: true)
        }
        section(sectionHeader("AWS Logging Options")) {
            if (logLevel == null && logLevelTime == null) {
                log.info "${app.name}: Setting Initial logLevel and logLevelTime defaults"
                app.updateSetting("logLevel", [type: "enum", value: "3"])
                app.updateSetting("logLevelTime", [type: "enum", value: "30"])
            }
            input name: "logLevel", type: "enum", title: fmtTitle("Logging Level"), submitOnChange: true,
                description: fmtDesc("Logs selected level and above"), defaultValue: "3", options: LOG_LEVELS
            input name: "logLevelTime", type: "enum", title: fmtTitle("Logging Level Time"), submitOnChange: true,
                description: fmtDesc("Time to enable Debug/Trace logging"), defaultValue: "10", options: LOG_TIMES
            input name: "SyncLogOptions", type: "button", title: "Sync Log Options in all Devices"
        }
    }
}

def remoteSensorPage() {
    List<String> effectiveMacs = getEffectiveMacs()

    dynamicPage(name: "remoteSensorPage", nextPage: "optionsPage", uninstall: false, install: false) {
        effectiveMacs.each { mac ->
            Map info = state.stationCatalog?.get(mac)
            if (!info) return
            String cmac = cleanMac(mac)
            List keys = info.keys ?: []
            String stationLabel = info.name ?: mac

            def existingConsole = resolveRemoteSensorDevice(mac, 0)
            if (existingConsole && settings["sensor_${cmac}_0"] == null) {
                app.updateSetting("sensor_${cmac}_0", [type: "string", value: existingConsole.label])
            }
            if (keys.contains('tempinf')) {
                section(sectionHeader("${stationLabel}: Inside Console Sensor Name (Required)")) {
                    input(name: "sensor_${cmac}_0",
                          type: "text",
                          title: fmtTitle("Enter a unique location name for this inside console (e.g., Kitchen, Office, Basement)"),
                          required: true,
                          defaultValue: existingConsole?.label,
                          submitOnChange: true)
                }
            }

            if ((info.remoteCount ?: 0) > 0) {
                section(sectionHeader("${stationLabel}: Remote Temperature/Soil Sensors (${info.remoteCount})")) {
                    paragraph getFormat("text-red",
                        "Create short descriptive names for each remote sensor or accept the default. Avoid special characters.")
                    for (int i = 1; i <= MaxNumRemoteSensors(); i++) {
                        def existingRemote = resolveRemoteSensorDevice(mac, i)
                        if (existingRemote && settings["sensor_${cmac}_${i}"] == null) {
                            app.updateSetting("sensor_${cmac}_${i}", [type: "string", value: existingRemote.label])
                        }
                        if (keys.contains("temp${i}f")) {
                            input(name: "sensor_${cmac}_${i}", type: "text",
                                  title: getImage("checkMarkGreen") + fmtTitle("${stationLabel} Remote #${i} → Temp Sensor"),
                                  defaultValue: existingRemote?.label ?: "Temp Sensor #${i}", required: true)
                        } else if (keys.contains("soiltemp${i}")) {
                            input(name: "sensor_${cmac}_${i}", type: "text",
                                  title: getImage("checkMarkGreen") + fmtTitle("${stationLabel} Remote #${i} → Soil Temp Sensor"),
                                  defaultValue: existingRemote?.label ?: "Soil Temp Sensor #${i}", required: true)
                        } else if (keys.contains("soilhum${i}")) {
                            input(name: "sensor_${cmac}_${i}", type: "text",
                                  title: getImage("checkMarkGreen") + fmtTitle("${stationLabel} Remote #${i} → Soil Moisture Sensor"),
                                  defaultValue: existingRemote?.label ?: "Soil Moisture Sensor #${i}", required: true)
                        }
                    }
                }
            }

            if ((info.pmCount ?: 0) > 0) {
                def existingPM = resolvePMDevice(mac)
                if (existingPM && settings["pm_${cmac}"] == null) {
                    app.updateSetting("pm_${cmac}", [type: "string", value: existingPM.label])
                }
                section(sectionHeader("${stationLabel}: Particulate Monitor Name")) {
                    paragraph image: AppImg("ambient-weather-pm25.jpg"),
                        title: fmtTitle("Friendly name for your Ambient Particulate Monitor PM10/PM25/AQIN/CO2"),
                        required: false, null
                    input(name: "pm_${cmac}", type: "text",
                          title: fmtTitle("${stationLabel} Particulate Monitor"),
                          defaultValue: existingPM?.label ?: "AWS Particle Monitor", required: true)
                }
            }
        }
    }
}

def notifyPage() {
    List<String> effectiveMacs = getEffectiveMacs()
    Boolean hasTemp = effectiveMacs.any { mac -> state.stationCatalog?.get(mac)?.keys?.any { it ==~ /^temp1?f$/ } }
    Boolean hasRain = effectiveMacs.any { mac -> state.stationCatalog?.get(mac)?.keys?.contains('hourlyrainin') }

    dynamicPage(name: "notifyPage", title: "Weather Alerts/Notifications", uninstall: false, install: false) {
        section(sectionHeader("Enable Pushover™ and/or Notification device(s)")) {
            input("pushoverEnabled", "bool", title: "Use Notification Device(s) for Alert Notifications", required: false, submitOnChange: true)
            if (pushoverEnabled) {
                input(name: "pushoverDevices", type: "capability.notification", title: "", required: false, multiple: true,
                      description: "Select notification device(s)", submitOnChange: true)
                paragraph ""
            }
        }

        if (pushoverEnabled) {
            section(sectionHeader("Weather Station Notify Options")) {
                input(name: "notifyAlertFreq", type: "enum",
                      required: true,
                      title: "Restrict notification(s) per event type to once every NUMBER of hours (Default is 24, Once/day)",
                      options: [0, 1, 2, 4, 6, 12, 24],
                      defaultValue: 24,
                      submitOnChange: true,
                      multiple: false)
                if (hasTemp) {
                    input(name: "notifyAlertLowTemp", type: "number", required: false,
                          title: "Notify when a temperature value is EQUAL OR BELOW this value. Leave blank to cancel.")
                    input(name: "notifyAlertHighTemp", type: "number", required: false,
                          title: "Notify when a temperature value is EQUAL OR ABOVE this value. Leave blank to cancel.")
                }
                if (hasRain) {
                    input(name: "notifyRain", type: "bool", required: false,
                          title: "Notify when RAIN is detected")
                }
            }
        }
        section(hideable: true, hidden: true, sectionHeader("Last Notification Times")) {
            paragraph image: "", required: false, SMSNotifcationHistory()
        }
    }
}

def appButtonHandler(String buttonName) {
    logDebug "appButtonHandler: buttonName: ${buttonName}"
    if (buttonName == "SyncLogOptions") {
        Integer level = settings.logLevel as Integer ?: 0
        Integer time = settings.logLevelTime as Integer ?: 0
        logInfo "Synchronizing all AWS devices to level: '${LOG_LEVELS[level]}' and time: '${LOG_TIMES[time]}'"
        getAllChildDevices().each {
            it.syncLogLevelApp2Children(level, time)
        }
        getChildApps()?.each { child ->
            try { child.syncLogLevelFromParent(level, time) } catch (ignored) {}
        }
    }
}

def initialize() {
    logInfo "initialize()"
    // Remove legacy bloated state keys if upgrading from v6.x
    state.remove("respdata")
    state.remove("ambientMap")
    state.remove("weatherStationDataIndex")

    state.notifyHistory         = state.notifyHistory ?: [:]
    state.notifyAlertFreq       = notifyAlertFreq ?: 24
    state.tempUnitsDisplay      = tempUnits ?: "°F"
    state.windUnitsDisplay      = windUnits ?: "mph"
    state.measureUnitsDisplay   = measureUnits ?: "in"
    state.baroUnitsDisplay      = baroUnits ?: "inHg"

    List<String> effectiveMacs = getEffectiveMacs()
    if (effectiveMacs) {
        List<String> names = effectiveMacs.collect { state.stationCatalog?.get(it)?.name ?: it }
        state.weatherStationName = names.join(", ")
    } else {
        state.weatherStationName = PARENT_DEVICE_NAME
    }

    addAmbientChildDevices()

    if (state.schedulerFreq != schedulerFreq) {
        logDebug "state.schedulerFreq -> ${state.schedulerFreq} and schedulerFreq -> ${schedulerFreq}"
        setScheduler(schedulerFreq)
        effectiveMacs.each { mac ->
            def d = resolveStationDevice(mac)
            if (d) d.sendEvent(name: "scheduleFreqMin", value: schedulerFreq)
        }
        state.schedulerFreq = schedulerFreq
    }
    checkLogLevel()
}

def installed() {
    logInfo "installed()"
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

def setPollingInterval(pollingInterval = null) {
    logDebug "==> setPollingInterval(${pollingInterval})"
    if (POLLING_OPTIONS_MAP.containsKey(pollingInterval)) {
        app.updateSetting("schedulerFreq", [type: "enum", value: pollingInterval])
        if (state.schedulerFreq != schedulerFreq) {
            setScheduler(schedulerFreq)
            getEffectiveMacs().each { mac ->
                def d = resolveStationDevice(mac)
                if (d) d.sendEvent(name: "scheduleFreqMin", value: schedulerFreq)
            }
            state.schedulerFreq = schedulerFreq
        }
        return true
    } else {
        logErr "Invalid polling interval '${pollingInterval}'. Valid keys: ${POLLING_OPTIONS_MAP.keySet()}"
        return false
    }
}

def scheduleCheckReset(Boolean quiet = false) {
    if (schedulerFreq && schedulerFreq != '0') {
        setScheduler(schedulerFreq)
        if (!quiet) {
            Date end = new Date()
            use(TimeCategory) {
                end = end + schedulerFreq.toInteger().minutes
            }
            logInfo "Reset next CRON Refresh to ~${schedulerFreq} mins from now (${end.format('h:mm:ss a', location.timeZone)})"
        }
    }
}

def refresh() {
    updateMyLabel('refreshing')
    logInfo "Device: 'Refresh ALL'"
    Integer runID = new Random().nextInt(10000)
    main(runID)
}

def autoScheduleHandler() {
    Integer runID = new Random().nextInt(10000)
    logInfo "Executing Cron Schedule runID: ${runID} every ${schedulerFreq} min(s)"
    main(runID)
}

def main(runID = null) {
    runID = runID ?: new Random().nextInt(10000)
    logInfo "Main(#${runID}): Fetching Ambient Weather Station API data"
    fetchAmbientDataAsync(runID)
}

def retryQuick(data) {
    logInfo "retryQuick #${state.retry} RunID: ${data.runID}"
    updateMyLabel('retry')
    fetchAmbientDataAsync(data.runID)
}

Boolean fetchStationCatalogSync() {
    String key = getApiKey()
    if (!key) {
        state.httpError = "missing api key"
        return false
    }
    Map params = [
        uri        : "https://api.ambientweather.net",
        path       : "/v1/devices",
        contentType: 'application/json',
        query      : ["applicationKey": APPKEY, "apiKey": key]
    ]
    try {
        httpGet(params) { resp ->
            if (resp.status != 200) {
                state.httpError = "http status ${resp.status}"
                return false
            }
            state.remove("httpError")
            updateStationCatalog(resp.data as List)
        }
        return true
    } catch (e) {
        state.httpError = e.toString().toLowerCase()
        logDebug "fetchStationCatalogSync error: ${e}"
        return false
    }
}

void updateStationCatalog(List stations) {
    if (!stations) return
    Map catalog = [:]
    stations.each { st ->
        String mac = st?.macAddress
        if (!mac) return
        Map lastData = (st.lastData instanceof Map) ? st.lastData : [:]
        String loc = st.info?.location ?: (st.info?.containsKey("coords") ? st.info.coords.location : "")
        catalog[mac] = [
            name       : st.info?.name ?: mac,
            location   : loc ?: "",
            keys       : lastData.keySet().collect { it.toString() },
            remoteCount: countStationRemoteSensors(lastData),
            pmCount    : countStationParticulateMonitors(lastData)
        ]
    }
    state.stationCatalog = catalog
}

void fetchAmbientDataAsync(runID = 0) {
    Long currentMs = now()
    Long lastMs = state.lastGETAmbientStationData ?: 0L
    Integer timeSecsLastRun = ((currentMs - lastMs) / 1000).toInteger()
    if (runID != 0 && lastMs > 0 && timeSecsLastRun < 2) {
        logWarn "Aborting AmbientStationData run ${runID}: Too Short (${timeSecsLastRun}s) for API Limits"
        return
    }
    state.lastGETAmbientStationData = currentMs
    String key = getApiKey()
    if (!key) {
        logErr "Severe Error: The API key is UNDEFINED in ${app.name}"
        return
    }
    state.retry = state.retry ?: 0
    scheduleCheckReset(true)

    Map params = [
        uri        : "https://api.ambientweather.net",
        path       : "/v1/devices",
        contentType: 'application/json',
        query      : ["applicationKey": APPKEY, "apiKey": key]
    ]
    asynchttpGet('ambientDataCallback', params, [runID: runID])
}

void ambientDataCallback(resp, data) {
    def runID = data?.runID ?: "async"
    if (resp.hasError() || resp.status != 200) {
        String errMsg = resp.hasError() ? resp.getErrorMessage() : "HTTP ${resp.status}"
        logWarn "Ambient API async error (runID ${runID}): ${errMsg}"
        state.httpError = errMsg?.toLowerCase()
        if (resp.status == 401 || errMsg?.toLowerCase()?.contains("unauthorized")) {
            updateMyLabel('unauthorized')
            return
        }
        state.retry = (state.retry ?: 0).toInteger() + 1
        if (state.retry.toInteger() < 4) {
            logInfo "Waiting 10 seconds to retry runID ${runID}: Attempt #${state.retry}"
            updateMyLabel('retry')
            runIn(10, 'retryQuick', [overwrite: true, data: [runID: runID]])
        }
        return
    }

    state.remove("httpError")
    state.retry = 0
    List stations = (resp.json instanceof List) ? resp.json : []
    if (!stations) {
        logWarn "Ambient API returned empty station list (runID ${runID})"
        return
    }

    updateStationCatalog(stations)
    Map<String, Map> byMac = [:]
    stations.each { st ->
        if (st?.macAddress) byMac[st.macAddress] = st
    }

    // 1. Process stations managed directly by this App (Single App Multi-MAC)
    getEffectiveMacs().each { mac ->
        Map stationObj = byMac[mac]
        if (stationObj) {
            processStation(stationObj, runID)
        } else {
            logWarn "Selected station MAC ${mac} was not found in Ambient API response"
        }
    }

    // 2. Dispatch to any per-station Child Apps (Parent-Child)
    getChildApps()?.each { child ->
        try {
            String childMac = child.getManagedMac()
            if (childMac && byMac[childMac]) {
                child.processStationData(byMac[childMac], runID)
            }
        } catch (ex) {
            logErr "Error dispatching data to Child App '${child?.label}': ${ex}"
        }
    }

    updateMyLabel('updated')
    logInfo "SUCCESS: AmbientStationData updated for runID: ${runID}"
}

void processStation(Map stationObj, runID = "async") {
    String mac = stationObj.macAddress
    def d = resolveStationDevice(mac)
    if (!d) {
        logWarn "Station child device not found for MAC ${mac}; creating devices now"
        addAmbientChildDevices()
        d = resolveStationDevice(mac)
        if (!d) return
    }

    Map rawLastData = (stationObj.lastData instanceof Map) ? stationObj.lastData : [:]
    if (!rawLastData) return

    // Resolve units (fallback to hub location scale)
    if (tempUnits == null) {
        String scale = location?.temperatureScale ?: "F"
        state.tempUnitsDisplay = "°${scale}"
        state.windUnitsDisplay = (scale == "F") ? "mph" : "kph"
        state.measureUnitsDisplay = (scale == "F") ? "in" : "cm"
        state.baroUnitsDisplay = (scale == "F") ? "inHg" : "mmHg"
    }
    Map unitSettings = [
        tempUnits   : state.tempUnitsDisplay,
        windUnits   : state.windUnitsDisplay,
        measureUnits: state.measureUnitsDisplay,
        baroUnits   : state.baroUnitsDisplay
    ]

    Boolean keepAll = (keepDataKeysAllOption != false)
    Map filtered = filterStationLastData(rawLastData, keepAll, (keepDataKeys instanceof List) ? keepDataKeys : [])
    Map lastData = convertStationLastData(filtered, unitSettings)

    String serverDate = convertToCurrentTimeZone(rawLastData.date?.toString())
    String updatedStr = tileLastUpdated()

    if (lastData.containsKey('lastRain')) {
        try {
            Date dateRain = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", lastData.lastRain.toString())
            use(TimeCategory) {
                def m = ((new Date() - dateRain) =~ /(.+)\b,/)
                if (m.find()) {
                    d.sendEvent(name: "lastRainDuration", value: m[0][1], displayed: false)
                }
            }
        } catch (ignored) {}
    }

    d.sendEvent(name: "scheduleFreqMin", value: schedulerFreq, descriptionText: "AWS Polling Interval")
    d.sendEvent(name: "lastSTupdate", value: updatedStr)
    d.sendEvent(name: "macAddress", value: mac)

    if (!lastData.containsKey('tempf') && lastData.containsKey('temp1f')) {
        d.sendEvent(name: "temperature", value: lastData.temp1f, units: state.tempUnitsDisplay)
    }
    if (!lastData.containsKey('tempf') && lastData.containsKey('humidity1')) {
        d.sendEvent(name: "humidity", value: lastData.humidity1, units: "%", displayed: false)
        d.sendEvent(name: "humidity_display", value: "${lastData.humidity1}%")
    }
    if (!lastData.containsKey('tempinf') && lastData.containsKey('temp1f')) {
        d.sendEvent(name: "tempinf", value: lastData.temp1f, units: state.tempUnitsDisplay, displayed: false)
        d.sendEvent(name: "tempinf_display", value: "${lastData.temp1f}${state.tempUnitsDisplay}")
    }
    if (!lastData.containsKey('humidityin') && lastData.containsKey('humidity1')) {
        d.sendEvent(name: "humidityin", value: lastData.humidity1, units: "%", displayed: false)
        d.sendEvent(name: "humidityin_display", value: "${lastData.humidity1}%")
    }

    Map infoBase = (stationObj.info instanceof Map) ? stationObj.info : [:]
    d.sendEvent(name: 'pwsName',   value: infoBase?.name)
    d.sendEvent(name: 'location',  value: infoBase?.location)
    d.sendEvent(name: 'lat',       value: infoBase?.coords?.coords?.lat)
    d.sendEvent(name: 'lon',       value: infoBase?.coords?.coords?.lon)
    d.sendEvent(name: 'address',   value: infoBase?.coords?.address)
    d.sendEvent(name: 'elevation', value: infoBase?.coords?.elevation)

    lastData.each { k, v ->
        if (v == null) return
        switch (k) {
            case ~/.*rain.*/:
                d.sendEvent(name: "${k}_display", value: "${v} ${state.measureUnitsDisplay}")
                break
            case ~/^barom.*/:
                d.sendEvent(name: "${k}_display", value: "${v} ${state.baroUnitsDisplay}")
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
                if (showBattery != null && !showBattery) {
                    v = 1
                    lastData[k] = 1
                }
                break
        }

        Boolean okToSendEvent = true
        def remoteSensorDev = null
        switch (k) {
            case 'dateutc':
                okToSendEvent = false
                break
            case 'date':
                v = serverDate
                break
            case 'battin':
                k = 'battery'
                d.sendEvent(name: k, value: v.toInteger() * 100, units: '%', displayed: false)
                break
            case 'battout':
                k = 'battery'
                v = v.toInteger() * 100
                break
            case ~/^batt[0-9].*/:
                okToSendEvent = false
                break
            case 'lastRain':
                v = convertToCurrentTimeZone(v.toString())
                break
            case 'lightning_time':
                v = new Date(v as Long).toString()
                break
            case 'tempf':
                k = 'temperature'
                break
            case ~/^feelsLike$|^feelsLikein$/:
                break
            case 'windspeedmph':
                d.sendEvent(name: "wind", value: v, displayed: false)
                d.sendEvent(name: "windSpeed", value: v, displayed: false)
                break
            case 'winddir':
                String windDirLong = degToCompass(lastData?.winddir, true)
                d.sendEvent(name: 'winddirection', value: windDirLong, displayed: false)
                d.sendEvent(name: 'wind_cardinal', value: degToCompass(lastData?.winddir, false), displayed: false)
                d.sendEvent(name: 'winddir2', value: "${windDirLong} (${lastData.winddir}º)")
                d.sendEvent(name: 'windVector', value: lastData?.winddir, displayed: false)
                d.sendEvent(name: 'windDirection', value: lastData?.winddir, displayed: false)
                break
            case 'uv':
                String uviRange
                switch (v) {
                    case { it < 3 }:  uviRange = "Low (${v})"; break
                    case { it < 6 }:  uviRange = "Medium (${v})"; break
                    case { it < 8 }:  uviRange = "High (${v})"; break
                    case { it < 11 }: uviRange = "Very High (${v})"; break
                    default:          uviRange = "Extreme (${v})"; break
                }
                d.sendEvent(name: 'ultravioletIndexDisplay', value: uviRange)
                k = 'ultravioletIndex'
                break
            case 'yearlyrainin':
                k = 'totalrainin'
                break
            case 'solarradiation':
                def vDisplay = null
                Float sr = v.toFloat()
                if (solarRadiationTileDisplayUnits == 'lux') sr = wm2lux(sr)
                else if (solarRadiationTileDisplayUnits == 'fc') sr = wm2fc(sr)
                if (solarRadiationDecimalFormat != null && sr > 0) {
                    String fmt = "%.${solarRadiationDecimalFormat}f"
                    sr = String.format(fmt, sr).toFloat()
                    vDisplay = String.format("%,.${solarRadiationDecimalFormat}f", sr)
                }
                v = sr
                String srUnits = solarRadiationTileDisplayUnits ?: 'W/m²'
                d.sendEvent(name: k, value: v, units: srUnits)
                d.sendEvent(name: 'solarradiation_display', value: "${vDisplay ?: String.format('%,.0f', v)} ${srUnits}", units: srUnits)
                k = 'illuminance'
                break
            case 'tempinf':
                remoteSensorDev = resolveRemoteSensorDevice(mac, 0)
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "temperature", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDev.sendEvent(name: "date", value: serverDate, displayed: false)
                    remoteSensorDev.sendEvent(name: "lastSTupdate", value: updatedStr, displayed: false)
                    if (lastData.containsKey('battout')) {
                        remoteSensorDev.sendEvent(name: "battery", value: lastData.battout.toInteger() * 100, displayed: false)
                    }
                }
                break
            case 'humidityin':
                remoteSensorDev = resolveRemoteSensorDevice(mac, 0)
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "humidity", value: v, units: "%", displayed: false)
                    remoteSensorDev.sendEvent(name: "humidity_display", value: "${v}%")
                }
                break
            case ~/^temp[0-9][0-9]?f$|^soiltemp[0-9][0-9]?$/:
                Integer idx = k.findAll(/\d+/)[0].toInteger()
                remoteSensorDev = resolveRemoteSensorDevice(mac, idx)
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "temperature", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDev.sendEvent(name: "lastSTupdate", value: updatedStr, displayed: false)
                    remoteSensorDev.sendEvent(name: "date", value: serverDate, displayed: false)
                    String battKey = "batt${idx}"
                    if (lastData.containsKey(battKey)) {
                        remoteSensorDev.sendEvent(name: "battery", value: lastData[battKey].toInteger() * 100, displayed: false)
                    }
                }
                okToSendEvent = false
                break
            case ~/^dewPoint\d+/:
                Integer idx = k.findAll(/\d+/)[0].toInteger()
                remoteSensorDev = resolveRemoteSensorDevice(mac, idx)
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "dewpoint", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDev.sendEvent(name: "dewPoint", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDev.sendEvent(name: "dewPoint_display", value: "${v}${state.tempUnitsDisplay}", units: state.tempUnitsDisplay)
                }
                okToSendEvent = false
                break
            case ~/^feelsLike\d+/:
                Integer idx = k.findAll(/\d+/)[0].toInteger()
                remoteSensorDev = resolveRemoteSensorDevice(mac, idx)
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "feelsLike", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDev.sendEvent(name: "feelsLike_display", value: "${v}${state.tempUnitsDisplay}", units: state.tempUnitsDisplay)
                }
                okToSendEvent = false
                break
            case ~/^feelsLikein\d+/:
                Integer idx = k.findAll(/\d+/)[0].toInteger()
                remoteSensorDev = resolveRemoteSensorDevice(mac, idx)
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "feelsLikein", value: v, units: state.tempUnitsDisplay)
                    remoteSensorDev.sendEvent(name: "feelsLikein_display", value: "${v}${state.tempUnitsDisplay}", units: state.tempUnitsDisplay)
                }
                okToSendEvent = false
                break
            case ~/^humidity[0-9][0-9]?$|^soilhum[0-9][0-9]?$/:
                Integer idx = k.findAll(/\d+/)[0].toInteger()
                remoteSensorDev = resolveRemoteSensorDevice(mac, idx)
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "humidity", value: v, units: "%", displayed: false)
                    remoteSensorDev.sendEvent(name: "humidity_display", value: "${v}%")
                }
                okToSendEvent = false
                break
            case ~/(^pm.*)|(^co2.*)|(^aqi.*)/:
                remoteSensorDev = resolvePMDevice(mac)
                if (remoteSensorDev) {
                    String sensorUnits = ''
                    if (k.startsWith('co2')) sensorUnits = 'ppm'
                    else if (k ==~ /^pm\d\d.*/) sensorUnits = 'µg/m3'
                    else if (k.contains('temp')) sensorUnits = state.tempUnitsDisplay
                    else if (k.contains('humidity')) sensorUnits = '%'
                    remoteSensorDev.sendEvent(name: k, value: v, units: sensorUnits)
                    remoteSensorDev.sendEvent(name: "lastSTupdate", value: updatedStr, displayed: false)
                    remoteSensorDev.sendEvent(name: "date", value: serverDate, displayed: false)
                    if (lastData.containsKey("batt_25")) {
                        remoteSensorDev.sendEvent(name: "battery", value: lastData.batt_25.toInteger() * 100, displayed: false)
                    } else if (lastData.containsKey("batt_co2")) {
                        remoteSensorDev.sendEvent(name: "battery", value: lastData.batt_co2.toInteger() * 100, displayed: false)
                    }
                }
                okToSendEvent = false
                break
        }

        if (okToSendEvent) {
            switch (k) {
                case 'battery':
                case 'date':
                    d.sendEvent(name: k, value: v, displayed: false)
                    break
                case ~/^temp.*/:
                    d.sendEvent(name: k, value: v, units: state.tempUnitsDisplay, displayed: false)
                    break
                case ~/^feelsLike$|^feelsLikein$/:
                    d.sendEvent(name: k, value: v, units: state.tempUnitsDisplay)
                    break
                case 'dewPointin':
                case 'dewPoint':
                    d.sendEvent(name: k, value: v, units: state.tempUnitsDisplay, displayed: false)
                    d.sendEvent(name: k.toLowerCase(), value: v, units: state.tempUnitsDisplay, displayed: false)
                    break
                case 'illuminance':
                    d.sendEvent(name: k, value: v, units: solarRadiationTileDisplayUnits ?: 'W/m²', displayed: false)
                    break
                case ~/^humidity.*/:
                    d.sendEvent(name: k, value: v, units: '%', displayed: false)
                    break
                case ~/.*rain.*/:
                    d.sendEvent(name: k, value: v, units: state.measureUnitsDisplay, displayed: false)
                    break
                case 'winddir':
                    d.sendEvent(name: k, value: v, units: 'º', displayed: false)
                    break
                case ~/^wind.*/:
                case 'maxdailygust':
                    d.sendEvent(name: k, value: v, units: state.windUnitsDisplay, displayed: false)
                    break
                case ~/^barom.*/:
                    d.sendEvent(name: k, value: v, units: state.baroUnitsDisplay, displayed: false)
                    break
                default:
                    d.sendEvent(name: k, value: v)
                    break
            }
        }
    }

    notifyEventsForStation(stationObj, lastData)
}

def addAmbientChildDevices() {
    List<String> effectiveMacs = getEffectiveMacs()
    effectiveMacs.each { mac ->
        Map info = state.stationCatalog?.get(mac)
        List keys = info?.keys ?: []
        String stationName = info?.name ?: mac
        String cmac = cleanMac(mac)

        // 1. Main Station Console Device
        def stationDev = resolveStationDevice(mac)
        if (!stationDev) {
            String dni = getStationDNI(mac, false)
            String awsName = "${stationName}-Console"
            String awsLabel = (effectiveMacs.size() > 1) ? "AWS-${stationName}" : "AWS-Console"
            try {
                addChildDevice(DTHnamespace(), DTHName(), dni, null, ["name": awsName, "label": awsLabel, completedSetup: true])
                logInfo "Added station device '${awsLabel}' with DNI '${dni}'"
            } catch (ex) {
                logErr "Failed to add '${DTHName()}': ${ex}"
            }
        }

        // 2. Remote Sensors (0 = inside console, 1..8 = remote sensors)
        for (int i = 0; i <= MaxNumRemoteSensors(); i++) {
            String defaultLabel = null
            if (i == 0) {
                if (keys.contains('tempinf')) defaultLabel = "${stationName} Inside"
            } else {
                if (keys.contains("temp${i}f")) defaultLabel = "Temp Sensor #${i}"
                else if (keys.contains("soiltemp${i}")) defaultLabel = "Soil Temp Sensor #${i}"
                else if (keys.contains("soilhum${i}")) defaultLabel = "Soil Moisture Sensor #${i}"
            }
            String customLabel = settings["sensor_${cmac}_${i}"] ?: ((effectiveMacs.size() == 1) ? settings["${DTHDNIRemoteSensorName()}${i}"] : null)
            String labelVal = customLabel ?: defaultLabel
            if (!labelVal) continue

            def existing = resolveRemoteSensorDevice(mac, i)
            if (!existing) {
                String dni = getRemoteSensorDNI(mac, i, false)
                try {
                    addChildDevice(DTHnamespace(), DTHRemoteSensorName(), dni, null, ["name": "${stationName}-${labelVal}", "label": "AWS-${labelVal}", completedSetup: true])
                    logInfo "Added remote sensor #${i} '${labelVal}' with DNI '${dni}'"
                } catch (ex) {
                    logErr "Failed to add '${DTHRemoteSensorName()}': ${ex}"
                }
            } else if (customLabel && existing.label != labelVal && existing.label != "AWS-${labelVal}") {
                existing.label = labelVal
            }
        }

        // 3. Particulate Monitor
        String customPM = settings["pm_${cmac}"] ?: ((effectiveMacs.size() == 1) ? settings["${DTHDNIPMName()}"] : null)
        String pmLabel = customPM ?: (((info?.pmCount ?: 0) > 0) ? "AWS Particle Monitor" : null)
        if (pmLabel) {
            def existingPM = resolvePMDevice(mac)
            if (!existingPM) {
                String dni = getPMDNI(mac, false)
                try {
                    addChildDevice(DTHnamespace(), DTHPMSensorName(), dni, null, ["name": "${stationName}-${pmLabel}", "label": "AWS-${pmLabel}", completedSetup: true])
                    logInfo "Added particulate monitor '${pmLabel}' with DNI '${dni}'"
                } catch (ex) {
                    logErr "Failed to add '${DTHPMSensorName()}': ${ex}"
                }
            } else if (customPM && existingPM.label != pmLabel && existingPM.label != "AWS-${pmLabel}") {
                existingPM.label = pmLabel
            }
        }
    }
}

def setScheduler(schedulerFreq) {
    String scheduleHandler = 'autoScheduleHandler'
    unschedule(scheduleHandler)
    Integer randomInt = Math.abs(new Random().nextInt() % 59) + 1
    logInfo "Auto Schedule Refresh Rate is now -> ${schedulerFreq} mins"
    switch (schedulerFreq) {
        case '0':   logInfo "Auto Schedule Refresh Rate is now: OFF"; break
        case '1':   runEvery1Minute(scheduleHandler); break
        case '2':   schedule("0 ${randomInt}/2 * * * ?", scheduleHandler); break
        case '3':   schedule("0 ${randomInt}/3 * * * ?", scheduleHandler); break
        case '4':   schedule("0 ${randomInt}/4 * * * ?", scheduleHandler); break
        case '5':   runEvery5Minutes(scheduleHandler); break
        case '10':  runEvery10Minutes(scheduleHandler); break
        case '15':  runEvery15Minutes(scheduleHandler); break
        case '30':  runEvery30Minutes(scheduleHandler); break
        case '60':  runEvery1Hour(scheduleHandler); break
        case '120': schedule("0 ${randomInt} 0/2 * * ?", scheduleHandler); break
        case '180': runEvery3Hours(scheduleHandler); break
        default:    unschedule(); break
    }
}

def informationList(variable) {
    if (variable == "apiHelp") {
        List items = [
            "You MUST enter your Ambient Weather API key in the ${appNameVersion()}.",
            "Visit your Ambient Weather Dashboard's Account page.",
            "Create/Copy your API key from the bottom of the page.",
            "Return to your Hubitat App and enter it."
        ]
        return items.withIndex().collect { item, idx -> "${idx + 1}. ${item}" }.join("\n")
    }
    return variable
}

String SMSNotifcationHistory() {
    if (!state.notifyHistory) return "No notifications sent yet."
    String today = new Date(now()).format("MMM-dd-yyyy", location.timeZone)
    StringBuilder sb = new StringBuilder()
    state.notifyHistory.each { cmac, hist ->
        sb.append("Station ${cmac}:\n")
        ["lowTempDT": "Low Temp", "highTempDT": "High Temp", "rainDT": "Rain Detected"].each { key, label ->
            Long ts = hist[key] as Long
            if (ts) {
                String dStr = new Date(ts).format("MMM-dd-yyyy", location.timeZone)
                String formatted = (dStr == today) ? "Today @ " + new Date(ts).format("h:mm a", location.timeZone) : new Date(ts).format("MMM-dd-yyyy h:mm a", location.timeZone)
                sb.append("  • ${label}: ${formatted}\n")
            } else {
                sb.append("  • ${label}: --\n")
            }
        }
    }
    return sb.toString()
}

void notifyEventsForStation(Map stationObj, Map lastData) {
    if (!pushoverEnabled) return
    String mac = stationObj.macAddress
    String cmac = cleanMac(mac)
    String stationName = stationObj.info?.name ?: mac
    Long nowMs = now()

    state.notifyHistory = state.notifyHistory ?: [:]
    state.notifyHistory[cmac] = state.notifyHistory[cmac] ?: [:]
    Map hist = state.notifyHistory[cmac]

    def tempCheck = lastData.tempf != null ? lastData.tempf : lastData.temp1f
    if (notifyAlertLowTemp != null && tempCheck != null && tempCheck.toFloat() <= notifyAlertLowTemp.toFloat()) {
        if (lastNotifyDT(hist.lowTempDT, "Low Temp (${stationName})")) {
            send_message("${DTHName()} - '${stationName}': LOW TEMP ALERT: ${tempCheck}${state.tempUnitsDisplay} <= ${notifyAlertLowTemp}${state.tempUnitsDisplay}")
            hist.lowTempDT = nowMs
        }
    }
    if (notifyAlertHighTemp != null && tempCheck != null && tempCheck.toFloat() >= notifyAlertHighTemp.toFloat()) {
        if (lastNotifyDT(hist.highTempDT, "High Temp (${stationName})")) {
            send_message("${DTHName()} - '${stationName}': HIGH TEMP ALERT: ${tempCheck}${state.tempUnitsDisplay} >= ${notifyAlertHighTemp}${state.tempUnitsDisplay}")
            hist.highTempDT = nowMs
        }
    }
    if (notifyRain && lastData.hourlyrainin != null && lastData.hourlyrainin.toFloat() > 0) {
        if (lastNotifyDT(hist.rainDT, "Rain (${stationName})")) {
            send_message("${DTHName()} - '${stationName}': RAIN DETECTED: ${lastData.hourlyrainin} ${state.measureUnitsDisplay}/hr")
            hist.rainDT = nowMs
        }
    }
}

Boolean lastNotifyDT(lastDT, String eventName) {
    if (!lastDT) return true
    BigDecimal hours = (((now() - (lastDT as Long)) / 1000.0) / 3600.0).round(1)
    Boolean rc = hours >= (state.notifyAlertFreq ?: 24).toInteger()
    logInfo "${eventName} last sent ${hours} hours ago -> ${rc ? 'OK to Notify' : 'TOO EARLY TO SEND'}"
    return rc
}

String unitsSet() {
    if ([tempUnits, windUnits, measureUnits, baroUnits, solarRadiationTileDisplayUnits].findAll { it != null }.join() == '') return "Tap to Select"
    return sprintf("%s, %s, %s, %s, %s", tempUnits, windUnits, measureUnits, baroUnits, solarRadiationTileDisplayUnits)
}

void send_message(String msgData) {
    if (pushoverEnabled && settings.pushoverDevices != null) {
        settings.pushoverDevices.each { it.deviceNotification(msgData) }
    }
}