/*******************************************************************
*** SanderSoft - Core App/Device Helpers                        ***
/*******************************************************************/


library (
    base: "app",
    author: "Kurt Sanders",
    category: "Apps",
    description: "Core functions for Ambient Weather Station Suite.",
    name: "AWSLibrary",
    namespace: "kurtsanders",
    documentationLink: "https://github.com/KurtSanders/STAmbientWeather/blob/master/README.md",
    version: "1.0.0",
    disclaimer: "This library is only for use with SanderSoft Apps and Drivers."
)

import groovy.json.*
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.time.TimeCategory
import groovy.transform.Field
import hubitat.helper.RMUtils
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@Field static String AUTHOR_NAME                   = "Kurt Sanders"
@Field static String NAMESPACE                     = "kurtsanders"
@Field static final String COMM_LINK               = "https://community.hubitat.com/t/release-ambient-weather-station-app/128838"
@Field static final String GITHUB_LINK             = "https://github.com/KurtSanders/STAmbientWeather?tab=readme-ov-file#ambient-weather-station-aws"
@Field static final String GITHUB_IMAGES_LINK      = "https://raw.githubusercontent.com/kurtsanders/HubitatPackages/master/resources/images/"


def setLibraryVersion() {
    state.libraryVersion = "1.0.0"
}

def uninstalled() {
    sendLocationEvent(name: "updateVersionInfo", value: "${app.id}:remove")
    unschedule()
    removeChildDevices(getChildDevices())
}

private removeChildDevices(delete) {
    delete.each {deleteChildDevice(it.deviceNetworkId)}
}

def displayVersion() {
    setVersion()
    setLibraryVersion()
    section() {
        def currentYear = new Date().format("yyyy", location.timeZone)
        if(state.appType == "parent") { href "removePage", title:"${getImage("optionsRed")} <b>Remove App and all child apps</b>", description:"" }
        paragraph getFormat("line")
        if(state.version) {
            bMes = "<div style='color:#1A77C9;text-align:center;font-size:20px;font-weight:bold'>${state.name} - ${state.version}"
        } else {
            bMes = "<div style='color:#1A77C9;text-align:center;font-size:20px;font-weight:bold'>${state.name}"
        }
        bMes += "<br><small>Library Ver: ${state.libraryVersion}</small>"
        bMes += "</div>"
        paragraph "${bMes}"
        paragraph("<a href='https://www.paypal.com/donate/?hosted_button_id=E4WXT86RTPXDC'>Please consider making a small donation to support the developers application via PayPal™.</a><br>" +
                  "<small><i>Copyright \u00a9 2018-${currentYear} SandersSoft™ Inc - All rights reserved.</i></small><br>")
    }
}

void updateMyLabel(key=null) {
    def timeStamp = new Date().format("h:mm:ss a", location.timeZone)

    String myLabel = state.weatherStationName
    if ((myLabel == null) || !app.label.startsWith(myLabel)) {
        myLabel = app.label
    }
    if (myLabel.contains('<span')) {
        myLabel = myLabel.substring(0, myLabel.indexOf('<span'))
    }
    switch (key) {
        case 'unauthorized':
        newLabel = myLabel + "<span style=\"color:red\"> Unauthorized at ${timeStamp} </span>"
        break;
        case 'updated':
        newLabel = myLabel + "<span style=\"color:green\"> Updated at ${timeStamp} </span>"
        break;
        case 'refreshing':
        newLabel = myLabel + "<span style=\"color:orange\"> Refreshing at ${timeStamp}</span>"
        break;
        case 'retry':
        newLabel = myLabel + "<span style=\"color:red\"> Re-trying #${state.retry} at ${timeStamp}</span>"
        break;
        default:
            newLabel = myLabel
        break;
    }
    if (newLabel && (app.label != newLabel)) app.updateLabel(newLabel)
}

