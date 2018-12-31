/**
*  Copyright 2018 SanderSoft
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
*  Author: Kurt Sanders
*
*  Date: 2018-12-30
*/
// Start Version Information
def version() {
    return ["V3.0", "Requires Ambient WS Service Manager App V3"]
}
// End Version Information
import groovy.time.*
import java.text.DecimalFormat
metadata {
    definition (name: "Ambient Weather Station Remote Sensor V3", namespace: "kurtsanders", author: "kurt@kurtsanders.com") {
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Sensor"
        capability "Refresh"

        attribute "lastSTupdate", "string"
        attribute "version", "string"
        
        command "refresh"
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
            tileAttribute("device.humidity", key: "SECONDARY_CONTROL") {
                attributeState("humidity", label:'${currentValue}%', unit:"%", defaultState: true)
            }
        }
    }

    valueTile("lastSTupdate", "device.lastSTupdate", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
        state("default", label: '${currentValue}')
    }
    standardTile("refresh", "device.weather", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
        state "default", label: "", action: "refresh", icon:"st.secondary.refresh"
    }

    main(["temperature"])
    details(
        [
            // Inside Sensors
            "temperature", 
            "humidity",
            "lastSTupdate",
            "refresh"
        ]
    )
}

def initialize() {
    // initialize API's
    log.info "DTH Section: Initialize"
}

def installed() {
    log.info "DTH Section: Installed"
}

def uninstalled() {
    log.info "DTH Section: Uninstalled"
    unschedule()
}

def updated() {
    log.info "DTH Section: Updated"
}

def refresh() {	    
    parent.refresh()
}