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
*  Ambient Weather Station Child
*
*  Author: Kurt Sanders, SanderSoft™
*/

#include kurtsanders.AWSLibrary
@Field static String PARENT_DEVICE_NAME            = "Ambient Weather Station"
@Field static final String VERSION                 = "7.0.0"

String appModified()            { return "Nov-23-2026" }
String DTHName()                { return PARENT_DEVICE_NAME }
String DTHRemoteSensorName()    { return PARENT_DEVICE_NAME + " Remote Sensor" }
String DTHPMSensorName()        { return "Ambient Particulate Monitor" }
String DTHnamespace()           { return NAMESPACE }
String AppImg(imgName)          { return GITHUB_IMAGES_LINK + "${imgName}" }
Integer MaxNumRemoteSensors()   { return MAX_REMOTE_SENSORS }

definition(
    name              : "Ambient Weather Station Child",
    namespace         : NAMESPACE,
    author            : AUTHOR_NAME,
    description       : "Per-station child app for Ambient™ Weather Station (independent units, data keys, and alerts)",
    category          : "",
    parent            : "kurtsanders:Ambient Weather Station",
    iconUrl           : "",
    iconX2Url         : "",
    documentationLink : COMM_LINK,
    singleInstance    : false,
    pausable          : false
)

preferences {
    page(name: "mainPage")
    page(name: "unitsPage")
    page(name: "DataPage")
    page(name: "remoteSensorPage")
    page(name: "notifyPage")
}

String getManagedMac() {
    return weatherStationMac
}

Map getStationCatalog() {
    Map catalog = parent?.state?.stationCatalog
    if (!catalog) {
        parent?.fetchStationCatalogSync()
        catalog = parent?.state?.stationCatalog
    }
    return catalog ?: [:]
}

String getStationDNI(String mac)      { return "${app.id}:${cleanMac(mac)}:Station" }
String getRemoteSensorDNI(String mac, Integer idx) { return "${app.id}:${cleanMac(mac)}:Remote:${idx}" }
String getPMDNI(String mac)           { return "${app.id}:${cleanMac(mac)}:PM" }

