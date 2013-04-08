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
package au.gov.ga.earthsci.core.model.raster;

import org.gdal.gdal.Dataset;

import au.gov.ga.earthsci.common.util.Validate;
import au.gov.ga.earthsci.model.IModel;
import au.gov.ga.earthsci.worldwind.common.util.Util;

/**
 * A simple DTO that contains parameters used for creating {@link IModel}
 * instances from GDAL rasters.
 * 
 * @author James Navin (james.navin@ga.gov.au)
 */
public class GDALRasterModelParameters
{

	/** The raster band to use for elevation values */
	private int elevationBandIndex = 1;

	/** The source coordinate system / projection */
	private String sourceProjection;

	/** The offset to apply to the 'elevation' values */
	private Double offset;

	/** The scale factor to apply to the 'elevation' values */
	private Double scale;

	/**
	 * Create a new parameters object, populated with any sensible defaults
	 * obtainable from the provided dataset
	 * 
	 * @param ds
	 *            The GDAL dataset to obtain defaults from
	 */
	public GDALRasterModelParameters(Dataset ds)
	{
		Validate.notNull(ds, "A dataset is required"); //$NON-NLS-1$

		String datasetProjection = ds.GetProjection();
		if (!Util.isBlank(datasetProjection))
		{
			sourceProjection = datasetProjection;
		}
	}

	/**
	 * @return the {@value #elevationBandIndex}
	 */
	public int getElevationBandIndex()
	{
		return elevationBandIndex;
	}

	/**
	 * @return the sourceProjection
	 */
	public String getSourceProjection()
	{
		return sourceProjection;
	}

	public Double getScaleFactor()
	{
		return scale;
	}

	public void setScaleFactor(Double scale)
	{
		this.scale = scale;
	}

	public Double getOffset()
	{
		return this.offset;
	}

	public void setOffset(Double offset)
	{
		this.offset = offset;
	}
}
