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
package au.gov.ga.earthsci.layer.ui;

import java.net.URL;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;

import au.gov.ga.earthsci.application.ImageRegistry;
import au.gov.ga.earthsci.application.parts.info.InfoPart;
import au.gov.ga.earthsci.application.parts.legend.LegendPart;
import au.gov.ga.earthsci.common.ui.viewers.IControlProvider;
import au.gov.ga.earthsci.layer.tree.ILayerTreeNode;

/**
 * {@link IControlProvider} implementation for the layer tree. Must be disposed
 * of after use.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
@Creatable
public class LayerTreeControlProvider implements IControlProvider
{
	@Inject
	private EPartService partService;

	@Inject
	private EModelService modelService;

	@Inject
	private MWindow window;

	@PreDestroy
	public void dispose()
	{
	}

	private void createURLClickableLabel(Composite parent, final ILayerTreeNode layerNode, final URL url,
			final Image image, final Image hoverImage, final boolean info)
	{
		if (url != null)
		{
			final Label label = new Label(parent, SWT.NONE);
			label.setBackground(parent.getBackground());
			label.setImage(image);

			label.addMouseTrackListener(new MouseTrackAdapter()
			{
				@Override
				public void mouseEnter(MouseEvent e)
				{
					label.setImage(hoverImage);
				}

				@Override
				public void mouseExit(MouseEvent e)
				{
					label.setImage(image);
				}
			});


			label.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseUp(MouseEvent e)
				{
					MPart part;
					if (info)
					{
						part = partService.showPart(InfoPart.PART_ID, PartState.VISIBLE);
					}
					else
					{
						String tag = url.toString();
						tag += "|" + layerNode.getId(); //$NON-NLS-1$
						String label = "Legend" + ": " + layerNode.getLabelOrName(); //$NON-NLS-2$
						part = LegendPart.showPart(partService, modelService, window, tag, label);
					}

					String inputName = info ? InfoPart.INPUT_NAME : LegendPart.INPUT_NAME;
					part.getContext().modify(inputName, layerNode);
					part.getContext().declareModifiable(inputName);
				}
			});
		}
	}

	@Override
	public Control getControl(Composite parent, Object element, Item item, ControlEditor editor)
	{
		editor.grabVertical = true;
		editor.minimumWidth = 20;
		editor.horizontalAlignment = SWT.RIGHT;

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(parent.getBackground());
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginBottom = layout.marginLeft = layout.marginRight = layout.marginTop = 0;
		composite.setLayout(layout);

		if (element instanceof ILayerTreeNode)
		{
			ILayerTreeNode node = (ILayerTreeNode) element;
			createURLClickableLabel(composite, node, node.getInformationURL(),
					ImageRegistry.getInstance().get(ImageRegistry.ICON_INFORMATION_WHITE), ImageRegistry.getInstance()
							.get(ImageRegistry.ICON_INFORMATION), true);
			createURLClickableLabel(composite, node, node.getLegendURL(),
					ImageRegistry.getInstance().get(ImageRegistry.ICON_LEGEND_WHITE),
					ImageRegistry.getInstance().get(ImageRegistry.ICON_LEGEND), false);
		}

		return composite;
	}

	@Override
	public boolean updateControl(Control control, Object element, Item item, ControlEditor editor)
	{
		return true;
	}

	@Override
	public Rectangle overrideBounds(Rectangle bounds, Control control, Object element, Item item)
	{
		bounds.x += bounds.width;
		return bounds;
	}

	@Override
	public void disposeControl(Control control, Object element, Item item)
	{
		control.dispose();
	}
}
