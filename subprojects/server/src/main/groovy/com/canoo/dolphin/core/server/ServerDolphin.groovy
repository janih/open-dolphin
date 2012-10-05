/*
 * Copyright 2012 Canoo Engineering AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canoo.dolphin.core.server

import com.canoo.dolphin.core.Dolphin
import com.canoo.dolphin.core.ModelStore
import com.canoo.dolphin.core.comm.Command
import com.canoo.dolphin.core.comm.CreatePresentationModelCommand
import com.canoo.dolphin.core.server.comm.ActionRegistry
import com.canoo.dolphin.core.server.comm.NamedCommandHandler
import com.canoo.dolphin.core.server.comm.ServerConnector
import com.canoo.dolphin.core.server.action.*

/**
 * The main Dolphin facade on the server side.
 * Responsibility: single access point for dolphin capabilities.
 * Collaborates with server model store and current response.
 * Threading model: confined to a single controller thread.
 */

class ServerDolphin extends Dolphin {

    /** the server model store is unique per user session */
    final ModelStore serverModelStore

    /** the serverConnector is unique per user session */
    final ServerConnector serverConnector

    ServerDolphin(ModelStore serverModelStore, ServerConnector serverConnector) {
        this.serverModelStore = serverModelStore
        this.serverConnector = serverConnector
    }

    ServerDolphin() {
        this(new ModelStore(), new ServerConnector())
    }

    @Override
    ModelStore getModelStore() {
        serverModelStore
    }

    void registerDefaultActions() {
        register new StoreValueChangeAction()
        register new StoreAttributeAction()
        register new CreatePresentationModelAction()
        register new SwitchPresentationModelAction()
        register new StoreInitialValueChangeAction()
        register new PresentationModelLinkAction()
    }

    void register(DolphinServerAction action) {
        action.serverDolphin = this
        serverConnector.register(action)
    }

    /** groovy-friendly convenience method to register a named action */
    void action(String name, Closure logic) {
        def serverAction = new ClosureServerAction(name, logic)
        register(serverAction)
    }
    /** java-friendly convenience method to register a named action */
    void action(String name, NamedCommandHandler namedCommandHandler) {
        def serverAction = new NamedServerAction(name, namedCommandHandler)
        register(serverAction)
    }

    /**
     * Creates a presentation model on the client side.<br/>
     * Automatically syncs any additional data on model/attributes
     *
     * @param response list of commands to be sent back to the client
     * @param transientModel the model that should be created on the client side
     * @param handler optional callback called after the model has been added to the serverModelStore
     */
    void createPresentationModel(List<Command> response, ServerPresentationModel transientModel, Closure handler) {
        createPresentationModel(response, transientModel, new CreatePresentationModelHandler() {
            @Override
            void call(List<Command> callbackResponse, ServerPresentationModel presentationModel) {
                handler(callbackResponse, presentationModel)
            }
        })
    }

    /**
     * Creates a presentation model on the client side.<br/>
     * Automatically syncs any additional data on model/attributes
     *
     * @param response list of commands to be sent back to the client
     * @param transientModel the model that should be created on the client side
     * @param handler optional callback called after the model has been added to the serverModelStore
     */
    void createPresentationModel(List<Command> response, ServerPresentationModel transientModel, CreatePresentationModelHandler handler) {
        register(new DolphinServerAction() {
            void registerIn(ActionRegistry registry) {
                Closure commandHandler = null
                commandHandler = { CreatePresentationModelCommand command, List<Command> callbackResponse ->
                    if (command.pmId != transientModel.id) return
                    ServerPresentationModel realizedPresentationModel = serverModelStore.findPresentationModelById(transientModel.id)
                    for (transientAttribute in transientModel.attributes) {
                        ServerAttribute realizedAttribute = realizedPresentationModel.findAttributeByPropertyName(transientAttribute.propertyName)
                        if (!realizedAttribute) continue
                        realizedAttribute.qualifier = transientAttribute.qualifier
                    }
                    handler.call(callbackResponse, realizedPresentationModel)
                    registry.unregister(CreatePresentationModelCommand, commandHandler)
                }

                registry.register(CreatePresentationModelCommand, commandHandler)
            }
        })
        response << CreatePresentationModelCommand.makeFrom(transientModel)
    }

    /** groovy-friendly convenience method for a typical case of creating a ServerPresentationModel with initial values*/
    ServerPresentationModel presentationModel(Map<String, Object> attributeNamesAndValues, String id, String presentationModelType = null) {
        List attributes = attributeNamesAndValues.collect {key, value -> new ServerAttribute(key, value) }
        ServerPresentationModel result = new ServerPresentationModel(id, attributes)
        result.presentationModelType = presentationModelType
        result
    }
}