def mainPage() {
    cleanAppLabel()
    if (tempUnits == null) {
        app.updateSetting("tempUnits", [type: "enum", value: "°F"])
        app.updateSetting("windUnits", [type: "enum", value: "mph"])
        app.updateSetting("measureUnits", [type: "enum", value: "in"])
        app.updateSetting("baroUnits", [type: "enum", value: "inHg"])
        app.updateSetting("solarRadiationTileDisplayUnits", [type: "enum", value: "W/m²"])
    }
    if (keepDataKeysAllOption == null) {
        app.updateSetting("keepDataKeysAllOption", [type: "bool", value: true])
    }

    Map catalog = getStationCatalog()
    Set<String> takenMacs = new HashSet<>()
    try {
        parent?.getSelectedMacs()?.each { if (it) takenMacs.add(it.toString()) }
        parent?.getChildApps()?.each { child ->
            if (child?.id != app.id) {
                String m = child.getManagedMac()
                if (m) takenMacs.add(m)
            }
        }
    } catch (ignored) {}

    Map stationOptions = [:]
    catalog.each { mac, info ->
        if (mac == weatherStationMac || !takenMacs.contains(mac)) {
            String loc = info.location ? " @ ${info.location}" : ""
            stationOptions[mac] = "${info.name}${loc} (${mac})"
        }
    }

    Map selectedInfo = weatherStationMac ? catalog[weatherStationMac] : null
    if (selectedInfo?.name) {
        state.weatherStationName = selectedInfo.name
    }
    Boolean needsInsideName = false
    if (weatherStationMac && selectedInfo?.keys?.contains('tempinf')) {
        String cmac = cleanMac(weatherStationMac)
        def existingConsole = getChildDevice(getRemoteSensorDNI(weatherStationMac, 0))
        if (!settings["sensor_${cmac}_0"] && !existingConsole) {
            needsInsideName = true
        }
    }
    Boolean canInstall = (weatherStationMac != null && !needsInsideName)

    dynamicPage(name: "mainPage", title: "Ambient Weather Station Child Configuration", install: canInstall, uninstall: true) {
        section(sectionHeader("Select Weather Station")) {
            if (stationOptions) {
                input(name: "weatherStationMac", type: "enum",
                      title: fmtTitle("Select an unassigned Ambient Weather Station for this Child App"),
                      options: stationOptions,
                      multiple: false,
                      required: true,
                      submitOnChange: true)
            } else {
                paragraph getFormat("text-red", "All weather stations on your account are already assigned to the Parent App or another Child App. Remove one from the Parent App (Option A) or another Child App first.")
            }
            if (selectedInfo) {
                paragraph image: AppImg("blue-ball-100.jpg"),
                    title: fmtTitle("${selectedInfo.name}"),
                    required: false,
                    "Location: ${selectedInfo.location ?: 'Not Provided'}" +
                    "\nMAC Address: ${weatherStationMac}" +
                    "\nRemote Sensors: ${selectedInfo.remoteCount ?: 0}" +
                    "\nParticulate Monitor: ${selectedInfo.pmCount ?: 0}"
            }
        }

        if (weatherStationMac) {
            section(sectionHeader("Station Preferences")) {
                input(name: "showBattery", type: "bool",
                      title: fmtTitle("Show battery level from sensor(s)?"),
                      defaultValue: true, required: true)
                href(name: "unitsLink",
                     title: fmtTitle("Select Weather Units of Measure"),
                     description: "<span style=\"color:green\">${unitsSet()}</span>",
                     page: "unitsPage")
                href(name: "dataLink",
                     title: fmtTitle("Select/DeSelect Weather Station Data Keys"),
                     description: keepDataKeysAllOption ? "<span style=\"color:green\">All Data Keys Selected</span>" : "<span style=\"color:blue\">${keepDataKeys?.size() ?: 0} Keys Selected</span>",
                     page: "DataPage")
                href(name: "notifyLink",
                     title: fmtTitle("Weather Alerts/Notification (Optional)"),
                     description: pushoverEnabled ? "<span style=\"color:green\">Alerts Activated</span>" : "<span style=\"color:blue\">Tap to Activate Alerts</span>",
                     page: "notifyPage")
                href(name: "sensorLink",
                     title: fmtTitle("Weather Sensor Names" + (needsInsideName ? " (Required)" : "")),
                     description: needsInsideName ? "<span style=\"color:red\">REQUIRED: Tap to name your inside console sensor (e.g. Kitchen, Office) before saving</span>" : "<span style=\"color:blue\">Tap to Name Console, Remote, and Particulate Sensors</span>",
                     page: "remoteSensorPage")
            }

            section(sectionHeader("Name this Child App")) {
                label(name: "name",
                      title: fmtTitle("Assign a name to this Child App"),
                      defaultValue: selectedInfo?.name ?: "Ambient Station Child",
                      required: false,
                      submitOnChange: true)
            }
        }

        section(sectionHeader("Logging Options")) {
            input name: "logLevel", type: "enum", title: fmtTitle("Logging Level"), submitOnChange: true,
                description: fmtDesc("Logs selected level and above"), defaultValue: "3", options: LOG_LEVELS
            input name: "logLevelTime", type: "enum", title: fmtTitle("Logging Level Time"), submitOnChange: true,
                description: fmtDesc("Time to enable Debug/Trace logging"), defaultValue: "10", options: LOG_TIMES
            input name: "SyncLogOptions", type: "button", title: "Sync Log Options in all Devices"
        }
    }
}

