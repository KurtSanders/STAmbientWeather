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

    metadata {
        definition (name: "Ambient Weather Station Tile", namespace: "kurtsanders", author: "kurt@kurtsanders.com") {
            capability "Illuminance Measurement"
            capability "Temperature Measurement"
            capability "Relative Humidity Measurement"
            capability "Sensor"
            capability "Refresh"
            capability "Motion Sensor"
            capability "Water Sensor"

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
            attribute "humidtyin", "string"
            attribute "lastRain", "string"
            attribute "macAddress", "string"
            attribute "maxdailygust", "string"
            attribute "monthlyrainin", "string"
            attribute "solarradiation", "string"
            attribute "temperature", "string"
            attribute "tempinf", "string"
            attribute "totalrainin", "string"
            attribute "uv", "string"
            attribute "weeklyrainin", "string"
            attribute "winddir", "string"
            attribute "winddirection", "string"
            attribute "windgustmph", "string"
            attribute "windspeedmph", "string"   
            // End of Ambient Weather API Rest MAP

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
            multiAttributeTile(name:"temperature", type:"generic", width:6, height:4, canChangeIcon: false) {
                tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
                    attributeState("temperature",label:'${currentValue}°',
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
            state "tempinf", label: 'Inside Temp\n${currentValue}°', 
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
        valueTile("humidityin", "device.humidityin", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Inside Humidity\n${currentValue}%', 
                backgroundColors:[
                    [value: 32, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 92, color: "#d04e00"],
                    [value: 98, color: "#bc2323"]
                ]
        }
        valueTile("baromrelin", "device.baromrelin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Rel Pres \n${currentValue} in '
        }
        valueTile("baromabsin", "device.baromabsin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Abs Pres\n${currentValue} in'
        }
        valueTile("location", "device.location", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Location\n${currentValue}'
        }
        valueTile("name", "device.name", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "name", label:'Name\n${currentValue}'
        }
        valueTile("humidity", "device.humidity", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Humidity\n${currentValue}%'
        }
        valueTile("eventrainin", "device.eventrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Rain Event\n${currentValue} in', 
                backgroundColors:[ 
                    [value: 0,    color: "#ffffff"],
                    [value: 0.25, color: "#153591"],
                    [value: 0.50, color: "#1e9cbb"],
                    [value: 0.75, color: "#90d2a7"],
                    [value: 1.00, color: "#44b621"],
                    [value: 1.50, color: "#f1d801"],
                    [value: 2.00, color: "#d04e00"],
                    [value: 3.00, color: "#bc2323"]
                ]
        }
        valueTile("hourlyrainin", "device.hourlyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Rain Hr\n${currentValue} in', 
                backgroundColors:[ 
                    [value: 0,    color: "#ffffff"],
                    [value: 0.25, color: "#153591"],
                    [value: 0.50, color: "#1e9cbb"],
                    [value: 0.75, color: "#90d2a7"],
                    [value: 1.00, color: "#44b621"],
                    [value: 1.50, color: "#f1d801"],
                    [value: 2.00, color: "#d04e00"],
                    [value: 3.00, color: "#bc2323"]
                ]
        }
        valueTile("dailyrainin", "device.dailyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Rain Daily\n${currentValue} in', 
                backgroundColors:[ 
                    [value: 0,    color: "#ffffff"],
                    [value: 0.25, color: "#153591"],
                    [value: 0.50, color: "#1e9cbb"],
                    [value: 0.75, color: "#90d2a7"],
                    [value: 1.00, color: "#44b621"],
                    [value: 1.50, color: "#f1d801"],
                    [value: 2.00, color: "#d04e00"],
                    [value: 3.00, color: "#bc2323"]
                ]
        }
        valueTile("weeklyrainin", "device.weeklyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Rain Weekly\n${currentValue} in', 
                backgroundColors:[ 
                    [value: 0,    color: "#ffffff"],
                    [value: 0.25, color: "#153591"],
                    [value: 0.50, color: "#1e9cbb"],
                    [value: 0.75, color: "#90d2a7"],
                    [value: 1.00, color: "#44b621"],
                    [value: 1.50, color: "#f1d801"],
                    [value: 2.00, color: "#d04e00"],
                    [value: 3.00, color: "#bc2323"]
                ]
        }
        valueTile("monthlyrainin", "device.monthlyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Rain Monthly\n${currentValue} in'
        }
        valueTile("totalrainin", "device.totalrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Rain Total\n${currentValue} in'
        }
        valueTile("lastRain", "device.lastRain", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "lastRain", label:'Rain Last Date\n${currentValue}'
        }
        valueTile("lastRainDuration", "device.lastRainDuration", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "lastRainDuration", label:'Time Since Last Rain\n${currentValue}'
        }
        valueTile("uv", "device.uv", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "uv", label: 'UV Index\n${currentValue}'
        }
        standardTile("water", "device.water", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "wet", label: 'Rain', backgroundColor:"#77ec20", icon: "st.Weather.weather10"
            state "dry", label: 'No Rain', icon: "st.Weather.weather12"
        }
        standardTile("motion", "device.motion", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "active",   label: 'Wind',   backgroundColor:"#00ff00", icon:"st.motion.motion.active"
            state "inactive", label: 'No Wind', icon: "st.Weather.weather3"
        }
        valueTile("dewPoint", "device.dewPoint", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "dewPoint", label:'Dewpoint\n${currentValue}°'
        }
        valueTile("winddir", "device.winddir", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "winddir", label: 'Wind Dir Deg\n${currentValue}º'
        }
        valueTile("winddirection", "device.winddirection", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "winddirection", label: 'Wind Dir Compass ${currentValue}'
        }
        valueTile("windspeedmph", "device.windspeedmph", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "windspeedmph", label: 'Wind Speed\n${currentValue} mph', 
                backgroundColors:[ 
                    [value: 0,    color: "#ffffff"],
                    [value: 5, color: "#153591"],
                    [value: 10, color: "#1e9cbb"],
                    [value: 15, color: "#90d2a7"],
                    [value: 20, color: "#44b621"],
                    [value: 25, color: "#f1d801"],
                    [value: 30, color: "#d04e00"],
                    [value: 50, color: "#bc2323"]
                ]
        }
        valueTile("windgustmph", "device.windgustmph", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "windgustmph", label: 'Wind Gust\n${currentValue} mph', 
                backgroundColors:[ 
                    [value: 0,    color: "#ffffff"],
                    [value: 5, color: "#153591"],
                    [value: 10, color: "#1e9cbb"],
                    [value: 15, color: "#90d2a7"],
                    [value: 20, color: "#44b621"],
                    [value: 25, color: "#f1d801"],
                    [value: 30, color: "#d04e00"],
                    [value: 50, color: "#bc2323"]
                ]
        }
        valueTile("maxdailygust", "device.maxdailygust", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "maxdailygust", label: 'Wind Daily Gust\n${currentValue} mph', 
                backgroundColors:[ 
                    [value: 0,    color: "#ffffff"],
                    [value: 5, color: "#153591"],
                    [value: 10, color: "#1e9cbb"],
                    [value: 15, color: "#90d2a7"],
                    [value: 20, color: "#44b621"],
                    [value: 25, color: "#f1d801"],
                    [value: 30, color: "#d04e00"],
                    [value: 50, color: "#bc2323"]
                ]
        }
        valueTile("macAddress", "device.macAddress", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "macAddress", label: 'macAddress\n ${currentValue}'
        }
        valueTile("scheduleFreqMin", "device.scheduleFreqMin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "scheduleFreqMin", label: 'Run Every\n${currentValue} mins',
                backgroundColors:[ 
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
        }
        valueTile("lastSTupdate", "device.lastSTupdate", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state("default", label: 'Last Updated\n ${currentValue}')
        }
        standardTile("refresh", "device.weather", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
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
                "baromrelin", 
                "baromabsin", 
                "humidity" , 
                "dewPoint", 
                "solarradiation", 
                "uv", 
                "winddir",
                "winddirection",
                "windspeedmph", 
                "motion",
                "windgustmph",
                "eventrainin", 
                "water", 
                "hourlyrainin", 
                "dailyrainin", 
                "weeklyrainin", 
                "monthlyrainin", 
                "totalrainin",
                "lastRain",
                "lastRainDuration",
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

// handle commands
def refresh() {
    log.info "Ambient Weather STATION: Executing 'Refresh'"

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
        def now = new Date().format('EEE MMM d, h:mm:ss a',location.timeZone)
        def currentDT = new Date()
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

        state.ambientMap.lastData[0].each{ k, v -> 
            if(k=='dateutc' || k=='date'){return}
            if(k=='lastRain'){v=Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", v).format('EEE MMM d, h:mm a',location.timeZone)}
            if(k=='tempf'){k='temperature'}
            if(debugVerbose){log.debug "sendEvent(name: ${k}, value: ${v})"}
            sendEvent(name: k, value: v)
        }
        state.ambientMap.info[0].each{ k, v -> 
            if(debugVerbose){log.debug "sendEvent(name: ${k}, value: '${v})'"}
            sendEvent(name: k, value: v)
        } 
    } else {
        if(debugVerbose){log.info "getAmbientStationData() did not return any weather data"}
    }
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
    def arr = ["N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"]
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
