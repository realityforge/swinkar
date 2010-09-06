package swinkar.ui.menus;

import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.concurrent.Callable;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.annotations.Updated;
import org.osgi.framework.BundleContext;
import swinkar.SwinkarUtil;

@Component( factory_method = "create" )
@Provides( specifications={ JMenuItem.class, JComponent.class})
public class MenuItem
  extends JMenuItem
{
  @ServiceProperty( name = "parentMenu" )
  @Property( name = "parentMenu", mandatory = true )
  private String m_parentMenu;

  @Property( name = "label", mandatory = true )
  private String m_label;

  @Property( name = "displayRank", mandatory = true )
  @ServiceProperty( name = "displayRank" )
  private int m_displayRank;

  @Property( name = "actionCommand", mandatory = true )
  private String m_actionCommand;

  @Requires( id = "actionListener", proxy = false, optional = true, filter="(match=nothing)" )
  private ActionListener m_actionListener;

  @Requires( id = "preparer", proxy = false, optional = true, nullable = false, filter="(match=nothing)" )
  private MenuItemPreparer m_preparer;

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

  @Updated
  public void configUpdated( final Dictionary config )
  {
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        setActionCommand( m_actionCommand );
        setText( m_label );
        for ( ActionListener listener : getActionListeners() )
        {
          removeActionListener( listener );
        }
        addActionListener( m_actionListener );
      }
    };
    SwinkarUtil.invokeAndWait( runnable );
  }

  // Whee - ugly method, not even sure we need it in the real app, but for now
  // this provides a way to differentiate menu items.  Can't call it 'getLabel' as that
  // clashes with the JMenuItem method of the same name.
  public String getLabelProperty()
  {
    return m_label;
  }

  public MenuItemPreparer getPreparer()
  {
    return m_preparer;
  }
}
