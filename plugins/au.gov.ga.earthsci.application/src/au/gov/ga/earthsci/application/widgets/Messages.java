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
package au.gov.ga.earthsci.application.widgets;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "au.gov.ga.earthsci.application.widgets.messages"; //$NON-NLS-1$
	public static String PositionEditor_ElevationLabel;
	public static String PositionEditor_ElevationUnits;
	public static String PositionEditor_LatitudeLabel;
	public static String PositionEditor_LatitudeUnits;
	public static String PositionEditor_LongitudeLabel;
	public static String PositionEditor_LongitudeUnits;
	public static String Vec4Editor_WLabel;
	public static String Vec4Editor_XLabel;
	public static String Vec4Editor_YLabel;
	public static String Vec4Editor_ZLabel;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