public String convertToCurrentTimeZone(String dateStr) {

    TimeZone utc = TimeZone.getTimeZone("UTC");
    SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    SimpleDateFormat destFormat = new SimpleDateFormat('EEE MMM d, h:mm a');
    sourceFormat.setTimeZone(utc);
    Date convertedDate = sourceFormat.parse(dateStr);
    return destFormat.format(convertedDate);

}
//get the current time zone

public String getCurrentTimeZone(){
    TimeZone tz = Calendar.getInstance().getTimeZone();
    return tz.getID();
}

String fmtTitle(String str) {
    return "<strong>${str}</strong>"
}
String fmtDesc(String str) {
    return "<div style='font-size: 85%; font-style: italic; padding: 1px 0px 4px 2px;'>${str}</div>"
}
String fmtHelpInfo(String str) {
    String info =     "${PARENT_DEVICE_NAME} v${VERSION}"
    String prefLink = "<a href='${COMM_LINK}' target='_blank'>${str}<br><div style='font-size: 70%;'>${info}</div></a>"
    String topStyle = "style='font-size: 18px; padding: 1px 12px; border: 2px solid Crimson; border-radius: 6px;'" //SlateGray
    String topLink =  "<a ${topStyle} href='${COMM_LINK}' target='_blank'>${str}<br><div style='font-size: 14px;'>${info}</div></a>"
    return "<div style='text-align: center; position: absolute; top: 0px; left: 400px; padding: 0px;'><ul class='nav'><li>${topLink}</ul></li></div>"
}
def getImage(type) {
    def loc = "<img src=" + GITHUB_IMAGES_LINK
    if(type == "Blank")          return "${loc}blank.png height=40 width=5}>"
    if(type == "checkMarkGreen") return "${loc}checkMarkGreen2.png height=30 width=30>"
    if(type == "optionsGreen")   return "${loc}options-green.png height=30 width=30>"
    if(type == "optionsRed")     return "${loc}options-red.png height=30 width=30>"
    if(type == "instructions")   return "${loc}instructions.png height=30 width=30>"
    if(type == "logo")           return "${loc}logo.png height=40>"
    if(type == "qmark")          return "${loc}question-mark-icon.png height=16>"
    if(type == "qmark2")         return "${loc}question-mark-icon-2.jpg height=16>"
    if(type == "button-red")     return "${loc}/button-red.png height=30 width=30>"
    if(type == "qmark")          return "${loc}question-mark-icon.png height=16>"
    if(type == "qmark2")         return "${loc}question-mark-icon-2.jpg height=16>"
}

def getFormat(type, myText="") {
    if(type == "header-blue") return "<div style='color:#ffffff;font-weight: bold;background-color:#309bff;border: 1px solid;box-shadow: 2px 3px #A9A9A9'>${myText}</div>"
    if(type == "header-red")  return "<div style='color:#ffffff;font-weight: bold;background-color:#ff0000;border: 1px solid;box-shadow: 2px 3px #A9A9A9'>${myText}</div>"
    if(type == "line")        return "<hr style='background-color:#1A77C9; height: 1px; border: 0;'>"
    if(type == "title")       return "<h2 style='color:#1A77C9;font-weight: bold'>${myText}</h2>"
    if(type == "text-green")  return "<div style='color:green'>${myText}</div>"
    if(type == "text-red")    return "<div style='color:red'>${myText}</div>"
    if(type == "text-blue")   return "<div style='color:blue'>${myText}</div>"
    if(type == "button-blue") return "<a style='color:white;text-align:center;font-size:20px;font-weight:bold;background-color:#03FDE5;border:1px solid #000000;box-shadow:3px 4px #8B8F8F;border-radius:10px' href='${page}'>${myText}</a>"
}

def help() {
    section("${getImage('instructions')} <b>${app.name} Online Documentation</b>", hideable: true, hidden: true) {
        paragraph "<a href='${GITHUB_LINK}#readme' target='_blank'><h4 style='color:DodgerBlue;'>Click this link to view Online Documentation for ${app.name}</h4></a>"
    }
}

def sectionHeader(title){
    return getFormat("header-blue", "${getImage("Blank")}"+" ${title}")
}

