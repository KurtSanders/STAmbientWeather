/**
*  Copyright 2018, 2019, 2021, 2022 SanderSoft
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
*  Version 6.0.0
*/

String getAppImg(imgName) 		{ return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/$imgName" }

metadata {
    definition (name: "Ambient Particulate Monitor",
    namespace: "kurtsanders",
    author: "kurt@kurtsanders.com",
    importUrl: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/drivers/ambient-particulate-monitor.driver"
    ) {
        capability "Sensor"
        capability "Battery"
        capability "Refresh"

        attribute "aqi_pm25_24h_aqin", "number"
        attribute "aqi_pm25_24h", "number"
        attribute "aqi_pm25_aqin", "number"
        attribute "aqi_pm25", "number"
        attribute "aqi", "enum", ["AQI: Good","AQI: Moderate","AQI: Unhealthy for Sensitive Groups","AQI: Unhealthy","AQI: Very Unhealthy","AQI: Hazardous","AQI: Extremely Hazardous"]
        attribute "co2_in_24h_aqin", "number"
        attribute "co2_in_aqin", "number"
        attribute "date", "string"
        attribute "lastSTupdate", "string"
        attribute "pm_in_humidity_aqin", "number"
        attribute "pm_in_temp_aqin", "number"
        attribute "pm10_in_24h_aqin", "number"
        attribute "pm10_in_aqin", "number"
        attribute "pm25_24h", "number"
        attribute "pm25_in_24h_aqin", "number"
        attribute "pm25_in_aqin", "number"
        attribute "pm25", "number"
        attribute "version", "string"

        command "refresh"
    }
}
def refresh() {
    Date now = new Date()
    def timeString = now.format("EEE MMM dd h:mm:ss a", location.timeZone)
    sendEvent(name: "lastSTupdate", value: "Cloud Refresh Requested at\n${timeString}...", "displayed":false)
    sendEvent(name: "aqi", value: "Refresh Requested at ${timeString}...", displayed: false)
    parent.refresh()
}
def installed() {
	log.debug "Ambient Particulate Monitor/AQIN device created"
    sendEvent(name: "aqi", value: "PM25 not updated - Please manually refresh", displayed: false)
}
def updated() {
}
def uninstalled() {
	log.debug "Ambient Particulate Monitor/AQIN device deleted"
}