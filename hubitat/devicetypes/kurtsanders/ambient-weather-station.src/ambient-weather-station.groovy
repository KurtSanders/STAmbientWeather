/**
*  Copyright 2018, 2019, 2021, 2022, 2023, 2024 SanderSoft
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
#include kurtsanders.kes-coreFunctions

@Field static String PARENT_DEVICE_NAME            = "Ambient Weather Station"
@Field static final String VERSION                 = "6.1.2"

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
        attribute "scheduleFreqMin", "string"
        attribute "version", "string"
        attribute "date", "string"

        command "refresh"
        command "clearAllDeviceCurrentStates"
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


/*******************************************************************
 *** SanderSoft - Preference Helpers                             ***
/*******************************************************************/

/*

String fmtTitle(String str) {
	return "<strong>${str}</strong>"
}
String fmtDesc(String str) {
	return "<div style='font-size: 85%; font-style: italic; padding: 1px 0px 4px 2px;'>${str}</div>"
}
String fmtHelpInfo(String str) {
	String info = "${PARENT_DEVICE_NAME} v${VERSION}"
	String prefLink = "<a href='${COMM_LINK}' target='_blank'>${str}<br><div style='font-size: 70%;'>${info}</div></a>"
	String topStyle = "style='font-size: 18px; padding: 1px 12px; border: 2px solid Crimson; border-radius: 6px;'" //SlateGray
	String topLink = "<a ${topStyle} href='${COMM_LINK}' target='_blank'>${str}<br><div style='font-size: 14px;'>${info}</div></a>"

	return "<div style='font-size: 160%; font-style: bold; padding: 2px 0px; text-align: center;'>${prefLink}</div>" +
		"<div style='text-align: center; position: absolute; top: 46px; right: 60px; padding: 0px;'><ul class='nav'><li>${topLink}</ul></li></div>"
}
*/

/********************************************************************
 ***** SanderSoft - Logging Functions                             ***
********************A************************************************/

/*

//Logging Level Options
@Field static final Map LOG_LEVELS = [0:"Off", 1:"Error", 2:"Warn", 3:"Info", 4:"Debug", 5:"Trace"]
@Field static final Map LOG_TIMES  = [0:"Indefinitely", 1:"01 Minute", 5:"05 Minutes", 10:"10 Minutes", 15:"15 Minutes", 30:"30 Minutes", 60:"1 Hour", 120:"2 Hours", 180:"3 Hours", 360:"6 Hours", 720:"12 Hours", 1440:"24 Hours"]
@Field static final String LOG_DEFAULT_LEVEL = 0

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

//Call this function from within updated() and configure() with no parameters: checkLogLevel()
void checkLogLevel(Map levelInfo = [level:null, time:null]) {
    unschedule(logsOff)
    //Set Defaults
    if (settings.logLevel == null) device.updateSetting("logLevel",[value:LOG_DEFAULT_LEVEL, type:"enum"])
    logDebug "==> settings.logLevel= ${settings.logLevel}"
    if (settings.logLevelTime == null) device.updateSetting("logLevelTime",[value:"0", type:"enum"])
    logDebug "==> settings.logLevelTime= ${settings.logLevelTime}"
    //Schedule turn off and log as needed
    if (levelInfo.level == null) levelInfo = getLogLevelInfo()
    String logMsg = "Logging Level is: ${LOG_LEVELS[levelInfo.level]} (${levelInfo.level})"
    if (levelInfo.level >= 1 && levelInfo.time > 0) {
        logMsg += " for ${LOG_TIMES[levelInfo.time]}"
        runIn(60*levelInfo.time, logsOff)
    }
    logInfo(logMsg)
}

//Function for optional command
void setLogLevel(String levelName, String timeName=null) {
	Integer level = LOG_LEVELS.find{ levelName.equalsIgnoreCase(it.value) }.key
	Integer time = LOG_TIMES.find{ timeName.equalsIgnoreCase(it.value) }.key
	device.updateSetting("logLevel",[value:"${level}", type:"enum"])
	device.updateSetting("logLevelTime",[value:"${time}", type:"enum"])
	checkLogLevel(level: level, time: time)
}

Map getLogLevelInfo() {
	Integer level = settings?.logLevel as Integer ?: 0
	Integer time  = settings?.logLevelTime as Integer ?: 0
	return [level: level, time: time]
}

def syncLogLevel(level, time) {
    device.updateSetting("logLevel"    ,[value: "${level}", type:"enum"])
    device.updateSetting("logLevelTime",[value: "${time}" , type:"enum"])
}

//Current Support
void logsOff() {
	logWarn "Debug and Trace logging disabled..."
	if (logLevelInfo.level >= 1) {
		device.updateSetting("logLevel",[value:"0", type:"enum"])
	}
}

//Logging Functions
void logErr(String msg) {
	if (logLevelInfo.level>=1) log.error "${device.name}: ${msg}"
}
void logWarn(String msg) {
	if (logLevelInfo.level>=2) log.warn "${device.name}: ${msg}"
}
void logInfo(String msg) {
	if (logLevelInfo.level>=3) log.info "${device.name}: ${msg}"
}
void logDebug(String msg) {
	if (logLevelInfo.level>=4) log.debug "${device.name}: ${msg}"
}
void logTrace(String msg) {
	if (logLevelInfo.level>=5) log.trace "${device.name}: ${msg}"
}
*/
