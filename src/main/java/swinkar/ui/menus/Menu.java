package swinkar.ui.menus;

import java.util.Dictionary;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.annotations.Updated;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.realityforge.swung_weave.RunInEDT;

@Component( immediate = true, managedservice = "Menu", factory_method = "create" )
@Provides( specifications = { JMenu.class, JComponent.class } )
@Wbp( filter = "(&(objectClass=javax.swing.JComponent)(parentMenu=*))",
      onArrival = "onArrival",
      onDeparture = "onDeparture",
      onModification = "onModification" )
public class Menu
  extends JMenu
  implements MenuContainer, MenuListener
{
  @ServiceProperty( name = "parentMenu" )
  @Property( name = "parentMenu", mandatory = true )
  private String m_parentMenu;

  @Property( name = "label", mandatory = true )
  private String m_label;

  @Property( name = "displayRank", mandatory = true )
  @ServiceProperty( name = "displayRank" )
  private int m_displayRank;

  @ServiceProperty( name = "menuId" )
  @Property( name = "menuId", mandatory = true )
  private String m_menuId;

  private final MenuSupport m_menuSupport;

  public Menu( final BundleContext bundleContext )
  {
    m_menuSupport = new MenuSupport( bundleContext, this );
    addMenuListener( this );
  }

  @SuppressWarnings( { "UnusedDeclaration" } )
  @RunInEDT
  public static Menu create( final BundleContext bundleContext )
  {
    return new Menu( bundleContext );
  }

  @Updated
  @RunInEDT
  public void configUpdated( final Dictionary config )
  {
    setText( m_label );
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

  @Override
  public void menuSelected( final MenuEvent e )
  {
    m_menuSupport.menuSelected();
  }

  @Override
  public void menuDeselected( final MenuEvent e )
  {
  }

  @Override
  public void menuCanceled( final MenuEvent e )
  {
  }
}
