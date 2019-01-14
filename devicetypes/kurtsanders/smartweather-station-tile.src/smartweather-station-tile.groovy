/**
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
 *  SmartWeather Station
 *
 *  Author: Kurt Sanders
 *
 *  Date: 2019-01-15
 */
metadata {
    definition (name: "SmartWeather Station Tile", namespace: "kurtsanders", author: "kurt@kurtsanders.com") {
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Refresh"

        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "city", "string"
        attribute "location", "string"
        attribute "wind", "string"
        attribute "weatherIcon", "string"
        attribute "feelsLike", "string"
        attribute "percentPrecip", "string"
        attribute "sunriseDate", "string"
        attribute "sunsetDate", "string"
        attribute "lastUpdate", "string"
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
            tileAttribute("device.feelsLike", key: "SECONDARY_CONTROL") {
                attributeState("feelsLike", label:'Feels like ${currentValue}°')
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

        valueTile("humidity", "device.humidity", decoration: "flat", height: 1, width: 2) {
            state "default", label:'${currentValue}% humidity'
        }

        valueTile("wind", "device.wind", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Wind\n${currentValue}'
        }

        valueTile("city", "device.city", decoration: "flat", height: 1, width: 2) {
            state "default", label:'${currentValue}'
        }

        valueTile("percentPrecip", "device.percentPrecip", decoration: "flat", height: 1, width: 2) {
            state "default", label:'${currentValue}% precip'
        }

        standardTile("refresh", "device.weather", decoration: "flat", height: 1, width: 2) {
            state "default", label: "", action: "refresh", icon:"st.secondary.refresh"
        }

        valueTile("rise", "device.localSunrise", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Sunrise ${currentValue}'
        }

        valueTile("set", "device.localSunset", decoration: "flat", height: 1, width: 2) {
            state "default", label:'Sunset ${currentValue}'
        }

        valueTile("lastUpdate", "device.lastUpdate", decoration: "flat", height: 1, width: 3, wordWrap: true) {
            state "default", label:'Last update:\n${currentValue}'
        }
        valueTile("location", "device.location", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'PWS Location\n${currentValue}'
        }


        main(["temperature"])
        details(
            ["temperature", 
             "feelsLike", 
             "weatherIcon", 
             "humidity", 
             "wind",
             "city", 
             "percentPrecip", 
             "rise", 
             "set",
             "refresh",
             "location",
             "lastUpdate",
            ]
        )
    }
}

// parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
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