def unitsPage() {
    dynamicPage(name: "unitsPage", title: "Units of Measure for '${state.weatherStationName ?: weatherStationMac}'", uninstall: false, install: false) {
        section("Weather Station Unit of Measure Options") {
            input(name: "tempUnits", type: "enum", title: fmtTitle("Temperature Units"), options: ['°F':'Fahrenheit °F','°C':'Celsius °C'], defaultValue: "°F", required: true)
            input(name: "windUnits", type: "enum", title: fmtTitle("Wind Speed Units"), options: ['mph':'Miles per Hour','fps':'Feet per Second','mps':'Meter per Second','kph':'Kilometers per Hour','knotts':'Knotts'], defaultValue: "mph", required: true)
            input(name: "measureUnits", type: "enum", title: fmtTitle("Rainfall Units"), options: ['in':'Inches','mm':'Millimeters','cm':'Centimeters'], defaultValue: "in", required: true)
            input(name: "baroUnits", type: "enum", title: fmtTitle("Barometer Units"), options: ['inHg':'inHg','mmHg':'mmHg', 'hPa':'hPa'], defaultValue: "inHg", required: true)
            input(name: "solarRadiationTileDisplayUnits", type: "enum", title: fmtTitle("Solar Radiation Units"), options: ['W/m²':'Imperial Units (W/m²)','lux':'Metric Units (lux)', 'fc':'Foot Candles (fc)'], defaultValue: "W/m²", required: true)
            input(name: "solarRadiationDecimalFormat", type: "enum", title: fmtTitle("Solar Radiation Decimal Format"), options: [0, 1, 2], defaultValue: 0, required: true)
        }
    }
}

def DataPage() {
    Map info = weatherStationMac ? getStationCatalog()[weatherStationMac] : null
    List keys = (info?.keys ?: []).findAll { !DATE_KEYS_TO_KEEP.contains(it) }.sort()

    dynamicPage(name: "DataPage", title: getFormat("title", "Data Key Import Selector"), submitOnChange: true, uninstall: false, install: false) {
        section() {
            input(name: "keepDataKeysAllOption", type: "bool",
                  title: "<span style=\"color:red\">Select ALL Data Keys? Toggle OFF to ONLY import selected keys</span>",
                  required: true, submitOnChange: true, defaultValue: true)
        }
        if (!keepDataKeysAllOption) {
            section(getFormat("header-blue", "Partial Data Keys Selector")) {
                input(name: "keepDataKeys", type: "enum", multiple: true,
                      title: "<span style=\"color:red\">Select Data Keys to import for '${state.weatherStationName}'</span>",
                      options: keys, offerAll: false, submitOnChange: true, required: true)
            }
        }
    }
}

def remoteSensorPage() {
    Map info = weatherStationMac ? getStationCatalog()[weatherStationMac] : null
    List keys = info?.keys ?: []
    String cmac = cleanMac(weatherStationMac ?: "")

    dynamicPage(name: "remoteSensorPage", uninstall: false, install: false) {
        if (keys.contains('tempinf')) {
            def existingConsole = getChildDevice(getRemoteSensorDNI(weatherStationMac, 0))
            section(sectionHeader("Inside Console Sensor Name (Required)")) {
                input(name: "sensor_${cmac}_0", type: "text", title: fmtTitle("Enter a unique location name for this inside console (e.g., Kitchen, Office, Basement)"),
                      required: true, defaultValue: existingConsole?.label, submitOnChange: true)
            }
        }
        if ((info?.remoteCount ?: 0) > 0) {
            section(sectionHeader("Remote Sensors (${info.remoteCount})")) {
                for (int i = 1; i <= MaxNumRemoteSensors(); i++) {
                    def existingRemote = getChildDevice(getRemoteSensorDNI(weatherStationMac, i))
                    if (keys.contains("temp${i}f")) {
                        input(name: "sensor_${cmac}_${i}", type: "text", title: fmtTitle("Remote #${i} → Temp Sensor"),
                              defaultValue: existingRemote?.label ?: "Temp Sensor #${i}", required: true)
                    } else if (keys.contains("soiltemp${i}")) {
                        input(name: "sensor_${cmac}_${i}", type: "text", title: fmtTitle("Remote #${i} → Soil Temp Sensor"),
                              defaultValue: existingRemote?.label ?: "Soil Temp Sensor #${i}", required: true)
                    } else if (keys.contains("soilhum${i}")) {
                        input(name: "sensor_${cmac}_${i}", type: "text", title: fmtTitle("Remote #${i} → Soil Moisture Sensor"),
                              defaultValue: existingRemote?.label ?: "Soil Moisture Sensor #${i}", required: true)
                    }
                }
            }
        }
        if ((info?.pmCount ?: 0) > 0) {
            def existingPM = getChildDevice(getPMDNI(weatherStationMac))
            section(sectionHeader("Particulate Monitor Name")) {
                input(name: "pm_${cmac}", type: "text", title: fmtTitle("Particulate Monitor Name"),
                      defaultValue: existingPM?.label ?: "AWS Particle Monitor", required: true)
            }
        }
    }
}