def syncLogLevelApp2Children(level, time) {
    device.updateSetting("logLevel"    ,[value: "${level}", type:"enum"])
    device.updateSetting("logLevelTime",[value: "${time}" , type:"enum"])
    checkLogLevel([level:level, time:time])
}

//Logging Level Options
@Field static final Map LOG_LEVELS = [0:"Off", 1:"Error", 2:"Warn", 3:"Info", 4:"Debug", 5:"Trace"]
@Field static final Map LOG_TIMES  = [0:"Indefinitely", 1:"1 Minute", 5:"5 Minutes", 10:"10 Minutes", 15:"15 Minutes", 30:"30 Minutes", 60:"1 Hour", 120:"2 Hours", 180:"3 Hours", 360:"6 Hours", 720:"12 Hours", 1440:"24 Hours"]
@Field static final String LOG_DEFAULT_LEVEL = 0

//Call this function from within updated() and configure() with no parameters: checkLogLevel()
void checkLogLevel(Map levelInfo = [level:null, time:null]) {
    unschedule(logsOff)
    //Set Defaults
    if (app) {
        if (settings.logLevel == null) app.updateSetting("logLevel",[value:LOG_DEFAULT_LEVEL, type:"enum"])
        if (settings.logLevelTime == null) app.updateSetting("logLevelTime",[value:"0", type:"enum"])
    } else {
        if (settings.logLevel == null) device.updateSetting("logLevel",[value:LOG_DEFAULT_LEVEL, type:"enum"])
        if (settings.logLevelTime == null) device.updateSetting("logLevelTime",[value:"0", type:"enum"])
    }
     //Schedule turn off and log as needed
    if (levelInfo.level == null) levelInfo = getLogLevelInfo()
    String logMsg = "Logging Level is: ${LOG_LEVELS[levelInfo.level]}"
    if (levelInfo.level >= 1 && levelInfo.time > 0) {
        logMsg += " for ${LOG_TIMES[levelInfo.time]}"
        runIn(60*levelInfo.time, logsOff, [overwrite: true])
    }
    if (levelInfo.time == 0) logMsg += " (${LOG_TIMES[levelInfo.time]})"
    logInfo(logMsg)
}

//Function for optional command
void setLogLevel(String levelName, String timeName=null) {
    Integer level = LOG_LEVELS.find{ levelName.equalsIgnoreCase(it.value) }.key
    Integer time = LOG_TIMES.find{ timeName.equalsIgnoreCase(it.value) }.key
    if (app) {
        app.updateSetting("logLevel",[value:"${level}", type:"enum"])
        app.updateSetting("logLevelTime",[value:"${level}", type:"enum"])
    } else {
        device.updateSetting("logLevel",[value:"${level}", type:"enum"])
        device.updateSetting("logLevelTime",[value:"${time}", type:"enum"])
    }
    checkLogLevel(level: level, time: time)
}

Map getLogLevelInfo() {
    Integer level = settings.logLevel as Integer ?: 0
    Integer time = settings.logLevelTime as Integer ?: 0
    return [level: level, time: time]
}

void logsOff() {
    logInfo "Logging auto disabled"
    setLogLevel("Off","Indefinitely")
}

//Logging Functions
def logMessage(String msg) {
    if (app) {
        return "<span style='color: blue'>${app.name}</span>: ${msg}"
    } else {
        return "<span style='color: green'>${device.name}</span>: ${msg}"
    }
}

void logErr(String msg) {
    if (logLevelInfo.level>=1) log.error "${logMessage(msg)}"
}
void logWarn(String msg) {
    if (logLevelInfo.level>=2) log.warn "${logMessage(msg)}"
}
void logInfo(String msg) {
    if (logLevelInfo.level>=3) log.info "${logMessage(msg)}"
}

void logDebug(String msg) {
        if (logLevelInfo.level>=4) log.debug "${logMessage(msg)}"
}

void logTrace(String msg) {
        if (logLevelInfo.level>=5) log.trace "${logMessage(msg)}"
}