package com.canoo.dolphin.demo

import com.canoo.dolphin.core.server.action.SavePresentationModelAction
import com.canoo.dolphin.core.server.action.StoreInitialValueChangeAction

def config = new CustomJavaFxInMemoryConfig()
config.registerDefaultActions()
config.register(new SavePresentationModelAction(config.modelStore))
config.register(new StoreInitialValueChangeAction(config.modelStore))

new SaveView().show()