package au.gov.ga.earthsci.notification.ui;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author James Navin (james.navin@ga.gov.au)
 */
public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "au.gov.ga.earthsci.notification.ui.messages"; //$NON-NLS-1$
	public static String NotificationView_AcknowledgedColumnLabel;
	public static String NotificationView_CategoryColumnLabel;
	public static String NotificationView_CreateColumnLabel;
	public static String NotificationView_DescriptionColumnLabel;
	public static String NotificationView_FilterTextBoxLabel;
	public static String NotificationView_ProgressDescription;
	public static String NotificationView_SearchLabel;
	public static String NotificationView_TitleColumnLabel;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
