/*
 * Copyright 2012-2015 Canoo Engineering AG.
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

package org.opendolphin.demo

import org.opendolphin.core.comm.Command
import org.opendolphin.core.comm.NamedCommand
import org.opendolphin.core.server.DTO
import org.opendolphin.core.server.GServerDolphin
import org.opendolphin.core.server.Slot
import org.opendolphin.core.server.comm.NamedCommandHandler

class PullTreeActionHandler implements NamedCommandHandler {

    void handleCommand(NamedCommand command, List<Command> response) {
        // a stand-in for some arbitrary kind of tree in the application domain
        def domainTreeRoot = new NodeBuilder().earth {
            europe {
                switzerland {
                    basel()
                    zurich()
                }
                germany()
            }
            america()
            australia()
        }

        makePM(domainTreeRoot, response)
    }

    private void makePM(node, List<Command> response) {
        DTO model = new DTO(new Slot("parent", node.parent()?.name()))
        GServerDolphin.presentationModel(response, node.name(), null, model)
        node.children().each {
            makePM it, response // recurse
        }
    }
}