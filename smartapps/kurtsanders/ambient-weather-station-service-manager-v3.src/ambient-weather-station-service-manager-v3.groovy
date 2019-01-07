/**
*  Copyright 2019 SanderSoft
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
*  Ambient Weather Station V3.02
*
*  Author: Kurt Sanders
*
*  Date: 2019-01-07
*/
import groovy.time.*
import java.text.DecimalFormat

String DTHName() 				{ return (noColorTiles)?"Ambient Weather Station V3 No Color Tiles":"Ambient Weather Station V3" }
String DTHRemoteSensorName() 	{ return "Ambient Weather Station Remote Sensor V3"}
String DTHDNI() 				{ return "MyAmbientWeatherStationV3" }
String DTHDNIRemoteSensorName() { return "remoteTempfHumiditySensorName"}
String appName() 				{ return "Ambient Weather Station Service Manager V3" }
String appAuthor()	 			{ return "SanderSoft" }
String getAppImg(imgName) 		{ return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/$imgName" }
String wikiURL(pageName)		{ return "https://github.com/KurtSanders/STAmbientWeather/wiki/$pageName"}
// ========================================================================================
// This APP key is ONLY for this application - Do not copy or use elsewhere
String appKey() {return "33054086b3d745779f5ac35e147baa76f13e75d44ea245388ba598911905fb50"}
// ========================================================================================

definition(
    name: 			"Ambient Weather Station Service Manager V3",
    namespace: 		"kurtsanders",
    author: 		"kurt@kurtsanders.com",
    description: 	"Ambient Personal Weather Station Service Manager V3",
    category: 		"My Apps",
    iconUrl:   		getAppImg("blue-ball.jpg"),
    iconX2Url: 		getAppImg("blue-ball.jpg"),
    iconX3Url: 		getAppImg("blue-ball.jpg"),
    singleInstance: true
)
{
// The following API Key is to be entered in this SmartApp's settings in the SmartThings IDE App Setting.
    appSetting 		"apiKey"
}
preferences {
    page(name: "mainPage")
    page(name: "optionsPage")
    page(name: "remoteSensorPage")
}

def mainPage() {
    def apiappSetupCompleteBool = !( (appSettings.apiKey==null) && (appSettings.apiKey=="") )
    def setupMessage = ""
    def setupTitle = "${appName()} API Check"
    def nextPageName = "optionsPage"
    def getAmbientStationDataRC = getAmbientStationData()
    if ( (apiappSetupCompleteBool) && (getAmbientStationDataRC) ) {
        setupMessage = "SUCCESS! You have completed entering a valid Ambient API Key for a ${appName()}"
        setupTitle = "Please confirm the Ambient Weather Station Information below and if correct, Tap 'NEXT' to continue to the 'Settings' page'"
    } else {
        setupMessage = "Setup Incomplete: Please check and/or complete the REQUIRED API key setup in the SmartThings IDE (App Settings Section) for ${appName()}"
        nextPageName = null
    }
    dynamicPage(name: "mainPage", title: setupTitle, nextPage: nextPageName, uninstall:true, install:false) {
        section(hideable: apiappSetupCompleteBool, hidden: apiappSetupCompleteBool, setupMessage ) {
            paragraph "The API string key is used to securely connect your weather station to ${appName()}."
            paragraph image: getAppImg("blue-ball.jpg"),
                title: "Required API Key",
                required: false,
                informationList("apiHelp")
            href(name: "hrefReadme",
                 title: "${appName()} Setup/Read Me Page",
                 required: false,
                 style: "external",
                 url: "https://github.com/KurtSanders/STAmbientWeather",
                 description: "tap to view the Setup/Read Me page")
            href(name: "hrefAmbient",
                 title: "Ambient Weather Dashboard Account Page for API Key",
                 required: false,
                 style: "external",
                 url: "https://dashboard.ambientweather.net/account",
                 description: "tap to login and view your Ambient Weather's dashboard")
            href(name: "hrefUSA",
                 title: "SmartThings IDE USA",
                 required: false,
                 style: "external",
                 url: "https://graph.api.smartthings.com/",
                 description: "tap to view the US SmartThings IDE website in mobile browser")
            href(name: "hrefEurope",
                 title: "SmartThings IDE Europe",
                 required: false,
                 style: "external",
                 url: "https://graph-eu01-euwest1.api.smartthings.com/",
                 description: "tap to view the Europe SmartThings IDE website in mobile browser")
        }
        if (apiappSetupCompleteBool && getAmbientStationDataRC) {
            section ("Ambient Weather Station Information") {
                paragraph image: getAppImg("blue-ball.jpg"),
                    title: "${state?.ambientMap?.info.name[0]}",
                    required: false,
                    "Location: ${state?.ambientMap.info.location[0]}" +
                    "\nMac Address: ${state?.ambientMap[0].macAddress}" +
                    "\nRemote Temp/Hydro Sensors: ${state?.countRemoteTempHumiditySensors}"
            }
        }
        section ("STAmbientWeather™ - ${appAuthor()}") {
            href(name: "hrefVersions",
                 image: getAppImg("wi-direction-right.png"),
                 title: "${informationList('version')} : ${informationList('date')}",
                 required: false,
                 style:"embedded",
                 url: wikiURL("Features-by-Version")
                )
        }
    }
}

def optionsPage () {
    def remoteSensorsExist = (state.countRemoteTempHumiditySensors>0)
    def lastPageName = remoteSensorsExist?"remoteSensorPage":""
    dynamicPage(name: "optionsPage", title: "Ambient Tile Settings", nextPage: lastPageName, uninstall:false, install : !remoteSensorsExist ) {
        section("Weather Station Options") {
            input "zipCode", type: "number",
                title: "Enter ZipCode for local Weather API Forecast/Moon (Required)",
                required: true
            input name: "schedulerFreq", type: "enum",
                title: "Run Weather Station Refresh Every (mins)?",
                options: ['0','1','2','3','4','5','10','15','30','60','180'],
                required: true
            input "${DTHDNIRemoteSensorName()}0", type: "text",
                title: "Weather Station Console Room Location Short Name",
                required: true
            paragraph "Background Color Option for Displaying Values"
            paragraph image: getAppImg("No-Color-Option.jpg"),
                title: "Initial ANDROID Install Setup Choice",
                required: false,
                "This NO COLOR option is highly recommended to set ON for Android devices that cannot render color backgrounds on value tiles. If you don't set it now you can do so within the IDE and switch the device to 'Ambient Weather Station V3 No Color Tiles'"
            input name: "noColorTiles", type: "bool",
                title: "Remove Color Backgrounds in Tiles (Recommended ON for Android Users)",
                required: false
            paragraph "Select Solar Radiation Units of Measure"
            paragraph image: getAppImg("blue-ball.jpg"),
                title: "'W/m²' Metric, 'lux' Imperial, 'fc' Footcandles",
                required: false,
                "The Ambient weather station measures Solar Radiation (Light) as watt/square meter (W/m²)'.  W/m² can be converted to illuminance (1 W/m² = 683 lux) or to Footcandles (1 Lux = 0.0929 Footcandles"
            input name: "solarRadiationTileDisplayUnits", type: "enum",
                title: "Select Solar Radiation ('Light' Tile) Units of Measure",
                options: ['W/m²','lux', 'fc'],
                required: true
        }
        section(hideable: true, hidden: true, "Optional: SmartThings IDE Live Logging Levels") {
            input name: "debugVerbose", type: "bool",
                title: "Show Debug Messages in Live Logging IDE",
                required: false
            input name: "infoVerbose", type: "bool",
                title: "Show Info Messages in Live Logging IDE",
                required: false
            input name: "WUVerbose", type: "bool",
                title: "Show Local Weather Info Messages in Live Logging IDE",
                required: false
        }
    }
}

def remoteSensorPage() {
    dynamicPage(name: "remoteSensorPage", title: "Ambient Tile Settings", uninstall:false, install : true ) {
        def i = 1
        section("Provide Location names for your ${state?.countRemoteTempHumiditySensors} remote temperature/hydro sensors") {
            paragraph "Information Regarding Ambient Remote Sensors"
            paragraph image: getAppImg("blue-ball.jpg"),
                title: "Information, Issues and Instructions",
                required: false,
                "You MUST create short descriptive names for each remote sensor. Do not use special characters in the names.\n\n" +
                "If you wish to change the short name of the remote sensor, DO NOT change them in the IDE 'My Devices' editor, as this app will rename them automatically when you SAVE the page.\n\n" +
                "Please note that remote sensors are numbered based in the bit switch on the sensor (1-8) and reported on Ambient Network API as 'tempNf' where N is an integer 1-8.  " +
                "If a remote sensor is deleted from your network or non responsive from your group of Ambient remote sensors, you may have to re-verify and/or rename the remainder of the remote sensors in this app and manually delete that sensor from the IDE 'My Devices' editor."
            for (i; i <= state?.countRemoteTempHumiditySensors; i++) {
                input "${DTHDNIRemoteSensorName()}${i}", type: "text",
                    title: "Ambient Remote Sensor #${i}",
                    required: true
            }
        }
    }
}

def initialize() {
    log.info "initialize Section: Start"
    if(infoVerbose){
        log.info "Ambient API string-> ${appSettings?.apiKey}"
        log.info "The location zip code for your Hub Location is '${location.zipCode}' and your preference zipCode value is '${zipCode}'"
    }
    if(debugVerbose){
        try {
            log.debug "ambientMap.macAddress -> ${state.ambientMap[0].macAddress}"
        } catch(NullPointerException e) {
            // do something other
        }
    }
    // Check for all devices needed to run this app
    addAmbientChildDevice()
	// Set user defined refresh rate
    if(state.schedulerFreq!=schedulerFreq) {
        state.schedulerFreq = schedulerFreq
        if(debugVerbose){log.debug "state.schedulerFreq->${state.schedulerFreq}"}
        setScheduler(state.schedulerFreq)
        def d = getChildDevice(state.deviceId)
        d.sendEvent(name: "scheduleFreqMin", value: state.schedulerFreq)
    }
    log.info "initialize Section: End"
}

def installed() {
    log.info "Installed Section: Start"
    state.deviceId = DTHDNI()
    initialize()
    runIn(10, main)
    log.info "Installed Section: End"
}

def updated() {
    log.info "Updated Section: Start"
	initialize()
    log.info "Updated Section: End"
}

def uninstalled() {
    log.info "Section Started: Uninstalled"
    unschedule()
    // Remove all devices
    getAllChildDevices().each {
        log.debug "Deleting AmbientWS device: ${it.label}"
        deleteChildDevice(it.deviceNetworkId)
    }
    log.info "Section Ended: Uninstalled"
}

def addAmbientChildDevice() {
    // add Ambient Weather Reporter Station device
    def AWSDNI = getChildDevice(state.deviceId)
    if (!AWSDNI) {
        def AWSName = state.ambientMap.info.name[0]?:DTHName()
        log.info "Adding AmbientWS Device: ${AWSName} with DNI: ${state.deviceId}"
        try {
            addChildDevice("kurtsanders", DTHName(), DTHDNI(), null, ["name": AWSName, "label": AWSName, completedSetup: true])
        } catch(physicalgraph.app.exception.UnknownDeviceTypeException ex) {
            log.error "The Ambient Weather Device Handler '${DTHName()}' was not found in your 'My Device Handlers', Error-> '${ex}'.  Please install this in the IDE's 'My Device Handlers'"
            return false
        }
        log.info "Added ${AWSName} with DNI: ${DTHDNI()}"
    } else {
        log.info "Verified Weather Station '${getChildDevice(state.deviceId)}' = DNI: '${DTHDNI()}'"
    }
    // add Ambient Weather Remote Sensor Device(s)
    def remoteSensorNamePref
    def remoteSensorNameDNI
    def remoteSensorNumber
    settings.each { key, value ->
        if ( key.startsWith(DTHDNIRemoteSensorName()) ) {
            remoteSensorNamePref = "Ambient - ${value}"
            remoteSensorNameDNI = getChildDevice(key)
            remoteSensorNumber = key.reverse()[0..0]
            if (remoteSensorNumber.toInteger() <= state.countRemoteTempHumiditySensors.toInteger()) {
                if (!remoteSensorNameDNI) {
                    log.info "Adding AmbientWS Remote Sensor Device: ${value}"
                    try {
                        addChildDevice("kurtsanders", DTHRemoteSensorName(), "${key}", null, ["name": remoteSensorNamePref, "label": remoteSensorNamePref, completedSetup: true])
                    } catch(physicalgraph.app.exception.UnknownDeviceTypeException ex) {
                        log.error "The Ambient Weather Device Handler '${DTHRemoteSensorName()}' was not found in your 'My Device Handlers', Error-> '${ex}'.  Please install this in the IDE's 'My Device Handlers'"
                        return false
                    }
                    log.info "Added Ambient Remote Sensor: ${remoteSensorNamePref} with DNI: ${key}"
                } else {
                    log.info "Verified Remote Sensor #${remoteSensorNumber} of ${state.countRemoteTempHumiditySensors} Exists: ${remoteSensorNamePref} = DNI: ${key}"
                    if ( (remoteSensorNameDNI.label == remoteSensorNamePref) && (remoteSensorNameDNI.name == remoteSensorNamePref) ) {
                        log.info "Device Label/Name Pref Match: Name: ${remoteSensorNameDNI.name} = Label: ${remoteSensorNameDNI.label} == Pref Label: ${remoteSensorNamePref} -> NO CHANGE"
                    } else {
                        log.error "Device Label/Name Pref Mis-Match: Name: ${remoteSensorNameDNI.name} <> Label: ${remoteSensorNameDNI.label} <> Pref Label: ${remoteSensorNamePref} -> RENAMING"
                        remoteSensorNameDNI.label = remoteSensorNamePref
                        remoteSensorNameDNI.name  = remoteSensorNamePref
                        log.info "Successfully Renamed Device Label and Names for: ${remoteSensorNameDNI}"
                    }
                }
            } else {
                log.warn "Device ${remoteSensorNumber} DNI: ${key} '${remoteSensorNameDNI.name}' exceeds # of remote sensors (${state.countRemoteTempHumiditySensors}) reporting from Ambient -> ACTION REQUIRED"
                log.warn "Please verify that all Ambient Remote Sensors are online and reporting to Ambient Network.  If so, please manual delete the device in the SmartThings 'My Devices' view"
            }
        }
    }
}

def refresh() {
    main()
}

def main() {
    log.info "SmartApp Section: Refresh"
    def d = getChildDevice(state.deviceId)
    def remoteSensorDNI = ""
    def now = new Date().format('EEE MMM d, h:mm:ss a',location.timeZone)
    def nowTime = new Date().format('h:mm a',location.timeZone).toLowerCase()
    def currentDT = new Date()

    // Weather Underground Station Forecast
    log.info "Executing API Weather Forecast, Sunrise, Sunset Info for zipCode: ${zipCode}"
    // Current conditions
    try {
        def obs = get("conditions")?.current_observation
        if(WUVerbose){log.info "obs --> ${obs}"}
        if (obs) {
            def weatherIcon = obs.icon_url.split("/")[-1].split("\\.")[0]
            d.sendEvent(name: "weatherIcon", value: weatherIcon, displayed: false)
        } else {
            log.error "Severre error retrieving current Weather API"
        }
    } catch (e) {
        log.error "Error '${e}' retrieving current weather conditions"
    }
    // Get Age of Lunar Moon, Sunrise, Sunset info from Weather Underground
    // Get Sunset, Sunrise from Weather Underground
    def ltf = new java.text.SimpleDateFormat("HH:mm")
    def tf = new java.text.SimpleDateFormat("h:mm a")
    def a
    try {
        a = get("astronomy")?.moon_phase
        if(WUVerbose){
            log.info "get('astronomy')?.moon_phase --> ${a}"
            log.info "ageOfMoon -> ${a.ageOfMoon}"
        }
        if (a) {
            d.sendEvent(name: "moonAge", value: "${a.ageOfMoon}", displayed: false)
            def sunriseTime = ltf.parse("${a.sunrise.hour}:${a.sunrise.minute}")
            def sunsetTime  = ltf.parse("${a.sunset.hour}:${a.sunset.minute}")
            def localSunrise   = "${tf.format(sunriseTime)}"
            def localSunset    = "${tf.format(sunsetTime)}"
            if(WUVerbose){log.info "localSunrise->${localSunrise}, localSunset-> ${localSunset}"}
            d.sendEvent(name: "localSunrise", value: localSunrise , descriptionText: "Sunrise today is at ${localSunrise}", displayed: false)
            d.sendEvent(name: "localSunset" , value: localSunset  , descriptionText: "Sunset today is at ${localSunset}", displayed: false)
        }
    } catch (e) {
        log.error "Error '${e}' retrieving current age of the Astronomy Info Underground API: get('astronomy')?.moon_phase --> ${a}"
    }

    try {
        // Forecast
        def f = get("forecast")
        if (f) {
            if(WUVerbose){log.info "Forecast-> ${f}"}
        } else {
            log.error "Severre error getting WU forecast: ${f}"
        }
        def f1= f?.forecast?.simpleforecast?.forecastday
        //    def f2= f?.forecast?.txt_forecast?.forecastday[0].fcttext
        def f2= sprintf(
            "Forecast for : %s\n%s %s, %s",
            zipCode,
            f?.forecast?.txt_forecast?.forecastday[0].fcttext,
            f?.forecast?.txt_forecast?.forecastday[1].title.toLowerCase().capitalize(),
            f?.forecast?.txt_forecast?.forecastday[1].fcttext
        )
        d.sendEvent(name: "weather", value: f2, descriptionText: "")
        if (f1) {
            if(WUVerbose){log.info "Forecastday-> ${f1}"}
            def icon = f1[0].icon_url.split("/")[-1].split("\\.")[0]
            def value = f1[0].pop as String // as String because of bug in determining state change of 0 numbers
            d.sendEvent(name: "forecastIcon", value: icon, displayed: false)
        }
        else {
            if(debugVerbose){log.warn "Error WU forecastday Forecast not found"}
        }
    } catch(e) {
        log.error "Error '${e}' retrieving current forecast"
    }

    // Weather Alerts
    checkForSevereWeather()

    // Ambient Weather Station
    log.info "Ambient Weather Station Reporter: Executing 'Refresh Routine' every: ${schedulerFreq} min(s)"
    if (getAmbientStationData()) {
    if(debugVerbose){log.debug "httpget resp status = ${state.respStatus}"}
        if(infoVerbose){log.info "Processing Ambient Weather data returned from getAmbientStationData())"}
        if(debugVerbose || infoVerbose) {
            state.ambientMap[0].each{ k, v ->
                log.info "${k} = ${v}"
                if (k instanceof Map) {
                    k.each { x, y ->
                        log.info "${x} = ${y}"
                    }
                }
                if (v instanceof Map) {
                    v.each { x, y ->
                        log.info "${x} = ${y}"
                    }
                }
            }
        }
        if(debugVerbose){log.debug "Checking Weather Station data array for 'Last Rain Date' information..."}
        if (state.ambientMap[0].lastData.containsKey('lastRain')) {
            if(debugVerbose){log.debug "Weather Station has 'Last Rain Date' information...Processing"}
            def dateRain = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", state.ambientMap.lastData.lastRain[0])
            use (groovy.time.TimeCategory) {
                if(debugVerbose){log.debug ("lastRainDuration -> ${currentDT - dateRain}")}
                d.sendEvent(name:"lastRainDuration", value: currentDT - dateRain)
            }
        } else {
            if(debugVerbose){log.debug "Weather Station does NOT provide 'Last Rain Date' information...Skipping"}
            d.sendEvent(name:"lastRainDuration", value: "N/A", displayed: false)
        }
        if ((state.ambientMap[0].lastData.containsKey('totalrainin')==false) || (state.ambientMap[0].lastData.containsKey('yearlyrainin')==false)) {
            d.sendEvent(name:"totalrainin", value: "N/A", displayed: false)
        }
        d.sendEvent(name:"lastSTupdate", value: sprintf("%s Tile Updated at:\n%s","${informationList('version')}", now), displayed: false)
        d.sendEvent(name:"macAddress", value: state.ambientMap.macAddress, displayed: false)

        def waterState = state.ambientMap.lastData.hourlyrainin[0]?.toFloat()>0?'wet':'dry'
        if(debugVerbose){log.debug "water -> ${waterState}"}
        d.sendEvent(name:'water', value: waterState)

        def motionState = state.ambientMap.lastData.windspeedmph[0]?.toFloat()>0?'active':'inactive'
        if(debugVerbose){log.debug "Wind motion -> ${motionState}"}
        d.sendEvent(name:'motion', value: motionState)

        def winddirectionState = degToCompass(state.ambientMap.lastData?.winddir[0])
        if(debugVerbose){log.debug "Wind Direction -> ${winddirectionState}"}
        d.sendEvent(name:'winddirection', value: winddirectionState)
        d.sendEvent(name:'winddir2', value: winddirectionState + " (" + state.ambientMap.lastData.winddir[0] + "º)")

        state.ambientMap.info[0].each{ k, v ->
            if(debugVerbose){log.debug "sendEvent(name: ${k}, value: ${v})"}
            d.sendEvent(name: k, value: v)
        }
        state.ambientMap.lastData[0].each{ k, v ->
            if(k=='dateutc' || k=='date'){return}
            if(k=='lastRain'){v=Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", v).format('EEE MMM d, h:mm a',location.timeZone)}
            if(k=='tempf'){k='temperature'}
            if(k=='feelsLike') {
                switch(true) {
                    case {waterState=='wet'} :
                    if(debugVerbose){log.debug "secondaryControl Wet"}
                    DecimalFormat df = new DecimalFormat("0.00")
                    def numberForRainLevel = df.format(state.ambientMap.lastData.hourlyrainin[0])
                    d.sendEvent(name: 'secondaryControl', value: sprintf("Raining at %s in/hr at %s", numberForRainLevel, nowTime) )
                    break
                    case { (v > state.ambientMap.lastData.tempf[0]) } :
                    if(debugVerbose){log.debug "secondaryControl FeelsLike"}
                    d.sendEvent(name: 'secondaryControl', value: sprintf("Feels like %sº at %s", v, nowTime) )
                    break
                    case { (motionState == 'active') } :
                    if(debugVerbose){log.debug "secondaryControl Wind"}
                    d.sendEvent(name: 'secondaryControl', value: sprintf("Wind is %s mph %s at %s", state.ambientMap.lastData.windspeedmph[0], winddirectionState, nowTime) )
                    break
                    default :
                    if(debugVerbose){log.debug "secondaryControl Default Humidity"}
                    d.sendEvent(name: 'secondaryControl', value: sprintf("Humidity is %s%% at %s", state.ambientMap.lastData.humidity[0], nowTime) )
                    break
                }
            }
            try {
                if( (v.isNumber()) && (v>0) && (v<0.1) ) {
                    if(debugVerbose){log.debug "${k}: Converting number '${v}'<0.1 -> 0.1}"}
                    v = 0.1
                }
            }
            catch (e) {
                log.error("caught exception assigning ${k} : ${v} to a value of 0.1", e)
            }
            if(k=='uv') {
                k='ultravioletIndex'
            }
            if(k=='yearlyrainin') {
                k='totalrainin'
            }
            if(k=='solarradiation') {
                k='illuminance'
                v = v.toInteger()
                if(v > 0) {
                    switch(solarRadiationTileDisplayUnits) {
                        case ('lux'):
                        v = (v * 683)
                        break
                        case ('fc'):
                        v = (v * 683 * 0.0929).toInteger()
                        break
                        default:
                            break
                    }
                    d.sendEvent(name: k, value: v, units: (solarRadiationTileDisplayUnits==null)?'W/m²':solarRadiationTileDisplayUnits)
                }
            }
            if (k=='windspeedmph') {
                d.sendEvent(name: "power", value: v, "unit" : "mph")
                d.sendEvent(name: "energy", value: v, , "unit" : "mph")
            }
            // Weather Console Sensors
            if (k=='tempinf') {
                remoteSensorDNI = getChildDevice("${DTHDNIRemoteSensorName()}0")
                if (remoteSensorDNI !=null) {
                    remoteSensorDNI.sendEvent(name: "temperature", value: v)
                    remoteSensorDNI.sendEvent(name:"lastSTupdate", value: tileLastUpdated(), displayed: false)
                } else {
                    log.error "Missing ${DTHDNIRemoteSensorName()}0"
                }
            }
            if (k=='humidityin') {
                remoteSensorDNI = getChildDevice("${DTHDNIRemoteSensorName()}0")
                if (remoteSensorDNI !=null) {
                    remoteSensorDNI.sendEvent(name: "humidity", value: v)
                } else {
                    log.error "Missing ${DTHDNIRemoteSensorName()}0"
                }
            }
            // Remote Temperature & Humidity Sensors
            if (k.matches('temp[0-9]f')) {
                remoteSensorDNI = getChildDevice("${DTHDNIRemoteSensorName()}${k[4..4]}")
                if (remoteSensorDNI !=null) {
                    remoteSensorDNI.sendEvent(name: "temperature", value: v)
                    remoteSensorDNI.sendEvent(name:"lastSTupdate", value: tileLastUpdated(), displayed: false)
                } else {
                    log.error "Missing ${${DTHDNIRemoteSensorName()}${k[4..4]}}"
                }
                return
            }
            if (k.matches('humidity[0-9]')) {
                remoteSensorDNI = getChildDevice("${DTHDNIRemoteSensorName()}${k[8..8]}")
                if (remoteSensorDNI !=null) {
                    remoteSensorDNI.sendEvent(name: "humidity", value: v)
                } else {
                    log.error "Missing ${${DTHDNIRemoteSensorName()}${k[8..8]}}"
                }
                return
            }
            if(debugVerbose){log.debug "sendEvent(name: ${k}, value: ${v})"}
            d.sendEvent(name: k, value: v)
        }
    } else {
        if(debugVerbose){log.debug "getAmbientStationData() did not return any weather data"}
    }
}

def logdata(name) {
    log.debug "${name}"
    return
}

def get(feature) {
    if(debugVerbose){log.debug "get feature->${feature}, zipCode->${zipCode}"}
    getWeatherFeature(feature, "${zipCode}")
}

def getAmbientStationData() {
	if(infoVerbose){log.info "Start: getAmbientStationData()"}
    if( (appSettings.apiKey==null) || (appSettings.apiKey=="") ) {
        log.error("Severre Error: The API key is undefined in this SmartApp's IDE properties settings, exiting")
        return false
    }

    def params = [
        uri			: "http://api.ambientweather.net/v1/devices?applicationKey=${appKey()}&apiKey=${appSettings.apiKey}"
    ]
    try {
        httpGet(params) { resp ->
            // get the data from the response body
            state.ambientMap = resp.data
            state.respStatus = resp.status
            if (resp.status != 200) {
                log.error "AmbientWeather.Net: response status code: ${resp.status}"
                log.error "AmbientWeather.Net: response: ${resp.data}"
                return false
            }
            state.countRemoteTempHumiditySensors =  state.ambientMap.lastData[0].keySet().count { it.matches('temp[0-9]f') }
        }
    } catch (e) {
        log.warn("getAmbientStationData() Try/Catch Error: Unable to get the Ambient Station Data, Error: $e")
        return false
    }
    if(infoVerbose){log.info "End: getAmbientStationData()"}
    return true
}

def degToCompass(num) {
    def val = Math.floor((num.toFloat() / 22.5) + 0.5).toInteger()
    def arr = ["N", "North NE", "NE", "East NE", "E", "East SE", "SE", "South SE", "S", "South SW", "SW", "West SW", "W", "West NW", "NW", "North NW"]
    return arr[(val % 16)]
}

def setScheduler(schedulerFreq) {
    if(infoVerbose){log.info "Section: setScheduler(${schedulerFreq})"}
    unschedule()
    if(debugVerbose){log.debug "Refresh Rate is now -> ${schedulerFreq}"}
    switch(schedulerFreq) {
        case '0':
        unschedule()
        break
        case '1':
        runEvery1Minute(refresh)
        break
        case '2':
        schedule("20 0/2 * * * ?",refresh)
        break
        case '3':
        schedule("20 0/3 * * * ?",refresh)
        break
        case '4':
        schedule("20 0/4 * * * ?",refresh)
        break
        case '5':
        runEvery5Minutes(refresh)
        break
        case '10':
        runEvery10Minutes(refresh)
        break
        case '15':
        runEvery15Minutes(refresh)
        break
        case '30':
        runEvery30Minutes(refresh)
        break
        case '60':
        runEvery1Hour(refresh)
        break
        case '180':
        runEvery3Hours(refresh)
        break
        default :
        if(debugVerbose){log.error "Unknown Schedule Frequency Value ${schedulerFreq}"}
        unschedule()
    }
}

def checkForSevereWeather() {
    if(infoVerbose){log.info "Section: checkForSevereWeather()"}
    def alerts = get("alerts")?.alerts
    def alertCountMsg = "${alerts?.size()} current weather alert(s) for ${zipCode}"
    if(debugVerbose){log.debug "WUSTATION:  ${alertCountMsg}"}
    def d = getChildDevice(state.deviceId)
    if(WUVerbose){log.info "WUSTATION: alerts = ${alerts}"}

    if (alerts==[]){
        d.sendEvent(name: "alertMessage", value: "", displayed: false)
        d.sendEvent(name: "alertDescription", value: "${alertCountMsg}", displayed: false)
    } else {
        def alertKeys = alerts?.collect{it.type + it.date_epoch} ?: []
        def alertMsg = ""
        def alertDesc = []
        def alertNL = ""
        alerts.each {alert ->
            alertDesc << "${alert.description}"
            alertMsg += "${alert.message.replaceAll("[\n\r]", "")}${alertNL}"
            alertNL=",\n"
        }
        if(debugVerbose){
            log.debug "WUSTATION: alertMsg = ${alertMsg}"
            log.debug "WUSTATION: alertDesc = ${alertDesc}"
        }
        d.sendEvent(name: "alertDescription", value: alertDesc, descriptionText: "")
        d.sendEvent(name: "alertMessage", value: alertMsg, descriptionText: "")
    }
}

def tileLastUpdated() {
    def now = new Date().format('EEE MMM d, h:mm:ss a',location.timeZone)
    return sprintf("%s Tile Last Updated at:\n%s","${informationList('version')}", now)
}
def informationList(listName) {
    def textList = []
    switch(listName) {
        case ("version"):
        return "V3.0.2"
        case ('date'):
        return  "Jan-07-2019"
        break
        case ("apiHelp") :
        // Help Text for API Key
        textList =  [
            "You MUST enter your Ambient Weather API key in the ${appName()} SmartApp Settings section.",
            "Visit your Ambient Weather Dashboards's Account page.",
            "Create/Copy your API key from the bottom of the page",
            "Return to your SmartThings IDE 'My SmartApps' browser page.",
            "EDIT the ${appName()} SmartApp.",
            "Press the App Settings button at the top right of the page.",
            "Scroll down the page, expand the 'Settings' section.",
            "Enter or paste your Ambient API key in the API value input box.",
            "Press Update on bottom of page to save.",
            "Exit the SmartApp and Start ${appName()} Setup again on your mobile phone."
        ]
        break
        default:
            return
        break
    }
    def numberedText = ""
    textList.eachWithIndex { item, index ->
        numberedText += "${index+1}. ${item}"
        numberedText += (index<textList.size()-1)?"\n":''
    }
    return numberedText
}
