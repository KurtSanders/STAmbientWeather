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
*  Ambient Weather Station
*
*  Author: Kurt Sanders, SanderSoft™
*  Version 6.0.0
*/
import groovy.time.*
import java.text.SimpleDateFormat;

metadata {
    definition (name: "Ambient Weather Station",
    namespace: "kurtsanders",
    author: "kurt@kurtsanders.com",
    importUrl: "https://raw.githubusercontent.com/KurtSanders/STAmbientWeather/master/hubitat/drivers/ambient-weather-station.driver"
    ) {
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
        // Actual numeric values from Ambient Weather API non rounded
        attribute "windspeedmph_real", "number"
        attribute "windgustmph_real", "number"
        attribute "maxdailygust_real", "number"
        attribute "tempf_real", "number"
        attribute "hourlyrainin_real", "number"
        attribute "eventrainin_real", "number"
        attribute "dailyrainin_real", "number"
        attribute "weeklyrainin_real", "number"
        attribute "monthlyrainin_real", "number"
        attribute "totalrainin_real", "number"
        attribute "baromrelin_real", "number"
        attribute "baromabsin_real", "number"
        attribute "humidity_real", "number"
        attribute "tempinf_real", "number"
        attribute "humidityin_real", "number"
        attribute "solarradiation_real", "number"
        attribute "feelsLike_real", "number"
        attribute "dewPoint_real", "number"
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

		// Numeric values from Ambient API are rounded to 0.1 if 0 < X < 0.1 because SmartThings Tiles cannot display values less than 0.1 and greater than zero
        attribute "baromabsin", "string"
        attribute "baromrelin", "string"
        attribute "city", "string"
        attribute "dailyrainin", "string"
        attribute "date", "string"
        attribute "dateutc", "string"
        attribute "dewPoint", "string"
        attribute "dewpoint", "string"
        attribute "eventrainin", "string"
        attribute "feelsLike", "string"
        attribute "feelslike", "string"
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
        attribute "solarradiation", "string"
        attribute "temperature", "string"
        attribute "tempinf", "string"
        attribute "totalrainin", "string"
        attribute "weeklyrainin", "string"
        attribute "windVector", "string"
        attribute "winddir", "string"
        attribute "winddirection", "string"
        attribute "windgustmph", "string"
        attribute "windspeedmph", "string"
        attribute "ultravioletIndexDisplay", "string"
        attribute "lightning_day", "number"
        attribute "lightning_time", "number"
        attribute "lightning_distance", "number"
        attribute "lightning_hour", "number"
        attribute "batt_lightning", "number"
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
        attribute "unitsOfMeasure", "string"

        command "refresh"
    }
}

def initialize() {
    def naStndardFields = [
        "baromabsin_display",
        "baromrelin_display",
        "dailyrainin_display",
        "dewPoint_display",
        "eventrainin_display",
        "feelsLike_display",
        "hourlyrainin_display",
        "lastRain",
        "lastRainDuration",
        "monthlyrainin_display",
        "solarradiation",
        "totalrainin_display",
        "ultravioletIndexDisplay",
        "weeklyrainin_display",
        "windgustmph_display",
        "windPhrase",
        "windspeedmph_display",
        "unitsOfMeasure"
    ]
    naStndardFields.eachWithIndex { field, i ->
        log.debug "${i}) Setting Initial Weather Field: '${field}' to 'N/A'"
        sendEvent(name: "${field}", value: "N/A")
    }
}

def installed() {
    initialize()
}

def updated() {
}

def refresh() {
    Date now = new Date()
    def timeString = now.format("EEE MMM dd h:mm:ss a", location.timeZone)
    sendEvent(name: "secondaryControl", value: "Cloud Refresh Requested...", "displayed":false)
    sendEvent(name: "lastSTupdate", value: "Cloud Refresh Requested at\n${timeString}...", "displayed":false)
    log.info "User requested a 'Manual Refresh' from Ambient Weather Station device, sending refresh() request to parent smartApp"

    parent.refresh()
}
