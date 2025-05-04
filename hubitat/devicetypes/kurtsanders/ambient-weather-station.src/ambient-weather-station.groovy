/**
*  Copyright 2018, 2019, 2021, 2022, 2023, 2024, 2025 SanderSoft
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
*/
import groovy.time.*
import java.text.SimpleDateFormat;
import groovy.transform.Field
#include kurtsanders.AWSLibrary

@Field static String PARENT_DEVICE_NAME            = "Ambient Weather Station"
@Field static final String VERSION                 = "6.6.0"

metadata {
    definition (name: PARENT_DEVICE_NAME,
    namespace: NAMESPACE,
    author: AUTHOR_NAME,
    importUrl: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/drivers/ambient-weather-station.driver"
    ) {
        capability "IlluminanceMeasurement"
        capability "TemperatureMeasurement"
        capability "RelativeHumidityMeasurement"
        capability "Sensor"
        capability "Refresh"
        capability "UltravioletIndex"
        capability "Battery"

        // Start of Ambient Weather API Rest MAP
        // Actual numeric values from Ambient Weather API non rounded
        attribute "wind", "number"              //SharpTool.io
        attribute "windDirection", "number"     //Hubitat  OpenWeather
        attribute "windSpeed", "number"         //Hubitat  OpenWeather
        attribute "wind_cardinal", "string"

        // Display values from Ambient Weather API rounded and with {optional} units
        attribute "windspeedmph_display", "string"
        attribute "windgustmph_display", "string"
        attribute "maxdailygust_display", "string"
        attribute "tempf_display", "string"
        attribute "hourlyrainin_display", "string"
        attribute "eventrainin_display", "string"
        attribute "dailyrainin_display", "string"
        attribute "weeklyrainin_display", "string"
        attribute "monthlyrainin_display", "string"
        attribute "totalrainin_display", "string"
        attribute "baromrelin_display", "string"
        attribute "baromabsin_display", "string"
        attribute "humidity_display", "string"
        attribute "tempinf_display", "string"
        attribute "humidityin_display", "string"
        attribute "solarradiation_display", "string"
        attribute "feelsLike_display", "string"
        attribute "dewPoint_display", "string"

        attribute "baromabsin", "number"
        attribute "baromrelin", "number"
        attribute "city", "string"
        attribute "dailyrainin", "number"
        attribute "date", "string"
        attribute "dateutc", "string"
        attribute "dewPoint", "number"
        attribute "dewpoint", "number"
        attribute "eventrainin", "number"
        attribute "feelslike", "number"
        attribute "hourlyrainin", "number"
        attribute "humidityin", "number"
        attribute "lastRain", "string"
        attribute "location", "string"
        attribute "lastRainDuration", "string"
        attribute "macAddress", "string"
        attribute "maxdailygust", "number"
        attribute "monthlyrainin", "number"
        attribute "pwsName", "string"
        attribute "solarradiation", "number"
        attribute "tempinf", "number"
        attribute "totalrainin", "number"
        attribute "weeklyrainin", "number"
        attribute "windVector", "string"
        attribute "winddir", "string"
        attribute "winddirection", "string"
        attribute "windgustmph", "number"
        attribute "windspeedmph", "number"
        attribute "ultravioletIndexDisplay", "string"
        attribute "lightning_day", "number"
        attribute "lightning_time", "number"
        attribute "lightning_distance", "number"
        attribute "lightning_hour", "number"
        attribute "batt_lightning", "number"
        // End of Ambient Weather API Rest MAP

        // Weather Forecast & Misc attributes
        attribute "lastSTupdate", "string"
        attribute "scheduleFreqMin", "number"
        attribute "version", "string"
        attribute "date", "string"

        command "refresh"
        command "clearAllDeviceCurrentStates"
        command "setPollingInterval", [[name:"Set AWS Polling Interval*", type:"ENUM", description:"Set AWS Polling Interval", constraints:POLLING_OPTIONS_MAP]]

    }
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

def setPollingInterval(pollingInterval) {
    def pollingKey = POLLING_OPTIONS_MAP.find { it.value == pollingInterval }?.key
    log.debug "Set pollingInterval = ${pollingInterval} (${pollingKey})" 
    parent.setPollingInterval(pollingKey)   
}

//Additional Preferences
preferences {
	//Logging Options
	input name: "logLevel", type: "enum", title: fmtTitle("Logging Level"),
		description: fmtDesc("Logs selected level and above"), defaultValue: 0, options: LOG_LEVELS
	input name: "logLevelTime", type: "enum", title: fmtTitle("Logging Level Time"),
		description: fmtDesc("Time to enable Debug/Trace logging"),defaultValue: 0, options: LOG_TIMES
	//Help Link
	input name: "helpInfo", type: "hidden", title: fmtHelpInfo("Community Link")
}