def notifyPage() {
    Map info = weatherStationMac ? getStationCatalog()[weatherStationMac] : null
    List keys = info?.keys ?: []
    Boolean hasTemp = keys.any { it ==~ /^temp1?f$/ }
    Boolean hasRain = keys.contains('hourlyrainin')

    dynamicPage(name: "notifyPage", title: "Weather Alerts/Notifications", uninstall: false, install: false) {
        section(sectionHeader("Notification Device(s)")) {
            input("pushoverEnabled", "bool", title: "Use Notification Device(s) for Alert Notifications", required: false, submitOnChange: true)
            if (pushoverEnabled) {
                input(name: "pushoverDevices", type: "capability.notification", title: "", required: false, multiple: true,
                      description: "Select notification device(s)", submitOnChange: true)
            }
        }
        if (pushoverEnabled) {
            section(sectionHeader("Alert Options")) {
                input(name: "notifyAlertFreq", type: "enum", required: true,
                      title: "Restrict notifications per event type to once every NUMBER of hours",
                      options: [0, 1, 2, 4, 6, 12, 24], defaultValue: 24, submitOnChange: true)
                if (hasTemp) {
                    input(name: "notifyAlertLowTemp", type: "number", required: false, title: "Notify when temp <= value")
                    input(name: "notifyAlertHighTemp", type: "number", required: false, title: "Notify when temp >= value")
                }
                if (hasRain) {
                    input(name: "notifyRain", type: "bool", required: false, title: "Notify when RAIN is detected")
                }
            }
        }
    }
}

def appButtonHandler(String buttonName) {
    if (buttonName == "SyncLogOptions") {
        Integer level = settings.logLevel as Integer ?: 0
        Integer time = settings.logLevelTime as Integer ?: 0
        getAllChildDevices().each { it.syncLogLevelApp2Children(level, time) }
    }
}

void syncLogLevelFromParent(Integer level, Integer time) {
    app.updateSetting("logLevel", [value: "${level}", type: "enum"])
    app.updateSetting("logLevelTime", [value: "${time}", type: "enum"])
    checkLogLevel([level: level, time: time])
    getAllChildDevices().each { it.syncLogLevelApp2Children(level, time) }
}

def installed() {
    logInfo "Child installed()"
    initialize()
    parent?.refresh()
}

def updated() {
    logInfo "Child updated()"
    cleanAppLabel()
    initialize()
    parent?.refresh()
}

def initialize() {
    state.notifyAlertFreq     = notifyAlertFreq ?: 24
    state.tempUnitsDisplay    = tempUnits ?: "°F"
    state.windUnitsDisplay    = windUnits ?: "mph"
    state.measureUnitsDisplay = measureUnits ?: "in"
    state.baroUnitsDisplay    = baroUnits ?: "inHg"
    addStationChildDevices()
    checkLogLevel()
}

