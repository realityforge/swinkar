package swinkar;

import java.util.Dictionary;
import java.util.concurrent.Callable;
import javax.swing.JMenu;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.annotations.Updated;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import swinkar.ui.MenuContainer;
import swinkar.ui.MenuSupport;

@Component( immediate = true, managedservice = "TopLevelMenu", factory_method = "create" )
@Provides( specifications = { JMenu.class } )
@Wbp( filter = "(&(objectClass=javax.swing.JMenu)(parentMenu=*))",
      onArrival = "onArrival",
      onDeparture = "onDeparture",
      onModification = "onModification" )
public class DynamicMenu
  extends JMenu
  implements MenuContainer
{
  @SuppressWarnings( { "UnusedDeclaration" } )
  @ServiceProperty( name = "parentMenu" )
  @Property( name = "parentMenu", mandatory = true )
  private String m_parentMenu;

  @SuppressWarnings( { "UnusedDeclaration" } )
  @Property( name = "label", mandatory = true )
  private String m_label;

  @SuppressWarnings( { "UnusedDeclaration" } )
  @Property( name = "displayRank", mandatory = true )
  @ServiceProperty( name = "displayRank" )
  private int m_displayRank;

  @SuppressWarnings( { "UnusedDeclaration" } )
  @Property( name = "menuId", mandatory = true )
  private String m_menuId;

  private final MenuSupport m_menuSupport;

  public DynamicMenu( final BundleContext bundleContext )
  {
    m_menuSupport = new MenuSupport( bundleContext, this );
  }

  @SuppressWarnings( { "UnusedDeclaration" } )
  public static DynamicMenu create( final BundleContext bundleContext )
  {
    return SwinkarUtil.invokeAndWait( new Callable<DynamicMenu>()
    {
      @Override
      public DynamicMenu call() throws Exception
      {
        return new DynamicMenu( bundleContext );
      }
    } );
  }

  @SuppressWarnings( { "UnusedDeclaration" } )
  @Updated
  public void configUpdated( final Dictionary config )
  {
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        setText( m_label );
      }
    };
    SwinkarUtil.invokeAndWait( runnable );
  }

  @SuppressWarnings( { "UnusedDeclaration" } )
  public void onArrival( final ServiceReference reference )
    throws Exception
  {
    m_menuSupport.onArrival( reference );
  }

  @SuppressWarnings( { "UnusedDeclaration" } )
  public void onDeparture( final ServiceReference reference )
    throws Exception
  {
    m_menuSupport.onDeparture( reference );
  }

  @SuppressWarnings( { "UnusedDeclaration" } )
  public void onModification( final ServiceReference reference )
    throws Exception
  {
    m_menuSupport.onModification( reference );
  }

  public String getMenuId()
  {
    return m_menuId;
  }
}
