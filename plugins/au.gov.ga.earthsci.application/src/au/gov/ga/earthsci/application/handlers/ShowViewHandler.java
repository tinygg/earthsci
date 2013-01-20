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
package au.gov.ga.earthsci.application.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

/**
 * Handler used to show a part (or "View").
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class ShowViewHandler
{
	@Inject
	private EPartService partService;

	@Inject
	private MApplication application;

	@Inject
	private EModelService modelService;

	@Execute
	public void execute(IEclipseContext context, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell)
			throws InvocationTargetException, InterruptedException
	{
		final ShowViewDialog dialog = new ShowViewDialog(shell, partService);
		dialog.open();
		if (dialog.getReturnCode() != Window.OK)
			return;

		for (MPart part : dialog.getSelection())
		{
			showPart(part);
		}
	}

	private void showPart(MPart part)
	{
		MPartStack stack = null;
		MPartDescriptor descriptor = findDescriptor(part.getElementId());

		if (descriptor != null && descriptor.isAllowMultiple())
		{
			part = partService.createPart(part.getElementId());

			//find other parts to put this part next to
			List<MPart> siblings = modelService.findElements(application, part.getElementId(), MPart.class, null);

			//use this opportunity to clean up old parts
			for (MPart sibling : siblings)
			{
				//multi parts are not reused, so remove them if they are not ToBeRendered
				if (!sibling.isToBeRendered() && sibling.getParent() != null)
				{
					sibling.getParent().getChildren().remove(sibling);
				}
			}

			//select a stack next to one of the siblings
			for (MPart sibling : siblings)
			{
				if (sibling.isToBeRendered())
				{
					Object parent = sibling.getParent();
					if (parent instanceof MPartStack)
					{
						stack = (MPartStack) parent;
						break;
					}
				}
			}
		}

		if (stack != null)
		{
			stack.getChildren().add(part);
		}
		else
		{
			partService.showPart(part, PartState.ACTIVATE);
		}
	}

	private MPartDescriptor findDescriptor(String id)
	{
		for (MPartDescriptor descriptor : application.getDescriptors())
		{
			if (descriptor.getElementId().equals(id))
			{
				return descriptor;
			}
		}
		return null;
	}
}