def refresh() {
    parent?.refresh()
}

def setPollingInterval(pollingInterval = null) {
    return parent?.setPollingInterval(pollingInterval)
}

void addStationChildDevices() {
    if (!weatherStationMac) return
    Map info = getStationCatalog()[weatherStationMac]
    List keys = info?.keys ?: []
    String stationName = info?.name ?: weatherStationMac
    String cmac = cleanMac(weatherStationMac)

    String stationDNI = getStationDNI(weatherStationMac)
    if (!getChildDevice(stationDNI)) {
        try {
            addChildDevice(DTHnamespace(), DTHName(), stationDNI, null, ["name": "${stationName}-Console", "label": "AWS-${stationName}", completedSetup: true])
        } catch (ex) {
            logErr "Failed to add '${DTHName()}': ${ex}"
        }
    }

    // Inside console sensor (index 0) + remote sensors (1..MaxNumRemoteSensors)
    for (int i = 0; i <= MaxNumRemoteSensors(); i++) {
        String defaultLabel = null
        if (i == 0) {
            if (keys.contains('tempinf')) defaultLabel = "${stationName} Inside"
        } else {
            if (keys.contains("temp${i}f")) defaultLabel = "Temp Sensor #${i}"
            else if (keys.contains("soiltemp${i}")) defaultLabel = "Soil Temp Sensor #${i}"
            else if (keys.contains("soilhum${i}")) defaultLabel = "Soil Moisture Sensor #${i}"
        }
        String labelVal = settings["sensor_${cmac}_${i}"] ?: defaultLabel
        if (!labelVal) continue

        String dni = getRemoteSensorDNI(weatherStationMac, i)
        def dev = getChildDevice(dni)
        if (!dev) {
            try {
                addChildDevice(DTHnamespace(), DTHRemoteSensorName(), dni, null, ["name": "${stationName}-${labelVal}", "label": "AWS-${labelVal}", completedSetup: true])
            } catch (ex) {
                logErr "Failed to add '${DTHRemoteSensorName()}': ${ex}"
            }
        } else if (settings["sensor_${cmac}_${i}"] && dev.label != labelVal && dev.label != "AWS-${labelVal}") {
            dev.label = labelVal
        }
    }

    String pmLabel = settings["pm_${cmac}"] ?: (((info?.pmCount ?: 0) > 0) ? "AWS Particle Monitor" : null)
    if (pmLabel) {
        String dni = getPMDNI(weatherStationMac)
        def dev = getChildDevice(dni)
        if (!dev) {
            try {
                addChildDevice(DTHnamespace(), DTHPMSensorName(), dni, null, ["name": "${stationName}-${pmLabel}", "label": "AWS-${pmLabel}", completedSetup: true])
            } catch (ex) {
                logErr "Failed to add '${DTHPMSensorName()}': ${ex}"
            }
        } else if (settings["pm_${cmac}"] && dev.label != pmLabel && dev.label != "AWS-${pmLabel}") {
            dev.label = pmLabel
        }
    }
}

