/**
*  Copyright 2018, 2019, 2022 SanderSoft
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
*  Ambient Weather Station Remote Sensor
*
*  Author: Kurt Sanders, SanderSoft™
*  Version 6.0.0
*/

import groovy.time.*
import java.text.SimpleDateFormat;

String getAppImg(imgName) 		{ return "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/images/$imgName" }

metadata {
    definition (name: "Ambient Weather Station Remote Sensor",
    namespace: "kurtsanders",
    author: "kurt@kurtsanders.com",
    importUrl: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/drivers/ambient-weather-station%20remote-sensor.driver"
    ) {
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Battery"
        capability "Refresh"

        attribute "date", "string"
        attribute "lastSTupdate", "string"
        attribute "version", "string"

        command "refresh"
    }
}
def refresh() {
    Date now = new Date()
    def timeString = now.format("EEE MMM dd h:mm:ss a", location.timeZone)
    log.info "User requested a 'Manual Refresh' from Ambient Weather Station device, sending refresh() request to parent smartApp"
    sendEvent(name: "lastSTupdate", value: "Cloud Refresh Requested at\n${timeString}...", "displayed":false)
    sendEvent(name: "secondaryControl", value: "Cloud Refresh Requested...", "displayed":false)
    parent.refresh()
}
def installed() {
}
def updated() {
}
def uninstalled() {
}
