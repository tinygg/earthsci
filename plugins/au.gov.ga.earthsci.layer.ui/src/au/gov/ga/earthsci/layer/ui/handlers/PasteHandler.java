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
package au.gov.ga.earthsci.layer.ui.handlers;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;

import au.gov.ga.earthsci.layer.IPersistentLayer;
import au.gov.ga.earthsci.layer.intent.IntentLayerLoader;
import au.gov.ga.earthsci.layer.tree.ILayerNode;
import au.gov.ga.earthsci.layer.tree.ILayerTreeNode;
import au.gov.ga.earthsci.layer.tree.LayerNode;
import au.gov.ga.earthsci.layer.ui.dnd.LayerTransfer;
import au.gov.ga.earthsci.layer.ui.dnd.LayerTransferData;
import au.gov.ga.earthsci.layer.ui.dnd.LayerTransferData.TransferredLayer;
import au.gov.ga.earthsci.layer.worldwind.ITreeModel;

/**
 * Handles paste commands for the layer tree.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class PasteHandler
{
	@Inject
	private ITreeModel model;

	@Inject
	private IEclipseContext context;

	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) ILayerTreeNode target, TreeViewer viewer,
			Clipboard clipboard)
	{
		if (target == null)
		{
			target = model.getRootNode();
		}

		LayerTransferData data = (LayerTransferData) clipboard.getContents(LayerTransfer.getInstance());
		if (data != null)
		{
			for (TransferredLayer layer : data.getLayers())
			{
				ILayerTreeNode node = layer.getNode();
				target.addChild(node);
				viewer.add(target, node);
				viewer.reveal(node);

				initializeAllLayers(node, context);
			}
		}

		String[] filenames = (String[]) clipboard.getContents(FileTransfer.getInstance());
		if (filenames != null)
		{
			for (String filename : filenames)
			{
				File file = new File(filename);
				if (file.isFile())
				{
					LayerNode node = new LayerNode();
					node.setName(file.getName());
					node.setEnabled(true);
					target.addChild(node);
					viewer.add(target, node);
					viewer.reveal(node);

					IntentLayerLoader.load(file.toURI(), node, context);
				}
			}
		}
	}

	public static void initializeAllLayers(ILayerTreeNode node, IEclipseContext context)
	{
		if (node instanceof ILayerNode)
		{
			final ILayerNode layerNode = (ILayerNode) node;
			IPersistentLayer layer = layerNode.getLayer();
			layer.initialize(layerNode, context);
		}
		for (ILayerTreeNode child : node.getChildren())
		{
			initializeAllLayers(child, context);
		}
	}

	@CanExecute
	public boolean canExecute(Clipboard clipboard)
	{
		LayerTransferData data = (LayerTransferData) clipboard.getContents(LayerTransfer.getInstance());
		if (data != null)
		{
			return true;
		}
		String[] filenames = (String[]) clipboard.getContents(FileTransfer.getInstance());
		if (filenames != null)
		{
			return true;
		}
		return false;
	}
}
