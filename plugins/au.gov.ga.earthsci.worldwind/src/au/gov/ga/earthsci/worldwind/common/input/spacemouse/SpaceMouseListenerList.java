/*******************************************************************************
 * Copyright 2013 Geoscience Australia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package au.gov.ga.earthsci.worldwind.common.input.spacemouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for maintaining a list of {@link ISpaceMouseListener}s.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class SpaceMouseListenerList implements ISpaceMouseListener
{
	private final List<ISpaceMouseListener> listeners = new ArrayList<ISpaceMouseListener>();

	public void add(ISpaceMouseListener listener)
	{
		listeners.add(listener);
	}

	public void remove(ISpaceMouseListener listener)
	{
		listeners.remove(listener);
	}

	@Override
	public void axisChanged(SpaceMouseAxisEvent event)
	{
		for (int i = listeners.size() - 1; i >= 0; i--)
		{
			listeners.get(i).axisChanged(event);
		}
	}

	@Override
	public void buttonChanged(SpaceMouseButtonEvent event)
	{
		for (int i = listeners.size() - 1; i >= 0; i--)
		{
			listeners.get(i).buttonChanged(event);
		}
	}
}
