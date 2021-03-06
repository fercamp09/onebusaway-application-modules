/**
 * Copyright (C) 2011 Brian Ferris <bdferris@onebusaway.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.transit_data_federation.bundle.tasks.transfer_pattern.graph;

import org.onebusaway.transit_data_federation.impl.otp.GraphContext;
import org.onebusaway.transit_data_federation.impl.otp.graph.AbstractEdge;
import org.onebusaway.transit_data_federation.services.transit_graph.BlockStopTimeEntry;
import org.onebusaway.transit_data_federation.services.transit_graph.StopTimeEntry;
import org.onebusaway.transit_data_federation.services.tripplanner.StopTimeInstance;
import org.opentripplanner.routing.core.EdgeNarrative;
import org.opentripplanner.routing.core.State;
import org.opentripplanner.routing.core.StateEditor;

public class TPOfflineBlockDwellEdge extends AbstractEdge {

  private final StopTimeInstance _instance;

  public TPOfflineBlockDwellEdge(GraphContext context, StopTimeInstance instance) {
    super(context);
    _instance = instance;
  }

  @Override
  public State traverse(State s0) {

    BlockStopTimeEntry bst = _instance.getStopTime();
    StopTimeEntry stopTime = bst.getStopTime();
    int dwellTime = stopTime.getSlackTime();

    EdgeNarrative narrative = createNarrative(s0);
    StateEditor edit = s0.edit(this, narrative);
    edit.incrementTimeInSeconds(dwellTime);
    edit.incrementWeight(dwellTime);
    return edit.makeState();
  }

  private EdgeNarrative createNarrative(State s0) {
    TPOfflineBlockArrivalVertex fromVertex = new TPOfflineBlockArrivalVertex(
        _context, _instance);
    TPOfflineBlockDepartureVertex toVertex = new TPOfflineBlockDepartureVertex(
        _context, _instance);
    return narrative(s0, fromVertex, toVertex);
  }
}
