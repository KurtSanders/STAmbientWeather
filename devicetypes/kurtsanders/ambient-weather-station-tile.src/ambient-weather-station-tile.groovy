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
*  Date: 2018-08-10
*/
import groovy.time.*
import java.text.DecimalFormat
metadata {
    definition (name: "Ambient Weather Station Tile", namespace: "kurtsanders", author: "kurt@kurtsanders.com") {
        capability "Illuminance Measurement"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Refresh"
        capability "Motion Sensor"
        capability "Water Sensor"
        capability "Ultraviolet Index"

        // Start of Ambient Weather API Rest MAP
        attribute "baromabsin", "string"
        attribute "baromrelin", "string"
        attribute "city", "string"
        attribute "dailyrainin", "string"
        attribute "date", "string"
        attribute "dateutc", "string"
        attribute "dewPoint", "string"
        attribute "eventrainin", "string"
        attribute "feelsLike", "string"
        attribute "hourlyrainin", "string"
        attribute "humidty", "string"
        attribute "humidityin", "string"
        attribute "lastRain", "string"
        attribute "lastRainDuration", "string"
        attribute "macAddress", "string"
        attribute "maxdailygust", "string"
        attribute "monthlyrainin", "string"
        attribute "temperature", "string"
        attribute "tempinf", "string"
        attribute "totalrainin", "string"
        attribute "weeklyrainin", "string"
        attribute "winddir", "string"
        attribute "winddirection", "string"
        attribute "windgustmph", "string"
        attribute "windspeedmph", "string"   
        // End of Ambient Weather API Rest MAP
        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "weatherIcon", "string"
        attribute "forecastIcon", "string"
        attribute "sunriseDate", "string"
        attribute "sunsetDate", "string"
        attribute "alertKeys", "string"
        attribute "alert", "string"
        
        command "refresh"
    }
    preferences {
        input name: "apiString", type: "text",
            title: "Ambient API Key (To view your keys, please visit: https://dashboard.ambientweather.net/account)",
            description: "API Key",
            required: true,
            displayDuringSetup: true
        input name: "appString", type: "text",
            title: "Ambient APPLICATION Key (To request an Application Key, please email support@ambientweather.com, and include a description of your project and the MAC address of your weather station.)",
            description: "Application Key",
            required: true,
            displayDuringSetup: true
        input "zipCode", "text", title: "Zip Code for Weather Forecast (optional)", required: false
        input name: "schedulerFreq", type: "enum",
            title: "Run Refresh Every (mins)?",
            options: ['Off','1','2','3','4','5','10','15','30','60','180'],
            required: true,
            displayDuringSetup: true
        input name: "debugVerbose", type: "bool",
            title: "Show Debug Messages in IDE", 
            description: "Verbose Mode", 
            required: false
        input name: "infoVerbose", type: "bool",
            title: "Show Info Messages in IDE", 
            description: "Verbose Mode", 
            required: false
    }
    tiles(scale: 2) {
        multiAttributeTile(name:"temperature", type:"generic", width:6, height:3, canChangeIcon: false) {
            tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
                attributeState("default",label:'${currentValue}º',
                               backgroundColors:[
                                   [value: 32, color: "#153591"],
                                   [value: 44, color: "#1e9cbb"],
                                   [value: 59, color: "#90d2a7"],
                                   [value: 74, color: "#44b621"],
                                   [value: 84, color: "#f1d801"],
                                   [value: 92, color: "#d04e00"],
                                   [value: 98, color: "#bc2323"]
                               ])
            }
            tileAttribute("device.feelsLike", key: "SECONDARY_CONTROL") {
                attributeState("feelsLike", label:'Feels Like ${currentValue}°')
            }
        }
    }
    valueTile("tempinf", "device.tempinf", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Inside Temp\n${currentValue}°', 
            backgroundColors:[
                [value: 60, color: "#153591"],
                [value: 65, color: "#1e9cbb"],
                [value: 69, color: "#90d2a7"],
                [value: 71, color: "#44b621"],
                [value: 73, color: "#f1d801"],
                [value: 75, color: "#d04e00"],
                [value: 80, color: "#bc2323"]
            ]
    }    
    standardTile("weatherIcon", "device.weatherIcon", width: 2, height: 2, decoration: "flat") {
        state "chanceflurries", icon:"st.custom.wu1.chanceflurries", label: ""
        state "chancerain", icon:"st.custom.wu1.chancerain", label: ""
        state "chancesleet", icon:"st.custom.wu1.chancesleet", label: ""
        state "chancesnow", icon:"st.custom.wu1.chancesnow", label: ""
        state "chancetstorms", icon:"st.custom.wu1.chancetstorms", label: ""
        state "clear", icon:"st.custom.wu1.clear", label: ""
        state "cloudy", icon:"st.custom.wu1.cloudy", label: ""
        state "flurries", icon:"st.custom.wu1.flurries", label: ""
        state "fog", icon:"st.custom.wu1.fog", label: ""
        state "hazy", icon:"st.custom.wu1.hazy", label: ""
        state "mostlycloudy", icon:"st.custom.wu1.mostlycloudy", label: ""
        state "mostlysunny", icon:"st.custom.wu1.mostlysunny", label: ""
        state "partlycloudy", icon:"st.custom.wu1.partlycloudy", label: ""
        state "partlysunny", icon:"st.custom.wu1.partlysunny", label: ""
        state "rain", icon:"st.custom.wu1.rain", label: ""
        state "sleet", icon:"st.custom.wu1.sleet", label: ""
        state "snow", icon:"st.custom.wu1.snow", label: ""
        state "sunny", icon:"st.custom.wu1.sunny", label: ""
        state "tstorms", icon:"st.custom.wu1.tstorms", label: ""
        state "cloudy", icon:"st.custom.wu1.cloudy", label: ""
        state "partlycloudy", icon:"st.custom.wu1.partlycloudy", label: ""
        state "nt_chanceflurries", icon:"st.custom.wu1.nt_chanceflurries", label: ""
        state "nt_chancerain", icon:"st.custom.wu1.nt_chancerain", label: ""
        state "nt_chancesleet", icon:"st.custom.wu1.nt_chancesleet", label: ""
        state "nt_chancesnow", icon:"st.custom.wu1.nt_chancesnow", label: ""
        state "nt_chancetstorms", icon:"st.custom.wu1.nt_chancetstorms", label: ""
        state "nt_clear", icon:"st.custom.wu1.nt_clear", label: ""
        state "nt_cloudy", icon:"st.custom.wu1.nt_cloudy", label: ""
        state "nt_flurries", icon:"st.custom.wu1.nt_flurries", label: ""
        state "nt_fog", icon:"st.custom.wu1.nt_fog", label: ""
        state "nt_hazy", icon:"st.custom.wu1.nt_hazy", label: ""
        state "nt_mostlycloudy", icon:"st.custom.wu1.nt_mostlycloudy", label: ""
        state "nt_mostlysunny", icon:"st.custom.wu1.nt_mostlysunny", label: ""
        state "nt_partlycloudy", icon:"st.custom.wu1.nt_partlycloudy", label: ""
        state "nt_partlysunny", icon:"st.custom.wu1.nt_partlysunny", label: ""
        state "nt_sleet", icon:"st.custom.wu1.nt_sleet", label: ""
        state "nt_rain", icon:"st.custom.wu1.nt_rain", label: ""
        state "nt_sleet", icon:"st.custom.wu1.nt_sleet", label: ""
        state "nt_snow", icon:"st.custom.wu1.nt_snow", label: ""
        state "nt_sunny", icon:"st.custom.wu1.nt_sunny", label: ""
        state "nt_tstorms", icon:"st.custom.wu1.nt_tstorms", label: ""
        state "nt_cloudy", icon:"st.custom.wu1.nt_cloudy", label: ""
        state "nt_partlycloudy", icon:"st.custom.wu1.nt_partlycloudy", label: ""
    }
    valueTile("alert", "device.alert", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Weather Alerts:\n ${currentValue}'
    }
    valueTile("rise", "device.localSunrise", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Sunrise\n ${currentValue}'
    }
    valueTile("set", "device.localSunset", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Sunset\n ${currentValue}'
    }
    valueTile("weather", "device.weather", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'${currentValue}'
    }
    valueTile("humidityin", "device.humidityin", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Inside Humidity\n${currentValue}%', backgroundColors: TileBgColors('humidity')
    }
    valueTile("baromrelin", "device.baromrelin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rel Pres \n${currentValue} in '
    }
    valueTile("baromabsin", "device.baromabsin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Abs Pres\n${currentValue} in'
    }
    valueTile("location", "device.location", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Location\n${currentValue}'
    }
    valueTile("name", "device.name", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Name\n${currentValue}'
    }
    valueTile("humidity", "device.humidity", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Humidity\n${currentValue}%', backgroundColors: TileBgColors('humidity')
    }
    valueTile("eventrainin", "device.eventrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Event\n${currentValue} in/hr', backgroundColors: TileBgColors('rain')
    }
    valueTile("hourlyrainin", "device.hourlyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Hr\n${currentValue} in',  backgroundColors: TileBgColors('rain')
    }
    valueTile("dailyrainin", "device.dailyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Daily\n${currentValue} in', backgroundColors: TileBgColors('rain')
    }
    valueTile("weeklyrainin", "device.weeklyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Weekly\n${currentValue} in', backgroundColors: TileBgColors('rain')
    }
    valueTile("monthlyrainin", "device.monthlyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Monthly\n${currentValue} in'
    }
    valueTile("totalrainin", "device.totalrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Total\n${currentValue} in'
    }
    valueTile("lastRain", "device.lastRain", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Last Date\n${currentValue}'
    }
    valueTile("lastRainDuration", "device.lastRainDuration", inactiveLabel: false, width: 5, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Time Since Last Rain\n${currentValue}'
    }
    valueTile("ultravioletIndex", "device.ultravioletIndex", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'UVI\n${currentValue}'
    }
    valueTile("solarradiation", "device.illuminance", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Light\n${currentValue}', backgroundColors: TileBgColors('solar')
    }
    standardTile("water", "device.water", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "wet", label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-rain.png'
        state "dry", label: 'No Rain', icon: "st.Weather.weather12"
    }
    standardTile("motion", "device.motion", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "active",   label: '',    icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-windy.png'
        state "inactive", label: 'No Wind', icon: "st.Weather.weather3"
    }
    valueTile("dewPoint", "device.dewPoint", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Dewpoint\n${currentValue}°'
    }
    valueTile("winddir", "device.winddir", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Direction\n${currentValue}º'
    }
    valueTile("winddir2", "device.winddir2", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Direction\n${currentValue}'
    }
    standardTile("winddirection", "device.winddirection", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
    // "N", "North NE", "NE", "East NE", "E", "East SE", "SE", "South SE", "S", "South SW", "SW", "West SW", "W", "West NW", "NW", "North NW"
        state "default",    label: '${currentValue}'
        state "N",        	label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-up.png'
        state "North NE", 	label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-up.png'
        state "NE",   		label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-up-left.png'
        state "East NE",   	label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-up-left.png'
        state "E",          label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-right.png'
        state "East SE",    label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-right.png'
        state "SE",         label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-down-right.png'
        state "South SE",   label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-down-right.png'
        state "S",          label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-down.png'
        state "South SW",   label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-down.png'
        state "SW",         label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-down-left.png'
        state "West SW",    label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-down-left.png'
        state "W",          label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-left.png'
        state "West NW",    label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-left.png'
        state "NW",         label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-up-left.png'
        state "North NW",   label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-direction-up-left.png'
    }
    valueTile("windspeedmph", "device.windspeedmph", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Speed\n${currentValue} mph', backgroundColors: TileBgColors('wind')
    }
    valueTile("windgustmph", "device.windgustmph", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Gust\n${currentValue} mph', backgroundColors: TileBgColors('wind')
    }
    valueTile("maxdailygust", "device.maxdailygust", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Daily Gust\n${currentValue} mph', backgroundColors: TileBgColors('wind')
    }
    valueTile("macAddress", "device.macAddress", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'macAddress\n ${currentValue}'
    }
    valueTile("scheduleFreqMin", "device.scheduleFreqMin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Run Every\n${currentValue} mins', backgroundColors: TileBgColors('scheduleFreqMin')
    }
    valueTile("lastSTupdate", "device.lastSTupdate", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state("default", label: 'Last Updated\n ${currentValue}')
    }
    standardTile("refresh", "device.weather", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: "", action: "refresh", icon:"st.secondary.refresh"
    }

    main(["temperature"])
    details(
        [
            // Inside Sensors
            "temperature", 
            "feelsLike", 
            "tempinf", 
            "humidityin" , 
            // Outside Sensors
            "weatherIcon", 
            "eventrainin", 
            "water", 
            "hourlyrainin", 
            "dailyrainin", 
            "weeklyrainin", 
            "monthlyrainin", 
            "lastRain",
            "lastRainDuration",
            "totalrainin",
            "winddir2",
            "winddirection",
            "windspeedmph", 
            "motion",
            "windgustmph",
            "baromrelin", 
            "baromabsin", 
            "humidity" , 
            "dewPoint", 
            "solarradiation", 
            "ultravioletIndex", 
            "rise", 
            "set",
            "alert",
            "weather",
            "name", 
            "location",
            "macAddress",
            "scheduleFreqMin",
            "lastSTupdate",
            "refresh"
        ]
    )
}

