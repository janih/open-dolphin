package org.opendolphin.demo.sharedStation

import org.opendolphin.demo.JavaFxInMemoryConfig

def config = new JavaFxInMemoryConfig()
def dolphin = config.serverDolphin

def transitions = [
    wakeup : [status: 'awake'  , wakeup: false, play: true,  gotobed: true],
    play   : [status: 'playing', wakeup: false, play: false, gotobed: true],
    gotobed: [status: 'asleep' , wakeup: true,  play: false, gotobed: false]
]

// workflow is a server-side responsibility
transitions.each { action, newStateFeatures ->
    dolphin.action(action) { cmd, resp ->
        def current_user = dolphin["current_user"]
        newStateFeatures.each { key, value ->
            dolphin.changeValueCommand(resp, current_user[key], value)
        }
    }
}

new SharedStationView().show(config.clientDolphin)