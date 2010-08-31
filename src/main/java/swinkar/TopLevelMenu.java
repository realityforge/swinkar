package swinkar;

import java.util.Dictionary;
import javax.swing.JMenu;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.annotations.Updated;

@Component( immediate = true, managedservice = "TopLevelMenu" )
@Provides( specifications = { JMenu.class } )
public class TopLevelMenu
  extends JMenu
{
  @Property( name = "label", mandatory = true )
  private String m_label;

  @Property( name = "displayRank", mandatory = true )
  @ServiceProperty( name = "displayRank" )
  private int m_displayRank;

  @Updated
  public void configUpdated( final Dictionary config )
  {
    System.out.println( "TopLevelMenu.configUpdated" );
    setText( m_label );
  }
}