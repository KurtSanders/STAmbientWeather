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
*  Date: 2018-11-22
*/
// Start Version Information
def version() {
    return ["V2.0", "Requires Ambient WS Service Manager App V2"]
}
// End Version Information
metadata {
    definition (name: "Ambient Weather Station Android", namespace: "kurtsanders", author: "kurt@kurtsanders.com") {
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
        attribute "humidity", "string"
        attribute "humidityin", "string"
        attribute "lastRain", "string"
        attribute "location", "string"
        attribute "lastRainDuration", "string"
        attribute "macAddress", "string"
        attribute "maxdailygust", "string"
        attribute "monthlyrainin", "string"
        attribute "name", "string"
        attribute "temperature", "string"
        attribute "tempinf", "string"
        attribute "totalrainin", "string"
        attribute "weeklyrainin", "string"
        attribute "winddir", "string"
        attribute "winddirection", "string"
        attribute "windgustmph", "string"
        attribute "windspeedmph", "string"   
        // End of Ambient Weather API Rest MAP
        attribute "moonAge", "number"
        Attribute "lastSTupdate", "string"
        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "weatherIcon", "string"
        attribute "forecastIcon", "string"
        attribute "scheduleFreqMin", "string"
        attribute "sunriseDate", "string"
        attribute "sunsetDate", "string"
        attribute "alertDescription", "string"
        attribute "alertMessage", "string"
        attribute "version", "string"
        
        command "refresh"
    }
    tiles(scale: 2) {
        multiAttributeTile(name:"temperature", type:"generic", width:6, height:4, canChangeIcon: false) {
            tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
                attributeState("default",label:'${currentValue}°',
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
                attributeState("default", label:'Feels Like ${currentValue}°')
                attributeState("0.1", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("0.2", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("0.3", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("0.4", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("0.5", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("0.6", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("0.7", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("0.8", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("0.9", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("1.1", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("1.2", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("1.3", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("1.4", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("1.5", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("1.6", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("1.7", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("1.8", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("1.9", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("2.0", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("2.1", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("2.2", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("2.3", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("2.4", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("2.5", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("2.6", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("2.7", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("2.8", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("2.9", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("3.0", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("3.1", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("3.2", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("3.3", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("3.4", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("3.5", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("3.6", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("3.7", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("3.8", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("3.9", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
                attributeState("4.0", label:'Raining at ${currentValue} in/hr', icon: "st.custom.wuk.chancerain")
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
    valueTile("alertDescription", "device.alertDescription", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'${currentValue}'
    }
    valueTile("alertMessage", "device.alertMessage", inactiveLabel: false, width: 6, height: 3, decoration: "flat", wordWrap: true) {
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
        state "default", label:'Inside Humidity\n${currentValue}%', backgroundColors: [ 
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
        state "default", label:'PWS Name\n${currentValue}'
    }
    valueTile("humidity", "device.humidity", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Humidity\n${currentValue}%', backgroundColors: [ 
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
    }
    valueTile("eventrainin", "device.eventrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Event\n${currentValue} in/hr', 
            backgroundColors:[
            [value: 0,   color: "#ffffff"],
            [value: 1,   color: "#0000ff"],
            [value: 10,  color: "#ff0000"]
	]
    }
    valueTile("hourlyrainin", "device.hourlyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Hr\n${currentValue} in',  backgroundColors: [
            [value: 0,   color: "#ffffff"],
            [value: 1,   color: "#0000ff"],
            [value: 10,  color: "#ff0000"]
	]
    }
    valueTile("dailyrainin", "device.dailyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Daily\n${currentValue} in', backgroundColors: [
            [value: 0,   color: "#ffffff"],
            [value: 1,   color: "#0000ff"],
            [value: 10,  color: "#ff0000"]
	]
    }
    valueTile("weeklyrainin", "device.weeklyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Weekly\n${currentValue} in', backgroundColors: [
            [value: 0,   color: "#ffffff"],
            [value: 1,   color: "#0000ff"],
            [value: 10,  color: "#ff0000"]
	]
    }
    valueTile("monthlyrainin", "device.monthlyrainin", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label:'Rain Monthly\n${currentValue} in'
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
        state "default", label: 'UVI\n${currentValue}', backgroundColors: [        
            [value: 0,    color: "#000000"],
            [value: 12,   color: "#ffffff"]
        ]     }
    valueTile("solarradiation", "device.illuminance", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Light\n${currentValue}', backgroundColors: [        
            [value: 0,    color: "#000000"],
            [value: 550,  color: "#ffffff"]
        ] 
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
    standardTile("moonAge", "device.moonAge", inactiveLabel: false, width: 2, height: 2, decoration: "flat", wordWrap: true) {
        state "default",    label: 'Age of Moon: ${currentValue}'
        state "1",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-1.png" 
        state "2",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-2.png" 
        state "3",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-3.png" 
        state "4",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-4.png" 
        state "5",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-5.png" 
        state "6",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-6.png" 
        state "7",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-7.png" 
        state "8",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-8.png" 
        state "9",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-9.png" 
        state "10",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-10.png" 
        state "11",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-11.png" 
        state "12",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-12.png" 
        state "13",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-13.png" 
        state "14",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-14.png" 
        state "15",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-15.png" 
        state "16",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-16.png" 
        state "17",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-17.png" 
        state "18",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-18.png" 
        state "19",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-19.png" 
        state "20",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-20.png" 
        state "21",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-21.png" 
        state "22",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-22.png" 
        state "23",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-23.png" 
        state "24",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-24.png" 
        state "25",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-25.png" 
        state "26",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-26.png" 
        state "27",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-27.png"
        state "28",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-28.png"
        state "29",        	label: '${currentValue}', icon: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/beta/images/moon-phase-symbol-29.png"
         
    }
    standardTile("winddirection", "device.winddirection", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
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
        state "default", label: 'Wind Speed\n${currentValue} mph', backgroundColors: [
            [value: 0,  color: "#ffffff"],
            [value: 5,  color: "#153591"],
            [value: 10, color: "#1e9cbb"],
            [value: 15, color: "#90d2a7"],
            [value: 20, color: "#44b621"],
            [value: 25, color: "#f1d801"],
            [value: 30, color: "#d04e00"],
            [value: 50, color: "#bc2323"]
	]
    }
    valueTile("windgustmph", "device.windgustmph", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Gust\n${currentValue} mph', backgroundColors: [
            [value: 0,  color: "#ffffff"],
            [value: 5,  color: "#153591"],
            [value: 10, color: "#1e9cbb"],
            [value: 15, color: "#90d2a7"],
            [value: 20, color: "#44b621"],
            [value: 25, color: "#f1d801"],
            [value: 30, color: "#d04e00"],
            [value: 50, color: "#bc2323"]
	]
    }
    valueTile("maxdailygust", "device.maxdailygust", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Wind Daily Gust\n${currentValue} mph', backgroundColors: [
            [value: 0,  color: "#ffffff"],
            [value: 5,  color: "#153591"],
            [value: 10, color: "#1e9cbb"],
            [value: 15, color: "#90d2a7"],
            [value: 20, color: "#44b621"],
            [value: 25, color: "#f1d801"],
            [value: 30, color: "#d04e00"],
            [value: 50, color: "#bc2323"]
	]
    }
    valueTile("macAddress", "device.macAddress", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'macAddress\n ${currentValue}'
    }
    valueTile("scheduleFreqMin", "device.scheduleFreqMin", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: 'Run Every\n${currentValue} mins', backgroundColors: [
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
    }
    valueTile("lastSTupdate", "device.lastSTupdate", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
        state("default", label: '${currentValue}')
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
            "weatherIcon", 
            "water", 
            "eventrainin", 
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
            "humidity", 
            "dewPoint", 
            "solarradiation", 
            "ultravioletIndex", 
            "rise", 
            "set",
            "moonAge",
            "name", 
            "location",
            "scheduleFreqMin",
            "lastSTupdate",
            "weather",
            "alertDescription",
            "refresh",
            "alertMessage"
        ]
    )
}

def initialize() {
    // initialize API's
    log.info "DTH Section: Initialize"
}

def installed() {
    log.info "DTH Section: Installed"
}

def uninstalled() {
    log.info "DTH Section: Uninstalled"
    unschedule()
}

def updated() {
    log.info "DTH Section: Updated"
}

def refresh() {	    
    parent.refresh()
}
