package swinkar.ui.menus;

import java.util.concurrent.Callable;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.osgi.framework.BundleContext;
import swinkar.SwinkarUtil;

@Component( immediate = true, managedservice = "Separator", factory_method = "create" )
@Provides( specifications = { JSeparator.class, JComponent.class } )
public class Separator
  extends JSeparator
{
  @SuppressWarnings( { "UnusedDeclaration" } )
  @ServiceProperty( name = "parentMenu" )
  @Property( name = "parentMenu", mandatory = true )
  private String m_parentMenu;

  @SuppressWarnings( { "UnusedDeclaration" } )
  @Property( name = "displayRank", mandatory = true )
  @ServiceProperty( name = "displayRank" )
  private int m_displayRank;

  @SuppressWarnings( { "UnusedDeclaration" } )
  public static Separator create( final BundleContext bundleContext )
  {
    return SwinkarUtil.invokeAndWait( new Callable<Separator>()
    {
      @Override
      public Separator call() throws Exception
      {
        return new Separator();
      }
    } );
  }
}
