/**
*  Copyright 2018, 2019, 2022, 2023, 2024, 2025 SanderSoft
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
*/

import groovy.time.*
import java.text.SimpleDateFormat;
import groovy.transform.Field
#include kurtsanders.AWSLibrary

@Field static String PARENT_DEVICE_NAME            = "Ambient Weather Station Remote Sensor"
@Field static final String VERSION                 = "6.3.0"

metadata {
    definition (name: PARENT_DEVICE_NAME,
    namespace: NAMESPACE,
    author: AUTHOR_NAME,
    importUrl: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/drivers/ambient-weather-station%20remote-sensor.driver"
    ) {
        capability "TemperatureMeasurement"
        capability "RelativeHumidityMeasurement"
        capability "Sensor"
        capability "Battery"
        capability "Refresh"

        attribute "date", "string"
        attribute "lastSTupdate", "string"
        attribute "version", "string"
        attribute "feelsLike_display", "string"
        attribute "feelsLike", "number"
        attribute "dewPoint_display", "string"
        attribute "dewPoint", "number"
        attribute "dewpoint", "number"


        command "refresh"
        command "clearAllDeviceCurrentStates"

    }
}

def deleteDeviceData() {
    logInfo "${device.name}: deleteDeviceData..."
    clearAllDeviceCurrentStates(false)
}

def clearAllDeviceCurrentStates(refresh=true) {
    logInfo "Clearing current states of this device..."
    device.currentStates.eachWithIndex {item, index ->
        device.deleteCurrentState(item.name)
        logInfo "Deleted ${index}. → ${item.name}"
    }
    if (refresh) parent.refresh()
}

def refresh() {
    Date now = new Date()
    def timeString = now.format("EEE MMM dd h:mm:ss a", location.timeZone)
    logInfo "User requested a 'Manual Refresh' from Ambient Weather Station device, sending refresh() request to parent smartApp"
    parent.refresh()
}
def initialize() {
    checkLogLevel()
}
def installed() {
    checkLogLevel()
}
def updated() {
    checkLogLevel()
}
