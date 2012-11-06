/*******************************************************************************
 * Copyright 2012 Geoscience Australia
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
package au.gov.ga.earthsci.application.parts.layer.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.Clipboard;

import au.gov.ga.earthsci.application.parts.layer.LayerTransfer;
import au.gov.ga.earthsci.application.parts.layer.LayerTransferData;
import au.gov.ga.earthsci.application.parts.layer.LayerTransferData.TransferredLayer;
import au.gov.ga.earthsci.core.model.layer.ILayerTreeNode;
import au.gov.ga.earthsci.core.worldwind.ITreeModel;

/**
 * Handles paste commands for the layer tree.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class PasteHandler
{
	@Inject
	private ITreeModel model;

	@Execute
	public void execute(TreeViewer viewer, Clipboard clipboard)
	{
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		ILayerTreeNode target = (ILayerTreeNode) sel.getFirstElement();
		if (target == null)
			target = model.getRootNode();

		LayerTransferData data = (LayerTransferData) clipboard.getContents(LayerTransfer.getInstance());
		for (TransferredLayer layer : data.getLayers())
		{
			ILayerTreeNode node = layer.getNode();
			target.add(node);
			viewer.add(target, node);
			viewer.reveal(node);
		}
	}
}