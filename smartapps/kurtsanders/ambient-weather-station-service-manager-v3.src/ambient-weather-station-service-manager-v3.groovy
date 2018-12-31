/**
*  Copyright 2018 SanderSoft
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
*  Ambient Weather Station V3
*
*  Author: Kurt Sanders
*
*  Date: 2018-12-30
*/
import groovy.time.*
import java.text.DecimalFormat

// Start Version Information
def version() {
//    return ["V1.0", "Original Code Base"]
//    return ["V2.0", "Service Mgr App Implementation"]
    return ["V3.0", "2018-12-30", "Added the ability to view up to 8 Ambient Remote Sensor(s)"]
}
// End Version Information
String platform() { return "SmartThings" }
String DTHName() { return "Ambient Weather Station V3" }
String DTHRemoteSensorName() { return "Ambient Weather Station Remote Sensor V3"}
String DTHDNI() { return "MyAmbientWeatherStationV3" }
String DTHDNIRemoteSensorName() { return "remoteTempfHumiditySensorName"}
String appModified() { return  } 
String appAuthor()	 { return "SanderSoft" }
Boolean isST() { return (platform() == "SmartThings") }
String getAppImg(imgName) { return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/$imgName" }
definition(
    name: "Ambient Weather Station Service Manager V3",
    namespace: "kurtsanders",
    author: "kurt@kurtsanders.com",
    description: "Ambient Personal Weather Station Service Manager V3",
    category: "My Apps",
    iconUrl:   getAppImg("blue-ball.jpg"),
    iconX2Url: getAppImg("blue-ball.jpg"),
    iconX3Url: getAppImg("blue-ball.jpg"),
    singleInstance: true
)
{
    appSetting "apiKey"
    appSetting "appKey"
}

preferences {    
    page(name: "keysCheckPage")
    page(name: "mainPage")
    page(name: "settingsPage")
    page(name: "remoteSensorsPage")
}

def keysCheckPage() {
    def apiappSetupCompleteBool = !((appSettings?.apiKey==null) && (apiSettings?.apiKey==null))
    def setupMessage = null
    def setupTitle = "Ambient Weather Station API Check"
    def nextPageName = "mainPage"
    def getAmbientStationDataRC = getAmbientStationData()
    if (apiappSetupCompleteBool && getAmbientStationDataRC) {
        setupMessage = "SUCCESS! You have completed your Ambient API & APP Keys for your Ambient Weather Station named '${state.ambientMap.info.name[0]}'"
		setupMessage += (state.countRemoteTempHumiditySensors.toInteger()>0)?" with ${state.countRemoteTempHumiditySensors} remote temperature/humidity sensor(s).":"."
        setupTitle = "Tap NEXT to Continue to Settings Page"
    } else {
        setupMessage = "Setup Incomplete: Please check and/or complete the REQUIRED API and APP Keys setup in the SmartThings IDE (App Settings Section) for this application"
        nextPageName = null
    }
    dynamicPage(name: "keysCheckPage", title: setupTitle, nextPage: nextPageName, uninstall:true, install:false) {
//        log.debug "appSettings.apiKey->${appSettings.apiKey}"
//        log.debug "appSettings.appKey->${appSettings.appKey}"
        section(hideable: apiappSetupCompleteBool, hidden: apiappSetupCompleteBool, setupMessage ) {
            paragraph "The API & APP string keys are used to securely connect your weather station to this application."
            paragraph image: getAppImg("blue-ball.jpg"),
                title: "Required API & APP Keys",
                required: false,
                "You must have both an API and APP key from your Ambient Dashboard.  The actual api and app key values are set on the SmartThings IDE.  Edit the Ambient SmartApp, accessed by pressing the App Settings button. Scroll down the page, expand the Settings group, and set both key values."
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
        section {
            paragraph "App Version Information - ${appAuthor()}"
            paragraph image: getAppImg("blue-ball.jpg"),
                title: "Version: ${version()[0]}",
                required: false,
                "${version()[1]}: ${version()[2]}"
        }
    }
}

def mainPage() {
    dynamicPage(name: "mainPage", title: "Ambient Tile Settings", uninstall:false, install:true) {
        section("Weather Station Location for Local Weather") {
            input "zipCode", type: "number",
                title: "Enter ZipCode for local Weather API Forecast/Moon (Required)", 
                required: true
        }
        section("Weather Station Refresh Update Frequency") {
            input name: "schedulerFreq", type: "enum",
                title: "Run Weather Station Refresh Every (mins)?",
                options: ['0','1','2','3','4','5','10','15','30','60','180'],
                required: true
        }
        if (state.countRemoteTempHumiditySensors > 0) {
            section("Ambient Remote Temperature Sensors") {
                href name: "remoteSensorsPageLink", 
                    title: "Name ${state.countRemoteTempHumiditySensors} Ambient Remote Temperature Sensors (Required)", 
                    description: "", 
                    required: true, 
                    page: "remoteSensorsPage"
            }
        }
        section("IDE Live Logging Output Settings") {
            href(name: "settingsPageLink", title: "IDE Live Logging Output Settings", description: "", page: "settingsPage")
        }
    }
}

def remoteSensorsPage() {
    def i = 1
    dynamicPage(name: "remoteSensorsPage", uninstall:false, install:false) {
        section (hideable: true, hidden: true, "<== Please click to READ Remote Sensor Issues") {
            paragraph "Information Regarding Ambient Remote Sensors"
            paragraph image: getAppImg("blue-ball.jpg"),
                title: "Information, Issues and Instructions",
                required: false,
                "You MUST create short descriptive names for each remote sensor.\n\n" +
                "If you wish to change the short name of the remote sensor, DO NOT change them in the IDE 'My Devices' editor, as this app will rename them automatically when you SAVE the page.\n\n" +
                "Please note that remote sensors are dynamically indexed 1-8 based on Ambient Network API tempNf where N is an integer 1-8.  " +  
                "If a remote sensor is deleted or non responsive from your group of Ambient remote sensors, you may have to rename the remainder of the remote sensors in this app and manually delete that sensor from the IDE 'My Devices' editor."
        }

        section("Location names your ${state.countRemoteTempHumiditySensors} remote temperature Sensors") {
            for (i; i <= state.countRemoteTempHumiditySensors; i++) {           
                input "${DTHDNIRemoteSensorName()}${i}", type: "text",
                    title: "Provide a Short Descriptive Name for Ambient Remote Sensor #${i}", 
                    required: true
            }
        } 
    }
}

def settingsPage() {
    dynamicPage(name: "settingsPage", uninstall:false, install:false) {
        section("IDE Live Logging Messages Preferences") {
            input name: "debugVerbose", type: "bool",
                title: "Show Debug Messages in Live Logging IDE", 
                description: "Verbose Mode", 
                required: false
            input name: "infoVerbose", type: "bool",
                title: "Show Info Messages in Live Logging IDE", 
                description: "Verbose Mode", 
                required: false
            input name: "WUVerbose", type: "bool",
                title: "Show Local Weather Info Messages in Live Logging IDE", 
                description: "Verbose Mode", 
                required: false
        }
    }
}

def installed() {
    log.info "Section: Installed"
    state.deviceId = DTHDNI()
    //    device.deviceNetworkId = NewDNI
    addAmbientChildDevice()
    if(state.schedulerFreq!=schedulerFreq) {
        state.schedulerFreq = schedulerFreq
        if(debugVerbose){log.debug "state.schedulerFreq->${state.schedulerFreq}"}
        setScheduler(state.schedulerFreq)
        def d = getChildDevice(state.deviceId)
        d.sendEvent(name: "scheduleFreqMin", value: state.schedulerFreq)
    }
    main()
}

def uninstalled() {
    log.info "Section Started: Uninstalled"
    unschedule()
    removeAmbientChildDevice()
    log.info "Section Ended: Uninstalled"
}

def updated() {
    log.info "Section Started: Updated"
    state.deviceId = DTHDNI()
    addAmbientChildDevice()
    if(infoVerbose){
        log.info "Ambient API string-> ${appSettings?.apiKey}"
        log.info "Ambient APP string-> ${appSettings?.appKey}"
        log.info "The location zip code for your Hub Location is '${location.zipCode}' and your preference zipCode value is '${zipCode}'"
    }
    if(debugVerbose){
        try {
            log.debug "ambientMap.macAddress -> ${state.ambientMap[0].macAddress}"
        } catch(NullPointerException e) {
            // do something other
        }
    }
    if(state.schedulerFreq!=schedulerFreq) {
        state.schedulerFreq = schedulerFreq
        if(debugVerbose){log.debug "state.schedulerFreq->${state.schedulerFreq}"}
        setScheduler(schedulerFreq)
        def d = getChildDevice(state.deviceId)
        d.sendEvent(name: "scheduleFreqMin", value: schedulerFreq)
    }
//    getChildDevices().each {
//        log.info "AmbientWS Name: '${it.displayName}' with a NetworkID: ${it.deviceNetworkId}"
//        log.debug "Name: ${it.name} Label: ${it.label}"
//    }     
    log.info "Section Ended: Updated"
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

private removeAmbientChildDevice() {
    getAllChildDevices().each { 
        log.debug "Deleting AmbientWS device: ${it.deviceNetworkId}"
        deleteChildDevice(it.deviceNetworkId) 
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
    def obs = get("conditions")?.current_observation
//  def obs = getTwcConditions("${zipCode}")

    if(WUVerbose){log.info "obs --> ${obs}"}
    if (obs) {
        def weatherIcon = obs.icon_url.split("/")[-1].split("\\.")[0]
        d.sendEvent(name: "weatherIcon", value: weatherIcon, displayed: false)
    } else {
        log.error "Severre error retrieving current Weather Underground API: get(conditions)?.current_observation zipCode-> ${zipCode}" 
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
        log.error "Severre error '${e}' retrieving current age of the Astronomy Info Underground API: get('astronomy')?.moon_phase --> ${a}" 
    }

    // Forecast
    def f = get("forecast")
    if (f) {
        if(WUVerbose){log.info "WU Forecast-> ${f}"}
    } else {
        log.error "Severre error getting WU forecast: ${f}"    
    }
    def f1= f?.forecast?.simpleforecast?.forecastday
    //    def f2= f?.forecast?.txt_forecast?.forecastday[0].fcttext
    def f2= sprintf(
        "WU Forecast for zipcode: %s\n%s %s, %s",
        zipCode, 
        f?.forecast?.txt_forecast?.forecastday[0].fcttext, 
        f?.forecast?.txt_forecast?.forecastday[1].title.toLowerCase().capitalize(), 
        f?.forecast?.txt_forecast?.forecastday[1].fcttext
    )
    d.sendEvent(name: "weather", value: f2, descriptionText: "")

    if (f1) {
        if(WUVerbose){log.info "WU Forecastday-> ${f1}"}
        def icon = f1[0].icon_url.split("/")[-1].split("\\.")[0]
        def value = f1[0].pop as String // as String because of bug in determining state change of 0 numbers
        d.sendEvent(name: "forecastIcon", value: icon, displayed: false)
    }
    else {
        if(debugVerbose){log.warn "Error WU forecastday Forecast not found"}
    }

    // Weather Underground Alerts
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
        d.sendEvent(name:"lastSTupdate", value: sprintf("%s Tile Updated at:\n%s",version()[0], now), displayed: false)
        d.sendEvent(name:"macAddress", value: state.ambientMap.macAddress, displayed: false)

        def waterState = state.ambientMap.lastData.hourlyrainin[0].toFloat()>0?'wet':'dry'
        if(debugVerbose){log.debug "water -> ${waterState}"}
        d.sendEvent(name:'water', value: waterState)

        def motionState = state.ambientMap.lastData.windspeedmph[0].toFloat()>0?'active':'inactive'
        if(debugVerbose){log.debug "Wind motion -> ${motionState}"}
        d.sendEvent(name:'motion', value: motionState)

        def winddirectionState = degToCompass(state.ambientMap.lastData.winddir[0])
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
            if((k=='tempf') | (k=='tempf')){k='temperature'}
            if(k=='feelsLike') {
                if(waterState=='wet') {
                    DecimalFormat df = new DecimalFormat("0.0");
                    def numberForRainLevel = df.format(state.ambientMap.lastData.hourlyrainin[0])
                    if(debugVerbose){log.debug "Rain Detected, Changing secondary control value of Main Tile to display hourlyrainin ${numberForRainLevel}"}
                    d.sendEvent(name: 'secondaryControl', value: sprintf("Raining at %s in/hr at %s", numberForRainLevel, nowTime) )
                } else {
                    d.sendEvent(name: 'secondaryControl', value: sprintf("Feels like %sº at %s", v, nowTime) )
                }
            }
            if(v.isNumber() && v > 0 && v <= 0.1) {
                v=(v.toFloat()+0.04).round(1)
            }
            if(k=='uv') {
                k='ultravioletIndex'
            }
            if(k=='solarradiation') {
                k='illuminance'
            }
            if (k.matches('temp[0-9]f')) {
                remoteSensorDNI = getChildDevice("remoteTempfHumiditySensorName${k[4..4]}")
                remoteSensorDNI.sendEvent(name: "temperature", value: v)
                remoteSensorDNI.sendEvent(name:"lastSTupdate", value: sprintf("%s Tile Last Updated at:\n%s",version()[0], now), displayed: false)
                return
            }
            if (k.matches('humidity[0-9]')) {
                remoteSensorDNI = getChildDevice("remoteTempfHumiditySensorName${k[8..8]}")
                remoteSensorDNI.sendEvent(name: "humidity", value: v)
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
    if(appSettings.apiKey==null || appSettings.apiKey=="" || appSettings.appKey==null || appSettings.appKey=="") {
        log.error("Severre Error: API/APP keys are missing in device settings, exiting")
        return false
    }

    def params = [
        uri			: "http://api.ambientweather.net/v1/devices?applicationKey=${appSettings.appKey}&apiKey=${appSettings.apiKey}"
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

def TileBgColors(colorSetName) {
    switch(colorSetName) {
        case 'rain':
        return [ 
            [value: 0,   color: "#ffffff"],
            [value: 1,   color: "#0000ff"],
            [value: 10,  color: "#ff0000"]
        ]
        break
        case 'wind':
        return [ 
            [value: 0,  color: "#ffffff"],
            [value: 5,  color: "#153591"],
            [value: 10, color: "#1e9cbb"],
            [value: 15, color: "#90d2a7"],
            [value: 20, color: "#44b621"],
            [value: 25, color: "#f1d801"],
            [value: 30, color: "#d04e00"],
            [value: 50, color: "#bc2323"]
        ]
        break
        case 'humidity':
        return [ 
            [value: 0, color: "#ffffff"],
            [value: 10, color: "#1e9cbb"],
            [value: 20, color: "#90d2a7"],
            [value: 30, color: "#44b621"],
            [value: 40, color: "#f1d801"],
            [value: 50, color: "#d04e00"],
            [value: 60, color: "#d04e00"],
            [value: 70, color: "#d04e00"],
            [value: 80, color: "#d04e00"],
            [value: 90, color: "#d04e00"],
            [value: 99, color: "#ff0000"]
        ]
        break
        case 'solar':
        return [        
            [value: 0,    color: "#000000"],
            [value: 550,  color: "#ffffff"]
        ] 
        break
        case 'uvi':
        return [        
            [value: 0,    color: "#000000"],
            [value: 12,   color: "#ffffff"]
        ] 
        break
        case 'scheduleFreqMin':
        return [
            [value: '0',  color: "#FF0000"],
            [value: '1',    color: "#9400D3"],
            [value: '2',    color: "#00FF00"],
            [value: '3',    color: "#FFFF00"],
            [value: '4',    color: "#FF7F00"],
            [value: '5',    color: "#4B0082"],
            [value: '10',   color: "#0000FF"],
            [value: '15',   color: "#00FF00"],
            [value: '30',   color: "#FFFF00"],
            [value: '60',   color: "#FF7F00"],
            [value: '180',  color: "#ff69b4"]
        ]    
        break
        case 'default':
        return [                
            [value: '0',  color: "#ffffff"]
        ]
    }
}

private estimateLux(sunriseDate, sunsetDate, weatherIcon) {
    def lux = 0
    def now = new Date().time
    if (now > sunriseDate.time && now < sunsetDate.time) {
        //day
        switch(weatherIcon) {
            case 'tstorms':
            lux = 200
            break
            case ['cloudy', 'fog', 'rain', 'sleet', 'snow', 'flurries',
                  'chanceflurries', 'chancerain', 'chancesleet',
                  'chancesnow', 'chancetstorms']:
            lux = 1000
            break
            case 'mostlycloudy':
            lux = 2500
            break
            case ['partlysunny', 'partlycloudy', 'hazy']:
            lux = 7500
            break
            default:
                //sunny, clear
                lux = 10000
        }

        //adjust for dusk/dawn
        def afterSunrise = now - sunriseDate.time
        def beforeSunset = sunsetDate.time - now
        def oneHour = 1000 * 60 * 60

        if(afterSunrise < oneHour) {
            //dawn
            lux = (long)(lux * (afterSunrise/oneHour))
        } else if (beforeSunset < oneHour) {
            //dusk
            lux = (long)(lux * (beforeSunset/oneHour))
        }
    }
    else {
        //night - always set to 10 for now
        //could do calculations for dusk/dawn too
        lux = 10
    }
    lux
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