package swinkar;

import java.util.Dictionary;
import java.util.concurrent.Callable;
import javax.swing.JMenu;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.annotations.Updated;

@Component( immediate = true, managedservice = "TopLevelMenu", factory_method = "create" )
@Provides( specifications = { JMenu.class } )
public class TopLevelMenu
  extends JMenu
{
  @Property( name = "label", mandatory = true )
  private String m_label;

  @Property( name = "displayRank", mandatory = true )
  @ServiceProperty( name = "displayRank" )
  private int m_displayRank;

  public static TopLevelMenu create()
  {
    return SwinkarUtil.invokeAndWait( new Callable<TopLevelMenu>()
    {
      @Override
      public TopLevelMenu call() throws Exception
      {
        return new TopLevelMenu();
      }
    } );
  }

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
