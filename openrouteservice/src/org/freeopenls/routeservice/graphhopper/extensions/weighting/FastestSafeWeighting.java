/*
 *  Licensed to Peter Karich under one or more contributor license
 *  agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  Peter Karich licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the
 *  License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.freeopenls.routeservice.graphhopper.extensions.weighting;

import com.graphhopper.routing.util.FastestWeighting;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.PriorityCode;
import com.graphhopper.util.EdgeIteratorState;

/**
 * Special weighting for (motor)bike
 * <p>
 * 
 * @author Peter Karich
 */
public class FastestSafeWeighting extends FastestWeighting {
	private Double THRESHOLD_AVOID_AT_ALL_COSTS = (double) (PriorityCode.AVOID_AT_ALL_COSTS.getValue() / (double)PriorityCode.BEST
			.getValue());

	
	/**
	 * For now used only in BikeCommonFlagEncoder and MotorcycleFlagEncoder
	 */
	public static final int KEY = 101;

	public FastestSafeWeighting(double maxSpeed, FlagEncoder encoder) {
		super(maxSpeed, encoder);

	}

	@Override
	public double calcWeight(EdgeIteratorState edgeState, boolean reverse, int prevOrNextEdgeId) {
		double weight = super.calcWeight(edgeState, reverse, prevOrNextEdgeId);
		if (Double.isInfinite(weight))
			return Double.POSITIVE_INFINITY;

		double priority = getFlagEncoder().getDouble(edgeState.getFlags(), KEY);

		if (priority <= THRESHOLD_AVOID_AT_ALL_COSTS)
			weight *= 2;

		return weight;
	}
}