void processStationData(Map stationObj, runID = "async") {
    if (!weatherStationMac || stationObj?.macAddress != weatherStationMac) return
    def d = getChildDevice(getStationDNI(weatherStationMac))
    if (!d) {
        addStationChildDevices()
        d = getChildDevice(getStationDNI(weatherStationMac))
        if (!d) return
    }

    Map rawLastData = (stationObj.lastData instanceof Map) ? stationObj.lastData : [:]
    if (!rawLastData) return

    Map unitSettings = [
        tempUnits   : state.tempUnitsDisplay ?: "°F",
        windUnits   : state.windUnitsDisplay ?: "mph",
        measureUnits: state.measureUnitsDisplay ?: "in",
        baroUnits   : state.baroUnitsDisplay ?: "inHg"
    ]
    Boolean keepAll = (keepDataKeysAllOption != false)
    Map filtered = filterStationLastData(rawLastData, keepAll, (keepDataKeys instanceof List) ? keepDataKeys : [])
    Map lastData = convertStationLastData(filtered, unitSettings)

    String serverDate = convertToCurrentTimeZone(rawLastData.date?.toString())
    String updatedStr = tileLastUpdated()

    d.sendEvent(name: "scheduleFreqMin", value: parent?.schedulerFreq ?: 15, descriptionText: "AWS Polling Interval")
    d.sendEvent(name: "lastSTupdate", value: updatedStr)
    d.sendEvent(name: "macAddress", value: weatherStationMac)

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
                d.sendEvent(name: "${k}_display", value: "${v} ${unitSettings.measureUnits}")
                break
            case ~/^barom.*/:
                d.sendEvent(name: "${k}_display", value: "${v} ${unitSettings.baroUnits}")
                break
            case ~/^tempi?n?f$|^dewPoint$|^feelsLikein$|^feelsLike$/:
                d.sendEvent(name: "${k}_display", value: "${v}${unitSettings.tempUnits}")
                break
            case ~/^wind.*|^maxdailygust$/:
                d.sendEvent(name: "${k}_display", value: "${v} ${unitSettings.windUnits}")
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
            case 'dateutc': okToSendEvent = false; break
            case 'date': v = serverDate; break
            case 'battin': k = 'battery'; d.sendEvent(name: k, value: v.toInteger() * 100, units: '%', displayed: false); break
            case 'battout': k = 'battery'; v = v.toInteger() * 100; break
            case ~/^batt[0-9].*/: okToSendEvent = false; break
            case 'lastRain': v = convertToCurrentTimeZone(v.toString()); break
            case 'lightning_time': v = new Date(v as Long).toString(); break
            case 'tempf': k = 'temperature'; break
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
            case 'yearlyrainin': k = 'totalrainin'; break
            case 'solarradiation':
                Float sr = v.toFloat()
                if (solarRadiationTileDisplayUnits == 'lux') sr = wm2lux(sr)
                else if (solarRadiationTileDisplayUnits == 'fc') sr = wm2fc(sr)
                String srUnits = solarRadiationTileDisplayUnits ?: 'W/m²'
                d.sendEvent(name: k, value: sr, units: srUnits)
                d.sendEvent(name: 'solarradiation_display', value: "${String.format('%,.0f', sr)} ${srUnits}", units: srUnits)
                v = sr
                k = 'illuminance'
                break
            case 'tempinf':
                remoteSensorDev = getChildDevice(getRemoteSensorDNI(weatherStationMac, 0))
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "temperature", value: v, units: unitSettings.tempUnits)
                    remoteSensorDev.sendEvent(name: "date", value: serverDate, displayed: false)
                    remoteSensorDev.sendEvent(name: "lastSTupdate", value: updatedStr, displayed: false)
                }
                break
            case 'humidityin':
                remoteSensorDev = getChildDevice(getRemoteSensorDNI(weatherStationMac, 0))
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "humidity", value: v, units: "%", displayed: false)
                    remoteSensorDev.sendEvent(name: "humidity_display", value: "${v}%")
                }
                break
            case ~/^temp[0-9][0-9]?f$|^soiltemp[0-9][0-9]?$/:
                Integer idx = k.findAll(/\d+/)[0].toInteger()
                remoteSensorDev = getChildDevice(getRemoteSensorDNI(weatherStationMac, idx))
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "temperature", value: v, units: unitSettings.tempUnits)
                    remoteSensorDev.sendEvent(name: "lastSTupdate", value: updatedStr, displayed: false)
                    remoteSensorDev.sendEvent(name: "date", value: serverDate, displayed: false)
                    String battKey = "batt${idx}"
                    if (lastData.containsKey(battKey)) {
                        remoteSensorDev.sendEvent(name: "battery", value: lastData[battKey].toInteger() * 100, displayed: false)
                    }
                }
                okToSendEvent = false
                break
            case ~/^humidity[0-9][0-9]?$|^soilhum[0-9][0-9]?$/:
                Integer idx = k.findAll(/\d+/)[0].toInteger()
                remoteSensorDev = getChildDevice(getRemoteSensorDNI(weatherStationMac, idx))
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: "humidity", value: v, units: "%", displayed: false)
                    remoteSensorDev.sendEvent(name: "humidity_display", value: "${v}%")
                }
                okToSendEvent = false
                break
            case ~/(^pm.*)|(^co2.*)|(^aqi.*)/:
                remoteSensorDev = getChildDevice(getPMDNI(weatherStationMac))
                if (remoteSensorDev) {
                    remoteSensorDev.sendEvent(name: k, value: v)
                    remoteSensorDev.sendEvent(name: "lastSTupdate", value: updatedStr, displayed: false)
                    remoteSensorDev.sendEvent(name: "date", value: serverDate, displayed: false)
                }
                okToSendEvent = false
                break
        }
        if (okToSendEvent) {
            d.sendEvent(name: k, value: v)
        }
    }

    notifyEvents(stationObj, lastData)
    updateMyLabel('updated')
}

