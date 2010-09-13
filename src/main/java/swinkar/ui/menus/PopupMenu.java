package swinkar.ui.menus;

import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.handlers.event.Subscriber;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.realityforge.swung_weave.RunInEDT;

@Component( immediate = true, managedservice = "PopupMenu", factory_method = "create" )
@Provides( specifications = { JMenu.class, JPopupMenu.class } )
@Wbp( filter = "(&(objectClass=javax.swing.JComponent)(parentMenu=*))",
      onArrival = "onArrival",
      onDeparture = "onDeparture",
      onModification = "onModification" )
public class PopupMenu
  extends JPopupMenu
  implements MenuContainer
{
  @ServiceProperty( name = "menuId" )
  @Property( name = "menuId", mandatory = true )
  private String m_menuId;

  private final MenuSupport m_menuSupport;

  @SuppressWarnings( { "UnusedDeclaration" } )
  @RunInEDT
  public static PopupMenu create( final BundleContext bundleContext )
  {
    return new PopupMenu( bundleContext );
  }

  protected PopupMenu( final BundleContext context )
  {
    m_menuSupport = new MenuSupport( context, this );
    // createComponents();
  }

  public String getMenuId()
  {
    return m_menuId;
  }

  public void setMenuId( String menuId )
  {
    m_menuId = menuId;
  }

  @Subscriber( name = "eastLabelPopupSubscriber",
               topics = "backPanel/popupTriggered" )
  public void populateForEastLabel( Event event )
  {
    final String menuId = (String) event.getProperty( "menuId" );
    if ( !m_menuId.equals( menuId ) )
    {
      return;
    }

    System.out.println( "East Label Populated" );
    System.out.println( Arrays.asList( event.getPropertyNames() ) );

    final JComponent src = (JComponent) event.getProperty( "source" );
    final String context = (String) event.getProperty( "context" );
    System.out.println( "context = " + context );
    final MouseEvent mouseEvent = (MouseEvent) event.getProperty( "mouseEvent" );

    showInEDT( src, mouseEvent );
  }


  @RunInEDT
  private void showInEDT( final JComponent source, final MouseEvent me )
  {
    show( source, me.getX(), me.getY() );
  }

  @Override
  public java.awt.Component getMenuComponent( final int i )
  {
    return getComponent( i );
  }

  @Override
  public int getMenuComponentCount()
  {
    return getComponentCount();
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
}
