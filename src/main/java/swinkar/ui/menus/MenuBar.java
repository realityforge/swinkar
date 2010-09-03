package swinkar.ui.menus;

import java.util.concurrent.Callable;
import javax.swing.JMenuBar;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import swinkar.SwinkarUtil;

@Component( immediate = true, architecture = true, managedservice = "MenuBar", factory_method = "create" )
@Provides( specifications = { JMenuBar.class } )
@Wbp( filter = "(&(objectClass=javax.swing.JMenu)(parentMenu=*))",
      onArrival = "onArrival",
      onDeparture = "onDeparture",
      onModification = "onModification" )
public class MenuBar
  extends JMenuBar
  implements MenuContainer
{
  private final MenuSupport m_menuSupport;

  @SuppressWarnings( { "UnusedDeclaration" } )
  @ServiceProperty( name = "menuId" )
  @Property( name = "menuId", mandatory = true )
  private String m_menuId;

  @SuppressWarnings( { "UnusedDeclaration" } )
  public static MenuBar create( final BundleContext bundleContext )
  {
    return SwinkarUtil.invokeAndWait( new Callable<MenuBar>()
    {
      @Override
      public MenuBar call() throws Exception
      {
        return new MenuBar( bundleContext );
      }
    } );
  }

  public MenuBar( final BundleContext bundleContext )
  {
     m_menuSupport = new MenuSupport( bundleContext, this );
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

  public int getMenuComponentCount()
  {
    return getMenuCount();
  }

  public java.awt.Component getMenuComponent( final int i )
  {
    return getMenu( i );
  }

  public String getMenuId()
  {
    return m_menuId;
  }
}
