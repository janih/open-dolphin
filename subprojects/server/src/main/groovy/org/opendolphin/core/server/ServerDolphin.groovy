package org.opendolphin.core.server

import org.opendolphin.core.Dolphin
import org.opendolphin.core.server.action.DolphinServerAction
import org.opendolphin.core.server.comm.NamedCommandHandler


public interface ServerDolphin extends Dolphin<ServerAttribute, ServerPresentationModel> {

    void registerDefaultActions()

    void register(DolphinServerAction action)

    void action(String name, NamedCommandHandler namedCommandHandler)

    ServerPresentationModel presentationModel(String id, String presentationModelType, DTO dto)

    void removeAllPresentationModelsOfType(String type)
}