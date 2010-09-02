package swinkar;

import java.util.concurrent.Callable;
import javax.swing.JMenuBar;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import swinkar.ui.MenuContainer;
import swinkar.ui.MenuSupport;

@Component( immediate = true, architecture = true, managedservice = "MenuBar", factory_method = "create" )
@Provides( specifications = { JMenuBar.class } )
@Wbp( filter = "(&(objectClass=javax.swing.JMenu)(parentMenu=TopLevelMenu))",
      onArrival = "onArrival",
      onDeparture = "onDeparture",
      onModification = "onModification" )
public class MainMenuBar
  extends JMenuBar
  implements MenuContainer
{
  private final MenuSupport m_menuSupport;

  @SuppressWarnings( { "UnusedDeclaration" } )
  public static MainMenuBar create( final BundleContext bundleContext )
  {
    return SwinkarUtil.invokeAndWait( new Callable<MainMenuBar>()
    {
      @Override
      public MainMenuBar call() throws Exception
      {
        return new MainMenuBar( bundleContext );
      }
    } );
  }

  public MainMenuBar( final BundleContext bundleContext )
  {
    System.out.println( "MainMenuBar.MainMenuBar" );
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

  @Override
  public int getItemCount()
  {
    return getMenuCount();
  }

  public String getMenuId()
  {
    return "TopLevelMenu";
  }
}