def initialize() {
    // initialize API's
    if(infoVerbose){log.info "Section: Initialize"}
}

def installed() {
    if(infoVerbose){log.info "Section: Installed"}
}

def uninstalled() {
    if(infoVerbose){log.info "Section: Uninstalled"}
    unschedule()
}

def updated() {
    if(infoVerbose){log.info "Section: Updated"}
    if(infoVerbose){log.info "Ambient api string-> ${apiString}"}
    if(infoVerbose){log.info "Ambient app string-> ${appString}"}
    if(state.schedulerFreq!=schedulerFreq) {
        state.schedulerFreq = schedulerFreq
        if(debugVerbose){log.debug "state.schedulerFreq->${state.schedulerFreq}"}
        setScheduler(schedulerFreq)
    }
}
def configure() {
    if(infoVerbose){log.info "Section: Configure"}
    refresh()
}

def refresh() {
    // Weather Underground Station Forecast
    log.info "WUSTATION: Executing 'Weather Forecast for: ${location.name}"
    def now = new Date().format('EEE MMM d, h:mm:ss a',location.timeZone)
    def currentDT = new Date()

    // Current conditions
    def obs = get("conditions")?.current_observation
    if (obs) {
        if(debugVerbose){log.debug "obs --> ${obs}"}
        def weatherIcon = obs.icon_url.split("/")[-1].split("\\.")[0]
        send(name: "weather", value: obs.weather)
        send(name: "weatherIcon", value: weatherIcon, displayed: false)
    }
    // Sunrise / sunset
    def a = get("astronomy")?.moon_phase
    def today = localDate("GMT${obs.local_tz_offset}")
    def ltf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
    ltf.setTimeZone(TimeZone.getTimeZone("GMT${obs.local_tz_offset}"))
    def utf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    utf.setTimeZone(TimeZone.getTimeZone("GMT"))
    def sunriseDate = ltf.parse("${today} ${a.sunrise.hour}:${a.sunrise.minute}")
    def sunsetDate = ltf.parse("${today} ${a.sunset.hour}:${a.sunset.minute}")

    def tf = new java.text.SimpleDateFormat("h:mm a")
    tf.setTimeZone(TimeZone.getTimeZone("GMT${obs.local_tz_offset}"))
    def localSunrise = "${tf.format(sunriseDate)}"
    def localSunset = "${tf.format(sunsetDate)}"
    if(debugVerbose){log.debug "localSunrise->${localSunrise}, localSunset-> ${localSunset}"}
    send(name: "localSunrise", value: localSunrise, descriptionText: "Sunrise today is at $localSunrise")
    send(name: "localSunset", value: localSunset, descriptionText: "Sunset today at is $localSunset")

    // Forecast
    def f = get("forecast")
    def f1= f?.forecast?.simpleforecast?.forecastday
    if (f1) {
        def icon = f1[0].icon_url.split("/")[-1].split("\\.")[0]
        def value = f1[0].pop as String // as String because of bug in determining state change of 0 numbers
        send(name: "forecastIcon", value: icon, displayed: false)
    }
    else {
        if(debugVerbose){log.warn "Forecast not found"}
    }

    // Alerts
    def alerts = get("alerts")?.alerts
    def newKeys = alerts?.collect{it.type + it.date_epoch} ?: []
    if(debugVerbose){log.debug "WUSTATION: newKeys = ${newKeys}"}
    if(debugVerbose){log.trace device.currentState("alertKeys")}
    def oldKeys = device.currentState("alertKeys")?.jsonValue
    if(debugVerbose){log.debug "WUSTATION: oldKeys = ${oldKeys}"}

    def noneString = "no current weather alerts"
    if (!newKeys && oldKeys == null) {
        send(name: "alertKeys", value: newKeys.encodeAsJSON(), displayed: false)
        send(name: "alert", value: noneString, descriptionText: "${device.displayName} has no current weather alerts", isStateChange: true)
    }
    else if (newKeys != oldKeys) {
        if (oldKeys == null) {
            oldKeys = []
        }
        send(name: "alertKeys", value: newKeys.encodeAsJSON(), displayed: false)

        def newAlerts = false
        alerts.each {alert ->
            if (!oldKeys.contains(alert.type + alert.date_epoch)) {
                def msg = "${alert.description} from ${alert.date} until ${alert.expires}"
                send(name: "alert", value: pad(alert.description), descriptionText: msg, isStateChange: true)
                newAlerts = true
            }
        }
        if (!newAlerts && device.currentValue("alert") != noneString) {
            send(name: "alert", value: noneString, descriptionText: "${device.displayName} has no current weather alerts", isStateChange: true)
        }
    }
    else {
        if(debugVerbose){log.warn "No response from Weather Underground API"}
    }


    // Ambient Weather Station
    log.info "Ambient Weather STATION: Executing 'Refresh Routine' every: ${schedulerFreq} min(s)}"        
    if (getAmbientStationData()) {
        if(infoVerbose){log.info "Processing Ambient Weather data returned from getAmbientStationData())"}
        if(debugVerbose || infoVerbose) {
            state.ambientMap[0].each{ k, v -> 
                log.info "${k} = ${v}"
                if (v instanceof Map) {
                    v.each { x, y ->
                        log.info "${x} = ${y}"
                    }
                }
            }
        }
        def dateRain = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", state.ambientMap.lastData.lastRain[0])
        use (groovy.time.TimeCategory) {
            if(debugVerbose){log.debug ("lastRainDuration -> ${currentDT - dateRain}")}
            sendEvent(name:"lastRainDuration", value: currentDT - dateRain)
        }    
        sendEvent(name:"lastSTupdate", value: now)
        sendEvent(name:"macAddress", value: state.ambientMap.macAddress)

        def waterState = state.ambientMap.lastData.hourlyrainin[0].toFloat()>0?'wet':'dry'
        if(debugVerbose){log.debug "water -> ${waterState}"}
        sendEvent(name:'water', value: waterState)

        def motionState = state.ambientMap.lastData.windspeedmph[0].toFloat()>0?'active':'inactive'
        if(debugVerbose){log.debug "Wind motion -> ${motionState}"}
        sendEvent(name:'motion', value: motionState)

        def winddirectionState = degToCompass(state.ambientMap.lastData.winddir[0])
        if(debugVerbose){log.debug "Wind Direction -> ${winddirectionState}"}
        sendEvent(name:'winddirection', value: winddirectionState)
        sendEvent(name:'winddir2', value: winddirectionState + " (" + state.ambientMap.lastData.winddir[0] + "º)")
        
        state.ambientMap.lastData[0].each{ k, v -> 
            if(k=='dateutc' || k=='date'){return}
            if(k=='lastRain'){v=Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", v).format('EEE MMM d, h:mm a',location.timeZone)}
            if(k=='tempf'){k='temperature'}
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
            sendEvent(name: k, value: v)
        }
        state.ambientMap.info[0].each{ k, v -> 
            if(debugVerbose){log.debug "sendEvent(name: ${k}, value: '${v})'"}
        } 
    } else {
        if(debugVerbose){log.info "getAmbientStationData() did not return any weather data"}
    }
}

def logdata(name,val) {
    log.debug "${name} -> ${val}"
    return
}

private get(feature) {
    getWeatherFeature(feature, zipCode)
}

private localDate(timeZone) {
    def df = new java.text.SimpleDateFormat("yyyy-MM-dd")
    df.setTimeZone(TimeZone.getTimeZone(timeZone))
    df.format(new Date())
}

def getAmbientStationData() {
    if(apiString==null || apiString=="" || appString==null || appString=="") {
        log.error("Severre Error: API/APP keys are missing in device settings, exiting")
        return false
    }

    def params = [
        uri			: "https://api.ambientweather.net/v1/devices?applicationKey=${appString}&apiKey=${apiString}"
    ]
    try {
        httpGet(params) { resp ->
            // get the data from the response body
            state.ambientMap = resp.data
            if (resp.status != 200) {
                log.error "AmbientWeather.Net: response status code: ${resp.status}"
                log.error "AmbientWeather.Net: response: ${resp.data}"
                return false
            }
        }
    } catch (e) {
        log.error("getAmbientStationData() Try/Catch Error: Unable to get the Ambient Station Data, Error: $e")
        return false
    }
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
    sendEvent(name: "scheduleFreqMin", value: schedulerFreq)
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
            [value: 25,    color: "#FF0000"],
            [value: 50,    color: "#9400D3"],
            [value: 100,    color: "#00FF00"],
            [value: 150,    color: "#FFFF00"],
            [value: 200,    color: "#FF7F00"],
            [value: 250,    color: "#4B0082"],
            [value: 300,   color: "#0000FF"],
            [value: 350,   color: "#00FF00"],
            [value: 400,   color: "#FFFF00"],
            [value: 450,   color: "#FF7F00"],
            [value: 500,  color: "#ff69b4"],
            [value: 550,  color: "#ffffff"]
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
private send(map) {
    sendEvent(map)
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
