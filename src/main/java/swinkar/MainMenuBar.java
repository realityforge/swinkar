package swinkar;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@Component( immediate = true, architecture = true, managedservice = "MenuBar")
@Provides( specifications = { JMenuBar.class } )
@Wbp( filter = "(objectClass=javax.swing.JMenu)",
      onArrival = "onArrival",
      onDeparture = "onDeparture",
      onModification = "onModification" )
public class MainMenuBar
  extends JMenuBar
{
  private BundleContext m_bundleContext;
  private final Map<JMenu, Integer> m_menuRanks = new HashMap<JMenu, Integer>();

  public MainMenuBar( final BundleContext bundleContext )
  {
    System.out.println( "MainMenuBar.MainMenuBar" );
    m_bundleContext = bundleContext;
  }

  @SuppressWarnings( { "UnusedDeclaration" } )
  public void onArrival( final ServiceReference reference )
    throws Exception
  {
    System.out.println( "MainMenuBar.onArrival" );
    final JMenu menu = (JMenu)m_bundleContext.getService( reference );
    final int displayRank = (Integer)reference.getProperty( "displayRank" );
    addMenu( menu, displayRank );
  }

  @SuppressWarnings( { "UnusedDeclaration" } )
  public void onDeparture( final ServiceReference reference )
    throws Exception
  {
    System.out.println( "MainMenuBar.onDeparture" );
    final JMenu menu = (JMenu)m_bundleContext.getService( reference );
    removeMenu( menu );
  }

  @SuppressWarnings( { "UnusedDeclaration" } )
  public void onModification( final ServiceReference reference )
    throws Exception
  {
    System.out.println( "MainMenuBar.onModification" );
    final JMenu menu = (JMenu)m_bundleContext.getService( reference );
    final int displayRank = (Integer)reference.getProperty( "displayRank" );
    removeMenu( menu );
    addMenu( menu, displayRank );
  }

  private void addMenu( final JMenu menu, final int displayRank )
  {
    System.out.println( "addMenu('" + menu.getText() + "'," + displayRank + ")" );
    int index = 0;
    final int count = getMenuCount();
    for( int i = 0; i < count; i++ )
    {
      final JMenu other = getMenu( i );
      final int otherRank = m_menuRanks.get( other );
      index = i;
      if( otherRank > displayRank )
      {
        break;
      }
    }
    add( menu, index );
    m_menuRanks.put( menu, displayRank );
  }

  private void removeMenu( final JMenu menu )
  {
    System.out.println( "removeMenu('" + menu.getText() + "')" );
    final int count = getMenuCount();
    for( int i = 0; i < count; i++ )
    {
      if( getMenu( i ) == menu )
      {
        remove( i );
        break;
      }
    }
    m_menuRanks.remove( menu );
  }
}
