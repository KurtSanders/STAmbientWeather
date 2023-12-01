/**
*  Copyright 2018, 2019, 2021, 2022, 2023 SanderSoft
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
*  Version 6.1.0
*/
import groovy.time.*
import java.text.SimpleDateFormat;

metadata {
    definition (name: "Ambient Weather Station",
    namespace: "kurtsanders",
    author: "kurt@kurtsanders.com",
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
        attribute "feelsLike", "number"
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
        attribute "scheduleFreqMin", "string"
        attribute "version", "string"
        attribute "date", "string"

        command "refresh"
        command "ClearAllStates"
    }
}

def initialize() {
}

def installed() {
}

def updated() {
}

def ClearAllStates() {
    // This routine removes all device state values from previous/legacy releases of Abient Weather Station.  Optional to run by end user!
    def list = ["RelativeHumidity", "Motion", "PowerMeter", "Battery", "Illuminance", "UltravioletIndex",
                "EnergyMeter", "Water", "Temperature", "humidity_display", "wind_cardinal", "ultravioletIndex",
                "baromrelin_real", "scheduleFreqMin", "sunriseDate", "windVector", "hourlyrainin", "illuminance", "monthlyrainin_real", "pwsName",
                "windSpeed", "lightning_hour", "solarradiation", "feelslike", "feelsLike_display", "dailyrainin_real", "baromrelin", "feelsLike_real",
                "humidity", "tempinf_real", "weatherIcon", "alertDescription", "dateutc", "dewPoint", "secondaryControl", "dewPoint_display",
                "maxdailygust_real", "weeklyrainin_real", "temperature", "baromabsin_real", "solarradiation_real", "lastRain", "rainForecast",
                "tempinf", "localSunset", "unitsOfMeasure", "alertMessage", "totalrainin_real", "batt_lightning", "water", "totalrainin_display",
                "tempinf_display", "localSunrise", "lastSTupdate", "humidity_real", "motion", "windspeedmph", "totalrainin", "maxdailygust_display",
                "eventrainin_real", "eventrainin_display", "tempf_display", "weeklyrainin_display", "date", "feelsLike", "solarradiation_display",
                "humidityin_display", "moonAge", "baromabsin_display", "lightning_distance", "maxdailygust", "energy", "windDirection", "dailyrainin",
                "winddirection", "power", "tempf_real", "hourlyrainin_display", "forecastIcon", "windgustmph", "sunsetDate", "winddir", "lightning_day",
                "lightning_time", "baromrelin_display", "humidityin_real", "monthlyrainin", "date", "humidityin", "weeklyrainin", "windspeedmph_real",
                "dewpoint", "hourlyrainin_real", "eventrainin", "battery", "ultravioletIndexDisplay", "windPhrase", "windgustmph_display", "version",
                "dewPoint_real", "windspeedmph_display", "macAddress", "dailyrainin_display", "baromabsin", "monthlyrainin_display", "city", "location",
                "windgustmph_real", "wind", "lastRainDuration"]
    log.info "Clearing ${list.size()} current/stale states of this device..."
    list.eachWithIndex { item, index ->
        log.info "${index+1} of ${list.size()} removed ${item}"
        device.deleteCurrentState(item)
    }
    log.info "Refreshing currentstates of this device..."
    parent.refresh()
}

def refresh() {
    Date now = new Date()
    def timeString = now.format("EEE MMM dd h:mm:ss a", location.timeZone)
    log.info "User requested a 'Manual Refresh' from Ambient Weather Station device, sending refresh() request to parent smartApp"

    parent.refresh()
}
