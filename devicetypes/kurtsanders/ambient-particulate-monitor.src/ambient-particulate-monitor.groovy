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
*  Ambient Particulate Monitor
*
*  Author: Kurt Sanders, SanderSoft™
*
*/

String getAppImg(imgName) 		{ return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/$imgName" }

metadata {
    definition (name: "Ambient Particulate Monitor", namespace: "kurtsanders", author: "kurt@kurtsanders.com") {
        capability "Sensor"
        capability "Battery"
        capability "Refresh"

        attribute "pm25", "number"
        attribute "pm25_24h", "number"
        attribute "aqi", "enum", ["AQI: Good","AQI: Moderate","AQI: Unhealthy for Sensitive Groups","AQI: Unhealthy","AQI: Very Unhealthy","AQI: Hazardous","AQI: Extremely Hazardous"]
        attribute "date", "string"
        attribute "lastSTupdate", "string"
        attribute "version", "string"

        command "refresh"
    }
    tiles(scale: 2) {
        multiAttributeTile(name:"pm25", type:"generic", width:6, height:4, canChangeIcon: true ) {
            tileAttribute("device.pm25", key: "PRIMARY_CONTROL") {
                attributeState("default",label:'${currentValue}',
                               backgroundColors:[
                                   [value: 0, color: "#153591"],
                                   [value: 12, color: "#1e9cbb"],
                                   [value: 35, color: "#90d2a7"],
                                   [value: 55, color: "#44b621"],
                                   [value: 150, color: "#f1d801"],
                                   [value: 250, color: "#d04e00"],
                                   [value: 350, color: "#bc2323"]
                               ])
            }
            tileAttribute("device.aqi", key: "SECONDARY_CONTROL") {
                attributeState("aqi", label:'${currentValue}')
            }
        }
        valueTile("icon", "icon", width: 2, height: 2, decoration: "flat") {
            state "default", icon: getAppImg('ambient-weather-pm25.jpg')
        }
        valueTile("pm25display", "device.pm25", width: 2, height: 2,  wordWrap: true) {
            state("default", label: 'Current\n${currentValue}\nµg/m3', defaultState: true)
        }
        valueTile("pm25_24h", "device.pm25_24h", width: 2, height: 2,  wordWrap: true) {
            state("default", label: '24hrs\n${currentValue}\nµg/m3', defaultState: true)
        }
        standardTile("battery", "device.battery", width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", 	label: '', icon: getAppImg('battery-na.png')
            state "100", 		label: '', icon: getAppImg('battery-good.png')
            state "0", 			label: '', icon: getAppImg('battery-bad.png')
        }
        valueTile("date", "device.date", width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state("default", label: 'Ambient Server DateTime\n${currentValue}')
        }
        valueTile("lastSTupdate", "device.lastSTupdate", width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label: '${currentValue}', action: "refresh"
        }
        standardTile("refresh", "device.refresh", width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label: "", action: "refresh", icon:"st.secondary.refresh"
        }
    }

    main(["pm25"])
    details(
        [
            "pm25",
            "pm25display",
            "icon",
            "pm25_24h",
            "battery",
            "refresh",
            "date",
            "lastSTupdate"
        ]
    )
}
def refresh() {
    Date now = new Date()
    def timeString = now.format("EEE MMM dd h:mm:ss a", location.timeZone)
    sendEvent(name: "lastSTupdate", value: "Cloud Refresh Requested at\n${timeString}...", "displayed":false)
    sendEvent(name: "aqi", value: "Refresh Requested at ${timeString}...", displayed: false)
    parent.refresh()
}
def installed() {
	log.debug "Ambient Particulate Monitor device created"
    sendEvent(name: "aqi", value: "PM25 not updated - Please manually refresh", displayed: false)
}
def updated() {
}
def uninstalled() {
	log.debug "Ambient Particulate Monitor device deleted"
}