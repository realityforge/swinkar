package swinkar.ui;

import java.awt.CardLayout;
import java.util.concurrent.Callable;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import swinkar.SwinkarUtil;

@Component
/*
// TODO: Filter to only things that claim to be screens, not JComponents.  Do we have to do marker interfaces, should be able to it with a property
@Wbp( filter = "(objectClass=javax.swing.JComponent)",
      onArrival = "addScreen",
      onDeparture = "removeScreen" )
*/
public class ScreenManager
  extends JPanel
{
  private BundleContext m_bundleContext;

  public ScreenManager( final BundleContext bundleContext )
  {
    super( new CardLayout( ) );
    m_bundleContext = bundleContext;
    System.out.println( "We have a screen manager" );
  }

  public void addScreen(final ServiceReference reference)
  {
    System.out.println( "Adding a screen: " );
    final JComponent screen = (JComponent)m_bundleContext.getService( reference );
    final String screenName = (String) reference.getProperty( "screenName" );
    System.out.println( "Screen: " + screenName );
    add( screen, screenName );
    ((CardLayout)getLayout()).show( this, screenName );
  }

  public void removeScreen(final ServiceReference reference)
  {
    System.out.println( "Lost a screen: " );
  }
}