void notifyEvents(Map stationObj, Map lastData) {
    if (!pushoverEnabled || !settings.pushoverDevices) return
    String stationName = stationObj.info?.name ?: weatherStationMac
    Long nowMs = now()
    def tempCheck = lastData.tempf != null ? lastData.tempf : lastData.temp1f

    if (notifyAlertLowTemp != null && tempCheck != null && tempCheck.toFloat() <= notifyAlertLowTemp.toFloat()) {
        if (lastNotifyDT(state.notifyAlertLowTempDT, "Low Temp")) {
            settings.pushoverDevices.each { it.deviceNotification("${DTHName()} - '${stationName}': LOW TEMP ALERT: ${tempCheck}${state.tempUnitsDisplay} <= ${notifyAlertLowTemp}${state.tempUnitsDisplay}") }
            state.notifyAlertLowTempDT = nowMs
        }
    }
    if (notifyAlertHighTemp != null && tempCheck != null && tempCheck.toFloat() >= notifyAlertHighTemp.toFloat()) {
        if (lastNotifyDT(state.notifyAlertHighTempDT, "High Temp")) {
            settings.pushoverDevices.each { it.deviceNotification("${DTHName()} - '${stationName}': HIGH TEMP ALERT: ${tempCheck}${state.tempUnitsDisplay} >= ${notifyAlertHighTemp}${state.tempUnitsDisplay}") }
            state.notifyAlertHighTempDT = nowMs
        }
    }
    if (notifyRain && lastData.hourlyrainin != null && lastData.hourlyrainin.toFloat() > 0) {
        if (lastNotifyDT(state.notifyRainDT, "Rain")) {
            settings.pushoverDevices.each { it.deviceNotification("${DTHName()} - '${stationName}': RAIN DETECTED: ${lastData.hourlyrainin} ${state.measureUnitsDisplay}/hr") }
            state.notifyRainDT = nowMs
        }
    }
}

Boolean lastNotifyDT(lastDT, String eventName) {
    if (!lastDT) return true
    BigDecimal hours = (((now() - (lastDT as Long)) / 1000.0) / 3600.0).round(1)
    return hours >= (state.notifyAlertFreq ?: 24).toInteger()
}

String unitsSet() {
    if ([tempUnits, windUnits, measureUnits, baroUnits, solarRadiationTileDisplayUnits].findAll { it != null }.join() == '') return "Tap to Select"
    return sprintf("%s, %s, %s, %s, %s", tempUnits, windUnits, measureUnits, baroUnits, solarRadiationTileDisplayUnits)
}
