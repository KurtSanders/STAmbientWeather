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
*  SmartWeather Station Tile
*  Copyright 2018, 2019, 2020 SanderSoft
*  Author: Kurt Sanders, SanderSoft™
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License. You may obtain a copy of the License at:
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.*
*
*  Version 	: 5.00
*  Date		: 5-14-2020
*/

metadata {
    definition (
        name		: "SmartWeather Station Tile",
        namespace	: "kurtsanders",
        author		: "kurt@kurtsanders.com"
    )
    {
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Refresh"

        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "city", "string"
        attribute "location", "string"
        attribute "wind", "string"
        attribute "windVector", "string"
        attribute "weatherIcon", "string"
        attribute "feelsLike", "string"
        attribute "sunriseDate", "string"
        attribute "sunsetDate", "string"
        attribute "percentPrecip", "string"
        attribute "lastUpdate", "string"
        attribute "weather", "string"
        attribute "forecastIcon", "string"

    }
    tiles(scale: 2) {
        multiAttributeTile(name:"temperature", type:"generic", width:6, height:4, canChangeIcon: true ) {
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
                attributeState("feelsLike", label:'Feels like ${currentValue}°')
            }
        }

        valueTile("feelsLike", "device.feelsLike", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Feels like ${currentValue}°'
        }

        standardTile("weatherIcon", "device.weatherIcon", decoration: "flat", height: 2, width: 2) {
            state "00", icon:"https://smartthings-twc-icons.s3.amazonaws.com/00.png", label: "Current"
            state "01", icon:"https://smartthings-twc-icons.s3.amazonaws.com/01.png", label: "Current"
            state "02", icon:"https://smartthings-twc-icons.s3.amazonaws.com/02.png", label: "Current"
            state "03", icon:"https://smartthings-twc-icons.s3.amazonaws.com/03.png", label: "Current"
            state "04", icon:"https://smartthings-twc-icons.s3.amazonaws.com/04.png", label: "Current"
            state "05", icon:"https://smartthings-twc-icons.s3.amazonaws.com/05.png", label: "Current"
            state "06", icon:"https://smartthings-twc-icons.s3.amazonaws.com/06.png", label: "Current"
            state "07", icon:"https://smartthings-twc-icons.s3.amazonaws.com/07.png", label: "Current"
            state "08", icon:"https://smartthings-twc-icons.s3.amazonaws.com/08.png", label: "Current"
            state "09", icon:"https://smartthings-twc-icons.s3.amazonaws.com/09.png", label: "Current"
            state "10", icon:"https://smartthings-twc-icons.s3.amazonaws.com/10.png", label: "Current"
            state "11", icon:"https://smartthings-twc-icons.s3.amazonaws.com/11.png", label: "Current"
            state "12", icon:"https://smartthings-twc-icons.s3.amazonaws.com/12.png", label: "Current"
            state "13", icon:"https://smartthings-twc-icons.s3.amazonaws.com/13.png", label: "Current"
            state "14", icon:"https://smartthings-twc-icons.s3.amazonaws.com/14.png", label: "Current"
            state "15", icon:"https://smartthings-twc-icons.s3.amazonaws.com/15.png", label: "Current"
            state "16", icon:"https://smartthings-twc-icons.s3.amazonaws.com/16.png", label: "Current"
            state "17", icon:"https://smartthings-twc-icons.s3.amazonaws.com/17.png", label: "Current"
            state "18", icon:"https://smartthings-twc-icons.s3.amazonaws.com/18.png", label: "Current"
            state "19", icon:"https://smartthings-twc-icons.s3.amazonaws.com/19.png", label: "Current"
            state "20", icon:"https://smartthings-twc-icons.s3.amazonaws.com/20.png", label: "Current"
            state "21", icon:"https://smartthings-twc-icons.s3.amazonaws.com/21.png", label: "Current"
            state "22", icon:"https://smartthings-twc-icons.s3.amazonaws.com/22.png", label: "Current"
            state "23", icon:"https://smartthings-twc-icons.s3.amazonaws.com/23.png", label: "Current"
            state "24", icon:"https://smartthings-twc-icons.s3.amazonaws.com/24.png", label: "Current"
            state "25", icon:"https://smartthings-twc-icons.s3.amazonaws.com/25.png", label: "Current"
            state "26", icon:"https://smartthings-twc-icons.s3.amazonaws.com/26.png", label: "Current"
            state "27", icon:"https://smartthings-twc-icons.s3.amazonaws.com/27.png", label: "Current"
            state "28", icon:"https://smartthings-twc-icons.s3.amazonaws.com/28.png", label: "Current"
            state "29", icon:"https://smartthings-twc-icons.s3.amazonaws.com/29.png", label: "Current"
            state "30", icon:"https://smartthings-twc-icons.s3.amazonaws.com/30.png", label: "Current"
            state "31", icon:"https://smartthings-twc-icons.s3.amazonaws.com/31.png", label: "Current"
            state "32", icon:"https://smartthings-twc-icons.s3.amazonaws.com/32.png", label: "Current"
            state "33", icon:"https://smartthings-twc-icons.s3.amazonaws.com/33.png", label: "Current"
            state "34", icon:"https://smartthings-twc-icons.s3.amazonaws.com/34.png", label: "Current"
            state "35", icon:"https://smartthings-twc-icons.s3.amazonaws.com/35.png", label: "Current"
            state "36", icon:"https://smartthings-twc-icons.s3.amazonaws.com/36.png", label: "Current"
            state "37", icon:"https://smartthings-twc-icons.s3.amazonaws.com/37.png", label: "Current"
            state "38", icon:"https://smartthings-twc-icons.s3.amazonaws.com/38.png", label: "Current"
            state "39", icon:"https://smartthings-twc-icons.s3.amazonaws.com/39.png", label: "Current"
            state "40", icon:"https://smartthings-twc-icons.s3.amazonaws.com/40.png", label: "Current"
            state "41", icon:"https://smartthings-twc-icons.s3.amazonaws.com/41.png", label: "Current"
            state "42", icon:"https://smartthings-twc-icons.s3.amazonaws.com/42.png", label: "Current"
            state "43", icon:"https://smartthings-twc-icons.s3.amazonaws.com/43.png", label: "Current"
            state "44", icon:"https://smartthings-twc-icons.s3.amazonaws.com/44.png", label: "Current"
            state "45", icon:"https://smartthings-twc-icons.s3.amazonaws.com/45.png", label: "Current"
            state "46", icon:"https://smartthings-twc-icons.s3.amazonaws.com/46.png", label: "Current"
            state "47", icon:"https://smartthings-twc-icons.s3.amazonaws.com/47.png", label: "Current"
            state "na", icon:"https://smartthings-twc-icons.s3.amazonaws.com/na.png", label: "Current"
        }

        standardTile("forecastIcon", "device.forecastIcon", decoration: "flat", height: 2, width: 2) {
            state "00", icon:"https://smartthings-twc-icons.s3.amazonaws.com/00.png", label: "Forecast"
            state "01", icon:"https://smartthings-twc-icons.s3.amazonaws.com/01.png", label: "Forecast"
            state "02", icon:"https://smartthings-twc-icons.s3.amazonaws.com/02.png", label: "Forecast"
            state "03", icon:"https://smartthings-twc-icons.s3.amazonaws.com/03.png", label: "Forecast"
            state "04", icon:"https://smartthings-twc-icons.s3.amazonaws.com/04.png", label: "Forecast"
            state "05", icon:"https://smartthings-twc-icons.s3.amazonaws.com/05.png", label: "Forecast"
            state "06", icon:"https://smartthings-twc-icons.s3.amazonaws.com/06.png", label: "Forecast"
            state "07", icon:"https://smartthings-twc-icons.s3.amazonaws.com/07.png", label: "Forecast"
            state "08", icon:"https://smartthings-twc-icons.s3.amazonaws.com/08.png", label: "Forecast"
            state "09", icon:"https://smartthings-twc-icons.s3.amazonaws.com/09.png", label: "Forecast"
            state "10", icon:"https://smartthings-twc-icons.s3.amazonaws.com/10.png", label: "Forecast"
            state "11", icon:"https://smartthings-twc-icons.s3.amazonaws.com/11.png", label: "Forecast"
            state "12", icon:"https://smartthings-twc-icons.s3.amazonaws.com/12.png", label: "Forecast"
            state "13", icon:"https://smartthings-twc-icons.s3.amazonaws.com/13.png", label: "Forecast"
            state "14", icon:"https://smartthings-twc-icons.s3.amazonaws.com/14.png", label: "Forecast"
            state "15", icon:"https://smartthings-twc-icons.s3.amazonaws.com/15.png", label: "Forecast"
            state "16", icon:"https://smartthings-twc-icons.s3.amazonaws.com/16.png", label: "Forecast"
            state "17", icon:"https://smartthings-twc-icons.s3.amazonaws.com/17.png", label: "Forecast"
            state "18", icon:"https://smartthings-twc-icons.s3.amazonaws.com/18.png", label: "Forecast"
            state "19", icon:"https://smartthings-twc-icons.s3.amazonaws.com/19.png", label: "Forecast"
            state "20", icon:"https://smartthings-twc-icons.s3.amazonaws.com/20.png", label: "Forecast"
            state "21", icon:"https://smartthings-twc-icons.s3.amazonaws.com/21.png", label: "Forecast"
            state "22", icon:"https://smartthings-twc-icons.s3.amazonaws.com/22.png", label: "Forecast"
            state "23", icon:"https://smartthings-twc-icons.s3.amazonaws.com/23.png", label: "Forecast"
            state "24", icon:"https://smartthings-twc-icons.s3.amazonaws.com/24.png", label: "Forecast"
            state "25", icon:"https://smartthings-twc-icons.s3.amazonaws.com/25.png", label: "Forecast"
            state "26", icon:"https://smartthings-twc-icons.s3.amazonaws.com/26.png", label: "Forecast"
            state "27", icon:"https://smartthings-twc-icons.s3.amazonaws.com/27.png", label: "Forecast"
            state "28", icon:"https://smartthings-twc-icons.s3.amazonaws.com/28.png", label: "Forecast"
            state "29", icon:"https://smartthings-twc-icons.s3.amazonaws.com/29.png", label: "Forecast"
            state "30", icon:"https://smartthings-twc-icons.s3.amazonaws.com/30.png", label: "Forecast"
            state "31", icon:"https://smartthings-twc-icons.s3.amazonaws.com/31.png", label: "Forecast"
            state "32", icon:"https://smartthings-twc-icons.s3.amazonaws.com/32.png", label: "Forecast"
            state "33", icon:"https://smartthings-twc-icons.s3.amazonaws.com/33.png", label: "Forecast"
            state "34", icon:"https://smartthings-twc-icons.s3.amazonaws.com/34.png", label: "Forecast"
            state "35", icon:"https://smartthings-twc-icons.s3.amazonaws.com/35.png", label: "Forecast"
            state "36", icon:"https://smartthings-twc-icons.s3.amazonaws.com/36.png", label: "Forecast"
            state "37", icon:"https://smartthings-twc-icons.s3.amazonaws.com/37.png", label: "Forecast"
            state "38", icon:"https://smartthings-twc-icons.s3.amazonaws.com/38.png", label: "Forecast"
            state "39", icon:"https://smartthings-twc-icons.s3.amazonaws.com/39.png", label: "Forecast"
            state "40", icon:"https://smartthings-twc-icons.s3.amazonaws.com/40.png", label: "Forecast"
            state "41", icon:"https://smartthings-twc-icons.s3.amazonaws.com/41.png", label: "Forecast"
            state "42", icon:"https://smartthings-twc-icons.s3.amazonaws.com/42.png", label: "Forecast"
            state "43", icon:"https://smartthings-twc-icons.s3.amazonaws.com/43.png", label: "Forecast"
            state "44", icon:"https://smartthings-twc-icons.s3.amazonaws.com/44.png", label: "Forecast"
            state "45", icon:"https://smartthings-twc-icons.s3.amazonaws.com/45.png", label: "Forecast"
            state "46", icon:"https://smartthings-twc-icons.s3.amazonaws.com/46.png", label: "Forecast"
            state "47", icon:"https://smartthings-twc-icons.s3.amazonaws.com/47.png", label: "Forecast"
            state "na", icon:"https://smartthings-twc-icons.s3.amazonaws.com/na.png", label: "Forecast"
        }

        valueTile("weather", "device.weather", decoration: "flat", height: 1, width: 2) {
            state "default", label:'${currentValue}'
        }

        valueTile("humidity", "device.humidity", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Humidity\n${currentValue}%'
        }

        valueTile("wind", "device.windVector", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Wind Vector\n${currentValue}'
        }

        valueTile("windspeed", "device.wind", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Wind Speed\n${currentValue} MPH'
        }

        valueTile("city", "device.city", decoration: "flat", height: 1, width: 3) {
            state "default", label:'PWS Name\n${currentValue}'
        }

        valueTile("percentPrecip", "device.percentPrecip", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Precip\n${currentValue}%'
        }

        standardTile("refresh", "device.weather", decoration: "flat", height: 1, width: 1) {
            state "default", label: "", action: "refresh", icon:"st.secondary.refresh"
        }

        valueTile("rise", "device.localSunrise", decoration: "flat", height: 1, width: 1) {
            state "default", label:'Sunrise\n${currentValue}'
        }

        valueTile("set", "device.localSunset", decoration: "flat", height: 1, width: 1) {
            state "default", label:'Sunset\n${currentValue}'
        }

        valueTile("lastUpdate", "device.lastUpdate", decoration: "flat", height: 1, width: 3, wordWrap: true) {
            state "default", label:'Last update:\n${currentValue}', action: "refresh"
        }
        valueTile("location", "device.location", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'PWS Location\n${currentValue}'
        }
        main(["temperature"])
        details(
            ["temperature",
//             "feelsLike",
             "weatherIcon",
             "weather",
             "humidity",
             "wind",
             "percentPrecip",
             "forecastIcon",
             "windspeed",
             "rise",
             "set",
             "location",
             "refresh",
             "lastUpdate",
             "city"
            ]
        )
    }
}

def installed() {
}

def updated() {
}

def uninstalled() {
}

def refresh() {
    parent.refresh()
}
