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
*  Ambient Weather Station
*
*  Author: Kurt Sanders
*
*  Date: 2018-11-21
*/
import groovy.time.*
import java.text.DecimalFormat

// Start Version Information
def version() {
    return ["V1.0", "Original Code Base"]
}
// End Version Information
String platform() { return "SmartThings" }
String DTHName() { return "Ambient Weather Station" }
String appVersion()	 { return "1.0" }
String appModified() { return "2018-11-22" } 
String appAuthor()	 { return "Kurt Sanders" }
Boolean isST() { return (platform() == "SmartThings") }
String getAppImg(imgName) { return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/$imgName" }
Map minVersions() { //These define the minimum versions of code this app will work with.
    return [ambientDevice: 100]
}
definition(
  name: "Ambient Weather Station Service Manager",
  namespace: "kurtsanders",
  author: "kurt@kurtsanders.com",
  description: "Ambient Personal Weather Station Service Manager",
  category: "My Apps",
  iconUrl: getAppImg("blue-ball.jpg"),
  iconX2Url: getAppImg("blue-ball.jpg"),
    iconX3Url: getAppImg("blue-ball.jpg"),
    singleInstance: true
)
{
    appSetting "apiKey"
    appSetting "appKey"
}

preferences {    
        page(name: "mainPage")
        page(name: "settingsPage")
}

def mainPage() {
    dynamicPage(name: "mainPage", title: "Ambient Tile Settings", uninstall:true, install:true) {
        def apiappSetupCompleteBool = !((appSettings?.apiKey==null) && (apiSettings?.apiKey==null) && (state.ambientMap[0]?.info?.name==null))
        def setupMessage = null
        if (apiappSetupCompleteBool) {
            setupMessage = state.ambientMap[0].info.name?"SUCCESS You have completed your Ambient API & APP Keys for the Ambient weather station named: '${state.ambientMap[0].info.name}'":"Error in API or API Keys"
        } else if (getAmbientStationData()) {
            setupMessage = state.ambientMap[0].info.name?"SUCCESS You have completed your Ambient API & APP Keys for the Ambient weather station named: '${state.ambientMap[0].info.name}'":"Error in API or API Keys"
            apiappSetupCompleteBool = true
        } else {
            setupMessage = "Please complete the REQUIRED API and APP Keys setup in the SmartThings IDE (App Settings Section) for this application"
        }
        section("Weather Station Location for Local Weather") {
            input "zipCode", type: "number",
                title: "ZipCode for WU Weather API Forecast/Moon (Required)", 
                required: true
        }
        section("Weather Station Refresh Update Frequency:") {
            input name: "schedulerFreq", type: "enum",
                title: "Run Weather Station Refresh Every (mins)?",
                options: ['Off','1','2','3','4','5','10','15','30','60','180'],
                required: true
        }
        section(hideable: apiappSetupCompleteBool, hidden: apiappSetupCompleteBool, setupMessage ) {
        paragraph "The API & APP string keys are used to securely connect your weather station to this application."
            paragraph image: getAppImg("1453901679/blue-ball.jpg"),
                title: "Required API & APP Keys",
                required: true,
                "You must have both an API and APP key from your Ambient Dashboard.  You MUST enter both these keys in the ST browser IDE APP 'Settings' section for this SmartApp"
            href(name: "hrefUSA",
                 title: "SmartThings IDE USA",
                 required: true,
                 style: "external",
                 url: "https://graph.api.smartthings.com/",
                 description: "tap to view the US SmartThings IDE website in mobile browser")
            href(name: "hrefEurope",
                 title: "SmartThings IDE Europe",
                 required: true,
                 style: "external",
                 url: "https://graph-eu01-euwest1.api.smartthings.com/",
                 description: "tap to view the Europe SmartThings IDE website in mobile browser")
        }
        section("IDE Log Output Settings") {
            href(name: "settingsPageLink", title: "IDE Log Output Settings", description: "", page: "settingsPage")
        }

        section("App Version Information") {
            input name: "VersionInfo", type: "text",
                title: "Updates: " + version()[1], 
                description: "Version: " + version()[0], 
                required: false
        }
    }
}

def settingsPage() {
    dynamicPage(name: "settingsPage", uninstall:false, install:false) {
        section("IDE Logging Messages Preferences") {
            input name: "debugVerbose", type: "bool",
                title: "Show Debug Messages in IDE", 
                description: "Verbose Mode", 
                required: false
            input name: "infoVerbose", type: "bool",
                title: "Show Info Messages in IDE", 
                description: "Verbose Mode", 
                required: false
            input name: "WUVerbose", type: "bool",
                title: "Show Weather Underground Info Messages in IDE", 
                description: "Verbose Mode", 
                required: false
        }
    }
}

def appPage() {
    dynamicPage(name: "appPage", uninstall:false, install:false) {
        section("Weather Station APPLICATION KEY Information:") {
            input name: "appString", type: "text",
                title: "Ambient APPLICATION Key (To request an Application Key, please email support@ambientweather.com, and include a description of your project and the MAC address of your weather station.)",
                description: "Application Key",
                required: true
        }
    }
}

def installed() {
    if(infoVerbose){log.info "Section: Installed"}
    state.deviceId = 'MyAmbientWeatherStation'
    //    device.deviceNetworkId = "$"
    if (!addAmbientChildDevice()) {
		log.error "Error in addAmbientChildDevice()"
        return false
    }
    if(state.schedulerFreq!=schedulerFreq) {
        state.schedulerFreq = schedulerFreq
        if(debugVerbose){log.debug "state.schedulerFreq->${state.schedulerFreq}"}
        setScheduler(state.schedulerFreq)
        def d = getChildDevice(state.deviceId)
        d.sendEvent(name: "scheduleFreqMin", value: state.schedulerFreq)

    }
}

def uninstalled() {
    if(infoVerbose){log.info "Section: Uninstalled"}
    unschedule()
    unsubscribe()
    removeAmbientChildDevice()
}

def updated() {
    log.info "Section: Updated"
    if(infoVerbose){log.info "Ambient API string-> ${appSettings?.apiKey}"}
    if(infoVerbose){log.info "Ambient APP string-> ${appSettings?.appKey}"}
    if(infoVerbose){log.info "The location zip code for your Hub Location is '${location.zipCode}' and your preference zipCode value is '${zipCode}'"}
    state.deviceId = 'MyAmbientWeatherStation'
    log.info "ambientMap.macAddress -> ${state.ambientMap[0].macAddress}"
    getAllChildDevices().each {
    	log.info "it -> ${it}"
        log.info "AmbientWS device: ${it.deviceNetworkId}"
    }
    if(state.schedulerFreq!=schedulerFreq) {
        state.schedulerFreq = schedulerFreq
        if(debugVerbose){log.debug "state.schedulerFreq->${state.schedulerFreq}"}
        setScheduler(schedulerFreq)
        def d = getChildDevice(state.deviceId)
        d.sendEvent(name: "scheduleFreqMin", value: schedulerFreq)
    }
}

private addAmbientChildDevice() {
    // add Ambient Weather Reporter Station device
    log.debug "Adding AmbientWS device: ${state.deviceId}"
    if (!getChildDevice(state.deviceId)) {
        try { 
            addChildDevice("kurtsanders", DTHName(), state.deviceId, null, ["name": "Ambient Weather Station", label: "Ambient Weather Station", completedSetup: true])
        } catch(physicalgraph.app.exception.UnknownDeviceTypeException ex) {
            log.error "The Device Handler ${DTHName()} was not found, Error-> '${ex}'.  Please install this in the IDE's 'My Device Handlers'"
            return false
        }
    } 
    log.debug "Added ${DTHName()} with DNI: ${state.deviceId}"
}
private removeAmbientChildDevice() {
    getAllChildDevices().each { 
        log.debug "Deleting AmbientWS device: ${it.deviceNetworkId}"
        if (deleteChildDevice(it.deviceNetworkId)) {
            log.debug "Successly Deleted AmbientWS device: ${it.deviceNetworkId}"
        } else {
            log.error "Error Deleting AmbientWS device: ${it.deviceNetworkId}"
        }
    }
}

def refresh() {
    main()
}

def main() {    
    log.info "SmartApp Section: Refresh"
    def d = getChildDevice(state.deviceId)
    def now = new Date().format('EEE MMM d, h:mm:ss a',location.timeZone)
    def currentDT = new Date()

    // Weather Underground Station Forecast
    log.info "WUSTATION: Executing 'Weather Forecast, Sunrise, Sunset, Moon Info for zipCode: ${zipCode}"
    // Current conditions
    def obs = get("conditions")?.current_observation
    if(WUVerbose){log.info "obs --> ${obs}"}
    if (obs) {
        def weatherIcon = obs.icon_url.split("/")[-1].split("\\.")[0]
        sendEvent(name: "weatherIcon", value: weatherIcon, displayed: false)
    } else {
        log.error "Severre error retrieving current Weather Underground API: get(conditions)?.current_observation zipCode-> ${zipCode}" 
    }
    // Get Age of Lunar Moon from Weather Underground
    def a = get("astronomy")?.moon_phase
    if(WUVerbose){
        log.info "get('astronomy')?.moon_phase --> ${a}"
        log.info "ageOfMoon -> ${a.ageOfMoon}"
    }
    if (a) {
        d.sendEvent(name: "moonAge", value: "${a.ageOfMoon}", isStateChange: true)
    } else {
        log.error "Severre error retrieving current age of the Moon Weather Underground API: get('astronomy')?.moon_phase --> ${a}" 
    }

	// Get Sunset, Sunrise from Weather Underground
    def ltf = new java.text.SimpleDateFormat("HH:mm")
    def tf = new java.text.SimpleDateFormat("h:mm a")
    def sunriseTime = ltf.parse("${a.sunrise.hour}:${a.sunrise.minute}")
    def sunsetTime  = ltf.parse("${a.sunset.hour}:${a.sunset.minute}")
    def localSunrise   = "${tf.format(sunriseTime)}"
    def localSunset    = "${tf.format(sunsetTime)}"
    if(WUVerbose){log.info "localSunrise->${localSunrise}, localSunset-> ${localSunset}"}
    d.sendEvent(name: "localSunrise", value: localSunrise , descriptionText: "Sunrise today is at ${localSunrise}")
    d.sendEvent(name: "localSunset" , value: localSunset  , descriptionText: "Sunset today is at ${localSunset}")
 
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
            d.sendEvent(name:"lastRainDuration", value: "N/A")
        }
        d.sendEvent(name:"lastSTupdate", value: sprintf("%s Tile Updated at:\n%s",version()[0], now))
        d.sendEvent(name:"macAddress", value: state.ambientMap.macAddress)

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
            if(v.isNumber() && v > 0 && v <= 0.1) {
                v=(v.toFloat()+0.04).round(1)
            }
            if(k=='uv') {
                k='ultravioletIndex'
            }
            if(k=='solarradiation') {
                k='illuminance'
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
        case 'Off':
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
            [value: 'Off',  color: "#FF0000"],
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
        d.sendEvent(name: "alertMessage", value: "", descriptionText: "")
        d.sendEvent(name: "alertDescription", value: "${alertCountMsg}", descriptionText: "")
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