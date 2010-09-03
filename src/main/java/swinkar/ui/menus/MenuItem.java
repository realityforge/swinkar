package swinkar.ui.menus;

import java.util.Dictionary;
import java.util.concurrent.Callable;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.annotations.Updated;
import org.osgi.framework.BundleContext;
import swinkar.SwinkarUtil;

@Component( factory_method = "create" )
@Provides( specifications={ JMenuItem.class, JComponent.class})
public class MenuItem
  extends JMenuItem
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
  public static MenuItem create( )
  {
    return SwinkarUtil.invokeAndWait( new Callable<MenuItem>()
    {
      @Override
      public MenuItem call() throws Exception
      {
        return new MenuItem( );
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
}
