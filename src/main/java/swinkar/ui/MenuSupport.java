package swinkar.ui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.SwingUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import swinkar.SwinkarUtil;

public class MenuSupport
{
  private BundleContext m_bundleContext;
  private final MenuContainer m_menu;
  private final Map<Component, Integer> m_menuRanks = new HashMap<Component, Integer>();

  public MenuSupport( final BundleContext bundleContext, final MenuContainer menu )
  {
    m_bundleContext = bundleContext;
    m_menu = menu;
  }

  public void onArrival( final ServiceReference reference )
  {
    if ( ignore( reference ) )
    {
      return;
    }
    final JMenu menu = (JMenu)m_bundleContext.getService( reference );
    final int displayRank = (Integer)reference.getProperty( "displayRank" );
    final Runnable runnable = new Runnable()
    {
      public void run()
      {
        addItem( menu, displayRank );
      }
    };
    SwinkarUtil.invokeAndWait( runnable );
  }

  public void onDeparture( final ServiceReference reference )
  {
    if ( ignore( reference ) )
    {
      return;
    }
    final JMenu menu = (JMenu)m_bundleContext.getService( reference );
    final Runnable runnable = new Runnable()
    {
      public void run()
      {
        removeItem( menu );
      }
    };
    SwinkarUtil.invokeAndWait( runnable );
  }


  public void onModification( final ServiceReference reference )
  {
    if ( ignore( reference ) )
    {
      return;
    }
    final JMenu menu = (JMenu)m_bundleContext.getService( reference );
    final int displayRank = (Integer)reference.getProperty( "displayRank" );
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        removeItem( menu );
        addItem( menu, displayRank );
      }
    };
    SwingUtilities.invokeLater( runnable );
  }

  private void addItem( final Component menu, final int displayRank )
  {
    int index = -1;
    final int count = m_menu.getItemCount();
    for( int i = 0; i < count; i++ )
    {
      final Component other = m_menu.getComponent( i );
      final int otherRank = m_menuRanks.get( other );
      System.out.println( "other('" + other.getName() + "'," + otherRank + ") index = " + i );
      if( otherRank > displayRank )
      {
        index = i;
        break;
      }
      index = i + 1;
    }
    System.out.println( "addItem('" + menu.getName() + "'," + displayRank + ") index = " + index );
    m_menu.add( menu, index );
    m_menu.revalidate();
    m_menuRanks.put( menu, displayRank );
  }

  private void removeItem( final Component menu )
  {
    final int count = m_menu.getItemCount();
    int index = -1;
    for( int i = 0; i < count; i++ )
    {
      if( m_menu.getComponent( i ) == menu )
      {
        index = i;
        break;
      }
    }
    System.out.println( "removeItem('" + menu.getName() + "') index = " + index );
    m_menu.remove( index );
    m_menu.revalidate();
    m_menuRanks.remove( menu );
  }

  private boolean ignore( final ServiceReference reference )
  {
    return !m_menu.getMenuId().equals( reference.getProperty( "parentMenu" ) );
  }
}
