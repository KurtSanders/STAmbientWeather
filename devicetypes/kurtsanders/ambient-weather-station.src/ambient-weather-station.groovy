/**
*  Copyright 2018, 2019 SanderSoft
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
*/

String getAppImg(imgName)   	{ return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/$imgName" }
String getMoonIcon(imgNumber)   { return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/moon-phase-symbol-${imgNumber}.png" }

metadata {
    definition (name: "Ambient Weather Station", namespace: "kurtsanders", author: "kurt@kurtsanders.com") {
        capability "Illuminance Measurement"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Refresh"
        capability "Water Sensor"
        capability "Ultraviolet Index"
        capability "Battery"

        // Wind Motion Detection
        capability "Motion Sensor"
        // Wind Speed Psuedo Capability
        capability "Power Meter"
        capability "Energy Meter"

        // Start of Ambient Weather API Rest MAP
        attribute "baromabsin", "string"
        attribute "baromrelin", "string"
        attribute "city", "string"
        attribute "dailyrainin", "string"
        attribute "date", "string"
        attribute "dateutc", "string"
        attribute "dewPoint", "string"
        attribute "dewpoint", "string"
        attribute "eventrainin", "string"
        attribute "feelsLike", "string"
        attribute "feelslike", "string"
        attribute "hourlyrainin", "string"
        attribute "humidity", "string"
        attribute "humidityin", "string"
        attribute "lastRain", "string"
        attribute "location", "string"
        attribute "lastRainDuration", "string"
        attribute "macAddress", "string"
        attribute "maxdailygust", "string"
        attribute "monthlyrainin", "string"
        attribute "pwsName", "string"
        attribute "solarradiation", "string"
        attribute "temperature", "string"
        attribute "tempinf", "string"
        attribute "totalrainin", "string"
        attribute "weeklyrainin", "string"
        attribute "winddir", "string"
        attribute "winddirection", "string"
        attribute "windgustmph", "string"
        attribute "windspeedmph", "string"
        attribute "ultravioletIndexDisplay", "string"
        // End of Ambient Weather API Rest MAP

        // Weather Forecast & Misc attributes
        attribute "moonAge", "number"
        attribute "rainForecast", "string"
        attribute "windPhrase", "string"
        attribute "lastSTupdate", "string"
        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "weatherIcon", "string"
        attribute "secondaryControl", "string"
        attribute "forecastIcon", "string"
        attribute "scheduleFreqMin", "string"
        attribute "sunriseDate", "string"
        attribute "sunsetDate", "string"
        attribute "alertDescription", "string"
        attribute "alertMessage", "string"
        attribute "version", "string"
        attribute "date", "string"

        command "refresh"
    }
    tiles(scale: 2) {
        multiAttributeTile(name:"temperature", type:"generic", width:6, height:4, canChangeIcon: false) {
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
            tileAttribute("device.secondaryControl", key: "SECONDARY_CONTROL") {
                attributeState("secondaryControl", label:'${currentValue}')
            }
        }
    }
    standardTile("weatherIcon", "device.weatherIcon", decoration: "flat", height: 2, width: 2) {
        state "00", icon:"https://smartthings-twc-icons.s3.amazonaws.com/00.png", label: ""
        state "01", icon:"https://smartthings-twc-icons.s3.amazonaws.com/01.png", label: ""
        state "02", icon:"https://smartthings-twc-icons.s3.amazonaws.com/02.png", label: ""
        state "03", icon:"https://smartthings-twc-icons.s3.amazonaws.com/03.png", label: ""
        state "04", icon:"https://smartthings-twc-icons.s3.amazonaws.com/04.png", label: ""
        state "05", icon:"https://smartthings-twc-icons.s3.amazonaws.com/05.png", label: ""
        state "06", icon:"https://smartthings-twc-icons.s3.amazonaws.com/06.png", label: ""
        state "07", icon:"https://smartthings-twc-icons.s3.amazonaws.com/07.png", label: ""
        state "08", icon:"https://smartthings-twc-icons.s3.amazonaws.com/08.png", label: ""
        state "09", icon:"https://smartthings-twc-icons.s3.amazonaws.com/09.png", label: ""
        state "10", icon:"https://smartthings-twc-icons.s3.amazonaws.com/10.png", label: ""
        state "11", icon:"https://smartthings-twc-icons.s3.amazonaws.com/11.png", label: ""
        state "12", icon:"https://smartthings-twc-icons.s3.amazonaws.com/12.png", label: ""
        state "13", icon:"https://smartthings-twc-icons.s3.amazonaws.com/13.png", label: ""
        state "14", icon:"https://smartthings-twc-icons.s3.amazonaws.com/14.png", label: ""
        state "15", icon:"https://smartthings-twc-icons.s3.amazonaws.com/15.png", label: ""
        state "16", icon:"https://smartthings-twc-icons.s3.amazonaws.com/16.png", label: ""
        state "17", icon:"https://smartthings-twc-icons.s3.amazonaws.com/17.png", label: ""
        state "18", icon:"https://smartthings-twc-icons.s3.amazonaws.com/18.png", label: ""
        state "19", icon:"https://smartthings-twc-icons.s3.amazonaws.com/19.png", label: ""
        state "20", icon:"https://smartthings-twc-icons.s3.amazonaws.com/20.png", label: ""
        state "21", icon:"https://smartthings-twc-icons.s3.amazonaws.com/21.png", label: ""
        state "22", icon:"https://smartthings-twc-icons.s3.amazonaws.com/22.png", label: ""
        state "23", icon:"https://smartthings-twc-icons.s3.amazonaws.com/23.png", label: ""
        state "24", icon:"https://smartthings-twc-icons.s3.amazonaws.com/24.png", label: ""
        state "25", icon:"https://smartthings-twc-icons.s3.amazonaws.com/25.png", label: ""
        state "26", icon:"https://smartthings-twc-icons.s3.amazonaws.com/26.png", label: ""
        state "27", icon:"https://smartthings-twc-icons.s3.amazonaws.com/27.png", label: ""
        state "28", icon:"https://smartthings-twc-icons.s3.amazonaws.com/28.png", label: ""
        state "29", icon:"https://smartthings-twc-icons.s3.amazonaws.com/29.png", label: ""
        state "30", icon:"https://smartthings-twc-icons.s3.amazonaws.com/30.png", label: ""
        state "31", icon:"https://smartthings-twc-icons.s3.amazonaws.com/31.png", label: ""
        state "32", icon:"https://smartthings-twc-icons.s3.amazonaws.com/32.png", label: ""
        state "33", icon:"https://smartthings-twc-icons.s3.amazonaws.com/33.png", label: ""
        state "34", icon:"https://smartthings-twc-icons.s3.amazonaws.com/34.png", label: ""
        state "35", icon:"https://smartthings-twc-icons.s3.amazonaws.com/35.png", label: ""
        state "36", icon:"https://smartthings-twc-icons.s3.amazonaws.com/36.png", label: ""
        state "37", icon:"https://smartthings-twc-icons.s3.amazonaws.com/37.png", label: ""
        state "38", icon:"https://smartthings-twc-icons.s3.amazonaws.com/38.png", label: ""
        state "39", icon:"https://smartthings-twc-icons.s3.amazonaws.com/39.png", label: ""
        state "40", icon:"https://smartthings-twc-icons.s3.amazonaws.com/40.png", label: ""
        state "41", icon:"https://smartthings-twc-icons.s3.amazonaws.com/41.png", label: ""
        state "42", icon:"https://smartthings-twc-icons.s3.amazonaws.com/42.png", label: ""
        state "43", icon:"https://smartthings-twc-icons.s3.amazonaws.com/43.png", label: ""
        state "44", icon:"https://smartthings-twc-icons.s3.amazonaws.com/44.png", label: ""
        state "45", icon:"https://smartthings-twc-icons.s3.amazonaws.com/45.png", label: ""
        state "46", icon:"https://smartthings-twc-icons.s3.amazonaws.com/46.png", label: ""
        state "47", icon:"https://smartthings-twc-icons.s3.amazonaws.com/47.png", label: ""
        state "na", icon:"https://smartthings-twc-icons.s3.amazonaws.com/na.png", label: ""
    }
    standardTile("moonAge", "device.moonAge",  width: 2, height: 2, decoration: "flat", wordWrap: true) {
        state "default",    label: 'Age of Moon: ${currentValue}'
        state "0",        	label: '', icon: getMoonIcon('0')
        state "1",        	label: '', icon: getMoonIcon('1')
        state "2",        	label: '', icon: getMoonIcon('2')
        state "3",        	label: '', icon: getMoonIcon('3')
        state "4",        	label: '', icon: getMoonIcon('4')
        state "5",        	label: '', icon: getMoonIcon('5')
        state "6",        	label: '', icon: getMoonIcon('6')
        state "7",        	label: '', icon: getMoonIcon('7')
        state "8",        	label: '', icon: getMoonIcon('8')
        state "9",        	label: '', icon: getMoonIcon('9')
        state "10",        	label: '', icon: getMoonIcon('10')
        state "11",        	label: '', icon: getMoonIcon('11')
        state "12",        	label: '', icon: getMoonIcon('12')
        state "13",        	label: '', icon: getMoonIcon('13')
        state "14",        	label: '', icon: getMoonIcon('14')
        state "15",        	label: '', icon: getMoonIcon('15')
        state "16",        	label: '', icon: getMoonIcon('16')
        state "17",        	label: '', icon: getMoonIcon('17')
        state "18",        	label: '', icon: getMoonIcon('18')
        state "19",        	label: '', icon: getMoonIcon('19')
        state "20",        	label: '', icon: getMoonIcon('20')
        state "21",        	label: '', icon: getMoonIcon('21')
        state "22",        	label: '', icon: getMoonIcon('22')
        state "23",        	label: '', icon: getMoonIcon('23')
        state "24",        	label: '', icon: getMoonIcon('24')
        state "25",        	label: '', icon: getMoonIcon('25')
        state "26",        	label: '', icon: getMoonIcon('26')
        state "27",        	label: '', icon: getMoonIcon('27')
        state "28",        	label: '', icon: getMoonIcon('28')
        state "29",        	label: '', icon: getMoonIcon('29')
    }
    valueTile("tempinf", "device.tempinf", width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Inside Temp\n${currentValue}°'
    }
    valueTile("humidityin", "device.humidityin", width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Inside Humidity\n${currentValue}%'
    }
    valueTile("feelsLike", "device.feelsLike", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Feels Like\n${currentValue}º'
    }
    valueTile("rise", "device.localSunrise", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Sunrise\n ${currentValue}'
    }
    valueTile("set", "device.localSunset", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Sunset\n ${currentValue}'
    }
    valueTile("feelslike", "device.feelslike",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Feels Like\n${currentValue}º'
    }
    valueTile("baromrelin", "device.baromrelin", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rel Pres \n${currentValue} in '
    }
    valueTile("baromabsin", "device.baromabsin",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Abs Pres\n${currentValue} in'
    }
    valueTile("location", "device.location", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'PWS Location\n${currentValue}'
    }
    valueTile("pwsName", "device.pwsName",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'PWS Name\n${currentValue}'
    }
    valueTile("moonPhase", "device.moonPhase",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'${currentValue}'
    }
    valueTile("humidity", "device.humidity",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Humidity\n${currentValue}%'
    }
    valueTile("eventrainin", "device.eventrainin", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain/Event\n${currentValue} in/hr'
    }
    valueTile("hourlyrainin", "device.hourlyrainin",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'RainFall/Hour\n${currentValue} in'
    }
    valueTile("dailyrainin", "device.dailyrainin",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Today\n${currentValue} in'
    }
    valueTile("weeklyrainin", "device.weeklyrainin",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain/Week\n${currentValue} in'
    }
    valueTile("monthlyrainin", "device.monthlyrainin",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain/Month\n${currentValue} in'
    }
    valueTile("totalrainin", "device.totalrainin", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Total\n${currentValue} in'
    }
    valueTile("lastRain", "device.lastRain", width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Last Rain Date\n${currentValue}'
    }
    valueTile("lastRainDuration", "device.lastRainDuration", width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Duration Since Last Rain\n${currentValue}'
    }
    valueTile("ultravioletIndex", "device.ultravioletIndex", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'UVI\n${currentValue}'
    }
    valueTile("ultravioletIndexDisplay", "device.ultravioletIndexDisplay", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'UVI Risk\n${currentValue}'
    }
    valueTile("solarradiation", "device.solarradiation",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Light\n${currentValue}'
    }
    valueTile("illuminance", "device.illuminance",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: '${currentValue}'
    }
    standardTile("water", "device.water",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Rain Detection', icon: getAppImg('na.png')
        state "wet",     label: '', icon: getAppImg('wi-rain.png')
        state "dry",     label: 'No Rain', icon: "st.Weather.weather12"
    }
    standardTile("motion", "device.motion", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Detection', icon: getAppImg('na.png')
        state "active",   label: '',    icon: getAppImg('wi-windy.png')
        state "inactive", label: 'No Wind', icon: "st.Weather.weather3"
    }
    valueTile("dewPoint", "device.dewPoint", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Dew point\n${currentValue}°'
    }
    valueTile("dewpoint", "device.dewpoint", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Dew point\n${currentValue}°'
    }
    valueTile("winddir", "device.winddir",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Direction\n${currentValue}º'
    }
    valueTile("winddir2", "device.winddir2",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Direction\n${currentValue}'
    }
    standardTile("winddirection", "device.winddirection",  width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Direction', icon: getAppImg('na.png')
        state "N",        	label: '', icon: getAppImg('wi-direction-up.png')
        state "North NE", 	label: '', icon: getAppImg('wi-direction-up.png')
        state "NE",   		label: '', icon: getAppImg('wi-direction-up-left.png')
        state "East NE",   	label: '', icon: getAppImg('wi-direction-up-left.png')
        state "E",          label: '', icon: getAppImg('wi-direction-right.png')
        state "East SE",    label: '', icon: getAppImg('wi-direction-right.png')
        state "SE",         label: '', icon: getAppImg('wi-direction-down-right.png')
        state "South SE",   label: '', icon: getAppImg('wi-direction-down-right.png')
        state "S",          label: '', icon: getAppImg('wi-direction-down.png')
        state "South SW",   label: '', icon: getAppImg('wi-direction-down.png')
        state "SW",         label: '', icon: getAppImg('wi-direction-down-left.png')
        state "West SW",    label: '', icon: getAppImg('wi-direction-down-left.png')
        state "W",          label: '', icon: getAppImg('wi-direction-left.png')
        state "West NW",    label: '', icon: getAppImg('wi-direction-left.png')
        state "NW",         label: '', icon: getAppImg('wi-direction-up-left.png')
        state "North NW",   label: '', icon: getAppImg('wi-direction-up-left.png')
    }
    valueTile("windspeedmph", "device.windspeedmph", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Speed\n${currentValue} mph'
    }
    valueTile("windgustmph", "device.windgustmph", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Gust\n${currentValue} mph'
    }
    valueTile("maxdailygust", "device.maxdailygust", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Daily Gust\n${currentValue} mph'
    }
    valueTile("macAddress", "device.macAddress", width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'macAddress\n ${currentValue}'
    }
    valueTile("rainForecast", "device.rainForecast", width: 1, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: '${currentValue}'
    }
    valueTile("windPhrase", "device.windPhrase",  width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: '${currentValue}'
    }
    valueTile("scheduleFreqMin", "device.scheduleFreqMin", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Refresh Cycle\n${currentValue} mins', action: "refresh"
    }
    valueTile("lastSTupdate", "device.lastSTupdate", width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: '${currentValue}', action: "refresh"
    }
    standardTile("refresh", "device.weather", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: "", action: "refresh", icon:"st.secondary.refresh"
    }
    standardTile("battery", "device.battery", width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", 	label: '', icon: getAppImg('battery-na.png')
        state "100", 		label: '', icon: getAppImg('battery-good.png')
        state "0", 			label: '', icon: getAppImg('battery-bad.png')
    }
    valueTile("date", "device.date", width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state("default", label: 'Ambient Server DateTime\n${currentValue}')
    }
    valueTile("weather", "device.weather",  width: 6, height: 3, decoration: "flat", wordWrap: true) {
        state "default", label:'${currentValue}'
    }
    valueTile("alertDescription", "device.alertDescription", width: 6, height: 6, decoration: "flat", wordWrap: true) {
        state "default", label:'${currentValue}'
    }
    valueTile("alertMessage", "device.alertMessage", width: 6, height: 2, decoration: "flat", wordWrap: true) {
        state "default", label:'${currentValue}'
    }

    main(["temperature"])
    details(
        [
            // Inside Sensors
            "temperature",
            "tempinf",
            "humidityin" ,
            // Outside Sensors
            "weatherIcon",
            "feelsLike",
            "water",
            "eventrainin",
            "hourlyrainin",
            "dailyrainin",
            "weeklyrainin",
            "monthlyrainin",
            "lastRain",
            "lastRainDuration",
            "solarradiation",
            "totalrainin",
            "winddirection",
            "windspeedmph",
            "motion",
            "winddir2",
            "windgustmph",
            "dewPoint",
            "baromrelin",
            "baromabsin",
            "humidity",
            "ultravioletIndexDisplay",
            "rise",
            "set",
            "moonAge",
            "moonPhase",
            "location",
            "rainForecast",
            "windPhrase",
            "battery",
            "date",
            "lastSTupdate",
            "scheduleFreqMin",
            "weather",
            "alertMessage",
            "alertDescription"
        ]
    )
}

def initialize() {
    def naStndardFields = [
        "humidityin" ,
        "feelsLike",
        "water",
        "eventrainin",
        "hourlyrainin",
        "dailyrainin",
        "weeklyrainin",
        "monthlyrainin",
        "solarradiation",
        "lastRain",
        "lastRainDuration",
        "totalrainin",
        "winddirection",
        "windspeedmph",
        "motion",
        "winddir2",
        "windgustmph",
        "dewPoint",
        "baromrelin",
        "baromabsin",
        "humidity",
        "ultravioletIndexDisplay",
        "windPhrase",
        "battery"
    ]
    naStndardFields.each {
        log.debug "Updated Missing Weather Field: '${it}' to 'N/A'"
        sendEvent(name: it, value: "N/A")
    }
}

def installed() {
    initialize()
}

def updated() {
}

def refresh() {
    parent.refresh()
}
