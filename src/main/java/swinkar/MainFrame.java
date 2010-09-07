package swinkar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Subscriber;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.realityforge.swung_weave.RunInEDT;
import swinkar.ui.ScreenManager;

@Component( architecture = true, immediate = true, managedservice = "MainFrame", factory_method = "create" )
@Provides( specifications = { ActionListener.class } )
public class MainFrame
  extends JFrame
  implements ActionListener
{
  @Property( name = "width", value = "640" )
  private int m_width = 640;

  @Property( name = "height", value = "800" )
  private int m_height = 800;

  //@Requires( proxy = false, filter = "")
  @Requires( proxy = false, filter = "(&(objectClass=javax.swing.JMenuBar)(menuId=TopLevelMenu))" )
  private JMenuBar m_menuBar;

  @Requires( proxy = false, filter = "(role=ScreenManager)" )
  private JPanel m_screenManager;

  @RunInEDT
  public static MainFrame create()
  {
    System.out.println( "Constructing a MainFrame" );
    return new MainFrame();
  }

  @Subscriber( name = "MainFrame.Title",
               topics = "MainFrame/Title",
               data_key = "title",
               data_type = "java.lang.String" )
  @RunInEDT
  public void updateTitle( final String title )
  {
    setTitle( title );
  }

  @Validate
  @RunInEDT
  public void start()
    throws Exception
  {
    System.out.println( "Running a main frame!" );
    setPreferredSize( new Dimension( m_width, m_height ) );
    setSize( new Dimension( m_width, m_height ) );
    setJMenuBar( m_menuBar );
    setContentPane( m_screenManager );
    pack();
    setTitle( "My Initial Title" );
    MainFrame.this.repaint();
    setVisible( true );
  }

  @Invalidate
  @RunInEDT
  public void stop()
  {
    setVisible( false );
    dispose();
  }

  @Subscriber( name = "terminator",
               topics = "menuItem/actionPerformed",
               filter = "(command=shutdown)" )
  public void terminate( Event e )
  {
    stop();
  }


  @Override
  @RunInEDT
  public void actionPerformed( final ActionEvent e )
  {
    setVisible( false );
    dispose();
  }
}
