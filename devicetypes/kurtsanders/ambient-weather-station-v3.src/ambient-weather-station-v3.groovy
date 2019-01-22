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
*  Date: 2018-12-30
*/
// Start Version Information
def version() { return ["V3.0", "Requires Ambient WS Service Manager App V3"] }
// End Version Information
String getAppImg(imgName) 		{ return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/$imgName" }

metadata {
    definition (name: "Ambient Weather Station V3", namespace: "kurtsanders", author: "kurt@kurtsanders.com") {
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
        attribute "eventrainin", "string"
        attribute "feelsLike", "string"
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
        attribute "temperature", "string"
        attribute "tempinf", "string"
        attribute "totalrainin", "string"
        attribute "weeklyrainin", "string"
        attribute "winddir", "string"
        attribute "winddirection", "string"
        attribute "windgustmph", "string"
        attribute "windspeedmph", "string"
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
    valueTile("tempinf", "device.tempinf", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Inside Temp\n${currentValue}°', backgroundColors: TileBgColors('tempinf')
    }
    standardTile("weatherIcon", "device.weatherIcon", decoration: "flat", height: 2, width: 2) {
        state "0", icon:"https://smartthings-twc-icons.s3.amazonaws.com/00.png", label: ""
        state "1", icon:"https://smartthings-twc-icons.s3.amazonaws.com/01.png", label: ""
        state "2", icon:"https://smartthings-twc-icons.s3.amazonaws.com/02.png", label: ""
        state "3", icon:"https://smartthings-twc-icons.s3.amazonaws.com/03.png", label: ""
        state "4", icon:"https://smartthings-twc-icons.s3.amazonaws.com/04.png", label: ""
        state "5", icon:"https://smartthings-twc-icons.s3.amazonaws.com/05.png", label: ""
        state "6", icon:"https://smartthings-twc-icons.s3.amazonaws.com/06.png", label: ""
        state "7", icon:"https://smartthings-twc-icons.s3.amazonaws.com/07.png", label: ""
        state "8", icon:"https://smartthings-twc-icons.s3.amazonaws.com/08.png", label: ""
        state "9", icon:"https://smartthings-twc-icons.s3.amazonaws.com/09.png", label: ""
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
    valueTile("alertDescription", "device.alertDescription", inactiveLabel: false, width: 6, height: 6, decoration: "flat", wordWrap: true) {
        state "default", label:'${currentValue}'
    }
    valueTile("alertMessage", "device.alertMessage", inactiveLabel: false, width: 6, height: 2, decoration: "flat", wordWrap: true) {
        state "default", label:'${currentValue}'
    }
    valueTile("weather", "device.weather", inactiveLabel: false, width: 6, height: 3, decoration: "flat", wordWrap: true) {
        state "default", label:'${currentValue}'
    }
    valueTile("rise", "device.localSunrise", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Sunrise\n ${currentValue}'
    }
    valueTile("set", "device.localSunset", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Sunset\n ${currentValue}'
    }
    valueTile("humidityin", "device.humidityin", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Inside Humidity\n${currentValue}%', backgroundColors: TileBgColors('humidity')
    }
    valueTile("feelsLike", "device.feelsLike", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Feels Like\n${currentValue}º'
    }
    valueTile("baromrelin", "device.baromrelin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rel Pres \n${currentValue} in '
    }
    valueTile("baromabsin", "device.baromabsin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Abs Pres\n${currentValue} in'
    }
    valueTile("location", "device.location", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'PWS Location\n${currentValue}'
    }
    valueTile("pwsName", "device.pwsName", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'PWS Name\n${currentValue}'
    }
    valueTile("moonPhase", "device.moonPhase", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'${currentValue}'
    }
    valueTile("humidity", "device.humidity", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Humidity\n${currentValue}%', backgroundColors: TileBgColors('humidity')
    }
    valueTile("eventrainin", "device.eventrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain/Event\n${currentValue} in/hr', backgroundColors: TileBgColors('rain')
    }
    valueTile("hourlyrainin", "device.hourlyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'RainFall/Hour\n${currentValue} in',  backgroundColors: TileBgColors('rain')
    }
    valueTile("dailyrainin", "device.dailyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Today\n${currentValue} in', backgroundColors: TileBgColors('rain')
    }
    valueTile("weeklyrainin", "device.weeklyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain/Week\n${currentValue} in', backgroundColors: TileBgColors('rain')
    }
    valueTile("monthlyrainin", "device.monthlyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain/Month\n${currentValue} in'
    }
    valueTile("totalrainin", "device.totalrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Total\n${currentValue} in'
    }
    valueTile("lastRain", "device.lastRain", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Last Rain Date\n${currentValue}'
    }
    valueTile("lastRainDuration", "device.lastRainDuration", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Duration Since Last Rain\n${currentValue}'
    }
    valueTile("ultravioletIndex", "device.ultravioletIndex", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'UVI\n${currentValue}', backgroundColors: TileBgColors('uvi')
    }
    valueTile("solarradiation", "device.illuminance", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Light\n${currentValue}', backgroundColors: TileBgColors('solar')
    }
    standardTile("water", "device.water", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: ''
        state "wet", label: '', icon: 'https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/wi-rain.png'
        state "dry", label: 'No Rain', icon: "st.Weather.weather12"
    }
    standardTile("motion", "device.motion", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: ''
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
    standardTile("moonAge", "device.moonAge", inactiveLabel: false, width: 2, height: 2, decoration: "flat", wordWrap: true) {
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
    standardTile("winddirection", "device.winddirection", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default",    label: '${currentValue}'
        state "N",        	label: '', icon: getWindIcon('wi-direction-up')
        state "North NE", 	label: '', icon: getWindIcon('wi-direction-up')
        state "NE",   		label: '', icon: getWindIcon('wi-direction-up-left')
        state "East NE",   	label: '', icon: getWindIcon('wi-direction-up-left')
        state "E",          label: '', icon: getWindIcon('wi-direction-right')
        state "East SE",    label: '', icon: getWindIcon('wi-direction-right')
        state "SE",         label: '', icon: getWindIcon('wi-direction-down-right')
        state "South SE",   label: '', icon: getWindIcon('wi-direction-down-right')
        state "S",          label: '', icon: getWindIcon('wi-direction-down')
        state "South SW",   label: '', icon: getWindIcon('wi-direction-down')
        state "SW",         label: '', icon: getWindIcon('wi-direction-down-left')
        state "West SW",    label: '', icon: getWindIcon('wi-direction-down-left')
        state "W",          label: '', icon: getWindIcon('wi-direction-left')
        state "West NW",    label: '', icon: getWindIcon('wi-direction-left')
        state "NW",         label: '', icon: getWindIcon('wi-direction-up-left')
        state "North NW",   label: '', icon: getWindIcon('wi-direction-up-left')
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
    valueTile("macAddress", "device.macAddress", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'macAddress\n ${currentValue}'
    }
    valueTile("rainForecast", "device.rainForecast", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: '${currentValue}'
    }
    valueTile("windPhrase", "device.windPhrase", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: '${currentValue}'
    }
    valueTile("scheduleFreqMin", "device.scheduleFreqMin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Refresh\n${currentValue} mins', action: "refresh", backgroundColors: TileBgColors('scheduleFreqMin')
    }
    valueTile("lastSTupdate", "device.lastSTupdate", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state("default", label: '${currentValue}')
    }
    standardTile("refresh", "device.weather", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
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
            "ultravioletIndex",
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

def installed() {
}

def updated() {
}

def refresh() {
    parent.refresh()
}

def getWindIcon(image) {
    return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/${image}.png"
}

def getMoonIcon(imgNumber) {
    return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/moon-phase-symbol-${imgNumber}.png"
}

def TileBgColors(colorSetName) {
    switch(colorSetName) {
        case 'tempinf':
        return [
            [value: 60, color: "#153591"],
            [value: 65, color: "#1e9cbb"],
            [value: 69, color: "#90d2a7"],
            [value: 71, color: "#44b621"],
            [value: 73, color: "#f1d801"],
            [value: 75, color: "#d04e00"],
            [value: 80, color: "#bc2323"]
        ]
        break
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
            [value: 0,  color: "#ffffff"],
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
            [value: 0,    	color: "#000000"],
            [value: 1,   	color: "#CCCC00"],
            [value: 100000, color: "#FFFF00"]
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
            [value: 0,    color: "#FF0000"],
            [value: 1,    color: "#9400D3"],
            [value: 2,    color: "#00FF00"],
            [value: 3,    color: "#458b74"],
            [value: 4,    color: "#FF7F00"],
            [value: 5,    color: "#4B0082"],
            [value: 10,   color: "#0000FF"],
            [value: 15,   color: "#00FF00"],
            [value: 30,   color: "#FFFF00"],
            [value: 60,   color: "#FF7F00"],
            [value: 180,  color: "#ff69b4"]
        ]    
        break
        case 'default':
        return [                
            [value: '0',  color: "#ffffff"]
        ]
    }
}